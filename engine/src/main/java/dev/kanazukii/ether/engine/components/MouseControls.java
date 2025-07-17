package dev.kanazukii.ether.engine.components;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_1;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

import dev.kanazukii.ether.engine.GameObject;
import dev.kanazukii.ether.engine.MouseListener;
import dev.kanazukii.ether.engine.Window;

public class MouseControls extends Component {
    
    private GameObject heldObject = null;

    public void pickUpObject(GameObject gameObject){
        this.heldObject = gameObject;
        Window.getScene().addGameObject(gameObject);
    }

    public void placeObject(){
        System.out.println("Placed: " + heldObject.getName());
        this.heldObject = null;
    }

    @Override
    public void update(float deltaTime){
        if(heldObject != null){
            heldObject.transform.position.x = MouseListener.getOrthoX() - 40;
            heldObject.transform.position.y = MouseListener.getOrthoY() - 40;

            if(MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_1)){
                placeObject();
            }
        }
    }

}
