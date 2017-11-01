package org.sandec.wisatasmg.fragment;


import android.Manifest;
import android.app.ProgressDialog;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.sandec.wisatasmg.R;
import org.sandec.wisatasmg.adapter.WisataAdapter;
import org.sandec.wisatasmg.drawroutemap.DrawMarker;
import org.sandec.wisatasmg.drawroutemap.DrawRouteMaps;
import org.sandec.wisatasmg.drawroutemap.FetchUrl;
import org.sandec.wisatasmg.helper.GPSTracker;
import org.sandec.wisatasmg.model.ListWisataModel;
import org.sandec.wisatasmg.model.WisataModel;
import org.sandec.wisatasmg.networking.ApiServices;
import org.sandec.wisatasmg.networking.RetrofitConfig;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback, EasyPermissions.PermissionCallbacks {

    private GoogleMap mMap;
    ArrayList<WisataModel> listData;

    final Integer RC_CAMERA_AND_LOCATION = 101;

    String jarak;
    String waktu;

    GPSTracker gpsTracker;


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

        methodRequiresTwoPermission();

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        LatLng origin = new LatLng(-7.788969, 110.338382);
        LatLng destination = new LatLng(-7.781200, 110.349709);
        DrawRouteMaps.getInstance(getContext())
                .draw(origin, destination, mMap);

        LatLngBounds bounds = new LatLngBounds.Builder()
                .include(origin)
                .include(destination).build();


        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(origin,15));
        getLocation(origin, destination, mMap);

        ambilData();

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                return false;
            }
        });

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {

            }
        });

        gpsTracker = new GPSTracker(getContext());

        Toast.makeText(getActivity(),gpsTracker.getLatitude()+"",Toast.LENGTH_SHORT).show();



    }

    private void methodRequiresTwoPermission() {
        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION};
        if (EasyPermissions.hasPermissions(getActivity(), perms)) {
            // Already have permission, do the thing
            // ...
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, "Butuh Permission",
                    RC_CAMERA_AND_LOCATION, perms);
        }
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

    private void getLocation(final LatLng origin, LatLng destination, final GoogleMap map){
        OkHttpClient client = new OkHttpClient();

        String url_route = FetchUrl.getUrl(origin, destination);

        Request request = new Request.Builder()
                .url(url_route)
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {

            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                Log.d("route",response.body().toString());

                try {

                    JSONObject json = new JSONObject(response.body().string());
                    JSONArray routes = json.getJSONArray("routes");

                    JSONObject distance = routes.getJSONObject(0)
                                                .getJSONArray("legs")
                                                .getJSONObject(0)
                                                .getJSONObject("distance");

                    JSONObject duration = routes.getJSONObject(0)
                            .getJSONArray("legs")
                            .getJSONObject(0)
                            .getJSONObject("duration");

                    jarak = distance.getString("text");
                    waktu = duration.getString("text");

                    Log.d("distance", distance.toString());
                    Log.d("duration", duration.toString());




                } catch (JSONException e) {

                }

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Toast.makeText(getContext(),"Hey",Toast.LENGTH_SHORT).show();
                        map.addMarker(new MarkerOptions()
                                .title("Origin")
                                .position(origin)
                                .snippet(jarak + " " + waktu));

                    }
                });




            }
        });

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> list) {
        // Some permissions have been granted
        // ...
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> list) {
        // Some permissions have been denied
        // ...
    }
}
