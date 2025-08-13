package dev.kanazukii.ether.engine.components;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;

import org.jbox2d.common.Vec2;

import dev.kanazukii.ether.engine.KeyListener;
import dev.kanazukii.ether.engine.physcis2D.components.RigidBody2D;
import dev.kanazukii.ether.engine.utils.Configs;

public class PlayerControls extends Component{

    private float moveSpeed = Configs.GRID_WIDTH;

    public PlayerControls(){

    }

    @Override
    public void update(float deltaTime){
        if (gameObject.getComponent(RigidBody2D.class) == null 
        || gameObject.getComponent(RigidBody2D.class).getRawBody() == null) return;

        Vec2 velocity = gameObject.getComponent(RigidBody2D.class).getRawBody().getLinearVelocity();

        float desiredVelX = 0;

        if (KeyListener.isKeyPressed(GLFW_KEY_A)) {
            desiredVelX -= moveSpeed * 4;
        }
        if (KeyListener.isKeyPressed(GLFW_KEY_D)) {
            desiredVelX += moveSpeed * 4;
        }

        // Set new velocity (x axis only, keep y from gravity)
        gameObject.getComponent(RigidBody2D.class).getRawBody().setLinearVelocity(new Vec2(desiredVelX, velocity.y));

    
    }
}
