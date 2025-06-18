package dev.kanazukii.banana.engine.components;

import dev.kanazukii.banana.engine.Component;

public class SpriteRenderer extends Component{
    
    private boolean firstTime = false;

    @Override
    public void start(){
        System.out.println(gameObject.getName() + "'s SpriteRenderer is starting");
    }

    @Override
    public void update(float deltaTime){
        if(!firstTime){
            System.out.println(gameObject.getName() + "s SpriteRenderer is Updating");
            firstTime = true;
        }
    }
}
