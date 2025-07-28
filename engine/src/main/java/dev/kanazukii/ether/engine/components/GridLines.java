package dev.kanazukii.ether.engine.components;

import org.joml.Vector2f;
import org.joml.Vector3f;

import dev.kanazukii.ether.engine.Camera;
import dev.kanazukii.ether.engine.Window;
import dev.kanazukii.ether.engine.renderer.DebugRenderer;
import dev.kanazukii.ether.engine.utils.Configs;

public class GridLines extends Component {
    

    @Override
    public void update(float deltaTime){
        Camera camera = Window.getScene().getCamera();

        Vector2f cameraPosition = camera.getPosition();
        Vector2f projectionSize = camera.getProjectionSize();

        int firstX = (int)(cameraPosition.x/Configs.GRID_WIDTH) * Configs.GRID_WIDTH;
        int firstY = (int)(cameraPosition.y/Configs.GRID_HEIGHT) * Configs.GRID_HEIGHT;

        int numVerticalLines = (int)(projectionSize.x * camera.getZoom()/ Configs.GRID_WIDTH);
        int numHorinzontalLines = (int)(projectionSize.y * camera.getZoom()/ Configs.GRID_HEIGHT);

        int width = (int)(projectionSize.x * camera.getZoom());
        int height = (int)(projectionSize.y * camera.getZoom());

        int maxLines = numHorinzontalLines < numVerticalLines ? numVerticalLines : numHorinzontalLines;
        
        for(int i = 0; i < maxLines; i++){
            int x = firstX + (Configs.GRID_WIDTH * i);
            int y = firstY + (Configs.GRID_HEIGHT * i);

            if(i < numVerticalLines){
                DebugRenderer.addLine2D(
                    new Vector2f(x, firstY), 
                    new Vector2f(x, firstY + height),
                    new Vector3f(0.8f,0.8f,0.8f));
            }

            if(i < numHorinzontalLines){
                if(i < numVerticalLines){
                    DebugRenderer.addLine2D(
                        new Vector2f(firstX, y), 
                        new Vector2f(firstX + width, y),
                        new Vector3f(0.8f,0.8f,0.8f));
                }
            }
        }
    }
}
