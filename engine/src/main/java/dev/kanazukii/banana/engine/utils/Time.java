package dev.kanazukii.banana.engine.utils;

public class Time {
    
    public static float timeStart = System.nanoTime();

    public static float getDeltaTimeNano(){
        return (float) System.nanoTime() - timeStart;
    }

    public static float getDeltaTime(){
        return (float)(getDeltaTimeNano() * 1E-9);
    }


}
