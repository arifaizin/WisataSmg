package org.sandec.wisatasmg.fragment;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.sandec.wisatasmg.model.ListWisataModel;
import org.sandec.wisatasmg.R;
import org.sandec.wisatasmg.adapter.WisataAdapter;
import org.sandec.wisatasmg.model.WisataModel;
import org.sandec.wisatasmg.networking.ApiServices;
import org.sandec.wisatasmg.networking.RetrofitConfig;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {


    public HomeFragment() {
        // Required empty public constructor
    }

    //kenalin
    RecyclerView recyclerView;
    ArrayList<WisataModel> listData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        //hubungin
        recyclerView = (RecyclerView) view.findViewById(R.id.recyler_view);

        //Data
        listData = new ArrayList<>();

//        for (int i = 0; i < 10; i++) {
//            //fori -> bikin perulangan 'for' otomatis
//            WisataModel wisata1 = new WisataModel("Lawang Sewu", "Tugu Muda", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTatQ9906QslS6aaYdW6wN7h8uKg1on1j0x_xbFSaI19Wplkuw3pQ");
//            listData.add(wisata1);
//        }
        ambilData();

        //adapter
        WisataAdapter adapter = new WisataAdapter(listData, getActivity());
        recyclerView.setAdapter(adapter);

        // layout manager
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }

    private void ambilData() {
        final ProgressDialog progress = new ProgressDialog(getActivity());
        progress.setTitle("Loading");
        progress.setMessage("Mohon Bersabar");
        progress.show();

        ApiServices api = RetrofitConfig.getApiServices();
        Call<ListWisataModel> call = api.ambilDataWisata();
        call.enqueue(new Callback<ListWisataModel>() {
            @Override
            public void onResponse(Call<ListWisataModel> call, Response<ListWisataModel> response) {
                progress.hide();
                if (response.isSuccessful()){
                    if(response.body().getSuccess().toString().equals("true")){
                        listData = response.body().getWisata();
                        WisataAdapter adapter = new WisataAdapter(listData, getActivity());
                        recyclerView.setAdapter(adapter);
                        for (int i = 0; i < listData.size(); i++) {
                            Log.d(TAG, "onResponse: " + listData.get(i).getGambarWisata());
                        }
                    } else {
                        Toast.makeText(getActivity(), response.body().getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Respones is Not Succesfull", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ListWisataModel> call, Throwable t) {
                Toast.makeText(getActivity(), "Response Failure", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
