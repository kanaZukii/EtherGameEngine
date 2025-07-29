package dev.kanazukii.ether.engine.editor;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

import dev.kanazukii.ether.engine.GameObject;
import dev.kanazukii.ether.engine.MouseListener;
import dev.kanazukii.ether.engine.components.Uneditable;
import dev.kanazukii.ether.engine.renderer.PickingTexture;
import dev.kanazukii.ether.engine.scenes.Scene;
import imgui.ImGui;

public class InspectorWindow {

    private GameObject selectedGameObject = null;
    private PickingTexture pickingTex;

    private boolean disableSelection = false;

    private float debounceTime = 0.2f;

    public InspectorWindow(PickingTexture pickingTex){
        this.pickingTex = pickingTex;
    }

    public void ImGUI(){
        if(selectedGameObject != null){
            ImGui.begin("Inspector: " + selectedGameObject.getName());
            selectedGameObject.ImGUI();
            ImGui.end();
        }
    }

    public void update(float deltaTime, Scene currentScene){
        debounceTime -= deltaTime;

        if(MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT) && debounceTime < 0){
                int x = (int)MouseListener.getSreenX();
                int y = (int)MouseListener.getSreenY();
                int uid = pickingTex.readPixel(x, y);
                debounceTime = 0.2f;
                if(currentScene.getGameObjectByUID(uid) !=  null){
                    if(currentScene.getGameObjectByUID(uid).getComponent(Uneditable.class) !=  null) return;
                }
                
                if(!disableSelection){
                    selectedGameObject = currentScene.getGameObjectByUID(uid);
                }
        }
    }

    public void canSelectObject(boolean value){
        disableSelection = !value;
    }

    public GameObject getSelectedGameObject(){
        return selectedGameObject;
    }

}
