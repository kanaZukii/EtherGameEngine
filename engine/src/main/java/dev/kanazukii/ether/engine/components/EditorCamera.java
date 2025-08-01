package dev.kanazukii.ether.engine.components;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT_CONTROL;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_PERIOD;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_RIGHT;

import org.joml.Vector2f;

import dev.kanazukii.ether.engine.Camera;
import dev.kanazukii.ether.engine.KeyListener;
import dev.kanazukii.ether.engine.MouseListener;

public class EditorCamera extends Component {

    private Camera levelEditorCamera;
    private boolean reset = false;

    private float lerpTime = 0.0f;

    // For controlling camera with keyboard
    private float cameraSpeed = 400f;
    private Vector2f targetPosition;


    // For controlling camera with mouse and zooming
    private Vector2f clickOrigin;
    private float dragDebounce = 0.05f;
    private float dragSensitivity = 25.0f;
    private float scrollSensitivity = 0.1f;

    public EditorCamera(Camera camera) {
        this.levelEditorCamera = camera;
        this.targetPosition = new Vector2f(camera.getPosition());
        this.clickOrigin = new Vector2f(camera.getPosition());
    }

     // For mouse dragging and camera panning  and zooming controls 
    private void mouseControls(float deltaTime) {
        // If reset flag is not true allow zooming and keyboard controls
        if(MouseListener.getScrollY() != 0.0f && !reset){
            float addValue = (float)Math.pow(Math.abs(MouseListener.getScrollY()* scrollSensitivity), 1 / levelEditorCamera.getZoom());
            addValue *= -Math.signum(MouseListener.getScrollY());
            levelEditorCamera.addZoom(addValue);
        }

        // If keyboard controls is being used disable click and drag panning
        if(KeyListener.isKeyPressed(GLFW_KEY_LEFT_CONTROL) && !reset){return;}

        if(MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_RIGHT) && dragDebounce > 0.0f){
            clickOrigin = new Vector2f(MouseListener.getOrthoX(), MouseListener.getOrthoY());
            dragDebounce -= deltaTime;
            return;
        } else if (MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_RIGHT)){
            Vector2f newMousePos =  new Vector2f(MouseListener.getOrthoX(), MouseListener.getOrthoY());
            Vector2f delta = new Vector2f(newMousePos).sub(clickOrigin);
            levelEditorCamera.getPosition().sub(delta.mul(deltaTime).mul(dragSensitivity));
            clickOrigin.lerp(newMousePos, deltaTime);
        }

        if(!MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_RIGHT) && dragDebounce <= 0.0f) dragDebounce = 0.05f;
    }

    // Keyboard controls of the camera movement and also reset keybind
    private void keyboardControls(float deltaTime){
        if(KeyListener.isKeyPressed(GLFW_KEY_PERIOD)){
            reset = true;
        }

        // Set targetposition to current camera position when not being controlled by the keyboard
        if(!KeyListener.isKeyPressed(GLFW_KEY_LEFT_CONTROL) && !reset){ 
            targetPosition = levelEditorCamera.getPosition(); 
            return;
        }

            if (KeyListener.isKeyPressed(GLFW_KEY_W)) {
                targetPosition.add(0, cameraSpeed * deltaTime);
            }
            if (KeyListener.isKeyPressed(GLFW_KEY_A)) {
                targetPosition.sub(cameraSpeed * deltaTime, 0);
            }
            if (KeyListener.isKeyPressed(GLFW_KEY_S)) {
                targetPosition.sub(0 , cameraSpeed * deltaTime);
            }
            if (KeyListener.isKeyPressed(GLFW_KEY_D)) {
                targetPosition.add(cameraSpeed * deltaTime, 0);
            }

            levelEditorCamera.getPosition().lerp(targetPosition, deltaTime);
        
    }

    @Override
    public void editorUpdate(float deltaTime) {
        mouseControls(deltaTime);
        keyboardControls(deltaTime);

        // Reset to coordinates (0, 0) and default zoom
        if(reset){
            levelEditorCamera.getPosition().lerp(new Vector2f(0,0), lerpTime);
            levelEditorCamera.setZoom(levelEditorCamera.getZoom() 
                                        + ((1.0f - levelEditorCamera.getZoom()) * lerpTime));    
            lerpTime += 0.1f * deltaTime;
            if(Math.abs(levelEditorCamera.getPosition().x) <= 5 && Math.abs(levelEditorCamera.getPosition().y) <= 5){
                levelEditorCamera.getPosition().set(0,0);
                levelEditorCamera.setZoom(1.0f);
                lerpTime = 0;
                reset = false;
            }
        }
    }
}
