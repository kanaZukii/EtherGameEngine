package dev.kanazukii.ether.engine.physcis2D.components;

import org.joml.Vector2f;

import dev.kanazukii.ether.engine.components.Component;

public abstract class Collider extends Component{
    private Vector2f offset = new Vector2f(0);

    public Vector2f getOffset(){
        return offset;
    }

    public void setOffset(float offsetX, float offsetY){
        offset.set(offsetX, offsetY);
    }

}
