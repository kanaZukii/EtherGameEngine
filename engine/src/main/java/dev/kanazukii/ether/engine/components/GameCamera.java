package dev.kanazukii.ether.engine.components;

import org.joml.Vector2f;

import dev.kanazukii.ether.engine.Camera;
import dev.kanazukii.ether.engine.Window;

public class GameCamera extends Component{
    
    private transient Camera sceneCamera;
    private Vector2f cameraOffSet = new Vector2f();


    public GameCamera (){
        
    }

    @Override
    public void start(){
        sceneCamera = Window.getScene().getCamera();
    }
    

    @Override
    public void update(float deltaTime){
        cameraOffSet = new Vector2f(-(sceneCamera.getProjectionSize().x/2.0f),
                                    -(sceneCamera.getProjectionSize().y/2.0f)
                                    );
        Vector2f cameraPos = new Vector2f(gameObject.transform.position).add(cameraOffSet);
        sceneCamera.getPosition().set(cameraPos);
    }

}
