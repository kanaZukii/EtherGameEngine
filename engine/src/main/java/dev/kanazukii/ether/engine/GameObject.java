package dev.kanazukii.ether.engine;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import dev.kanazukii.ether.engine.components.Component;
import dev.kanazukii.ether.engine.components.ComponentDeserializer;
import dev.kanazukii.ether.engine.components.SpriteRenderer;
import dev.kanazukii.ether.engine.components.Texture;
import dev.kanazukii.ether.engine.components.TextureDeserializer;
import dev.kanazukii.ether.engine.components.Transform;
import dev.kanazukii.ether.engine.utils.AssetPool;
import imgui.ImGui;

public class GameObject {
    
    private static int ID_Count = 0;
    private int uID = -1;
    private boolean serialize = true;
    private boolean isDead = false;

    public String name;
    // List of all components (Sprite render and etc.) of the object
    private List<Component> components;
    public transient Transform transform;


    // Constructor for game object with a transform (Scale and position)
    public GameObject(String name){
        this.name = name;
        this.components = new ArrayList<>();

        // TODO: Fix uID assignment when loading serialized saved scenes
        generateID();
    }
    
    public void setSerialize(boolean value){
        serialize = value;
    }

    public boolean doSerialize(){
        return serialize;
    }

    // Retrieves a component based on the passed class use object.class
    public <T extends Component> T getComponent(Class<T> componentClass){
        for(Component comp : components){
            if(componentClass.isAssignableFrom(comp.getClass())){
                try{
                    return componentClass.cast(comp);
                } catch (ClassCastException e){
                    e.printStackTrace();
                    assert false : "Error Casting component";
                }
            }
        }

        return null;
    }

     // Remove a component based on the passed class use object.class
    public <T extends Component> void removeComponent(Class<T> componentClass){
        for(int i = 0; i < components.size(); i++){
            Component comp = components.get(i);
            if(componentClass.isAssignableFrom(comp.getClass())){
                components.remove(i);
                return;
            }
        }
    }

    public List<Component> getComponentList(){
        return components;
    }

    // Add a component to the Object
    public void addComponent (Component component){
        component.generateID();
        components.add(component);
        component.gameObject = this;
    }

    public void ImGUI(){
        for (Component component : components) {
            if(ImGui.collapsingHeader(component.getClass().getSimpleName())) component.ImGUI();
        }
    }

    // Starts all the components
    public void start(){
        for(int i = 0; i < components.size(); i++){
            components.get(i).start();
        }
    }

    // Calls all the update method of each component
    public void update(float deltaTime){
        for(int i = 0; i < components.size(); i++){
            components.get(i).update(deltaTime);
        }
    }

    public void editorUpdate(float deltaTime){
        for(int i = 0; i < components.size(); i++){
            components.get(i).editorUpdate(deltaTime);
        }
    }

    private void generateID(){
        if(this.uID == -1){
            this.uID = ID_Count;
            ID_Count++;
        }
    }

    private void generateNewID(){
        this.uID = ID_Count;
        ID_Count++;
    }

    public static void init(int maxID){
        ID_Count = maxID;
    }

    public void destroy(){
        isDead = true;
        for(int i = 0; i < components.size(); i++){
            components.get(i).destroy();
        }
    }

    public boolean isDead(){
        return isDead;
    }

    public int getUID(){
        return uID;
    }

    public GameObject copy(){
        Gson gson = new GsonBuilder()
                        .registerTypeAdapter(Component.class, new ComponentDeserializer())
                        .registerTypeAdapter(GameObject.class, new GameObjectDeserializer())
                        .registerTypeAdapter(Texture.class, new TextureDeserializer())
                        .create();
        String jsonObj = gson.toJson(this);
        GameObject newObj = gson.fromJson(jsonObj, GameObject.class);
        newObj.generateNewID();
        for(Component component: newObj.getComponentList()){
            component.generateNewID();
        }

        SpriteRenderer spriteRender = newObj.getComponent(SpriteRenderer.class);
        if(spriteRender != null && spriteRender.getTexture() != null){
            spriteRender.setTexture(AssetPool.getTexture(spriteRender.getTexture().getFilePath()));
        }

        return newObj;
    }
}
