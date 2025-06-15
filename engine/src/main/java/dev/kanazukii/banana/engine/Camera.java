package dev.kanazukii.banana.engine;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Camera {
    
    private Matrix4f projectionMatrix, viewMatrix;
    private Vector2f position;

    public Camera(Vector2f position){
        this.position = position;
        this.projectionMatrix = new Matrix4f();
        this.viewMatrix = new Matrix4f();
        adjustProjection();
    }

    public Vector2f getPosition(){
        return position;
    }

    public Matrix4f getViewMatrix(){
        Vector3f cameraFront = new Vector3f(0.0f, 0.0f, -1.0f);
        Vector3f cameraUp = new Vector3f(0.0f, 1.0f, 0.0f);
        viewMatrix.identity();
        viewMatrix = viewMatrix.lookAt( new Vector3f(position.x, position.y, 20.0f),
                                        cameraFront.add(position.x, position.y, 0.0f),
                                        cameraUp );

        return viewMatrix;
    }

    public Matrix4f getProjectionMatrix(){
        return projectionMatrix;
    }

    public void adjustProjection(){
        float tileSize = 32.0f;
        float width = 40.0f;
        float height = 21.0f;
        float farZ = 100.0f;

        projectionMatrix.identity();
        projectionMatrix.ortho(0.0f, tileSize * width, 0.0f, tileSize * height, 0, farZ);
    }
}
