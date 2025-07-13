package dev.kanazukii.ether.engine;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Camera {
    
    // To optimize projection to match the screen resolution
    private Matrix4f projectionMatrix, viewMatrix;

    // Varibales in controlling the camera in 2d plane (The vector for the bottom left corner of the camera)
    private Vector2f position;

    public Camera(Vector2f position){
        this.position = position;
        this.projectionMatrix = new Matrix4f();
        this.viewMatrix = new Matrix4f();
        adjustProjection();
    }

    // Getter of the position vector use this in moving the camera
    public Vector2f getPosition(){
        return position;
    }

    // View matrix specifies where should the camera is looking (in 3D space)
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

    // Camera view area base on game tile size (in pixels) and the farthest z index
    public void adjustProjection(){
        float tileSize = 32.0f;
        float width = 40.0f;
        float height = 21.0f;
        float farZ = 100.0f;

        projectionMatrix.identity();
        projectionMatrix.ortho(0.0f, tileSize * width, 0.0f, tileSize * height, 0, farZ);
    }
}
