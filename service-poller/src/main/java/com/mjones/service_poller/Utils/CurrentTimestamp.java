package com.mjones.service_poller.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CurrentTimestamp {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    public String getDate(){
        Date date = new Date();
        return dateFormat.format(date);
    }
}
