package com.ilham1012.ecgbpi.POJO;

/**
 * Created by Lab Desain 2 on 12/02/2016.
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

//private List<EcgRecord> ecgRecords;
//
///**
// * Dummy data for examples only
// */
//
//private void initializeData(){
//    ecgRecords = new ArrayList<>();
//    ecgRecords.add(new EcgRecord(1,  1, "2016-02-06 10:38:19", "test", "test.txt"));
//    ecgRecords.add(new EcgRecord(2,  1, "2016-02-06 10:47:27", "test 2", "test-2.txt"));
//    ecgRecords.add(new EcgRecord(3,  1, "2016-02-06 10:49:20", "Morning Record", "morning.txt"));
//    ecgRecords.add(new EcgRecord(4,  1, "2016-02-06 10:50:19", "Let's test", "testttt.txt"));
//    ecgRecords.add(new EcgRecord(5,  1, "2016-02-06 10:53:27", "Ok, test again", "test-2000.txt"));
//    ecgRecords.add(new EcgRecord(6,  1, "2016-02-06 10:59:20", "Night Record", "night.txt"));
//    ecgRecords.add(new EcgRecord(7,  1, "2016-02-06 11:38:19", "test 3", "test3.txt"));
//    ecgRecords.add(new EcgRecord(8,  1, "2016-02-06 11:47:27", "test 23", "test-23.txt"));
//    ecgRecords.add(new EcgRecord(9,  1, "2016-02-06 11:49:20", "Morning Record 2", "morning2.txt"));
//    ecgRecords.add(new EcgRecord(10, 1, "2016-02-06 11:50:19", "Let's test 2", "testttt2.txt"));
//    ecgRecords.add(new EcgRecord(11, 1, "2016-02-06 11:53:27", "Ok, test again 2", "test-20002.txt"));
//    ecgRecords.add(new EcgRecord(12, 1, "2016-02-06 11:59:20", "Night Record 2", "night2.txt"));
//    ecgRecords.add(new EcgRecord(13, 1, "2016-02-06 12:50:19", "Let's test 3", "testttt3.txt"));
//    ecgRecords.add(new EcgRecord(14, 1, "2016-02-06 12:53:27", "Ok, test again 3", "test-20003.txt"));
//    ecgRecords.add(new EcgRecord(15, 1, "2016-02-06 12:59:20", "Night Record 3", "night3.txt"));
//}
