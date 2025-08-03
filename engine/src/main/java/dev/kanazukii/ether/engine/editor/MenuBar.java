package dev.kanazukii.ether.engine.editor;

import dev.kanazukii.ether.engine.observers.EventSystem;
import dev.kanazukii.ether.engine.observers.events.Event;
import dev.kanazukii.ether.engine.observers.events.EventType;
import imgui.ImGui;

public class MenuBar {
    
    public void ImGUI(){
        ImGui.beginMenuBar();
        if(ImGui.beginMenu("File")){
            if(ImGui.menuItem("Save", "Crtl+S")){
                EventSystem.notify(null, new Event(EventType.SaveScene));
            }
            if(ImGui.menuItem("Load", "Crtl+O")){
                EventSystem.notify(null, new Event(EventType.LoadScene));
            }
            ImGui.endMenu();
        }
        ImGui.endMenuBar();
    }
}
