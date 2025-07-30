package dev.kanazukii.ether.engine.editor;

import org.joml.Vector2f;
import org.joml.Vector4f;

import imgui.ImGui;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiStyleVar;

public class EtherImGUi {

    private static float defaultColWidth = 220.0f;

    public static void dragVec2Controls(String label, Vector2f vec2){
        dragVec2Controls(label, vec2, 0.0f, defaultColWidth);
    }

    public static void dragVec2Controls(String label, Vector2f vec2, float resetVal){
        dragVec2Controls(label, vec2, resetVal, defaultColWidth);
    }

    public static void dragVec2Controls(String label, Vector2f vec2, float resetVal, float columnWidth){
        ImGui.pushID(label);

        ImGui.columns(2);
        ImGui.setColumnWidth(0, columnWidth);
        ImGui.text(label);

        ImGui.nextColumn();
        ImGui.pushStyleVar(ImGuiStyleVar.ItemSpacing, 0,0);
        float lineHeight = ImGui.getFontSize() + ImGui.getStyle().getFramePaddingY() * 2.0f;
        Vector2f buttonSize = new Vector2f(lineHeight + 3.0f, lineHeight);
        float width = (ImGui.calcItemWidth() - buttonSize.x * 2.0f)/ 2.0f;

        ImGui.pushItemWidth(width);
        ImGui.pushStyleColor(ImGuiCol.Button, 1.0f, 0.0f,0.0f,1.0f);
        ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 1.0f, 0.4f,0.4f,1.0f);
        ImGui.pushStyleColor(ImGuiCol.ButtonActive, 0.7f, 0.0f,0.0f,1.0f);
        if(ImGui.button("X", buttonSize.x, buttonSize.y)) vec2.x = resetVal;
        ImGui.popStyleColor(3);

        ImGui.sameLine();
        float[] vecValueX = {vec2.x};
        ImGui.dragFloat("##x", vecValueX, 0.5f);
        ImGui.popItemWidth();
        
        ImGui.sameLine();
        ImGui.pushItemWidth(width);
        ImGui.pushStyleColor(ImGuiCol.Button, 0.0f, 0.0f,1.0f,1.0f);
        ImGui.pushStyleColor(ImGuiCol.ButtonHovered, 0.4f, 0.4f,1.0f,1.0f);
        ImGui.pushStyleColor(ImGuiCol.ButtonActive, 0.0f, 0.0f,0.7f,1.0f);
        if(ImGui.button("Y", buttonSize.x, buttonSize.y)) vec2.y = resetVal;
        ImGui.popStyleColor(3);

        ImGui.sameLine();
        float[] vecValueY = {vec2.y};
        ImGui.dragFloat("##y", vecValueY, 0.5f);
        ImGui.popItemWidth();

        ImGui.nextColumn();

        vec2.x = vecValueX[0];
        vec2.y = vecValueY[0];

        ImGui.popStyleVar();
        ImGui.columns(1);
        ImGui.popID();
    }

    public static float dragFloatControls(String label, float val){
        ImGui.pushID(label);

        ImGui.columns(2);
        ImGui.setColumnWidth(0, defaultColWidth);
        ImGui.text(label);
        ImGui.nextColumn();

        float[] valArr = {val};
        ImGui.dragFloat("##dragfloat", valArr, 0.5f);

        ImGui.columns(1);
        ImGui.popID();

        return valArr[0];
    }

    public static int dragIntControls(String label, int val){
        ImGui.pushID(label);

        ImGui.columns(2);
        ImGui.setColumnWidth(0, defaultColWidth);
        ImGui.text(label);
        ImGui.nextColumn();

        int[] valArr = {val};
        ImGui.dragInt("##dragInt", valArr, 0.5f);

        ImGui.columns(1);
        ImGui.popID();

        return valArr[0];
    }

    public static boolean colorPicker4f(String label, Vector4f vec4){
        boolean res = false;
        ImGui.pushID(label);

        ImGui.columns(2);
        ImGui.setColumnWidth(0, defaultColWidth);
        ImGui.text(label);
        ImGui.nextColumn();

        float[] colorArr = {vec4.x, vec4.y, vec4.z, vec4.w};
        if(ImGui.colorEdit4("##colorpicker", colorArr)){
            vec4.set(colorArr[0], colorArr[1], colorArr[2], colorArr[3]);
            res = true;
        }

        ImGui.columns(1);
        ImGui.popID();

        return res;
    }

}
