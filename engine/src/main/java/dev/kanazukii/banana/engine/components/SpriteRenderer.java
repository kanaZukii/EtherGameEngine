package dev.kanazukii.banana.engine.components;

import org.joml.Vector4f;

import dev.kanazukii.banana.engine.Component;

public class SpriteRenderer extends Component{
    
    private Vector4f color;

    public SpriteRenderer(Vector4f color){
        this.color = color;
    }

    public Vector4f getColor(){
        return color;
    }

    @Override
    public void start(){
        
    }

    @Override
    public void update(float deltaTime){
        
    }
}
