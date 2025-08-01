package dev.kanazukii.ether.engine.components;

import dev.kanazukii.ether.engine.MouseListener;
import dev.kanazukii.ether.engine.editor.InspectorWindow;

public class ScaleGizmo extends Gizmo {

    public ScaleGizmo(Sprite scaleSprite, InspectorWindow inspector) {
        super(scaleSprite, inspector);
    }

    @Override
    public void editorUpdate(float deltaTime){
        
        if(activeGameObject != null){
            if(xAxisActive){
                activeGameObject.transform.scale.x -= MouseListener.getWorldDx();
            }

            if(yAxisActive){
                activeGameObject.transform.scale.y -= MouseListener.getWorldDy();
            }
        }
        
        super.editorUpdate(deltaTime);
    }
    
}
