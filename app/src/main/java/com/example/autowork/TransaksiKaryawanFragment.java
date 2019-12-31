package com.example.autowork;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.autowork.model.TransaksiKaryawan;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


import java.text.DecimalFormat;



/**
 * A simple {@link Fragment} subclass.
 */
public class TransaksiKaryawanFragment extends Fragment{

//    private ZXingScannerView zXingScannerView;

    public TransaksiKaryawanFragment() {
        // Required empty public constructor
    }





    private DatabaseReference database, database1;

    private EditText etBarkod, etNama, etJml;
    private TextView tvtotal, tvtotaltransaksi,tvtotaltransaksi1;
    private ProgressDialog loading;

    private String hasilbarkod;
    private Integer sjml, shrgjual, stotal;



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(getActivity(), "Data NULL", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Data = "+result.getContents(), Toast.LENGTH_SHORT).show();
                hasilbarkod = result.getContents();
                etBarkod.setText(hasilbarkod); // MENAMPILKAN BARKOD HASIL SCAN KE VIEW LAYOUT
                mencaribarkod();
            }

            // At this point we may or may not have a reference to the activity
//            displayToast();
        }
    }



    public void scane(){
        IntentIntegrator integrator = new IntentIntegrator(getActivity()).forSupportFragment(this);
        integrator.setCaptureActivity(Scaner.class);
        integrator.setOrientationLocked(false);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scan Barkod e?");
        integrator.initiateScan();
    }



    @Override
    public View onCreateView  (LayoutInflater inflater, ViewGroup container,
                               Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vt = inflater.inflate(R.layout.fragment_transaksi_karyawan, container, false);


        database1 = FirebaseDatabase.getInstance().getReference();

        etBarkod = vt.findViewById(R.id.et_barkod);
        etNama = vt.findViewById(R.id.et_nama);
        etJml = vt.findViewById(R.id.et_jml);
        tvtotal = vt.findViewById(R.id.tv_total);
        tvtotaltransaksi = vt.findViewById(R.id.tv_totaltransaksi);
        tvtotaltransaksi1 = vt.findViewById(R.id.tv_totaltransaksi1);




        // TOMBOL CENSEL
        vt.findViewById(R.id.btn_cancel).setOnClickListener((view) -> {
            scane();
        });

        vt.findViewById(R.id.btn_cancel1).setOnClickListener((view) -> {



        });



        // TOMBOL BARKODE
        vt.findViewById(R.id.btn_barkod).setOnClickListener((view) -> {
            mencaribarkod();
        });

        // TOMBOL TAMBAH BARANG UNTUK INPUT KE TRANSAKSI YANG DI TERUSKAN KE KASIR
        vt.findViewById(R.id.btn_tambahbarang).setOnClickListener((view) -> {

            String Sbarkod = etBarkod.getText().toString();
            String Snama = etNama.getText().toString();
            String Sjml = etJml.getText().toString();

            if (Snama.equals("") || Sjml.equals("")) {
                etJml.setError("Silahkan ISI data dengan BENAR!!!");
                etJml.requestFocus();

            } else {

                Long timestampl = System.currentTimeMillis();
                String timestamp = timestampl.toString();

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    // Name, email address, and profile photo Url
                    String name = user.getDisplayName();
                    String uid = user.getUid();

                    // end UPDATE TOTAL PEMBAYARAN PADA TABEL TRANSAKSI 1 =========================
                    inputDatabase(new TransaksiKaryawan(
                            Sbarkod,
                            Snama,
                            Sjml,
                            name,
                            uid,
                            timestamp)); // HASIL TOTAL PEMBAYARAN

                    /**
                     * MENSET BARKOD MENJADI ENABLE LAGI KARENA JIKA BARANG DITEMUKAN DARI DATABASE EDTI TEXT BARKOD DI SET MENJADI DISABLE
                     * DAN MENSET NAMA DAN JUMLAH MENJADI KOSONG LAGI
                     */
                    etBarkod.setEnabled(true);
                    etNama.setText("");
                    etJml.setText("");
                }
            }



        });
//*/

        /**
         * =========================================================================================================================================================(STAR)
         * FUNGSI MENDAPATKAN NILAI TOTAL TRANSAKSI DARI TABEL TRANSAKSI FIREBASE
         */
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String uid = user.getUid();
            database = FirebaseDatabase.getInstance().getReference().child(GlobalVariabel.Toko).child(GlobalVariabel.Transaksi).child(uid);
            database.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {

                    if (dataSnapshot2.child("totalTransaksi").exists()){
                        String totaltransaksi = dataSnapshot2.child("totalTransaksi").getValue(String.class);
                        DecimalFormat decim = new DecimalFormat("#,###.##");
                        tvtotaltransaksi.setText("Rp. "+decim.format(Integer.parseInt(totaltransaksi)));

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }

            });
        }

        /**
         * =========================================================================================================================================================(STAR)
         */


        return vt;
    }


    /**
     * =====================================================================================================================================(STAR)
     * PROSES VERIFIKASI BARKOD APAKAH ADA DI DATABASE ATAU TIDAK
     */
    private void mencaribarkod(){
        String sBarkod = etBarkod.getText().toString();

        if (sBarkod.equals("")) { /** JIKA BARCOD TIDAK DIISI MAKA AKAN MENGEKSEKUSI FUNGSI SCAN */
            scane();

        } else { /** JIKA DIISI MAKA AKAH DILAKUKAN PENCARIAN DATABASE */

            String etBarkod1 = etBarkod.getText().toString();

            database = FirebaseDatabase.getInstance().getReference().child(GlobalVariabel.Toko).child(GlobalVariabel.Gudang).child(etBarkod1);
            database.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    String nama = dataSnapshot.child("nama").getValue(String.class);
                    String hrgjual = dataSnapshot.child("hargajual").getValue(String.class);

                    etNama.setText(nama); /** MEMUNCULKAN NAMA DARI DATABASE JIKA ADA */

                    /**
                     * VERIFIKASI BARKOD APAKAH ADA DI DATABASE FIREBASE
                     * JIKA NAMA DARI DATABASE TIDAK ADA MAKA AKAN MUNCUL Code salah
                     */
                    if (dataSnapshot.child("nama").exists()) {

                        /**
                         * JIKA DATA DITEMUKAN MAKA HARGA DI JUMLAHKAN
                         */
                        etBarkod.setEnabled(false);//MENSET EDIT TEXT MENJADI DISABLE TIDAK BISA DI RUBAH
                        /**
                         * FUNSI PENJUMLAHAN DISAAT BERSAMAAN KITA MENGETIK DI EDIT TEXT
                         */
                        etJml.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {

                                String sJml = etJml.getText().toString();  /** JUMLAH DIAMBIL DARI EDIT TEXT */

                                if (sJml.equals("")) { /** INISIALISASI MENGHINDARI EROR JIKA EDIT TEXT KOSONG ATAU USER MELAKUKAN PENGHAPUSAN JUMLAH DI EDIT TEXT */
                                    sjml=0;

                                } else { /** JIKA EDIT TEXT JUMLAH TIDAK KOSONG MAKA DILAKUKAN KONVERSI DATA DARI STRING KE INTEGER */
                                    sjml = Integer.parseInt(sJml);
                                }

                                shrgjual = Integer.parseInt(hrgjual); /** KONVERSI VALUE HARGA JUAL DARI DATABASE FIREBASE */
                                stotal = sjml * shrgjual; /** DILAKUKAN PENJUMLAHAN UNTUK MENDAPATKAN TOTAL HARGA BARANG DIKALI JUMLAH BARANG*/

                                DecimalFormat decim = new DecimalFormat("#,###.##");
                                tvtotal.setText("Rp. "+decim.format(stotal));/** MENAMPILKAN TOTAL HARGA KE VIEW LAYOUT (KONVERSI VALUE TOTAL HARGA BARANG) */
                            }

                            @Override
                            public void afterTextChanged(Editable s) {

                            }
                        });


                    } else {
                        etBarkod.setError("Code Salah ??");
                        etBarkod.requestFocus();
                        etBarkod.selectAll();
                        dialogyesno();
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });



        }
    }

    /**
     * =====================================================================================================================================(END)


    /**
     * ================================================================================================(STAR)
     * PROSES PUSH DATA KE FIREBASE
     * @deprecated melakukan input data transaksi log dan update barang setelah transaksi
     * @param transaksiKaryawan trasaksi data barang
     */
    private void inputDatabase(TransaksiKaryawan transaksiKaryawan) {

        database = FirebaseDatabase.getInstance().getReference().child(GlobalVariabel.Toko).child("Trigger");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(!dataSnapshot.child("Trigger").exists()){
                    if(!dataSnapshot.child("Trigger").exists()){
                        /**
                         * DATA BARANG YANG MASUK TABEL TRANSAKSI 1
                         */
                        database1.child(GlobalVariabel.Toko)
                                .child("Trigger/Trigger")
                                .child("TRANSAKSI KARYAWAN")
                                .setValue(transaksiKaryawan);

                        Toast.makeText(getActivity(),
                                "Data BERHASIL  di input!!",
                                Toast.LENGTH_SHORT).show();

                        database.removeEventListener(this);

                        loading = ProgressDialog.show(getActivity(),
                                null,
                                "SEBENTAR...",
                                true,
                                false);

                        loading.dismiss();
                    }

                } else {

                    loading = ProgressDialog.show(getActivity(),
                            null,
                            "SEBENTAR...",
                            true,
                            false);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }


    /**
     * ================================================================================================(END)
     */


    /**
     * ==============================================================================================(STAR)
     * KETIKA SCAN DATA TIDAK DITEMUKAN MUNCUL DIALOG INI
     */
    private void dialogyesno(){
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        scane();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Apakah ingin SCAN ulang?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    /**
     * ==============================================================================================(STAR)
     */



}

