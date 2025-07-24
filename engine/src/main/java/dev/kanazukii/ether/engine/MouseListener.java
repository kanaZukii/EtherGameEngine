package dev.kanazukii.ether.engine;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

import java.util.Vector;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;
import org.joml.Vector4i;

public class MouseListener{

    private static MouseListener instance;
    private double scrollX, scrollY;
    private double xPos, yPos, prevX, prevY;

    private boolean mouseButtonPressed[] = new boolean[3];
    private boolean mouseDragging;

    private Vector2f gameViewPortPosition = new Vector2f(0.0f, 0.0f);
    private Vector2f gameViewPortSize = new Vector2f(1920, 1080);

    private MouseListener(){
        this.scrollX = 0;
        this.scrollY = 0;
        this.xPos = 0;
        this.yPos = 0;
        this.prevX = 0;
        this.prevY = 0;
    }

    public static MouseListener get(){
        if(instance == null){
            instance = new MouseListener();
        }

        return instance;
    }

    public static void mousePosCallback(long window, double xPos, double yPos){
        get().prevX = get().xPos;
        get().prevY = get().yPos;

        get().xPos = xPos;
        get().yPos = yPos;

        get().mouseDragging = get().mouseButtonPressed[0] || get().mouseButtonPressed[1] || get().mouseButtonPressed[2];
    }

    public static void mouseButtonCallback(long window, int button, int action, int mods)
    {
        if(action == GLFW_PRESS){
            if(button < get().mouseButtonPressed.length)
            {
                get().mouseButtonPressed[button] = true;
            }
        } else if (action == GLFW_RELEASE) 
        {
            if(button < get().mouseButtonPressed.length)
            {
                get().mouseButtonPressed[button] = false;
                get().mouseDragging = false;
            }
        }
    }

    public static void mouseScrollCallback(long window, double xOffset, double yOffset)
    {
        get().scrollX = xOffset;
        get().scrollY = yOffset;
    }

    public static void endFrame()
    {
        get().scrollX = 0;
        get().scrollY = 0;
        get().prevX = get().xPos;
        get().prevY = get().yPos;
    }

    public static float getX(){
        return (float)get().xPos;
    }

    public static float getY(){
        return (float)get().yPos;
    }

    public static void setViewPortSize(Vector2f viewPortSize){
        get().gameViewPortSize.set(viewPortSize);
    }

    public static void setViewPortPosition(Vector2f viewPortPos){
        get().gameViewPortPosition.set(viewPortPos);
    }

    public static float getSreenX(){
        float currentX = getX() - get().gameViewPortPosition.x;
        currentX = (currentX / (float)get().gameViewPortSize.x) * 1920.0f; // TODO: Make it dynamic, retrieve user's screen size
        
        return currentX;
    }

    public static float getSreenY(){
        float currentY = getY() - get().gameViewPortPosition.y;
        currentY = 1080.0f - ((currentY/(float)get().gameViewPortSize.y) * 1080.0f); // TODO: Make it dynamic, retrieve user's screen size

        return currentY;
    }

    public static float getOrthoX(){
        float currentX = getX() - get().gameViewPortPosition.x;
        currentX = (currentX / (float)get().gameViewPortSize.x) * 2.0f - 1.0f;
        Vector4f temp = new Vector4f(currentX,0,0,1);
        Camera camera = Window.getScene().getCamera();
        Matrix4f inverseView = new Matrix4f();
        camera.getInverseProjection().mul(camera.getInverseView(), inverseView);
        temp.mul(inverseView);
        currentX = temp.x;
        
        return currentX;
    }

    public static float getOrthoY(){
        float currentY = getY() - get().gameViewPortPosition.y;
        currentY = -((currentY/(float)get().gameViewPortSize.y) * 2.0f - 1.0f);
        Vector4f temp = new Vector4f(0,currentY,0,1);
        Camera camera = Window.getScene().getCamera();
        Matrix4f inverseView = new Matrix4f();
        camera.getInverseProjection().mul(camera.getInverseView(), inverseView);
        temp.mul(inverseView);
        currentY = temp.y;

        return currentY;
    }

    public static float getDx(){
        return (float)(get().prevX - get().xPos);
    }

    public static float getDy(){
        return (float)(get().prevY - get().yPos);
    }

    public static float getScrollX(){
        return (float)get().scrollX;
    }

    public static float getScrollY(){
        return (float)get().scrollY;
    }

    public static boolean isDragging(){
        return get().mouseDragging;
    }

    public static boolean mouseButtonDown(int button){
        if(button < get().mouseButtonPressed.length)
        {
            return get().mouseButtonPressed[button];
        }else{
            return false;
        }
    }
}