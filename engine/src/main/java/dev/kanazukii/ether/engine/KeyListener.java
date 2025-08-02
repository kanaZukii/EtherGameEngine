package dev.kanazukii.ether.engine;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class KeyListener {
    
    private static KeyListener instance;
    private boolean[] keyPressed =  new boolean[350];
    private boolean[] keyBeginPressed = new boolean[350];

    private KeyListener(){}

    public static KeyListener get(){
        if(instance == null){
            instance = new KeyListener();
        }

        return instance;
    }

    public static void keyCallback(long window, int key, int scancode, int action, int mods){
        if(action ==  GLFW_PRESS){
            if(key < get().keyPressed.length){
                get().keyPressed[key] = true;
                get().keyBeginPressed[key] = true;
            } 
        } else if(action == GLFW_RELEASE){
                get().keyPressed[key] = false;
                get().keyBeginPressed[key] = false;
        }
    }

    public static boolean isKeyPressed(int keyCode){
            return get().keyPressed[keyCode];
    }

    public static boolean isKeyBeginPress(int keyCode){
        boolean result = get().keyBeginPressed[keyCode];
        if(true){
            get().keyBeginPressed[keyCode] = false;
        }

        return result;
    }

}
