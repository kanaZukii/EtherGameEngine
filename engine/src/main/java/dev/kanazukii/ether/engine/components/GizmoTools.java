package dev.kanazukii.ether.engine.components;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_C;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_X;

import dev.kanazukii.ether.engine.KeyListener;
import dev.kanazukii.ether.engine.Window;

public class GizmoTools extends Component{
    
    private Spritesheet gizmoSprites;
    private int currentGizmo = 0;

    public GizmoTools(Spritesheet gizmoSprites){
        this.gizmoSprites = gizmoSprites;
    }

    @Override
    public void start(){
        gameObject.addComponent(new TranslateGizmo(gizmoSprites.getSprite(0), Window.getImGUILayer().getInspector()));
        gameObject.addComponent(new ScaleGizmo(gizmoSprites.getSprite(1), Window.getImGUILayer().getInspector()));
        
    }

    @Override
    public void update(float deltaTime){
        if(currentGizmo == 0){
            gameObject.getComponent(ScaleGizmo.class).setNotUsing();
            gameObject.getComponent(TranslateGizmo.class).setUsing();
        } else if(currentGizmo == 1){
            gameObject.getComponent(TranslateGizmo.class).setNotUsing();
            gameObject.getComponent(ScaleGizmo.class).setUsing();
        }

        if(KeyListener.isKeyPressed(GLFW_KEY_X)){
            currentGizmo =  0;
        }

        if(KeyListener.isKeyPressed(GLFW_KEY_C)){
            currentGizmo =  1;
        }
    }

}
