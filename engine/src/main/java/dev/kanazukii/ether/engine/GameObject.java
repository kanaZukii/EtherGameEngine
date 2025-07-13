package dev.kanazukii.ether.engine;

import java.util.ArrayList;
import java.util.List;

public class GameObject {
    
    private String name;
    // List of all components (Sprite render and etc.) of the object
    private List<Component> components;
    public Transform transform = new Transform();

    // Constructor of a game object without the transform
    public GameObject(String name){
        this.name = name;
        this.components = new ArrayList<>();
    }

    // Constructor for game object with a transform (Scale and position)
    public GameObject(String name, Transform transform){
        this.name = name;
        this.components = new ArrayList<>();
        this.transform = transform;
    }

    public String getName(){
        return name;
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

    // Add a component to the Object
    public void addComponent (Component component){
        components.add(component);
        component.gameObject = this;
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

}
