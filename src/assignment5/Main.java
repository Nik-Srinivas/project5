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
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Main extends Application {
	
	private static String myPackage;
	static {
		myPackage = Main.class.getPackage().toString().split(" ")[1];
	}
	private String[] classNames = new String[20];
	File myCritters = new File("C:/Users/Alienware/Desktop/422C Programs/project5/src/assignment5"); // File containing list of critter classes
 	private int numberOfFiles = 0;
 	private int numberOfCritters = 0;
	private String[] critterNames;
	
	final String[] imageNames = new String[]{"AMERICA"};
    final Image[] images = new Image[imageNames.length];
    final ImageView[] pics = new ImageView[imageNames.length];
	//final TitledPane[] tps = new TitledPane[imageNames.length];
	
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
	
	
	 @Override
	    public void start(Stage primaryStage) throws ClassNotFoundException {
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
		 	
		 	
		 	final TitledPane[] tps = new TitledPane[numberOfCritters];
		 	
	    	GridPane myLayout = new GridPane();
	        primaryStage.setTitle("Welcome to Critters!");
	        Button show = new Button("Show");
	        Button make = new Button("Make");
	        Button seed = new Button("Set Seed");
	        Button step = new Button("Run Time Step");
	        Button quit = new Button("Quit");
	        
	        ObservableList<String> differentCritters = FXCollections.observableArrayList();
	        for (int i = 0; i < numberOfCritters; i += 1) {
	        	differentCritters.add(critterNames[i]);
	        }
	        final ComboBox<String> listOfCritters = new ComboBox<>(differentCritters);
	        
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
	        
	        // Tilted Pane
	                               
	        final Accordion critterStatsList = new Accordion();        
	        
	        for (int i = 0; i < numberOfCritters; i += 1) { 
	        		String critter_stats = updateStats(critterNames[i]);
	        	    tps[i] = new TitledPane();
	        	    tps[i].setText(critterNames[i] + " Statistics");
	        	    tps[i].setContent(new Label("\n" + critter_stats));
	        }   
	        critterStatsList.getPanes().addAll(tps);
	        critterStatsList.setExpandedPane(tps[0]);
	        
	        TextField number_critters = new TextField();
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
	        		
	        		String critterType = listOfCritters.getValue();
	        		try {
	        			int val = Integer.parseInt(number_critters.getText());
	        			for (int i = 0; i < val; i += 1) {
			        		Critter.makeCritter(critterType);
	        			}
	        		} catch (InvalidCritterException e1) {
	        			System.out.print("error processing: " + critterType + "\n");
	        		}
	        		for (int i = 0; i < numberOfCritters; i += 1) { 
		        		String critter_stats = updateStats(critterNames[i]);
		        	    tps[i].setText(critterNames[i] + " Statistics");
		        	    tps[i].setContent(new Label("\n" + critter_stats));
	        		} 
	        	}
	        });
	        step.setOnAction(new EventHandler<ActionEvent>() {
	        	@Override
	        	public void handle(ActionEvent event) {
	        		int val = Integer.parseInt(step_number.getText());
	        		for (int i = 0; i < val; i += 1){
	        			Critter.worldTimeStep();
	        		}
	        		for (int i = 0; i < numberOfCritters; i += 1) { 
		        		String critter_stats = updateStats(critterNames[i]);
		        	    tps[i].setText(critterNames[i] + " Statistics");
		        	    tps[i].setContent(new Label("\n" + critter_stats));
	        		} 
	        	}
	        });
	        quit.setOnAction(new EventHandler<ActionEvent>() {
	            @Override
	            public void handle(ActionEvent event) {
	            	System.exit(1);
	            }
	        });
	        
	        myLayout.add(show, 0, 0);
	        myLayout.add(number_critters, 0, 1);
	        myLayout.add(make, 5, 1);
	        myLayout.add(step, 5, 7);
	        myLayout.add(step_number, 0, 7);
	        myLayout.add(quit, 0, 25);
	        myLayout.add(critterStatsList, 0, 20);
	        myLayout.add(listOfCritters, 0, 2);
	        
	        primaryStage.setScene(new Scene(myLayout, 300, 250));
	        primaryStage.show();
	    }
    
    

	public static void main(String[] args) {
		
		launch(args);
		
//		if (args.length != 0) {
//            try {
//                inputFile = args[0];
//                kb = new Scanner(new File(inputFile));			
//            } catch (FileNotFoundException e) {
//                System.out.println("USAGE: java Main OR java Main <input file> <test output>");
//                e.printStackTrace();
//            } catch (NullPointerException e) {
//                System.out.println("USAGE: java Main OR java Main <input file>  <test output>");
//            }
//            if (args.length >= 2) {
//                if (args[1].equals("test")) { // if the word "test" is the second argument to java
//                    // Create a stream to hold the output
//                    testOutputString = new ByteArrayOutputStream();
//                    PrintStream ps = new PrintStream(testOutputString);
//                    // Save the old System.out.
//                    old = System.out;
//                    // Tell Java to use the special stream; all console output will be redirected here from now
//                    System.setOut(ps);
//                }
//            }
//        } else { // if no arguments to main
//            kb = new Scanner(System.in); // use keyboard and console
//        }
//
//        /* Do not alter the code above for your submission. */
//        
//        CritterWorld.makeCritterWorld();
//        boolean dontQuit = true;
//        while (dontQuit) {
//        	System.out.print("critters> ");
//        	String userInput = kb.nextLine();
//        	String[] splitUserInput = userInput.split("\\s+");
//        	int splitIndex = 0;
//        	if (splitUserInput[splitIndex].equals("quit")) {
//        		if (splitUserInput.length > 1) {
//        			System.out.print("error processing: " + userInput + "\n");
//
//        		} else {
//        		dontQuit = false;
//        		}
//        	} else if (splitUserInput[splitIndex].equals("show")) {
//        		if (splitUserInput.length > 1) {
//        			System.out.println("error processing: " + userInput);
//        		} else {
//        			Critter.displayWorld();
//        		}
//        	} else if (splitUserInput[splitIndex].equals("step")) {
//        		if (splitUserInput.length > 2) {
//        			System.out.println("error processing: " + userInput);
//        		} else {
//	        		splitIndex++;
//	        		if (splitUserInput.length > 1) {
//	        			try {
//		        			int stepCount = Integer.parseInt(splitUserInput[splitIndex]);
//		        			if (stepCount < 1) { System.out.print("error processing: " + userInput + "\n"); }
//		        			for (int i = 0; i < stepCount; i++) {
//		        				Critter.worldTimeStep();
//		        			}
//	        			} catch (Exception e1) {
//	        				System.out.print("error processing: " + userInput + "\n");
//	        			}
//	        		} else {
//	        			Critter.worldTimeStep();
//	        		}
//        		}
//        	} else if (splitUserInput[splitIndex].equals("seed")) {
//        		if (splitUserInput.length == 2) {
//        			try {
//		        		splitIndex++;
//		        		long seedNumber = Long.valueOf(splitUserInput[splitIndex]);
//		        		if (seedNumber > 0) {
//		        			Critter.setSeed(seedNumber);
//		        		} else {
//	            			System.out.print("error processing: " + userInput + "\n");
//		        		}
//        			} catch (Exception e1) {
//            			System.out.print("error processing: " + userInput + "\n");
//        			}
//        		} else {
//        			System.out.print("error processing: " + userInput + "\n");
//        		}
//        		
//        	} else if (splitUserInput[splitIndex].equals("make")) {
//        		if (splitUserInput.length <= 1) {
//        			System.out.print("error processing: " + userInput + "\n");
//        		} else {
//	        		splitIndex++;
//	        		String className = new String(splitUserInput[splitIndex]);
//	        		splitIndex++;
//	        		if (splitUserInput.length == 3) {
//	        			try {
//		            		String rawCount = new String(splitUserInput[splitIndex]);
//		            		int count = Integer.parseInt(rawCount);
//		            		if (count < 0) {
//		            			System.out.print("error processing: " + userInput + "\n");
//		            		} else {
//		            			for (int i = 0; i < count; i++)
//			            			Critter.makeCritter(className);
//		            		}
//		            		
//	        			} catch (InvalidCritterException e1) {
//	        				System.out.print("error processing: " + userInput + "\n");
//	        			} catch (Exception e2) {
//	        				System.out.print("error processing: " + userInput + "\n");
//	        			}
//	        		} else if (splitUserInput.length > 3) {
//	        			System.out.print("error processing: " + userInput + "\n");
//	        		} else {
//	        			try {
//	        				Critter.makeCritter(className);
//	        			} catch (InvalidCritterException e1) {
//	        				System.out.print("error processing: " + userInput + "\n");
//	        			}
//	        		}
//        		}
//        		
//        	} else if (splitUserInput[splitIndex].equals("stats")) {		
//        		if ((splitUserInput.length > 2) || (splitUserInput.length < 2)) { System.out.print("error processing: " + userInput + "\n"); }
//        		else {
//        			splitIndex += 1;
//        			String statClass = splitUserInput[splitIndex];
//        			try {
//        				Class<?> className = Class.forName(myPackage + "." + statClass);		// Get class of input
//        				Method runStatsMethod = className.getMethod("runStats", new Class<?>[]{List.class});
//        				runStatsMethod.invoke(null, Critter.getInstances(statClass));	// Input null because static method, second parameter is the list
//        			} catch (Exception e){
//        				System.out.print("error processing: " + userInput + "\n");
//        			}
//        		}
//       		} else {
//        		System.out.print("invalid command: " + userInput + "\n");
//        	}
//        		
//        }
//        System.out.flush();
//		
		
	}

}
