package fintrack;

import fintrack.db.Conexao;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class FinApp extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Conexao.inicializar();
        FXMLLoader loader = new FXMLLoader(FinApp.class.getResource("/fintrack/view/main.fxml"));
        Scene scene = new Scene(loader.load());
        scene.getStylesheets().add(FinApp.class.getResource("/fintrack/view/style.css").toExternalForm());
        stage.setTitle("FinTrack - Finanças Pessoais");
        stage.setScene(scene);
        stage.setMinWidth(760);
        stage.setMinHeight(520);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
