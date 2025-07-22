package dev.kanazukii.ether.engine;

import static org.lwjgl.glfw.GLFW.*;

import org.lwjgl.glfw.GLFWCharCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;

import dev.kanazukii.ether.engine.editor.GameViewWindow;
import dev.kanazukii.ether.engine.scenes.Scene;
import imgui.ImFontAtlas;
import imgui.ImFontConfig;
import imgui.ImFontGlyphRangesBuilder;
import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.callback.ImStrConsumer;
import imgui.callback.ImStrSupplier;
import imgui.flag.ImGuiBackendFlags;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiConfigFlags;
import imgui.flag.ImGuiKey;
import imgui.flag.ImGuiMouseCursor;
import imgui.flag.ImGuiStyleVar;
import imgui.flag.ImGuiWindowFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.type.ImBoolean;

public class ImGUILayer {

    private String glslVersion = "#version 330 core";
    private long glfwWindowPtr;

     // Mouse cursors provided by GLFW
    private final long[] mouseCursors = new long[ImGuiMouseCursor.COUNT];

    // LWJGL3 renderer (SHOULD be initialized)
    private final ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();

    public ImGUILayer(long glfwWindow){
        this.glfwWindowPtr = glfwWindow;
    }

    // Initialize Dear ImGui.
    public void initImGui() {
        // IMPORTANT!!
        // This line is critical for Dear ImGui to work.
        ImGui.createContext();

        // ------------------------------------------------------------
        // Initialize ImGuiIO config
        final ImGuiIO io = ImGui.getIO();

        io.setIniFilename("imGui.ini"); // We don't want to save .ini file
        io.setConfigFlags(ImGuiConfigFlags.NavEnableKeyboard); // Navigation with keyboard
        io.setConfigFlags(ImGuiConfigFlags.DockingEnable); // For window docking
        io.setBackendFlags(ImGuiBackendFlags.HasMouseCursors); // Mouse cursors to display while resizing windows etc.
        io.setBackendPlatformName("imgui_java_impl_glfw");

        // ------------------------------------------------------------
        // Mapping and Input callbacks
        initInputs(io);

        // ------------------------------------------------------------
        // Fonts configuration
        initFonts(io);

        // Method initializes LWJGL3 renderer.
        // This method SHOULD be called after you've initialized your ImGui configuration (fonts and so on).
        // ImGui context should be created as well.
        imGuiGl3.init(glslVersion);
    }

    // TODO: FN Key combination crashing the app
    private void initInputs(ImGuiIO io){
        // Mouse cursors mapping
        mouseCursors[ImGuiMouseCursor.Arrow] = glfwCreateStandardCursor(GLFW_ARROW_CURSOR);
        mouseCursors[ImGuiMouseCursor.TextInput] = glfwCreateStandardCursor(GLFW_IBEAM_CURSOR);
        mouseCursors[ImGuiMouseCursor.ResizeAll] = glfwCreateStandardCursor(GLFW_ARROW_CURSOR);
        mouseCursors[ImGuiMouseCursor.ResizeNS] = glfwCreateStandardCursor(GLFW_VRESIZE_CURSOR);
        mouseCursors[ImGuiMouseCursor.ResizeEW] = glfwCreateStandardCursor(GLFW_HRESIZE_CURSOR);
        mouseCursors[ImGuiMouseCursor.ResizeNESW] = glfwCreateStandardCursor(GLFW_ARROW_CURSOR);
        mouseCursors[ImGuiMouseCursor.ResizeNWSE] = glfwCreateStandardCursor(GLFW_ARROW_CURSOR);
        mouseCursors[ImGuiMouseCursor.Hand] = glfwCreateStandardCursor(GLFW_HAND_CURSOR);
        mouseCursors[ImGuiMouseCursor.NotAllowed] = glfwCreateStandardCursor(GLFW_ARROW_CURSOR);

        // ------------------------------------------------------------
        // GLFW callbacks to handle user input

        glfwSetKeyCallback(glfwWindowPtr, (w, key, scancode, action, mods) -> {
            int imguiKey = mapGLFWKeyToImGuiKey(key);
                if (imguiKey != -1) {
                    io.addKeyEvent(imguiKey, action == GLFW_PRESS);
                }

                io.setKeyCtrl((mods & GLFW_MOD_CONTROL) != 0);
                io.setKeyShift((mods & GLFW_MOD_SHIFT) != 0);
                io.setKeyAlt((mods & GLFW_MOD_ALT) != 0);
                io.setKeySuper((mods & GLFW_MOD_SUPER) != 0);

                // If imGUI not listening to key inputs pass it to our KeyListener
                if (!io.getWantCaptureKeyboard()) {
                    KeyListener.keyCallback(w, key, scancode, action, mods);
                }
        });

        glfwSetCharCallback(glfwWindowPtr, (w, c) -> {
            if (c != GLFW_KEY_DELETE) {
                io.addInputCharacter(c);
            }
        });

        glfwSetMouseButtonCallback(glfwWindowPtr, (w, button, action, mods) -> {
            final boolean[] mouseDown = new boolean[5];

            mouseDown[0] = button == GLFW_MOUSE_BUTTON_1 && action != GLFW_RELEASE;
            mouseDown[1] = button == GLFW_MOUSE_BUTTON_2 && action != GLFW_RELEASE;
            mouseDown[2] = button == GLFW_MOUSE_BUTTON_3 && action != GLFW_RELEASE;
            mouseDown[3] = button == GLFW_MOUSE_BUTTON_4 && action != GLFW_RELEASE;
            mouseDown[4] = button == GLFW_MOUSE_BUTTON_5 && action != GLFW_RELEASE;

            io.setMouseDown(mouseDown);

            if (!io.getWantCaptureMouse() && mouseDown[1]) {
                ImGui.setWindowFocus(null);
            }

            // If imGUI not listening to mouse inputs pass it to our MouseListener
            if (!io.getWantCaptureMouse()) {
                MouseListener.mouseButtonCallback(w, button, action, mods);
            }
        });

        glfwSetScrollCallback(glfwWindowPtr, (w, xOffset, yOffset) -> {
            io.setMouseWheelH(io.getMouseWheelH() + (float) xOffset);
            io.setMouseWheel(io.getMouseWheel() + (float) yOffset);
        });

        io.setSetClipboardTextFn(new ImStrConsumer() {
            @Override
            public void accept(final String s) {
                glfwSetClipboardString(glfwWindowPtr, s);
            }
        });

        io.setGetClipboardTextFn(new ImStrSupplier() {
            @Override
            public String get() {
                final String clipboardString = glfwGetClipboardString(glfwWindowPtr);
                if (clipboardString != null) {
                    return clipboardString;
                } else {
                    return "";
                }
            }
        });
    }

    private void initFonts(ImGuiIO io ){

        final ImFontAtlas fontAtlas = io.getFonts();
        final ImFontConfig fontConfig = new ImFontConfig(); // Natively allocated object, should be explicitly destroyed

        // Freetype rendering
        fontAtlas.setFreeTypeRenderer(true);

        // Font config for additional fonts (Use this once there is already a font added to the config)
        // fontConfig.setMergeMode(true);  // Enable merge mode to merge cyrillic, japanese and icons with default font

        // Here we are using it just to combine all required glyphs in one place
        //final ImFontGlyphRangesBuilder rangesBuilder = new ImFontGlyphRangesBuilder(); // Glyphs ranges provide
        //rangesBuilder.addRanges(fontAtlas.getGlyphRangesDefault());
        //rangesBuilder.addRanges(fontAtlas.getGlyphRangesCyrillic());
        //rangesBuilder.addRanges(fontAtlas.getGlyphRangesJapanese());

        // Store it in a short array and pass it when adding fonts to the font atlas
        //final short[] glyphRanges = rangesBuilder.buildRanges();
        //fontAtlas.addFontFromMemoryTTF(loadFromResources("Tahoma.ttf"), 14, fontConfig, glyphRanges); // cyrillic glyphs
        //fontAtlas.addFontFromMemoryTTF(loadFromResources("NotoSansCJKjp-Medium.otf"), 14, fontConfig, glyphRanges); // japanese glyphs

        
        // Or just set one glyph range in the fontconfig
        fontConfig.setGlyphRanges(fontAtlas.getGlyphRangesDefault());

        fontAtlas.addFontFromFileTTF("assets/fonts/GenericMobileSystemNuevo.ttf", 20, fontConfig);

        fontAtlas.build();

        fontConfig.destroy(); // After all fonts were added we don't need this config more

    }

    private int mapGLFWKeyToImGuiKey(int glfwKey) {
        switch (glfwKey) {
            case GLFW_KEY_TAB: return ImGuiKey.Tab;
            case GLFW_KEY_LEFT: return ImGuiKey.LeftArrow;
            case GLFW_KEY_RIGHT: return ImGuiKey.RightArrow;
            case GLFW_KEY_UP: return ImGuiKey.UpArrow;
            case GLFW_KEY_DOWN: return ImGuiKey.DownArrow;
            case GLFW_KEY_PAGE_UP: return ImGuiKey.PageUp;
            case GLFW_KEY_PAGE_DOWN: return ImGuiKey.PageDown;
            case GLFW_KEY_HOME: return ImGuiKey.Home;
            case GLFW_KEY_END: return ImGuiKey.End;
            case GLFW_KEY_INSERT: return ImGuiKey.Insert;
            case GLFW_KEY_DELETE: return ImGuiKey.Delete;
            case GLFW_KEY_BACKSPACE: return ImGuiKey.Backspace;
            case GLFW_KEY_SPACE: return ImGuiKey.Space;
            case GLFW_KEY_ENTER: return ImGuiKey.Enter;
            case GLFW_KEY_ESCAPE: return ImGuiKey.Escape;
            case GLFW_KEY_KP_ENTER: return ImGuiKey.KeypadEnter;
            case GLFW_KEY_A: return ImGuiKey.A;
            case GLFW_KEY_C: return ImGuiKey.C;
            case GLFW_KEY_V: return ImGuiKey.V;
            case GLFW_KEY_X: return ImGuiKey.X;
            case GLFW_KEY_Y: return ImGuiKey.Y;
            case GLFW_KEY_Z: return ImGuiKey.Z;
            // add more as needed
            default: return -1;
        }
    }

    private void setDockSpace(){
        int windowFlags = ImGuiWindowFlags.MenuBar | ImGuiWindowFlags.NoDocking;

        ImGui.setNextWindowPos(0.0f, 0.0f, ImGuiCond.Always);
        ImGui.setNextWindowSize(Window.getWidth(), Window.getHeight());

        ImGui.pushStyleVar(ImGuiStyleVar.WindowRounding, 0.0f);
        ImGui.pushStyleVar(ImGuiStyleVar.WindowBorderSize, 0.0f);

        windowFlags |= ImGuiWindowFlags.NoTitleBar | ImGuiWindowFlags.NoCollapse | ImGuiWindowFlags.NoResize |
                    ImGuiWindowFlags.NoMove | ImGuiWindowFlags.NoBringToFrontOnFocus | ImGuiWindowFlags.NoNavFocus;

        ImGui.begin("DockSpace", new ImBoolean(true), windowFlags);
        ImGui.popStyleVar(2);

        // DockSpace
        ImGui.dockSpace(ImGui.getID("DockSpace"));
    }


    public void update(float deltaTime, Scene scene){
        startFrame(deltaTime);

        // Any Dear ImGui code SHOULD go between ImGui.newFrame()/ImGui.render() methods
        imGuiGl3.newFrame(); 
        ImGui.newFrame();
        setDockSpace();
        scene.ImGUI();
        scene.sceneImGUI();
        GameViewWindow.ImGUI();
        ImGui.showDemoWindow();
        ImGui.end();
        ImGui.render();

        endFrame();
    }

    private void startFrame(final float deltaTime) {
        float[] winHeight = {Window.getHeight()};
        float[] winWidth = {Window.getWidth()};
        double[] mousePosX = {0};
        double[] mousePosY = {0};

        // Get window properties and mouse position
        glfwGetCursorPos(glfwWindowPtr, mousePosX, mousePosY);

        // We SHOULD call those methods to update Dear ImGui state for the current frame
        final ImGuiIO io = ImGui.getIO();
        io.setDisplaySize(winWidth[0], winHeight[0]);
        io.setDisplayFramebufferScale(1,1);
        io.setMousePos((float) mousePosX[0], (float) mousePosY[0]);
        io.setDeltaTime(deltaTime);

        // Update the mouse cursor
        final int imguiCursor = ImGui.getMouseCursor();
        glfwSetCursor(glfwWindowPtr, mouseCursors[imguiCursor]);
        glfwSetInputMode(glfwWindowPtr, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
    }

    private void endFrame() {
        // After Dear ImGui prepared a draw data, we use it in the LWJGL3 renderer.
        // At that moment ImGui will be rendered to the current OpenGL context.
        ImGui.render();     // ImGui.render() must be called first to generate draw data
        imGuiGl3.renderDrawData(ImGui.getDrawData()); 
    }

    // If you want to clean a room after yourself - do it by yourself
    private void destroyImGui() {
        imGuiGl3.shutdown();
        ImGui.destroyContext();
    }
}
