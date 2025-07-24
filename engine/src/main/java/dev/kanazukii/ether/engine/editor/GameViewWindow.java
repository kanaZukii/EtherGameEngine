package dev.kanazukii.ether.engine.editor;

import org.joml.Vector2f;

import dev.kanazukii.ether.engine.MouseListener;
import dev.kanazukii.ether.engine.Window;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.ImVec4;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiWindowFlags;
import imgui.internal.ImGuiWindow;

public class GameViewWindow {

    private static float leftX, rightX, topY, botY;
    
    public static void ImGUI(){
        ImGui.begin("Scene Viewport", ImGuiWindowFlags.NoScrollbar | ImGuiWindowFlags.NoScrollWithMouse);

        ImVec2 windowSize = getFitSizeViewPort();
        ImVec2 windowPos = getCenteredPosViewPort(windowSize);

        ImGui.setCursorPos(windowPos);

        ImVec2 topLeftCorner = new ImVec2();
        ImGui.getCursorScreenPos(topLeftCorner);
        topLeftCorner.x -= ImGui.getScrollX();
        topLeftCorner.y -= ImGui.getScrollY();
        leftX = topLeftCorner.x;
        topY = topLeftCorner.y;
        rightX = leftX + windowSize.x;
        botY = topY + windowSize.y;

        int textureID = Window.getFrameBuffer().getTextureID();
        ImGui.image(textureID, windowSize.x, windowSize.y, 0, 1,1,0);

        MouseListener.setViewPortPosition(new Vector2f(topLeftCorner.x, topLeftCorner.y));
        MouseListener.setViewPortSize(new Vector2f(windowSize.x, windowSize.y));

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

    public static boolean getWantCaptureMouse(){
        return MouseListener.getX() >= leftX && MouseListener.getX() <= rightX &&
            MouseListener.getY() >= topY && MouseListener.getY() <= botY;
    }


}
