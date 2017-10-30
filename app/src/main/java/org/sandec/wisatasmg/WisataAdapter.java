package org.sandec.wisatasmg;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by idn on 10/30/2017.
 */

public class WisataAdapter extends RecyclerView.Adapter<WisataAdapter.MyViewHolder> {
    private ArrayList<WisataModel> listData;
    private Context context;

    public WisataAdapter(ArrayList<WisataModel> listData, Context context) {
        this.listData = listData;
        this.context = context;
    }

    //Mengubungkan dengan layout itemnya
    @Override
    public WisataAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.wisata_item_list, parent, false);
        return new MyViewHolder(itemView);
    }

    //Buat meset item RecyclerView
    @Override
    public void onBindViewHolder(WisataAdapter.MyViewHolder holder, int position) {
        holder.tvNamaWisata.setText(listData.get(position).getNamaWisata());
        holder.tvAlamatWisata.setText(listData.get(position).getAlamatWisata());
        Glide.with(context)
                .load("http://52.187.117.60/wisata_semarang/img/wisata/"+listData.get(position).getGambarWisata())
                .error(R.drawable.no_image_found)
                .into(holder.ivGambarWisata);
    }

    //Jumlah Item
    @Override
    public int getItemCount() {
        return listData.size();
    }

    //Inisialisasi Widger pada item
    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView ivGambarWisata;
        TextView tvNamaWisata, tvAlamatWisata;
        public MyViewHolder(View itemView) {
            super(itemView);
            ivGambarWisata = (ImageView) itemView.findViewById(R.id.iv_item_gambar);
            tvAlamatWisata = (TextView) itemView.findViewById(R.id.tv_item_alamat);
            tvNamaWisata = (TextView) itemView.findViewById(R.id.tv_item_nama);
        }
    }
}
