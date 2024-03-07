package github.B077AS.AudioMultiShare.main;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;
import github.B077AS.AudioMultiShare.audio.AudioDeviceSelector;
import github.B077AS.AudioMultiShare.audio.AudioRouting;

public class MainWindow extends Application {

	private AudioRouting audioRouting;
	private AudioDeviceSelector audioSelector;
	private ComboBox<String> sourceComboBox;
	private ComboBox<String> speakerOneComboBox;
	private ComboBox<String> speakerTwoComboBox;

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.audioRouting = new AudioRouting();
		this.audioSelector = new AudioDeviceSelector();
		
		primaryStage.setTitle("Audio Multi-Share");
		Image icon = new Image("/github/B077AS/AudioMultiShare/main/logo.png");		
		primaryStage.getIcons().add(icon);

		primaryStage.setOnCloseRequest((event) -> {
			try {
				this.audioRouting.stopRunning();
			} catch (Exception e) {
			}
		});

		BorderPane mainLayout = new BorderPane();

		Scene scene = new Scene(mainLayout, 900, 500);		

		Label titleLabel = new Label("Audio Multi-Share");
		titleLabel.setStyle(
				"-fx-font-size: 3em; " +
						"-fx-font-weight: bold; " +
						"-fx-text-fill: #0096c9; " +
						"-fx-font-family: 'Arial'; " +
						"-fx-padding: 20px;"
				);

		VBox centreBox = new VBox();
		centreBox.setSpacing(5);
		centreBox.setAlignment(Pos.CENTER);

		Label sourceLabel = new Label("Select Virtual Cable");

		Label speakerOneLabel = new Label("Speaker 1");

		Label speakerTwoLabel = new Label("Speaker 2");

		sourceComboBox = new ComboBox<String>(FXCollections.observableArrayList(audioSelector.getInputDevices().keySet()));
		sourceComboBox.setFocusTraversable(false);

		speakerOneComboBox = new ComboBox<String>(FXCollections.observableArrayList(audioSelector.getOutputDevices().keySet()));
		speakerOneComboBox.setFocusTraversable(false);

		speakerTwoComboBox = new ComboBox<String>(FXCollections.observableArrayList(audioSelector.getOutputDevices().keySet()));
		speakerTwoComboBox.setFocusTraversable(false);

		HBox buttonsBox=new HBox();
		buttonsBox.setPadding(new Insets(20, 0, 0, 0));
		buttonsBox.setSpacing(5);
		buttonsBox.setAlignment(Pos.CENTER);

		Button startButton = new Button("Start");
		startButton.setOnAction((event) -> {
			TargetDataLine source = (TargetDataLine)audioSelector.getInputDevices().get(sourceComboBox.getValue());
			SourceDataLine speakerOne = (SourceDataLine)audioSelector.getOutputDevices().get(speakerOneComboBox.getValue());
			SourceDataLine speakerTwo = (SourceDataLine)audioSelector.getOutputDevices().get(speakerTwoComboBox.getValue());

			if(source!=null && speakerOne!=null && speakerTwo!=null) {
				Thread serverPortThread = new Thread(() -> {
					this.audioRouting.startRouting(source, speakerOne, speakerTwo);
				});
				serverPortThread.setDaemon(true);
				serverPortThread.start();
			}else {
				Alert alert = new Alert(Alert.AlertType.WARNING);
				alert.setTitle("Error");
				alert.setHeaderText(null);
				alert.setContentText("Please select all fields.");
				alert.showAndWait();
			}
		});
		startButton.setFocusTraversable(false);

		Button stopButton = new Button("Stop");
		stopButton.setOnAction((event) -> {
			this.audioRouting.stopRunning();
		});
		stopButton.setFocusTraversable(false);

		Button refreshButton = new Button("Refresh");
		refreshButton.setOnAction((event) -> {
			this.audioSelector = new AudioDeviceSelector();
			sourceComboBox.setItems(FXCollections.observableArrayList(audioSelector.getInputDevices().keySet()));
			speakerOneComboBox.setItems(FXCollections.observableArrayList(audioSelector.getOutputDevices().keySet()));
			speakerTwoComboBox.setItems(FXCollections.observableArrayList(audioSelector.getOutputDevices().keySet()));
		});
		refreshButton.setFocusTraversable(false);

		buttonsBox.getChildren().addAll(startButton, stopButton, refreshButton);

		centreBox.getChildren().addAll(new Node[]{titleLabel, sourceLabel, sourceComboBox, speakerOneLabel, speakerOneComboBox, speakerTwoLabel, speakerTwoComboBox, buttonsBox});
		mainLayout.setCenter(centreBox);

		primaryStage.setScene(scene);
		primaryStage.show();
	}
}