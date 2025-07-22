package dev.kanazukii.ether.engine.editor;

import dev.kanazukii.ether.engine.Window;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.ImVec4;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiWindowFlags;
import imgui.internal.ImGuiWindow;

public class GameViewWindow {
    
    public static void ImGUI(){

        //ImGui.pushStyleColor(ImGuiCol.WindowBg, new ImVec4(0.4f, 0.4f, 0.4f, 1.0f)); // Set window background to red
        ImGui.begin("Scene Viewport", ImGuiWindowFlags.NoScrollbar | ImGuiWindowFlags.NoScrollWithMouse);
        //ImGui.popStyleColor();

        ImVec2 windowSize = getFitSizeViewPort();
        ImVec2 windowPos = getCenteredPosViewPort(windowSize);

        ImGui.setCursorPos(windowPos);
        int textureID = Window.getFrameBuffer().getTextureID();
        ImGui.image(textureID, windowSize.x, windowSize.y, 0, 1,1,0);


        ImGui.end();
    }


    public static ImVec2 getFitSizeViewPort(){
        ImVec2 windowSize = new ImVec2();
        ImGui.getContentRegionAvail(windowSize);
        windowSize.x -= ImGui.getScrollX();
        windowSize.y -= ImGui.getScrollY();

        float aspectWidth = windowSize.x;
        float aspectHeight = aspectWidth /Window.getTargetAspectRatio();
        if(aspectHeight > windowSize.y){
            // Turn on pillar box (Black bars to match aspect ratio)
            aspectHeight = windowSize.y;
            aspectWidth = aspectHeight * Window.getTargetAspectRatio();
        }

        return new ImVec2(aspectWidth, aspectHeight);
    }
    public static ImVec2 getCenteredPosViewPort(ImVec2 aspectSize){
        ImVec2 windowSize = new ImVec2();
        ImGui.getContentRegionAvail(windowSize);
        windowSize.x -= ImGui.getScrollX();
        windowSize.y -= ImGui.getScrollY();

        float viewPortX = (windowSize.x/2.0f) - (aspectSize.x/2.0f);
        float viewPortY = (windowSize.y/2.0f) - (aspectSize.y/2.0f);

        return new ImVec2(viewPortX + ImGui.getCursorPosX(), viewPortY + ImGui.getCursorPosY());
    }


}
