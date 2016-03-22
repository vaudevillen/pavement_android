package com.pnpc.pavement;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;

/**
 * Created by jjshin on 3/10/16.
 */
public interface PavementAPIService {

    @POST("readings")
    Call<Reading> postReading(@Body Reading reading);

    @POST("rides")
    Call<Ride> createRide(@Body Ride ride);

    @PUT("rides")
    Call<Ride> putRide(@Body Ride ride);

}
