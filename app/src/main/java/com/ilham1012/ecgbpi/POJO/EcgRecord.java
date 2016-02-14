package com.ilham1012.ecgbpi.POJO;

/**
 * Created by ilham on 12/02/2016.
 */
public class EcgRecord {
    int recording_id;
    int user_id;
    public String recording_time;
    public String recording_name;
    String file_url;

    public EcgRecord(){

    }

    public EcgRecord(int recording_id, int user_id, String recording_time, String recording_name, String file_url){
        this.recording_id = recording_id;
        this.user_id = user_id;
        this.recording_time = recording_time;
        this.recording_name = recording_name;
        this.file_url = file_url;
    }
}
