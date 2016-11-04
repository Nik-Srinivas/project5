package assignment5;

import javafx.scene.control.TextField;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Scanner;

	
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import assignment5.Painter;

public class Main extends Application {

    //APPLICATION STUFF
    static GridPane grid = new GridPane();
    
//	@Override
//	public void start(Stage primaryStage) {
//		try {			
//
//			grid.setGridLinesVisible(true);
//
//			Scene scene = new Scene(grid, Params.world_width, Params.world_height);
//			primaryStage.setScene(scene);
//			
//			primaryStage.show();
//			
//			// Paints the icons.
////			Painter.paint();
//			
//		} catch(Exception e) {
//			e.printStackTrace();		
//		}
//	}
    public static int sceneScale = 50;
    public class SecondStage extends Stage {
    	Label x = new Label("Second stage");
    	VBox y = new VBox();

    	SecondStage(){
    	    y.getChildren().add(x);
    	    this.setTitle("World");
    	    Scene scene = new Scene(grid, Params.world_width * sceneScale, Params.world_height * sceneScale);
    	    scene.setFill(javafx.scene.paint.Color.ANTIQUEWHITE);
    	    this.setScene(scene);
    	    this.show();
    	   }    
    	}
    
    @Override
    public void start(Stage primaryStage) {
    	new SecondStage();
    	GridPane myLayout = new GridPane();
        primaryStage.setTitle("Makin' Critters!");
        Button show = new Button("Show Me The World!");
        Button make = new Button("Make Critter!");
//        Button stats = new Button("What's Craig Up To?");
        Button step = new Button("Run a Time Step!");
        
//		primaryStage.setScene(scene);
//		primaryStage.show();
//        Button quit = new Button("Show Me The World!");
        
        // Slider for time steps
//        Slider slider = new Slider();
//        slider.setMin(1);
//        slider.setMax(100);
//        slider.setValue(40);
//        slider.setShowTickLabels(true);
//        slider.setShowTickMarks(true);
//        slider.setMajorTickUnit(50);
//        slider.setMinorTickCount(5);
//        slider.setBlockIncrement(10);
        
        TextField critter_name = new TextField();
        TextField step_number = new TextField();
        
        show.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	Critter.displayWorld();
            }
        });
        make.setOnAction(new EventHandler<ActionEvent>() {
        	@Override
        	public void handle(ActionEvent event) {
        			String input = critter_name.getText();
        			try {
		            	Critter.makeCritter(input);	
        			} catch (InvalidCritterException e1) {
        				System.out.print("error processing: " + input + "\n");
        			}
        	}
        });
        step.setOnAction(new EventHandler<ActionEvent>() {
        	@Override
        	public void handle(ActionEvent event) {
        		//int val = (int)slider.getValue();
        		int val = Integer.parseInt(step_number.getText());
        		for (int i = 0; i < val; i += 1){
        			Critter.worldTimeStep();
        		}
        		System.out.println(val);
        	}
        });
        
        Critter.displayWorld();
        myLayout.add(show, 0, 5);
        myLayout.add(critter_name, 0, 0);
        myLayout.add(make, 5, 0);
        myLayout.add(step, 0, 7);
        myLayout.add(step_number, 0, 9);
        primaryStage.setScene(new Scene(myLayout, 300, 250));
        primaryStage.show();
    }

	public static void main(String[] args) {
		launch(args);
	}

}
