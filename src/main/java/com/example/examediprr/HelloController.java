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
import javafx.scene.paint.Color;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class HelloController {
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
    public void onGoButton(ActionEvent actionEvent) {
        if (filenameTF.getText().trim().isEmpty()){
            return;
        }
        goPath(filenameTF.getText());
    }private void refreshInfo(File file) {
        if (file.exists()) {
            pathL.setText(file.getAbsolutePath());

            }
    }
    public void goPath(String path){
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
}