package dev.kanazukii.ether.engine.renderer;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class Line2D {
    
    private Vector2f start;
    private Vector2f end;
    private Vector3f color;
    private int duration;

    public Line2D(Vector2f start, Vector2f end, Vector3f color, int duration){
        this.start = start;
        this.end = end;
        this.color = color;
        this.duration = duration;
    }

    public int beginFrame(){
        duration--;
        return duration;
    }

    public void setStart(Vector2f start){
        this.start = start;
    }

    public void setEnd(Vector2f end){
        this.end = end;
    }

    public void setEnd(Vector3f color){
        this.color = color;
    }

    public void setDuration(int duration){
        this.duration = duration;
    }

    public int getDuration(){
        return duration;
    }

    public Vector2f getStart(){
        return start;
    }

    public Vector2f getEnd(){
        return end;
    }

    public Vector3f getColor(){
        return color;
    }

}
