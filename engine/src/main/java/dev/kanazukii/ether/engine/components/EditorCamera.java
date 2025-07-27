package dev.kanazukii.ether.engine.components;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_0;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_RIGHT;

import org.joml.Vector2f;

import dev.kanazukii.ether.engine.Camera;
import dev.kanazukii.ether.engine.KeyListener;
import dev.kanazukii.ether.engine.MouseListener;

public class EditorCamera extends Component {
    private Camera levelEditorCamera;
    private Vector2f targetPosition;
    private float cameraSpeed = 200f;
    private float lerpFactor = 10f;

    public EditorCamera(Camera camera) {
        this.levelEditorCamera = camera;
        this.targetPosition = new Vector2f(camera.getPosition()); // Start at current
    }

    @Override
    public void update(float deltaTime) {
        // Example: Move right if D key is pressed
        if (KeyListener.isKeyPressed(GLFW_KEY_D)) {
            targetPosition.add(cameraSpeed * deltaTime, 0);
        }

        if (KeyListener.isKeyPressed(GLFW_KEY_A)) {
            targetPosition.sub(cameraSpeed * deltaTime, 0);
        }

        if (KeyListener.isKeyPressed(GLFW_KEY_W)) {
            targetPosition.add(0, cameraSpeed * deltaTime);
        }

        if (KeyListener.isKeyPressed(GLFW_KEY_S)) {
            targetPosition.sub(0 , cameraSpeed * deltaTime);
        }

        // Smoothly lerp from current position to target
        levelEditorCamera.getPosition().lerp(targetPosition, deltaTime * lerpFactor);
    }
}
