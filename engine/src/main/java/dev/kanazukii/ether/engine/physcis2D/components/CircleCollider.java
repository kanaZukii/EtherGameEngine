package dev.kanazukii.ether.engine.physcis2D.components;

import dev.kanazukii.ether.engine.components.Component;

public class CircleCollider extends Component {
    private float radius = 1f;

    public void radius(float value){
        radius = value;
    }

    public float getRadius(){
        return radius;
    }
}
