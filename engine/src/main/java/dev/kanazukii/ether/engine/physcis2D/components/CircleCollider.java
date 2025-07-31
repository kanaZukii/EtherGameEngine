package dev.kanazukii.ether.engine.physcis2D.components;

public class CircleCollider extends Collider {
    private float radius = 1f;

    public void radius(float value){
        radius = value;
    }

    public float getRadius(){
        return radius;
    }
}
