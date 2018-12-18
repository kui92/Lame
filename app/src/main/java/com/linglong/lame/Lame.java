package com.linglong.lame;

/**
 * Created by kui on 2018/5/4.
 */

public class Lame {

    static {
        System.loadLibrary("lamemp3");
    }

    public native void init(int channel, int sampleRate, int brate);
    public native void destroy();
    public native byte[] encode(short[] buffer, int len);

}
