package dev.kanazukii.ether.engine;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_MAXIMIZED;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetCursorPosCallback;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetMouseButtonCallback;
import static org.lwjgl.glfw.GLFW.glfwSetScrollCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowSizeCallback;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BITS;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_ONE;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_RENDERER;
import static org.lwjgl.opengl.GL11.GL_VENDOR;
import static org.lwjgl.opengl.GL11.GL_VERSION;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL20.GL_SHADING_LANGUAGE_VERSION;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glGetString;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.system.MemoryUtil.NULL;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import dev.kanazukii.ether.engine.renderer.DebugRenderer;
import dev.kanazukii.ether.engine.renderer.FrameBuffer;
import dev.kanazukii.ether.engine.renderer.PickingTexture;
import dev.kanazukii.ether.engine.renderer.Renderer;
import dev.kanazukii.ether.engine.renderer.Shader;
import dev.kanazukii.ether.engine.scenes.LevelEditorScene;
import dev.kanazukii.ether.engine.scenes.LevelScene;
import dev.kanazukii.ether.engine.scenes.Scene;
import dev.kanazukii.ether.engine.utils.AssetPool;

public class Window {
    
    private int width, height;
    private String title;

    private static Window window = null;
    private long glfwWindow;
    private static FrameBuffer framebuffer;

    private static PickingTexture pickingTex;

    public float BG_r, BG_g, BG_b, BG_a;

    private static Scene currentScene;

    public static float FPS = 0.0f;
    public static int targetFPS = 0;

    private ImGUILayer imGUILayer;

    private Window()
    {
        this.width = 1920;
        this.height = 1080;
        this.title = "Hello World!";

        this.BG_r = 0.3f;
        this.BG_g = 0.3f;
        this.BG_b = 0.3f;
        this.BG_a = 1;
    }

    // Create one instance of Window
    public static Window get()
    {
        if(window == null){
            window = new Window();
        }

        return window;
    }

    public static int getWidth(){
        return get().width;
    }

    public static int getHeight(){
        return get().height;
    }

    public static Scene getScene(){
        return currentScene;
    }

    public static FrameBuffer getFrameBuffer(){
        return framebuffer;
    }

    public static float getTargetAspectRatio(){
        return 16.0f / 9.0f;  // TODO: Should be dynamic get the user's screen aspect ratio
    }

    public static void setHeight(int newHeight){
        get().height = newHeight;
    }

    public static void setWidth(int newWidth){
        get().width = newWidth;
    }

    // Create and start showing a window
    public void run()
    {
        System.out.println("What's up, Gamers");
        System.out.println("LWGL ver " + Version.getVersion());
        init();
        loop();

        // Free memory at exit
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);

        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public void changeScene(int index){
        switch (index) {
            case 0:
                currentScene = new LevelEditorScene();
                break;
            case 1:
                currentScene = new LevelScene();
                break;
        
            default:
                assert false : "Unknown Scene " + index;
                return;
        }

        currentScene.loadScene();
        currentScene.init();
        currentScene.start();
    }

    // Initialize and Create a GLFW window
    public void init()
    {
        // Error callback
        GLFWErrorCallback.createPrint(System.err).set();

        //Initialize GLFW
        if(!glfwInit()) { throw new IllegalStateException("Unable to initialize GFLW"); }

        // GFLW Configuration
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);

        //Create window
        glfwWindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);

        if(glfwWindow == NULL) { throw new IllegalStateException("FALIED to Create Window"); }

        // Mouse Callbacks
        glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);

        //Key Callbacks
        glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);
        glfwSetWindowSizeCallback(glfwWindow, (w, newWidth, newHeight) -> {
            Window.setWidth(newWidth);
            Window.setHeight(newHeight);
        });

        // OpenGL context current
        glfwMakeContextCurrent(glfwWindow);
        // Enable Vsync
        glfwSwapInterval(1);

        // Make window visible
        glfwShowWindow(glfwWindow);

        // This line is critical for LWJGL's interoperation with GLFW's
		// OpenGL context, or any context that is managed externally.
		// LWJGL detects the context that is current in the current thread,
		// creates the GLCapabilities instance and makes the OpenGL
		// bindings available for use.
        GL.createCapabilities();

        // For alpha blending and transparency
        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);

        // IMGUI
        imGUILayer = new ImGUILayer(glfwWindow);
        imGUILayer.initImGui();

        // Binded to the screen size TODO: Make it dynamic, match it to user's screen size
        framebuffer = new FrameBuffer(1920, 1080);
        glViewport(0, 0, 1920, 1080);

        pickingTex = new PickingTexture(1920, 1080); 

        //GL Info
        System.out.println("OpenGL Version: " + glGetString(GL_VERSION));
        System.out.println("Renderer: " + glGetString(GL_RENDERER));
        System.out.println("Vendor: " + glGetString(GL_VENDOR));
        System.out.println("GLSL Version: " + glGetString(GL_SHADING_LANGUAGE_VERSION));
        
        changeScene(0);
    }

    // Window Update and Draw Loop
    public void loop()
    {
        float startTime = (float)glfwGetTime();
        float endTime = (float)glfwGetTime();
        float deltaTime = -1.0f;
        
        Shader defaultShader = AssetPool.getShader("assets/shaders/default.glsl"); // TODO: To be change
        Shader pickingShader = AssetPool.getShader("assets/shaders/pickingShader.glsl");

        while(!glfwWindowShouldClose(glfwWindow))
        {
            //Poll Events for key listeners
            glfwPollEvents();

            // Render the dummy texture to use for picking/ inspecting game objs
            glDisable(GL_BLEND);
            pickingTex.enableWriting();
            
            glViewport(0, 0, 1920, 1080);
            glClearColor(0.0f,0.0f, 0.0f, 0.0f);
            glClear(GL_COLOR_BUFFER_BIT);
            glClear(GL_DEPTH_BUFFER_BIT);

            Renderer.bindShader(pickingShader);
            currentScene.render();

            if(MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT)){
                int x = (int)MouseListener.getSreenX();
                int y = (int)MouseListener.getSreenY();
                System.out.println(pickingTex.readPixel(x, y));
            }


            pickingTex.disableWriting();
            glEnable(GL_BLEND);

            // Render the actual game
            DebugRenderer.beginFrame();
            framebuffer.bind();

            glClearColor(BG_r, BG_g, BG_b, BG_a);
            glClear(GL_COLOR_BUFFER_BIT);

            if(deltaTime >= 0){
                DebugRenderer.render();
                Renderer.bindShader(defaultShader);
                currentScene.update(deltaTime);
                currentScene.render();
            }
            framebuffer.unbind();

            FPS = (float)(1/deltaTime);
            
            imGUILayer.update(deltaTime, currentScene);
            glfwSwapBuffers(glfwWindow);

            endTime = (float)glfwGetTime();
            deltaTime = endTime - startTime;
            startTime = endTime;
        }
        currentScene.saveAtExit();
    }

}
