package com.example.examediprr;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class VisorFicherosGuarController {
    public TextField filenameTF;
    public Label pathL;
    public Label type;
    public Label size;
    public Label numberFiles;
    public VBox vBoxListFiles;
    private String currentAbsPAth = "";
    public Label feedbackL;
    ListView listFilesLV = null;
    List<String> listFiles = null;
    @FXML
    public void onGoButton(ActionEvent actionEvent) throws IOException {
        if (filenameTF.getText().trim().isEmpty()){
            return;
        }
        goPath(filenameTF.getText());

    }private void refreshInfo(File file) throws IOException {
        if (file.exists()) {
            pathL.setText(file.getAbsolutePath());
            if(file.isDirectory()){
                try (Stream<Path> files = Files.list(Paths.get(file.getAbsolutePath()))) {
                    long count = files.count();
                    numberFiles.setText(Long.toString(count));
                }
                catch (Exception e){
                    e.getMessage();
                }
            }
            else{
                numberFiles.setText("---");
            }
            if (!file.exists() || !file.isFile() || file.isDirectory()){
                size.setText("---");
            }
            else{
                if(getFileSizeMegaBytes(file).isEmpty()){
                    size.setText("---");
                }
                else{
                    size.setText(getFileSizeMegaBytes(file));
                }

            }
            type.setText(cogerElTipoFichero(file));


        }
    }

    private String cogerElTipoFichero(File file) {
        if (file.isDirectory()){
            return "Carpeta";
        }
        else{
            return FilenameUtils.getExtension(file.getAbsolutePath());
        }
    }

    private static String getFileSizeMegaBytes(File file) {
        return (double) file.length() / (1024 * 1024) + " mb";
    }

    public void goPath(String path) throws IOException {
        listFiles = new ArrayList<>();

        File newFile = new File(path.trim());

        if (newFile.exists()) {
            currentAbsPAth = newFile.getAbsolutePath();
            if (newFile.isDirectory()) {
                try {
                    File[] arrayFiles = newFile.listFiles();
                    for (File f : arrayFiles) {
                        listFiles.add(f.getName());
                    }
                }catch (Exception e){
                    System.out.println(e.getMessage());

                }
            }
        }
        refreshInfo(newFile);
        fillListFiles(listFiles);
    }
    private void fillListFiles(List<String> listFiles) {
        ObservableList<Node> childs = vBoxListFiles.getChildren();
        if (listFilesLV == null) {
            listFilesLV = new ListView();
            listFilesLV.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    try {
                        String selected = listFilesLV.getSelectionModel().getSelectedItem().toString();
                        String newAbsPath = currentAbsPAth + File.separator + selected;

                        filenameTF.setText(newAbsPath);
                        goPath(newAbsPath);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
            });
            childs.add(listFilesLV);
        } else{
            listFilesLV.getItems().clear();
        }

        for (String fn:listFiles) {
            listFilesLV.getItems().add(fn);
        }


    }

    public void OnRegresarButtonClick(ActionEvent actionEvent) throws IOException {
        if (filenameTF.getText().trim().isEmpty()){
            return;
        }
        else{
            goBack(filenameTF.getText());
        }

    }

    private void goBack(String path) throws IOException {
        File ficheroActual= new File(path.trim());
        if(ficheroActual.exists()){
            Path pathOrigen=Paths.get(path).toAbsolutePath().getParent();
            goPath(pathOrigen.toString());
            filenameTF.setText(pathOrigen.toString());
        }
    }
}