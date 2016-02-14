package com.ilham1012.ecgbpi.POJO;

/**
 * Created by ilham1012 on 2/14/16.
 */

import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * Definition of REST service available in BITalino Server.
 */
public interface EcgRecordService {

    @POST("/ekg_record/")
    Response uploadReading(@Body EcgRecord ecgRecord);

}
