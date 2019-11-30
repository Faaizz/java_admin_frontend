package com.faaizz.dev.online_platform.GUI;

import com.faaizz.dev.online_platform.api_outbound.platform.StaffResource;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        try{

            // SET APPLICATION DATA
            SettingsData.setBase_URL("127.0.0.1:8000");
            SettingsData.setApi_path("/api");
            SettingsData.setApi_token("x6Q7KqJfghcRzgo1bCpKStslqsOhBR8VnQDe0NgAtAGOhnkWN6YCENhg21tO");

            // TODO: REMOVE THIS
            // Simulate Staff Login
            StaffResource staffResource= new StaffResource(SettingsData.getBase_URL(), SettingsData.getApi_path(), SettingsData.getApi_token());
            staffResource.login("barton.enid@bogan.com", "Qui tempore dolores qui excepturi corrupti magni.", "yes");

            Parent root= FXMLLoader.load(getClass().getResource("view/products/add.fxml"));

            primaryStage.setTitle("Admin Portal");

            //GET SCREEN SIZE
            Rectangle2D screenSize= Screen.getPrimary().getBounds();

            //SET SCENE AND MAKE ITS SIZE FULLSCREEN
            primaryStage.setScene(new Scene(root, ( screenSize.getWidth() - screenSize.getWidth()/10), ( screenSize.getHeight() - screenSize.getHeight()/5 )));

            // REMOVE WINDOW BORDER
            primaryStage.initStyle(StageStyle.UNDECORATED);

            //SHOW STAGE
            primaryStage.show();


        } catch(Exception e){
            //CATCH BLOCK TO HANDLE EXCEPTIONS THROWN FROM ANY PART OF THE APPLICATION
            Alert exceptionAlert= new Alert(Alert.AlertType.ERROR);

            exceptionAlert.setResizable(true);

            exceptionAlert.setHeaderText("An Exception Occurred");
            exceptionAlert.setContentText(e.getClass().getName() + "\n" + e.getMessage());

            exceptionAlert.show();

            e.printStackTrace();
        }

    }
}
