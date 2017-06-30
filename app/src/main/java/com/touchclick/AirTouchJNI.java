package com.touchclick;

public class AirTouchJNI
{
    static{

        try {
            System.loadLibrary("airTouch");
        }
        catch (UnsatisfiedLinkError ule) {
            System.err.println("WARNING: Could not load lib3djni.so");
            ule.printStackTrace();
        }
    }
//    com.touchclick.AirTouchJNI.mainFromJNI
    public native static String mainFromJNI();
    public native static String clickFromJNI();
    public native static String stayWackupFromJNI();
}