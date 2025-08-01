package dev.kanazukii.ether.engine.components;

import dev.kanazukii.ether.engine.MouseListener;
import dev.kanazukii.ether.engine.editor.InspectorWindow;

public class TranslateGizmo extends Gizmo {

    public TranslateGizmo(Sprite arrowSprite, InspectorWindow inspector){
        super(arrowSprite, inspector); 
    }

    @Override
    public void editorUpdate(float deltaTime){

        if(activeGameObject != null){
            if(xAxisActive){
                activeGameObject.transform.position.x -= MouseListener.getWorldDx();
            }

            if(yAxisActive){
                activeGameObject.transform.position.y -= MouseListener.getWorldDy();
            }
        }
        
        super.editorUpdate(deltaTime);
    }
}
