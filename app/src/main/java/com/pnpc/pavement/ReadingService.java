package com.pnpc.pavement;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by jjshin on 3/10/16.
 */
public interface ReadingService {

    @FormUrlEncoded
    @POST("readings")
    Call<Reading> createReading(@Field("reading") Reading reading);

}
