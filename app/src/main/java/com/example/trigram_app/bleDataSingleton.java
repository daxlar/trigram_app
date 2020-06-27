package com.example.trigram_app;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class bleDataSingleton {

    public static HashMap<String, Integer> macDataBuffer1 = new HashMap<>();
    public static HashMap<String, Integer> macDataBuffer2 = new HashMap<>();

    // false is macDataBuffer1;
    // true is macDataBuffer2;
    public static AtomicBoolean bufferDecider = new AtomicBoolean(false);
    public static AtomicBoolean finishedDataOperation = new AtomicBoolean(false);

}
