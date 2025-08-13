package dev.kanazukii.ether.engine.utils;

import java.io.File;
import java.nio.file.Path;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

public class FileManager {

    public static void setUpAssetsFolder(){
        File assetsFolder = new File("assets");
        if(!assetsFolder.exists()){
            assetsFolder.mkdirs();
        }
    }
    
    //TODO: Fix limit to only allowed root path
    public static String filePickerDialog(String extensionHint, String... fileExtensions) {
        File allowedRoot = new File("assets").getAbsoluteFile();

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(allowedRoot);

        FileNameExtensionFilter filter = new FileNameExtensionFilter(extensionHint, fileExtensions);
        fileChooser.setFileFilter(filter);
        fileChooser.setAcceptAllFileFilterUsed(false);

        int result = fileChooser.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile().getAbsoluteFile();

            // Check if file is inside allowedRoot
            if (selectedFile.toPath().startsWith(allowedRoot.toPath())) {
                try {
                    // Get relative path from allowedRoot
                    Path relativePath = allowedRoot.getParentFile().toPath().relativize(selectedFile.toPath());
                    return relativePath.toString();
                } catch (Exception e) {
                    e.printStackTrace();
                    // Fallback to absolute path
                    return selectedFile.getAbsolutePath();
                }
            } else {
                JOptionPane.showMessageDialog(null, "Selection outside allowed directory.");
            }
        }
        return null;
    }

}
