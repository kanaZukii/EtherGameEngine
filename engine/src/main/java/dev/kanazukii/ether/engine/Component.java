package dev.kanazukii.ether.engine;

public abstract class Component {
    
    // Keeps a refernece to the parent game object (Should be overwritten in the child class)
    public transient GameObject gameObject = null;

    // for initialization
    public void start(){
        
    }

    // For ImGUI integration and manipulation
    public void ImGUI(){

    }

    // update method of the component (Should be overwritten in the child class)
    public void update(float deltaTime){

    }

}
