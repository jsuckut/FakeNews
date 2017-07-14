/**
 * This is the main class for this program
 * @author Benjamin M. Abdel-Karim
 * @since 2017-05-13
 * @version 2017-05-13
 * @currentDevelopmentTime 19 h
 * @source: https://www.youtube.com/watch?v=oxshRmic9sk
 * Useful movie for the basics.
 */


package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import static sample.Controller.getNumberOfSentence;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Applications News Analyser");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

}
