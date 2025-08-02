package dev.kanazukii.ether.engine.physcis2D.components;

import org.joml.Vector2f;

import dev.kanazukii.ether.engine.renderer.DebugRenderer;
import dev.kanazukii.ether.engine.utils.Configs;


public class Box2DCollider extends Collider {
    private Vector2f halfSize = new Vector2f(Configs.GRID_WIDTH);
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

    @Override
    public void editorUpdate(float deltaTime){
        Vector2f center = new Vector2f(gameObject.transform.position).add(offset);
        DebugRenderer.addRect2D(center, halfSize, gameObject.transform.rotation);
    }

}
