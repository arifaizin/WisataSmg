package org.sandec.wisatasmg.fragment;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.sandec.wisatasmg.R;
import org.sandec.wisatasmg.adapter.WisataAdapter;
import org.sandec.wisatasmg.model.ListWisataModel;
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
public class MapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    ArrayList<WisataModel> listData;


    public MapFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        listData = new ArrayList<>();

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-6.9883853, 110.4062757);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,15));
        ambilData();

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

                        for (int i = 0; i < listData.size(); i++) {

                            Double lat = Double.valueOf(listData.get(i).getLatitudeWisata());
                            Double lng = Double.valueOf(listData.get(i).getLongitudeWisata());

                            LatLng sydney = new LatLng(lat,lng);
                            mMap.addMarker(new MarkerOptions().position(sydney).title(listData.get(i).getNamaWisata()));




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
