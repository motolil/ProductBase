package by.htc;

import by.htc.service.DatabaseInitializer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Главный класс приложения.
 */
public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        DatabaseInitializer.initialize();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MainView.fxml"));
        Parent root = loader.load();

        primaryStage.setTitle("Справочник товаров");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
                launch(args);
            }
}
