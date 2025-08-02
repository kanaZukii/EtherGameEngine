package dev.kanazukii.ether.engine.components;

import org.joml.Vector2f;
import org.joml.Vector3f;

import dev.kanazukii.ether.engine.Camera;
import dev.kanazukii.ether.engine.Window;
import dev.kanazukii.ether.engine.renderer.DebugRenderer;
import dev.kanazukii.ether.engine.utils.Configs;

public class GridLines extends Component {
    

    @Override
    public void editorUpdate(float deltaTime){
        Camera camera = Window.getScene().getCamera();

        Vector2f cameraPosition = camera.getPosition();
        Vector2f projectionSize = camera.getProjectionSize();

        float firstX = (float)(Math.floor(cameraPosition.x / Configs.GRID_WIDTH) * Configs.GRID_WIDTH);
        float firstY = (float)(Math.floor(cameraPosition.y / Configs.GRID_HEIGHT) * Configs.GRID_HEIGHT);

        int numVerticalLines = (int)Math.ceil(projectionSize.x * camera.getZoom() / Configs.GRID_WIDTH);
        int numHorizontalLines = (int)Math.ceil(projectionSize.y * camera.getZoom() / Configs.GRID_HEIGHT);

        float width = (float)(projectionSize.x * camera.getZoom());
        float height = (float)(projectionSize.y * camera.getZoom());

        final float epsilon = 0.0001f;

        int maxLines = Math.max(numVerticalLines, numHorizontalLines);

        for (int i = 0; i < maxLines; i++) {
            float x = firstX + (Configs.GRID_WIDTH * i) + epsilon;
            float y = firstY + (Configs.GRID_HEIGHT * i) + epsilon;

            if (i < numVerticalLines) {
                DebugRenderer.addLine2D(
                    new Vector2f(x, firstY),
                    new Vector2f(x, firstY + height),
                    new Vector3f(0.8f, 0.8f, 0.8f));
            }

            if (i < numHorizontalLines) {
                DebugRenderer.addLine2D(
                    new Vector2f(firstX, y),
                    new Vector2f(firstX + width, y),
                    new Vector3f(0.8f, 0.8f, 0.8f));
            }
        }
        
    }
}
