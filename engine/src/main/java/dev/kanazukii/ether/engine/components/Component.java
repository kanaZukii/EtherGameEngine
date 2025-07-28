package dev.kanazukii.ether.engine.components;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import dev.kanazukii.ether.engine.GameObject;
import imgui.ImGui;

public abstract class Component {
    
    private static int ID_Count = 0;
    private int uID = -1;

    // Keeps a refernece to the parent game object (Should be overwritten in the child class)
    public transient GameObject gameObject = null;

    // for initialization
    public void start(){
        
    }

    // For ImGUI integration and manipulation
    public void ImGUI(){
        try {
            Field[] fields = this.getClass().getDeclaredFields();
            for(Field field: fields){
                boolean isTransient = Modifier.isTransient(field.getModifiers());
                if(isTransient){ continue; }

                boolean isPrivate = Modifier.isPrivate(field.getModifiers());
                if(isPrivate){ field.setAccessible(true); }

                Class type = field.getType();
                Object value = field.get(this);
                String name = field.getName();

                if(type == int.class){
                    int val = (int)value;
                    int[] imInt = {val};
                    if(ImGui.dragInt(name, imInt)){ field.set(this, imInt[0]); }
                } else if (type == float.class){
                    float val = (float)value;
                    float[] imFloat = {val};
                    if(ImGui.dragFloat(name, imFloat)){ field.set(this, imFloat[0]); }
                } else if (type == boolean.class){
                    boolean val = (boolean)value;
                    if(ImGui.checkbox(name, val)){ field.set(this, !val); }
                } else if (type == Vector3f.class){
                    Vector3f val = (Vector3f)value;
                    float[] imVec = {val.x, val.y, val.z};
                    if(ImGui.dragFloat3(name, imVec)){ val.set(imVec[0],imVec[1],imVec[2]); }
                } else if (type == Vector2f.class){
                    Vector2f val = (Vector2f)value;
                    float[] imVec = {val.x, val.y};
                    if(ImGui.dragFloat2(name, imVec)){ val.set(imVec[0],imVec[1]);}
                }else if (type == Vector4f.class){
                    Vector4f val = (Vector4f)value;
                    float[] imVec = {val.x, val.y, val.z, val.w};
                    if(ImGui.dragFloat4(name, imVec)){ val.set(imVec[0],imVec[1],imVec[2],imVec[3]);}
                }
                

                if(isPrivate){ field.setAccessible(false); }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    // update method of the component (Should be overwritten in the child class)
    public void update(float deltaTime){

    }

    public void generateID(){
        if(this.uID == -1){
            this.uID = ID_Count;
            ID_Count++;
        }
    }

    public int getUID(){
        return this.uID;
    }

    public static void init(int maxID){
        ID_Count = maxID;
    }

}
