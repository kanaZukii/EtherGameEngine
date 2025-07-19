package dev.kanazukii.ether.engine;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Camera {
    
    // To optimize projection to match the screen resolution
    private Matrix4f projectionMatrix, viewMatrix, inverseProjection, inverseView;

    // Varibales in controlling the camera in 2d plane (The vector for the bottom left corner of the camera)
    private Vector2f position;

    private float worldUnits = 32.0f;
    private float width = 40.0f;
    private float height = 21.0f;
    private float farZ = 100.0f; // Farthest in the z axis (Doesn't really matter because we are in 2D)
    private Vector2f proejectionSize = new Vector2f(worldUnits * width, worldUnits * height); // 1280 x 672

    public Camera(Vector2f position){
        this.position = position;
        this.projectionMatrix = new Matrix4f();
        this.viewMatrix = new Matrix4f();
        this.inverseProjection = new Matrix4f();
        this.inverseView = new Matrix4f();
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
        viewMatrix.invert(inverseView);
        return viewMatrix;
    }

    public Matrix4f getProjectionMatrix(){
        return projectionMatrix;
    }

    public Vector2f getProjectionSize(){
        return proejectionSize;
    }

    // Camera view area base on game tile size (in pixels) and the farthest z index
    public void adjustProjection(){
        projectionMatrix.identity();
        projectionMatrix.ortho(0.0f, proejectionSize.x, 0.0f, proejectionSize.y, 0, farZ);
        projectionMatrix.invert(inverseProjection);
    }

    public Matrix4f getInverseProjection(){
        return inverseProjection;
    }

    public Matrix4f getInverseView(){
        return inverseView;
    }

}
