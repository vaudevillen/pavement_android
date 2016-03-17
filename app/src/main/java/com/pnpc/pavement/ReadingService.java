package com.pnpc.pavement;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by jjshin on 3/10/16.
 */
public interface ReadingService {

//    @FormUrlEncoded
    @POST("readings")
    Call<Reading> postReading(@Body Reading reading);
//    Call<Reading> createReading(@Field("start_lat") double start_lat, @Field("start_lon") double start_lon, @Field("end_lat") double end_lat,
//        @Field("end_lon") double end_lon, @Field("acceleration_x") String acceleration_x, @Field("acceleration_y") String acceleration_y,
//        @Field("acceleration_z") String acceleration_z, @Field("ride_id") int ride_id);

//
//    @FormUrlEncoded
//    @POST("readings")
//    Call<ReadingWrapper> createWrappedReading(@Field("reading") Reading reading);
}
