package dev.kanazukii.banana.engine;

import java.util.ArrayList;
import java.util.List;

public class GameObject {
    
    private String name;
    private List<Component> components;

    public GameObject(String name){
        this.name = name;
        this.components = new ArrayList<>();
    }

    public String getName(){
        return name;
    }

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

    public <T extends Component> void removeComponent(Class<T> componentClass){
        for(int i = 0; i < components.size(); i++){
            Component comp = components.get(i);
            if(componentClass.isAssignableFrom(comp.getClass())){
                components.remove(i);
                return;
            }
        }
    }

    public void addComponent (Component component){
        components.add(component);
        component.gameObject = this;
    }

    public void start(){
        for(int i = 0; i < components.size(); i++){
            components.get(i).start();
        }
    }

    public void update(float deltaTime){
        for(int i = 0; i < components.size(); i++){
            components.get(i).update(deltaTime);
        }
    }

}
