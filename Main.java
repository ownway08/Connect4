package com.internshala.connect4;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {
   private Controller controller;
    @Override
    public void start(Stage primaryStage) throws Exception{
       FXMLLoader loader=new FXMLLoader(getClass().getResource("game.fxml"));
        GridPane rootgridpane=loader.load();

        controller=loader.getController();
        controller.createplayground();
        MenuBar menuBar=createmenu();
        menuBar.prefWidthProperty().bind(primaryStage.widthProperty());
        Pane menupane= (Pane) rootgridpane.getChildren().get(0);
        menupane.getChildren().addAll(menuBar);
        Scene scene=new Scene(rootgridpane);
        primaryStage.setScene(scene);
        primaryStage.setTitle("CONNECT FOUR");
        primaryStage.setResizable(false);
        primaryStage.show();

    }

    private MenuBar createmenu(){
        Menu filemenu=new Menu("FILE");
        MenuItem newgame=new MenuItem("NEW GAME");
        newgame.setOnAction(event -> resetgame());
        MenuItem resetgame=new MenuItem("RESET");
        resetgame.setOnAction(event -> resetgame());
        MenuItem exitgame=new MenuItem("EXIT");
        exitgame.setOnAction(event -> exitgame());
        SeparatorMenuItem separatorMenuItem=new SeparatorMenuItem();
        filemenu.getItems().addAll(newgame,resetgame,separatorMenuItem,exitgame);
        MenuBar menuBar=new MenuBar();
        Menu helpmenu=new Menu("HELP");
        MenuItem aboutgame=new MenuItem("ABOUT GAME");
        aboutgame.setOnAction(event -> aboutconnect4());
        SeparatorMenuItem separatorMenuItem1=new SeparatorMenuItem();
        MenuItem aboutdeveloper = new MenuItem("ABOUT DEVELOPER");
        aboutdeveloper.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                aboutdeveloper();
            }
        });
        helpmenu.getItems().addAll(aboutgame,separatorMenuItem1,aboutdeveloper);
        menuBar.getMenus().addAll(filemenu,helpmenu);
        return menuBar;

    }

    private void aboutdeveloper() {
        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("ABOUT ME");
        alert.setHeaderText("TARUN KUMAR");
        alert.setContentText("I AM BTECH CSE STUDENT.\n I LIKE TO LEARN ABOUT  AND DEVLOP NEW GAME.\n THANK YOU");
        alert.show();
    }


    private void aboutconnect4() {
        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("ABOUT CONNECT 4");
        alert.setHeaderText("HOW TO PLAY?");
        alert.setContentText("Connect Four is a two-player connection game in which the players first choose a color and then take turns dropping colored discs from the top into a seven-column, six-row vertically suspended grid.\n The pieces fall straight down, occupying the next available space within the column. \nThe objective of the game is to be the first to form a horizontal, vertical, or diagonal line of four of one's own discs. Connect Four is a solved game. \nThe first player can always win by playing the right moves.\n");
        alert.show();
    }

    private void exitgame() {
        Platform.exit();
        System.exit(0);
    }


    private void resetgame() {
        controller.resetgame();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
