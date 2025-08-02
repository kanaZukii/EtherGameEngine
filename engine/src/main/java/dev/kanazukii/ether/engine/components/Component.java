package dev.kanazukii.ether.engine.components;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import dev.kanazukii.ether.engine.GameObject;
import dev.kanazukii.ether.engine.editor.EtherImGUi;
import imgui.ImGui;
import imgui.type.ImInt;

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
                    field.set(this, EtherImGUi.dragIntControls(name, val));
                } else if (type == float.class){
                    float val = (float)value;
                    field.set(this, EtherImGUi.dragFloatControls(name, val));
                } else if (type == boolean.class){
                    boolean val = (boolean)value;
                    if(ImGui.checkbox(name, val)){ field.set(this, !val); }
                } else if (type == Vector3f.class){
                    Vector3f val = (Vector3f)value;
                    float[] imVec = {val.x, val.y, val.z};
                    if(ImGui.dragFloat3(name, imVec)){ val.set(imVec[0],imVec[1],imVec[2]); }
                } else if (type == Vector2f.class){
                    Vector2f val = (Vector2f)value;
                    EtherImGUi.dragVec2Controls(name, val);
                }else if (type == Vector4f.class){
                    Vector4f val = (Vector4f)value;
                    float[] imVec = {val.x, val.y, val.z, val.w};
                    if(ImGui.dragFloat4(name, imVec)){ val.set(imVec[0],imVec[1],imVec[2],imVec[3]);}
                } else if (type.isEnum()){
                    String[] enumValues = getEnumValues(type);
                    String enumType = ((Enum)value).name();
                    ImInt index = new ImInt(indexOf(enumType, enumValues));
                    if(ImGui.combo(field.getName(), index, enumValues, enumValues.length)){
                        field.set(this, type.getEnumConstants()[index.get()]);
                    }
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


    public void editorUpdate(float deltaTime){
        
    }

    public void generateID(){
        if(this.uID == -1){
            this.uID = ID_Count;
            ID_Count++;
        }
    }

    public void generateNewID(){
        this.uID = ID_Count;
        ID_Count++;
    }

    public int getUID(){
        return this.uID;
    }


    private <T extends Enum<T>> String[] getEnumValues(Class<T> enumType){
        String[] enumValues = new String[enumType.getEnumConstants().length];
        int i = 0;
        for(T enumIntValue : enumType.getEnumConstants()){
            enumValues[i] = enumIntValue.name();
            i++;
        }
        return enumValues;
    }

    private int indexOf(String string, String[] arr){
        for(int i = 0; i < arr.length; i++){
            if(string.equals(arr[i])){
                return i;
            }
        }

        return -1;
    }


    public void destroy(){
        
    }

    public static void init(int maxID){
        ID_Count = maxID;
    }

}
