package fintrack;

import fintrack.db.Conexao;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Objects;

public class FinApp extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Conexao.inicializar();
        URL layout = Objects.requireNonNull(
                FinApp.class.getResource("/fintrack/view/main.fxml"),
                "Recurso FXML não encontrado: /fintrack/view/main.fxml");
        URL stylesheet = Objects.requireNonNull(
                FinApp.class.getResource("/fintrack/view/style.css"),
                "Recurso CSS não encontrado: /fintrack/view/style.css");

        FXMLLoader loader = new FXMLLoader(layout);
        Scene scene = new Scene(loader.load());
        scene.getStylesheets().add(stylesheet.toExternalForm());
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
