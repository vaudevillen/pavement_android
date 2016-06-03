package com.pnpc.pavement.Services;

import com.pnpc.pavement.Models.Reading;
import com.pnpc.pavement.Models.Ride;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by jjshin on 3/10/16.
 */
public interface PavementAPIService {

    @POST("readings")
    Call<Reading> postReading(@Body Reading reading);

    @POST("rides")
    Call<Ride> createRide(@Body Ride ride);

    @PUT("rides/{id}")
    Call<Ride> putRide(@Path("id") int id, @Body Ride ride);

}
