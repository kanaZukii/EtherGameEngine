package dev.kanazukii.ether.engine.physcis2D.components;

import org.joml.Vector2f;


public class Box2DCollider extends Collider {
    private Vector2f halfSize = new Vector2f(1);
    private Vector2f origin = new Vector2f(0);

    public Vector2f getHalfSize(){
        return halfSize;
    }

    public void setHalfSize(Vector2f halfsize){
        this.halfSize = halfsize;
    }

    public void setOrigin(float x, float y){
        origin.set(x, y);
    }

    public Vector2f getOrigin(){
        return origin;
    }


}
