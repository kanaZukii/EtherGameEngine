package dev.kanazukii.ether.engine.components;

import org.joml.Vector2f;
import org.joml.Vector4f;

import dev.kanazukii.ether.engine.GameObject;
import dev.kanazukii.ether.engine.Prefabs;
import dev.kanazukii.ether.engine.Window;
import dev.kanazukii.ether.engine.editor.InspectorWindow;
import dev.kanazukii.ether.engine.utils.Configs;

public class TranslateGizmo extends Component {
    private Vector4f xAxisColor = new Vector4f(0.0f, 0.0f, 1.0f, 0.75f);
    private Vector4f yAxisColor = new Vector4f(1.0f, 0.0f, 0.0f, 0.75f);
    private Vector4f hoverColor = new Vector4f(1.0f, 1.0f, 0.0f, 0.9f);
    private Vector2f yAxisOffset = new Vector2f(-Configs.GRID_WIDTH-2,-Configs.GRID_HEIGHT-2);
    private Vector2f xAxisOffset = new Vector2f(Configs.GRID_WIDTH*2,-Configs.GRID_HEIGHT-2);

    private GameObject xAxisObject;
    private GameObject yAxisObject;
    private SpriteRenderer xAxisSprite;
    private SpriteRenderer yAxisSprite;

    private GameObject activeGameObject = null;
    private InspectorWindow inspector;

    public TranslateGizmo(Sprite arrowSprite, InspectorWindow inspector){
        this.inspector = inspector; 

        xAxisObject = Prefabs.createSpriteObj(arrowSprite, 100, 100, 100);
        yAxisObject = Prefabs.createSpriteObj(arrowSprite, 100, 100, 100);
        xAxisSprite = xAxisObject.getComponent(SpriteRenderer.class);
        yAxisSprite = yAxisObject.getComponent(SpriteRenderer.class);

        Window.getScene().addGameObject(xAxisObject);
        Window.getScene().addGameObject(yAxisObject);
    }

    @Override
    public void start(){
        this.xAxisObject.transform.rotation =  90.0f;
        xAxisObject.setSerialize(false);
        yAxisObject.setSerialize(false);
    }

    @Override
    public void update(float deltaTime){
        activeGameObject = inspector.getSelectedGameObject();

        if(activeGameObject != null){
            setActive();
            Vector2f xGizmoPosition = new Vector2f(activeGameObject.transform.position).add(xAxisOffset);
            Vector2f yGizmoPosition = new Vector2f(activeGameObject.transform.position).add(yAxisOffset);
            xAxisObject.transform.position.set(xGizmoPosition);
            yAxisObject.transform.position.set(yGizmoPosition);
        } else{
            setInactive();
        }
        
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
}
