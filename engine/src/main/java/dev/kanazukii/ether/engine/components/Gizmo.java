package dev.kanazukii.ether.engine.components;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT_CONTROL;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

import org.joml.Vector2f;
import org.joml.Vector4f;

import dev.kanazukii.ether.engine.GameObject;
import dev.kanazukii.ether.engine.KeyListener;
import dev.kanazukii.ether.engine.MouseListener;
import dev.kanazukii.ether.engine.Prefabs;
import dev.kanazukii.ether.engine.Window;
import dev.kanazukii.ether.engine.editor.InspectorWindow;
import dev.kanazukii.ether.engine.utils.Configs;

public class Gizmo extends Component {
    private Vector4f xAxisColor = new Vector4f(0.1f, 0.1f, 0.9f, 0.8f);
    private Vector4f yAxisColor = new Vector4f(0.9f, 0.1f, 0.1f, 0.8f);
    private Vector4f hoverColor = new Vector4f(1.0f, 1.0f, 0.1f, 0.8f);
    private Vector2f yAxisOffset = new Vector2f(0,0);
    private Vector2f xAxisOffset = new Vector2f(0,0);

    private GameObject xAxisObject;
    private GameObject yAxisObject;
    private SpriteRenderer xAxisSprite;
    private SpriteRenderer yAxisSprite;

    protected boolean xAxisActive = false;
    protected boolean yAxisActive = false;

    protected GameObject activeGameObject = null;
    private InspectorWindow inspector;

    private boolean using = false;
    private boolean gameStart = true;

    private float gizmoWidth =  Configs.GRID_WIDTH;
    private float gizmoHeight =  gizmoWidth * 2;

    public Gizmo(Sprite arrowSprite, InspectorWindow inspector){
        this.inspector = inspector; 
        xAxisObject = Prefabs.createSpriteObj(arrowSprite, gizmoWidth, gizmoHeight, 100);
        yAxisObject = Prefabs.createSpriteObj(arrowSprite, gizmoWidth, gizmoHeight, 100);
        xAxisSprite = xAxisObject.getComponent(SpriteRenderer.class);
        yAxisSprite = yAxisObject.getComponent(SpriteRenderer.class);

        xAxisObject.addComponent(new Uneditable());
        yAxisObject.addComponent(new Uneditable());

        yAxisOffset.set(0,gizmoHeight/2.5f);
        xAxisOffset.set(gizmoWidth/1.25f,0);

        Window.getScene().addGameObject(xAxisObject);
        Window.getScene().addGameObject(yAxisObject);
        
    }

    @Override
    public void start(){
        this.xAxisObject.transform.rotation =  -90.0f;
        xAxisObject.setSerialize(false);
        yAxisObject.setSerialize(false);
    }

    @Override
    public void update(float deltaTime){
        if(gameStart) {
            setInactive(); 
            gameStart = false;
            return;
        }
    }

    @Override
    public void editorUpdate(float deltaTime){
        gameStart = true;

        if(!using) {
            setInactive(); 
            return;
        }

        activeGameObject = inspector.getSelectedGameObject();

        if(activeGameObject != null){
            setActive();
            // TODO: Move to its own keyBinding component clas
            if(!KeyListener.isKeyPressed(GLFW_KEY_LEFT_CONTROL) &&
                (KeyListener.isKeyPressed(GLFW_KEY_D) && KeyListener.isKeyBeginPress(GLFW_KEY_D))){
                GameObject newObj = this.activeGameObject.copy();
                Window.getScene().addGameObject(newObj);
                newObj.transform.position.add(0.1f, 0.1f);
                inspector.setSelectedGameObject(newObj);
                return;
            }
        } else{
            setInactive();
            return;
        }

        boolean xAxisHover = xCheckHover();
        boolean yAxisHover = yCheckHover();

        if(xAxisHover){
            xAxisSprite.setColor(hoverColor);
        }

        if(yAxisHover){
            yAxisSprite.setColor(hoverColor);
        }

        if((xAxisHover || xAxisActive) && MouseListener.isDragging() && MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT) && !yAxisActive){
            xAxisActive = true;
            yAxisActive = false;
            inspector.canSelectObject(false);
        } else{
            xAxisActive = false;
        }

        if((yAxisHover || yAxisActive) && MouseListener.isDragging() && MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT) && !xAxisActive){
            xAxisActive = false;
            yAxisActive = true;
            inspector.canSelectObject(false);
        }else{
            yAxisActive = false;
        }

        if(!xAxisActive && !yAxisActive)  inspector.canSelectObject(true);

        Vector2f xGizmoPosition = new Vector2f(activeGameObject.transform.position).add(xAxisOffset);
        Vector2f yGizmoPosition = new Vector2f(activeGameObject.transform.position).add(yAxisOffset);
        xAxisObject.transform.position.set(xGizmoPosition);
        yAxisObject.transform.position.set(yGizmoPosition);
        
    }

    private void setActive(){
        xAxisSprite.setColor(xAxisColor);
        yAxisSprite.setColor(yAxisColor);
    }

    private void setInactive(){
        activeGameObject = null;
        xAxisSprite.setColor(new Vector4f(0,0,0,0));
        yAxisSprite.setColor(new Vector4f(0,0,0,0));
    }

    private boolean xCheckHover(){
        Vector2f mousePosition = new Vector2f(MouseListener.getOrthoX(), MouseListener.getOrthoY());
        return mousePosition.x >= xAxisObject.transform.position.x - (gizmoWidth/2.0f) && mousePosition.x <= xAxisObject.transform.position.x  + (gizmoHeight/2.0f)
            && mousePosition.y >= xAxisObject.transform.position.y - (gizmoWidth/2.0f) && mousePosition.y <= xAxisObject.transform.position.y + (gizmoWidth/2.0f);
    }

    private boolean yCheckHover(){
        Vector2f mousePosition = new Vector2f(MouseListener.getOrthoX(), MouseListener.getOrthoY());
        return mousePosition.x >= yAxisObject.transform.position.x - (gizmoWidth/2.0f) && mousePosition.x <= yAxisObject.transform.position.x + (gizmoWidth/2.0f)
            && mousePosition.y >= yAxisObject.transform.position.y - (gizmoHeight/2.0f) && mousePosition.y <= yAxisObject.transform.position.y + (gizmoHeight/2.0f);
    }

    public void setUsing(){
        using = true;
    }

    public void setNotUsing(){
        using = false;
    }

}
