package dev.kanazukii.ether.engine.editor;

import java.util.List;

import dev.kanazukii.ether.engine.GameObject;
import dev.kanazukii.ether.engine.Window;
import imgui.ImGui;
import imgui.flag.ImGuiTreeNodeFlags;

public class HeirarchyWindow {
    public void ImGUI(){
        ImGui.begin("Scene Heirarchy");

        List<GameObject> gameObjects = Window.getScene().getGameObjects();
        int index = 0;
        for(GameObject gameObj : gameObjects){
            if(!gameObj.doSerialize()){
                continue;
            }

            ImGui.pushID(index);
            boolean treeNodeOpen = ImGui.treeNodeEx(gameObj.getName(),
                                            ImGuiTreeNodeFlags.DefaultOpen |
                                            ImGuiTreeNodeFlags.FramePadding |
                                            ImGuiTreeNodeFlags.OpenOnArrow |
                                            ImGuiTreeNodeFlags.SpanAvailWidth,
                                            gameObj.getName());
            ImGui.popID();

            if(treeNodeOpen){
                ImGui.treePop();
            }

            index++;
        }
        ImGui.end();

    }
}
