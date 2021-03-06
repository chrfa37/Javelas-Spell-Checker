package fxmlGUI;

import java.io.IOException;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import spellchecker.Dictionary;
import spellchecker.Word;

public class JavelasApp extends Application {

	private Stage primaryStage;
	private BorderPane root;
	protected ArrayList<Word> wrongWords;
	protected int counter;

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
        this.primaryStage.setTitle("JavelasSpellChecker");
        
        showMainMenu();

        try {
			Dictionary customDic = new Dictionary();
			Dictionary grDic = new Dictionary("Greek", 73, 974);
			Dictionary enDic = new Dictionary("English", 58, 122);
		} catch (IOException iOException) { 
			Alert alert = new Alert(AlertType.WARNING);
	        alert.initOwner(getPrimaryStage());
	        alert.setTitle("Error");
	        alert.setHeaderText("Error while loading the dictionaries");
	        alert.setContentText("The program was not able to load the dictionary files.");
	        alert.showAndWait();		
		}
        
        //showMistakeMenu(new Word("testWord"));
	}

	
	public void showMainMenu() {   
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(JavelasApp.class.getResource("MainMenu.fxml"));
			root = (BorderPane) loader.load();
			
        	Scene scene = new Scene(root);
        	primaryStage.setScene(scene);
            primaryStage.show();
            
            MainMenuController controller = loader.getController();
            controller.setJavelasApp(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void showMistakeMenu(String text) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(JavelasApp.class.getResource("MistakeMenu.fxml"));
			root = (BorderPane) loader.load();
			
        	Scene scene = new Scene(root);
        	primaryStage.setScene(scene);
            primaryStage.show();
            
            MistakeMenuController controller = loader.getController();
            controller.setJavelasApp(this);
            findWrongWords(text);
            if(wrongWords.size() > 0) {
            	resetCounter();
            	counter = 0;
            	handleMistake(controller, counter);
            }
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void handleMistake(MistakeMenuController controller, int index) {
		 controller.setWord(wrongWords.get(counter));
         controller.setWrongWordPanel();
	}
	
	public void findWrongWords(String text) {
		
		Word.createPunMap();
		Word word = new Word(null);
		wrongWords = new ArrayList<Word>();
		
		for (String wordStr: text.split(" ")) {
			boolean exists = false;
			word.setWordStr(wordStr);
			for (Dictionary dictionary: Dictionary.dictionaries) {
				if (word.getWordStr().matches("-?\\d+(\\.\\d+)+(\\,\\d+)?") || word.getWordStr().matches("-?\\d+(\\,\\d+)+(\\.\\d+)?")) {
					exists = true;
				} else {
					if (dictionary.wordExists(word)) {
						exists = true;
					}
					
					if (word.getWordStr().length() >= 2 && dictionary.wordExists(new Word(word.getWordStr().substring(0, 1).toLowerCase() + word.getWordStr().substring(1)))) {
						exists = true;
					}
				}
			}
			if (!exists) {
				wrongWords.add(new Word(word.getWordStr()));
			}		
		}
	}
	
	public void showThankYou() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(JavelasApp.class.getResource("Thankyou.fxml"));
			root = (BorderPane) loader.load();
			
        	Scene scene = new Scene(root);
        	primaryStage.setScene(scene);
            primaryStage.show();
            
            ThankYouController controller = loader.getController();
            controller.setJavelasApp(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void resetCounter() {
		this.counter = 0;
	}
	
	public void raiseCounter() {
		this.counter++;
	}

	public Stage getPrimaryStage() {
		return primaryStage;
	}

	public static void main(String[] args) {
		launch(args);
	}
}
