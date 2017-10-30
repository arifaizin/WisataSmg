package org.sandec.wisatasmg.networking;

import org.sandec.wisatasmg.ListWisataModel;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by idn on 10/30/2017.
 */

public interface ApiServices {
    @GET("read_wisata.php")
    Call<ListWisataModel> ambilDataWisata();
}
