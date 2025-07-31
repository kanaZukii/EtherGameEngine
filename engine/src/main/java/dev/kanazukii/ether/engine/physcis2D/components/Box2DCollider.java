package dev.kanazukii.ether.engine.physcis2D.components;

import org.joml.Vector2f;

import dev.kanazukii.ether.engine.components.Component;

public class Box2DCollider extends Component {
    private Vector2f halfSize = new Vector2f(1);

    public Vector2f getHalfSize(){
        return halfSize;
    }

    public void setHalfSize(Vector2f halfsize){
        this.halfSize = halfsize;
    }

}
