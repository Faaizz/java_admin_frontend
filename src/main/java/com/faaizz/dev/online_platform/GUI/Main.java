package com.faaizz.dev.online_platform.GUI;

import com.faaizz.dev.online_platform.GUI.exceptions.AuthenticationException;
import com.faaizz.dev.online_platform.api_outbound.platform.CookieStore;
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

import java.io.IOException;

public class Main extends Application {

    private Stage main_stage;
    private Rectangle2D screen_size;

    private static Main instance;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        try{

            // SET INSTANCE VARIABLE
            instance= this;

            // SET INTERNAL STATIC Stage VARIABLE
            this.main_stage= primaryStage;

            // SET APPLICATION DATA
            SettingsData.setBase_URL("127.0.0.1:8000");
            SettingsData.setApi_path("/api");
            SettingsData.setApi_token("x6Q7KqJfghcRzgo1bCpKStslqsOhBR8VnQDe0NgAtAGOhnkWN6YCENhg21tO");

            // LOAD LOGIN PAGE BY DEFAULT
            Parent root= FXMLLoader.load(getClass().getResource("view/login/login.fxml"));

            primaryStage.setTitle("Admin Portal");

            //GET SCREEN SIZE
            screen_size= Screen.getPrimary().getBounds();

            //SET SCENE AND MAKE ITS SIZE FULLSCREEN
            primaryStage.setScene(new Scene(root, ( screen_size.getWidth() - screen_size.getWidth()/10), ( screen_size.getHeight() - screen_size.getHeight()/5 )));

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


    /*========================================================================================*/

    /**
     * RETURN CURRENT INSTANCE
     * @return
     */
    public static Main getInstance(){
        return instance;
    }


    /*========================================================================================*/


    public void redirectToLogin() throws AuthenticationException, IOException {

        Parent login_page= FXMLLoader.load(getClass().getResource("view/login/login.fxml"));

        main_stage.setScene(new Scene(login_page, ( screen_size.getWidth() - screen_size.getWidth()/10), ( screen_size.getHeight() - screen_size.getHeight()/5 )));

    }


    /**
     * REDIRECT TO MANAGE PRODUCTS (SEARCH PRODUCTS)
     * @throws IOException
     * @throws AuthenticationException
     */
    public void redirectToManageProducts() throws IOException, AuthenticationException {

        // VERIFY AUTHENTICATION
        checkAuthentication();

        Parent manage_products= FXMLLoader.load(getClass().getResource("view/products/manage.fxml"));

        main_stage.setScene(new Scene(manage_products, ( screen_size.getWidth() - screen_size.getWidth()/10), ( screen_size.getHeight() - screen_size.getHeight()/5 )));

    }


    public void logout() throws Exception {

        // VERIFY AUTHENTICATION
        checkAuthentication();

        // LOGOUT USER
        StaffResource staff_resource= new StaffResource(SettingsData.getBase_URL(), SettingsData.getApi_path(), SettingsData.getApi_token());
        staff_resource.logout();

        InstanceData.setAuthenticated(false);
        InstanceData.setCurrentStaff(null);

        // REDIRECT TO LOGIN PAGE
        this.redirectToLogin();

    }

    /*========================================================================================*/

    /**
     * CHECKS IF A USER IS AUTHENTICATED IN THE CURRENT INSTANCE
     * @throws AuthenticationException
     */
    public void checkAuthentication() throws AuthenticationException {
        if(!InstanceData.getAuthenticated()){
            throw new AuthenticationException();
        }
    }

}
