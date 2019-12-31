package com.example.autowork;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.autowork.model.Meminta;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;



/**
 * A simple {@link Fragment} subclass.
 */
public class TambahDataFragment extends Fragment {


    public TambahDataFragment() {
        // Required empty public constructor
    }

    private ProgressDialog loading;

    private DatabaseReference database,database1;
    private EditText etbarkod, etnama, etjml, ethrgawal, ethrgjual;
//    private ProgressDialog loading;
//    private Button btn_tambahbarang, btn_cencel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_tambah_data, container, false);

        database = FirebaseDatabase.getInstance().getReference();
        database1 = FirebaseDatabase.getInstance().getReference();
//
        etbarkod = v.findViewById(R.id.et_barkod);
        etnama = v.findViewById(R.id.et_nama);
        etjml = v.findViewById(R.id.et_jml);
        ethrgawal = v.findViewById(R.id.et_hrgawal);
        ethrgjual = v.findViewById(R.id.et_hrgjual);


        /**
         * ==========================================================================================================================================================================(STAR)
         * TOMBOL PROSES INPUT DATA BARANG BARU KE FIREBASE
         */
        v.findViewById(R.id.btn_tambahbarang).setOnClickListener((view) -> {

//            database = FirebaseDatabase.getInstance();
            database.child(GlobalVariabel.Toko).child(GlobalVariabel.Gudang).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                    String Sbarkod = etbarkod.getText().toString();
                    String Snama = etnama.getText().toString();
                    String Sjml = etjml.getText().toString();
                    String Shrgawal = ethrgawal.getText().toString();
                    String Shrgjual = ethrgjual.getText().toString();
                    Long timestampl = System.currentTimeMillis();
                    String timestamp = timestampl.toString();

                    if (dataSnapshot.child(Sbarkod).exists()) {

                        Toast.makeText(getActivity(), "Data yang anda masukan sudah ada di gudang silahkan lakukan update stok untuk mengubah stok", Toast.LENGTH_SHORT).show();

                    } else {

                        if (Sbarkod.equals("")) {
                            etbarkod.setError("Silahkan masukkan code");
                            etbarkod.requestFocus();
                        } else if (Snama.equals("")) {
                            etnama.setError("Silahkan masukkan nama");
                            etnama.requestFocus();
                        } else if (Sjml.equals("")) {
                            etjml.setError("Silahkan masukkan jumlah");
                            etjml.requestFocus();
                        } else if (Shrgawal.equals("")) {
                            ethrgawal.setError("Silahkan masukkan harga awal");
                            ethrgawal.requestFocus();
                        } else if (Shrgjual.equals("")) {
                            ethrgjual.setError("Silahkan masukkan harga jual");
                            ethrgjual.requestFocus();
                        } else {


                            submit(new Meminta(
                                            Sbarkod,
                                            Snama,
                                            Sjml,
                                            Shrgawal,
                                            Shrgjual,
                                            timestamp));
                        }

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getActivity(), "  CEK eror ?  ", Toast.LENGTH_SHORT).show();
                }
            });



        });

        /**
         * ==========================================================================================================================================================================(END)
         */


        // Inflate the layout for this fragment
        //









        return v;



    }


    /**
     * PROSES PUSH DATA KE FIREBASE
     * @deprecated push data barang baru
     * @param meminta data barang
     */
    private void submit(Meminta meminta) {


        database.child(GlobalVariabel.Toko).child("Trigger");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (!dataSnapshot.exists()) {
                    database1.child(GlobalVariabel.Toko)
                            .child("Trigger")
                            .child("STOK BARU")
//                .child(id)
                            .setValue(meminta);

                    Toast.makeText(getActivity(),
                            "Data BERHASIL  di input!!",
                            Toast.LENGTH_SHORT).show();
                    database.removeEventListener(this);

                    /**
                     * SET TEXTVIEW MEJADI KOSONG
                     */
                    etbarkod.setText("");
                    etnama.setText("");
                    etjml.setText("");
                    ethrgawal.setText("");
                    ethrgjual.setText("");

                } else {
                    Toast.makeText(getActivity(),
                            "Data GAGAL  di input!!",
                            Toast.LENGTH_SHORT).show();
                    database.removeEventListener(this);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }







}
