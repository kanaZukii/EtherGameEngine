package dev.kanazukii.ether.engine.scenes;

public abstract class SceneInitializer {
    public abstract void init(Scene scene);
    public abstract void loadResource(Scene scene);
    public abstract void ImGUI();
}
