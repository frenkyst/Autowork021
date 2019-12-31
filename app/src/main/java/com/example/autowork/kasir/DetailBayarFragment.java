package com.example.autowork.kasir;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.autowork.GlobalVariabel;
import com.example.autowork.R;
import com.example.autowork.adapter.MemintaTransaksikasir;
import com.example.autowork.model.DetailBayar;
import com.example.autowork.model.Meminta;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailBayarFragment extends Fragment {


    public DetailBayarFragment() {
        // Required empty public constructor
    }

    private DatabaseReference database, database1;

    private ArrayList<Meminta> daftarReq;
    private MemintaTransaksikasir memintatransaksikasir;

    private RecyclerView rc_list_request;
    private ProgressDialog loading;

    String timestamp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_detail_bayar, container, false);

        if(GlobalVariabel.invisible.equals("ya")){
            v.findViewById(R.id.btn_bayar).setVisibility(View.INVISIBLE);
        } else {
            v.findViewById(R.id.btn_bayar).setVisibility(View.VISIBLE);
        }



        database = FirebaseDatabase.getInstance().getReference();
        database1 = FirebaseDatabase.getInstance().getReference();
        TextView tv_totalBayar;
        tv_totalBayar = v.findViewById(R.id.tv_totalBayar);

        rc_list_request = v.findViewById(R.id.rc_list_request);
        //fab_add = findViewById(R.id.fab_add);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        rc_list_request.setLayoutManager(mLayoutManager);
        rc_list_request.setItemAnimator(new DefaultItemAnimator());

        loading = ProgressDialog.show(getActivity(),
                null,
                "Please wait...",
                true,
                false);

        database.child(GlobalVariabel.Toko).child(GlobalVariabel.Transaksi+"/"+GlobalVariabel.uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                /**
                 * Saat ada data baru, masukkan datanya ke ArrayList
                 */
                daftarReq = new ArrayList<>();
                for (DataSnapshot noteDataSnapshot : dataSnapshot.child("transaksi").getChildren()) {
                    /**
                     * Mapping data pada DataSnapshot ke dalam object Wisata
                     * Dan juga menyimpan primary key pada object Wisata
                     * untuk keperluan Edit dan Delete data
                     */
                    Meminta requests = noteDataSnapshot.getValue(Meminta.class);
                    requests.setKey(noteDataSnapshot.getKey());

                    /**
                     * Menambahkan object Wisata yang sudah dimapping
                     * ke dalam ArrayList
                     */
                    daftarReq.add(requests);

                    /**
                     * =============================================================================(STAR)
                     * MENAMPILKAN TOTAL HARGA KESELURUHAN
                     * */
                    String totalbayar = dataSnapshot.child("totalTransaksi").getValue(String.class);

                    if(dataSnapshot.child("totalTransaksi").exists()) {
                        DecimalFormat decim = new DecimalFormat("#,###.##");
                        tv_totalBayar.setText("Rp. " + decim.format(Integer.parseInt(totalbayar)));

                    }
                    /**
                     * =============================================================================(END)
                     */


                }

                /**
                 * Inisialisasi adapter dan data hotel dalam bentuk ArrayList
                 * dan mengeset Adapter ke dalam RecyclerView
                 */
                memintatransaksikasir = new MemintaTransaksikasir(daftarReq, getActivity());
                rc_list_request.setAdapter(memintatransaksikasir);
                loading.dismiss();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                /**
                 * Kode ini akan dipanggil ketika ada error dan
                 * pengambilan data gagal dan memprint error nya
                 * ke LogCat
                 */
                System.out.println(databaseError.getDetails() + " " + databaseError.getMessage());
                loading.dismiss();
            }
        });


        /**
         * ========================================================================================================================================(STAR)
         *TOMBOL BAYAR UNTUK MELAKUKAN TRANSAKSI PEMBAYARAN OLEH KASIR
         */
        v.findViewById(R.id.btn_bayar).setOnClickListener((view) -> {

            Long timestampl = System.currentTimeMillis();
            timestamp = timestampl.toString();
            GlobalVariabel.timestamp = timestamp;

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                // Name, email address, and profile photo Url
                String name = user.getDisplayName();


                pushData(new DetailBayar(
                        name,
                        timestamp,
                        GlobalVariabel.uid));



            }



        });
        /**
         * ========================================================================================================================================(END)
         *TOMBOL BAYAR UNTUK MELAKUKAN TRANSAKSI PEMBAYARAN OLEH KASIR
         */



        return v;
    }



    private void pushData(DetailBayar detailBayar) {

        database.child(GlobalVariabel.Toko).child("Trigger");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (!dataSnapshot.child("TriggerBaya").exists()) {
                    if (!dataSnapshot.child("TriggerBaya").exists()) {
                        database1.child(GlobalVariabel.Toko)
                                .child("Trigger/TriggerBaya")
                                .setValue(detailBayar);

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

                        AppCompatActivity activity = (AppCompatActivity) getContext();
                        Fragment myFragment = new NotaPembayaranFragment();
                        activity.getSupportFragmentManager().popBackStackImmediate();
                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.framekasir, myFragment).addToBackStack(null).commit();
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



}
