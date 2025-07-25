package dev.kanazukii.ether.engine.editor;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

import dev.kanazukii.ether.engine.GameObject;
import dev.kanazukii.ether.engine.MouseListener;
import dev.kanazukii.ether.engine.renderer.PickingTexture;
import dev.kanazukii.ether.engine.scenes.Scene;
import imgui.ImGui;

public class InspectorWindow {

    private GameObject selectedGameObject = null;
    private PickingTexture pickingTex;

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
        if(MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT)){
                int x = (int)MouseListener.getSreenX();
                int y = (int)MouseListener.getSreenY();
                int uid = pickingTex.readPixel(x, y);
                selectedGameObject = currentScene.getGameObjectByUID(uid);
        }
    }
}
