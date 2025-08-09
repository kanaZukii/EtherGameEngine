package dev.kanazukii.ether.engine.editor;

import java.util.List;

import dev.kanazukii.ether.engine.GameObject;
import dev.kanazukii.ether.engine.Window;
import imgui.ImGui;
import imgui.flag.ImGuiTreeNodeFlags;

public class HierarchyWindow {

    private String payloadDragDropType = "SceneHierarchy";

    public void ImGUI(){
        ImGui.begin("Scene Hierarchy");

        List<GameObject> gameObjects = Window.getScene().getGameObjects();
        int index = 0;
        for(GameObject gameObj : gameObjects){
            if(!gameObj.doSerialize()){
                continue;
            }

            if(doTreeNode(index, gameObj)){
                ImGui.treePop();
            }

            index++;
        }
        ImGui.end();

    }

    private boolean doTreeNode(int index, GameObject gameObj){
            ImGui.pushID(index);
            boolean treeNodeOpen = ImGui.treeNodeEx(gameObj.name,
                                            ImGuiTreeNodeFlags.DefaultOpen |
                                            ImGuiTreeNodeFlags.FramePadding |
                                            ImGuiTreeNodeFlags.OpenOnArrow |
                                            ImGuiTreeNodeFlags.SpanAvailWidth,
                                            gameObj.name);
            ImGui.popID();

            if(ImGui.beginDragDropSource()){
                ImGui.setDragDropPayload(payloadDragDropType, gameObj);
                ImGui.text(gameObj.name);
                ImGui.endDragDropSource();
            }

            if(ImGui.beginDragDropTarget()){
                Object payLoadObject = ImGui.acceptDragDropPayload(payloadDragDropType);
                if(payLoadObject != null){
                    if(payLoadObject.getClass().isAssignableFrom(GameObject.class)){
                        GameObject payloadGameObj = (GameObject)payLoadObject;
                        System.out.println("Accepted: " + payloadGameObj.name);
                    }   
                }
                ImGui.endDragDropTarget();
            }

            return treeNodeOpen;
    }
}
