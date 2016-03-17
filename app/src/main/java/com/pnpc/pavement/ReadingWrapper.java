package com.pnpc.pavement;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jjshin on 3/15/16.
 */
public class ReadingWrapper {
    @SerializedName("reading")
    public Reading reading;

    public ReadingWrapper(Reading reading){
        this.reading = reading;
    }

}
