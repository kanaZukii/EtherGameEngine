package dev.kanazukii.ether.engine;

public abstract class Component {
    
    // Keeps a refernece to the parent game object (Should be overwritten in the child class)
    public GameObject gameObject = null;

    // for initialization
    public void start(){
        
    }

    // update method of the component (Should be overwritten in the child class)
    public void update(float deltaTime){

    }

}
