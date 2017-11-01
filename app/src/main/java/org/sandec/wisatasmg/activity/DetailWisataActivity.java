package org.sandec.wisatasmg.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.sandec.wisatasmg.R;
import org.sandec.wisatasmg.database.DatabaseHelper;
import org.sandec.wisatasmg.helper.Konstanta;

public class DetailWisataActivity extends AppCompatActivity {

    //logt
    private static final String TAG = "DetailWisataActivity";

    String dataNama, dataAlamat, dataDeskripsi, dataGambar;
    Boolean isFavorit;
    SharedPreferences pref;
    FloatingActionButton fab;
    DatabaseHelper database = new DatabaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_wisata);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //terima data
        dataNama = getIntent().getExtras().getString(Konstanta.DATA_NAMA);
        dataAlamat = getIntent().getExtras().getString(Konstanta.DATA_ALAMAT);
        dataDeskripsi = getIntent().getExtras().getString(Konstanta.DATA_DESKRIPSI);
        dataGambar = getIntent().getExtras().getString(Konstanta.DATA_GAMBAR);

        //logd untuk menampilkan di logcat
        Log.d(TAG, "onCreate: " + dataNama + dataGambar + dataDeskripsi + dataAlamat);

        //ambil data dari sharedpreference
        pref = getSharedPreferences(Konstanta.SETTING, MODE_PRIVATE);
        isFavorit = pref.getBoolean(Konstanta.TAG_PREF+dataNama, false);


        fab = (FloatingActionButton) findViewById(R.id.fab);
        cekFavorit(isFavorit);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //simpan ke favorit jika pref false

                if(isFavorit){
                    //jika love penuh
                    //hapus dari sqlite
                    long id = database.delete(dataNama);

                    if (id<=0){
                        Snackbar.make(view, "Favorit gagal dihapus dari database", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    } else {
                        Snackbar.make(view, "Favorit dihapus dari database", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();

                        //bikin false
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putBoolean(Konstanta.TAG_PREF+dataNama, false);
                        editor.commit();
                        isFavorit = false;
                    }

                } else {
                    //jika love kosong
                    //simpan ke sqlite
                    long id = database.insertData(dataNama,dataGambar, dataAlamat, dataDeskripsi, "12.232", "3.212");

                    Log.d(TAG, "id kembalian: "+id);
                    if (id<=0){
                        Snackbar.make(view, "Favorit gagal ditambahkan ke database", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    } else {
                        Snackbar.make(view, "Favorit ditambahkan ke database", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();

                        //bikin true
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putBoolean(Konstanta.TAG_PREF+dataNama, true);
                        editor.commit();
                        isFavorit = true;
                    }

                }

                cekFavorit(isFavorit);

            }
        });



        TextView tvAlamat = (TextView) findViewById(R.id.tv_detail_alamat);
        TextView tvDeskripsi = (TextView) findViewById(R.id.tv_detail_deskripsi);
        ImageView ivGambar = (ImageView) findViewById(R.id.iv_detail_gambar);

        tvAlamat.setText(dataAlamat);
        tvDeskripsi.setText(dataDeskripsi);
        Glide.with(DetailWisataActivity.this).load("http://52.187.117.60/wisata_semarang/img/wisata/"+dataGambar).into(ivGambar);

        getSupportActionBar().setTitle(dataNama);

    }

    private void cekFavorit(Boolean isFavorit) {
        //kalau true image favorit
        //kalau false image notfavorit
        if (isFavorit){
            fab.setImageResource(R.drawable.ic_action_isfavorit);
        } else {
            fab.setImageResource(R.drawable.ic_action_isnotfavorit);
        }
    }
}
