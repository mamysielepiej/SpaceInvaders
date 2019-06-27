package application;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;

/**
 * Startuje aplikacje.
 * Wczytuje plik fxml, ustawia parametry okna, tworzy handlery i uruchamia aplikacje.
 * @author ZuzaKunicka
 * @version 1.8
 *
 */

public class Main extends Application {
	
	/**
	 * Punkt pocz�tkowy aplikacji.
	 * Ma za zadanie ustawi� wybrane parametry na primaryStage i wczyta�
	 * z pliku fxml zawarto��, kt�r� umie�ci w scene, a scene w oknie.
	 * Dodaje handlery i definiuje jakie funkcje maja byc wywolywane dla danego zdarzenia.
	 * Adnotacja: Jest ona wykonywana przez JavaFX Application Thread
	 * @param	primaryStage	g��wne okno programu
	 * @see		Stage
	 * @see		Application
	 */
	@Override
	public void start(Stage primaryStage) throws IOException {
		primaryStage.setTitle("SpaceInvaders");
		primaryStage.setResizable(false);
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("/application/Sample.fxml"));
		StackPane mainLayout = loader.load();
		Scene scene = new Scene(mainLayout);
		Controller c = loader.getController();
		scene.addEventFilter(KeyEvent.KEY_PRESSED, e -> c.handler(e));
		primaryStage.addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, t -> c.close(t));
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	/**
	 *	G��wna funkcja programu. 
	 *	Wywo�uje funkcje z klasy Application, kt�ra rozpocznie program.
	 *	@param	args 	Command-Line Arguments
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
