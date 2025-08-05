package dev.kanazukii.ether.engine.components;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_1;

import dev.kanazukii.ether.engine.GameObject;
import dev.kanazukii.ether.engine.MouseListener;
import dev.kanazukii.ether.engine.Window;
import dev.kanazukii.ether.engine.utils.Configs;

public class MouseControls extends Component {
    
    private GameObject heldObject = null;
    
    public void pickUpObject(GameObject gameObject){
        this.heldObject = gameObject;
        Window.getScene().addGameObject(gameObject);
    }

    // Allow the user to deselect or discard the current object the mouse is holding
    public void dropObject(){
        
    }

    public void placeObject(){
        System.out.println("Placed: " + heldObject.name);
        heldObject = null;
        //pickUpObject(Prefabs.duplicateSpriteObj(heldObject));
    }

    private float placeCD = 0.0f;
    
    @Override
    public void editorUpdate(float deltaTime){

        placeCD += deltaTime;

        if(placeCD > 0.25f){
            placeCD = 0.25f;
        }

        if(heldObject != null){
            heldObject.transform.position.x = MouseListener.getOrthoX();
            heldObject.transform.position.y = MouseListener.getOrthoY();
            heldObject.transform.position.x = (int)(heldObject.transform.position.x / Configs.GRID_WIDTH) * Configs.GRID_WIDTH  + (Configs.GRID_WIDTH/2);
            heldObject.transform.position.y = (int)(heldObject.transform.position.y / Configs.GRID_HEIGHT) * Configs.GRID_HEIGHT + (Configs.GRID_HEIGHT/2);

            if(MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_1) && placeCD >= 0.15f){
                placeObject();
                placeCD = 0.0f;
            }
        }
    }

}
