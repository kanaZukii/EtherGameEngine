package dev.kanazukii.banana.engine;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_5;
import static org.lwjgl.glfw.GLFW.GLFW_MAXIMIZED;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetCursorPosCallback;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetMouseButtonCallback;
import static org.lwjgl.glfw.GLFW.glfwSetScrollCallback;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_RENDERER;
import static org.lwjgl.opengl.GL11.GL_VENDOR;
import static org.lwjgl.opengl.GL11.GL_VERSION;
import static org.lwjgl.opengl.GL20.GL_SHADING_LANGUAGE_VERSION;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glGetString;

import static org.lwjgl.system.MemoryUtil.NULL;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import dev.kanazukii.banana.engine.utils.Time;

public class Window {
    
    private int width, height;
    private String title;

    private static Window window = null;
    private long glfwWindow;

    public float r, g, b, a;

    private static Scene scene;

    public static float FPS = 0.0f;
    public static int targetFPS = 0;

    private Window()
    {
        this.width = 1920;
        this.height = 1080;
        this.title = "Hello World!";

        this.r = 1;
        this.g = 1;
        this.b = 1;
        this.a = 1;
    }

    // Create one instance of Window
    public static Window get()
    {
        if(window == null){
            window = new Window();
        }

        return window;
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
                scene = new LevelEditorScene();
                scene.init();
                break;
            case 1:
                scene = new LevelScene();
                scene.init();
                break;
        
            default:
                assert false : "Unknown Scene " + index;
                break; 
        }
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
        float startTime = Time.getDeltaTime();
        float endTime = Time.getDeltaTime();
        float deltaTime = -1.0f;

        while(!glfwWindowShouldClose(glfwWindow))
        {
            //Poll Events for key listeners
            glfwPollEvents();

            glClearColor(r, g, b, a);
            glClear(GL_COLOR_BUFFER_BIT);

            if(deltaTime >= 0){
                scene.update(deltaTime);
            }

            FPS = (float)(1/deltaTime);
            
            glfwSwapBuffers(glfwWindow);

            endTime = Time.getDeltaTime();
            deltaTime = endTime - startTime;
            startTime = endTime;
        }
    }

}
