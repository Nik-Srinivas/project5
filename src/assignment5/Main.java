package assignment5;

import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Main extends Application {
	
	private static String myPackage;
	static {
		myPackage = Main.class.getPackage().toString().split(" ")[1];
	}

	private String[] classNames = new String[100];
	
 	private int numberOfFiles = 0;
 	private int numberOfCritters = 0;
	private String[] critterNames;
	
    public String updateStats(String critter){
    	ByteArrayOutputStream output_capture = new ByteArrayOutputStream();
    	try {
    		// Alternate print stream to capture console output
    	    PrintStream ps = new PrintStream(output_capture);
    	    PrintStream old = System.out;
    	    System.setOut(ps);
    	    
    	    // Run stats to get output
			Class<?> className = Class.forName(myPackage + "." + critter);		// Get class of input
			Method runStatsMethod = className.getMethod("runStats", new Class<?>[]{List.class});
			runStatsMethod.invoke(null, Critter.getInstances(critter));	// Input null because static method, second parameter is the list
			
			// Revert to old print stream
    	    System.out.flush();
    	    System.setOut(old);
    	    
		} catch (Exception e){
			System.out.print("error processing in stats");
		}
		return output_capture.toString();
    }
    
    
    
 	public void listFilesForFolder(final File folder) {
 	    for (final File fileEntry : folder.listFiles()) {
 	        if (fileEntry.isDirectory()) {
 	            listFilesForFolder(fileEntry);
 	        } else {
 	            //System.out.println(fileEntry.getName());
 	        	classNames[numberOfFiles] = fileEntry.getName();
 	        	numberOfFiles += 1;
 	        }
 	    }
 	}
 	static GridPane grid = new GridPane();
 	public static int sceneScale = 50;
    public class SecondStage extends Stage {
    	Rectangle2D primaryScreenBounds2 = Screen.getPrimary().getVisualBounds();
    	Label x = new Label("Second stage");
    	VBox y = new VBox();

    	SecondStage(){
    		this.setX(primaryScreenBounds2.getMaxX()/2);
            this.setY(primaryScreenBounds2.getMinY());
            this.setWidth(primaryScreenBounds2.getWidth()/2);
            this.setHeight(primaryScreenBounds2.getHeight());
    	    y.getChildren().add(x);
    	    this.setTitle("World");
    	    Scene scene = new Scene(grid, Params.world_width * sceneScale, Params.world_height * sceneScale);
    	    scene.setFill(javafx.scene.paint.Color.ANTIQUEWHITE);
    	    this.setScene(scene);
    	    this.show();
    	   }    
    	}
    
    static VBox box = new VBox();
    public class ThirdStage extends Stage {
    	Rectangle2D primaryScreenBounds2 = Screen.getPrimary().getVisualBounds();
    	final Accordion critterStatsList = new Accordion();
        final TitledPane[] tps = new TitledPane[numberOfCritters];
    	ThirdStage(){
    		this.setX(primaryScreenBounds2.getMinX());
            this.setY(primaryScreenBounds2.getMaxY()/2);
            this.setWidth(primaryScreenBounds2.getWidth()/2);
            this.setHeight(primaryScreenBounds2.getHeight()/2);
            // Accordion to show statistics
	        for (int i = 0; i < numberOfCritters; i += 1) { 
	        		String critter_stats = updateStats(critterNames[i]);
	        	    tps[i] = new TitledPane();
	        	    tps[i].setText(critterNames[i] + " Statistics (" + critterNames[i].toString() + ")");
	        	    tps[i].setContent(new Label("\n" + critter_stats));
	        }   
	        critterStatsList.getPanes().addAll(tps);
	        critterStatsList.setExpandedPane(tps[0]);
	        
	        box.getChildren().addAll(critterStatsList);
	        Scene myScene = new Scene(box, primaryScreenBounds2.getWidth()/2, primaryScreenBounds2.getHeight()/2);
	        myScene.setUserAgentStylesheet("Ordo.css");
	        this.setScene(myScene);
	        this.show(); 
    	}
    	
    	public void refreshStats(){
    		for (int i = 0; i < numberOfCritters; i += 1) { 
        		String critter_stats = updateStats(critterNames[i]);
        	    tps[i].setText(critterNames[i] + " Statistics");
        	    tps[i].setContent(new Label("\n" + critter_stats));
    		}
    	}
    }
    
 	//Jonah
	 @Override
	 public void start(Stage primaryStage) throws ClassNotFoundException {
		 
		 //Initial creation of most major controls and layouts
		 new SecondStage();
		 BorderPane borders = new BorderPane();
		 HBox top = new HBox();
		 HBox bottom = new HBox();
	 	 VBox left = new VBox();
	 	 VBox right = new VBox();
	 	 GridPane center = new GridPane();
         primaryStage.setTitle("Welcome to Critters!");
         Button show = new Button("Show");
	     Button make = new Button("Make");
	     Button seed = new Button("Set Seed");
	     Button step = new Button("Run Time Step");
	     Button quit = new Button("Quit");
	     Button animate = new Button("Animate");
	     
		 
	     // Obtain Critter subclasses for make
		 String workingDir = System.getProperty("user.dir") + "/src/assignment5";	
		 File myCritters = new File(workingDir);
		 listFilesForFolder(myCritters);
		 	for (int k = 0; k < numberOfFiles - 1; k += 1) {
		 		try {
		 			int cutoff = classNames[k].indexOf(".");
		 			if (cutoff == 0) { continue; }
		 			String correctName = classNames[k].substring(0, cutoff);
		 			if (correctName.equals("Critter")) { continue; }
		 			Class<?> className = Class.forName(myPackage + "." + correctName);
		 			className.asSubclass(Critter.class); 
		 			classNames[numberOfCritters] = correctName;
		 			numberOfCritters += 1;
		 		} catch (Exception e){
		 			// Do nothing; name is not a class or a subclass of critter		
		 		}
		 	}
		 	critterNames = new String[numberOfCritters];
		 	for (int i = 0; i < numberOfCritters; i += 1){
		 		critterNames[i] = classNames[i];
		 	}
	   
	        ObservableList<String> differentCritters = FXCollections.observableArrayList();
	        for (int i = 0; i < numberOfCritters; i += 1) {
	        	differentCritters.add(critterNames[i]);
	        }
	        
	        // ComboBox for critter selection (make)
	        final ComboBox<String> listOfCritters = new ComboBox<>(differentCritters);
	        listOfCritters.setValue(critterNames[0]);
	        
	        // Create Stats window
	        ThirdStage statistics = new ThirdStage();
	        
	        // TextFields for integer input (make and step)
	        TextField number_critters = new TextField();
	        number_critters.setPromptText("Number of Critters...");
	        TextField step_number = new TextField();
	        step_number.setPromptText("Number of steps...");
	        TextField seed_number = new TextField();
	        seed_number.setPromptText("Seed value...");
	        
	        // Button Action Handling
	        show.setOnAction(new EventHandler<ActionEvent>() {
	            @Override
	            public void handle(ActionEvent event) {
	            	Critter.displayWorld();
	            	
	            }  
	        } ) ;
	        make.setOnAction(new EventHandler<ActionEvent>() {
	        	@Override
	        	public void handle(ActionEvent event) {
	        		
	        		String critterType = listOfCritters.getValue();
	        		try {
	        			int val = Integer.parseInt(number_critters.getText());
	        			for (int i = 0; i < val; i += 1) {
			        		Critter.makeCritter(critterType);
	        			}
	        			Critter.displayWorld();
	        		} catch (InvalidCritterException e1) {
	        			System.out.print("error processing: " + critterType + "\n");
	        		}
	        		statistics.refreshStats();
	        		Critter.displayWorld();
	        	}
	        });
	        step.setOnAction(new EventHandler<ActionEvent>() {
	        	@Override
	        	public void handle(ActionEvent event) {
	        		int val = Integer.parseInt(step_number.getText());
	        		for (int i = 0; i < val; i += 1){
	        			Critter.worldTimeStep();
	        			//ADDED THIS SO DISPLAY AUTO UPDATES
	        			Critter.displayWorld();
	        		}
	        		statistics.refreshStats();
	        		Critter.displayWorld();
	        	}
	        });
	        seed.setOnAction(new EventHandler<ActionEvent>() {
	        	@Override
	        	public void handle(ActionEvent event) {
	        		long val = Long.parseLong(step_number.getText());
	        		for (int i = 0; i < val; i += 1){
	        			Critter.setSeed(val);
	        		}
	        		statistics.refreshStats();
	        		Critter.displayWorld();
	        	}
	        });
	        quit.setOnAction(new EventHandler<ActionEvent>() {
	            @Override
	            public void handle(ActionEvent event) {
	            	System.exit(1);
	            }
	        });
	        animate.setOnAction(new EventHandler<ActionEvent>() {
	        	@Override
	        	public void handle(ActionEvent event) {
	        		Animate.animate(3);
	        		
	        	}
	        });
	        
	        // Find size of screen and set window sizes
	        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
	        primaryStage.setX(primaryScreenBounds.getMinX());
	        primaryStage.setY(primaryScreenBounds.getMinY());
	        primaryStage.setWidth(primaryScreenBounds.getWidth()/2);
	        primaryStage.setHeight(primaryScreenBounds.getHeight()/2);
	        
	        
	        // Extra code (may use)
//	        myLayout.setMaxSize(primaryScreenBounds.getHeight(), primaryScreenBounds.getWidth());
//	        myLayout.add(number_critters, 0, 1);
//	        myLayout.add(make, 5, 1);
//	        myLayout.add(step, 5, 7);
//	        myLayout.add(step_number, 0, 7);
//	        myLayout.add(quit, (int)primaryScreenBounds.getHeight(), (int)primaryScreenBounds.getWidth());
//	        myLayout.add(animate,  0, 30);
//	        //myLayout.add(critterStatsList, 0, 20);
//	        myLayout.add(listOfCritters, 0, 2);
	        
	        // Slider for time steps
//	        Slider slider = new Slider();
//	        slider.setMin(1);
//	        slider.setMax(100);
//	        slider.setValue(40);
//	        slider.setShowTickLabels(true);
//	        slider.setShowTickMarks(true);
//	        slider.setMajorTickUnit(50);
//	        slider.setMinorTickCount(5);
//	        slider.setBlockIncrement(10);
	        
	        top.setSpacing(10);
	        bottom.setSpacing(10);
	        left.setSpacing(10);
	        right.setSpacing(10);
	        center.setHgap(10);
	        center.setVgap(10);
	        
	        top.setPadding(new Insets(15, 12, 15, 12));
	        bottom.setPadding(new Insets(15, 12, 15, 12));
	        left.setPadding(new Insets(15, 12, 15, 12));
	        right.setPadding(new Insets(15, 12, 15, 12));
	        
	        int row = 0;
	        top.getChildren().addAll(listOfCritters, number_critters, make);
	        bottom.getChildren().addAll(quit);
	        left.getChildren().addAll(seed);
	        center.setPadding(new Insets(15, 12, 15, 12));
	        center.add(new Label("Make Critters"), 0, row);
	        center.add(listOfCritters, 0, row + 1);
	        center.add(number_critters, 1, row + 1);
	        center.add(make, 2, row + 1);
	        center.add(new Label("Critter Motion Handling"), 0, row + 4);
	        center.add(step_number, 1, row + 5);
	        center.add(step, 2, row + 5);
	        center.add(animate, 2, row + 6);
	        // Add a slider if we can
	        center.add(new Label("Set Seed"), 0, row + 9);
	        center.add(seed_number, 1, row + 10);
	        center.add(seed, 2, row + 10);
	        center.add(quit, 10, row + 12);
	        
	        // Final steps to display
	        borders.setTop(top);
	        borders.setBottom(bottom);
	        borders.setRight(right);
	        borders.setLeft(left);
	        //borders.setCenter(center);
	        Scene myScene = new Scene(center, primaryScreenBounds.getWidth()/2, primaryScreenBounds.getHeight()/2);
	        myScene.setUserAgentStylesheet("Ordo.css");
	        primaryStage.setScene(myScene);
	        primaryStage.show();
	        Critter.displayWorld();
	    }

	public static void main(String[] args) {
		launch(args);
	}

}
