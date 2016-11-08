package assignment5;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javafx.scene.shape.*;

public abstract class Critter {
	/* NEW FOR PROJECT 5 */
	public enum CritterShape {
		CIRCLE,
		SQUARE,
		TRIANGLE,
		DIAMOND,
		STAR
	}
	
	/* the default color is white, which I hope makes critters invisible by default
	 * If you change the background color of your View component, then update the default
	 * color to be the same as you background 
	 * 
	 * critters must override at least one of the following three methods, it is not 
	 * proper for critters to remain invisible in the view
	 * 
	 * If a critter only overrides the outline color, then it will look like a non-filled 
	 * shape, at least, that's the intent. You can edit these default methods however you 
	 * need to, but please preserve that intent as you implement them. 
	 */
	public javafx.scene.paint.Color viewColor() { 
		return javafx.scene.paint.Color.DEEPSKYBLUE; 
	}
	
	public javafx.scene.paint.Color viewOutlineColor() { return viewColor(); }
	public javafx.scene.paint.Color viewFillColor() { return viewColor(); }
	
	public abstract CritterShape viewShape(); 
	
	private static String myPackage;
	private	static List<Critter> population = new java.util.ArrayList<Critter>();
	private static List<Critter> babies = new java.util.ArrayList<Critter>();
	private static Map<String, ArrayList<Critter>> positionLog = new HashMap<String, ArrayList<Critter>>();
	private static boolean encounter_state = false; 		// Global for determining how our walk and run functions work depending on what we're doing!

	// Gets the package name.  This assumes that Critter and its subclasses are all in the same package.
	static {
		myPackage = Critter.class.getPackage().toString().split(" ")[1];
	}
	
	protected String look(int direction, boolean steps) {
		this.energy -= Params.look_energy_cost;
		int x_temp = this.x_coord;
		int y_temp = this.y_coord;
		String key = getPositionKey();
		String return_val = null;
		
		if (steps == false) { // Walk 
			move(1, direction);
			if (positionLog.containsKey(key)){	
				return_val = positionLog.get(key).get(0).toString();
			}
		}
		else if (steps == true) { // Run  
			move(2, direction);
			if (positionLog.containsKey(key)){	
				return_val = positionLog.get(key).get(0).toString();
			}
		}
		this.x_coord = x_temp;
		this.y_coord = y_temp;
		return return_val;
	}
	
	
	
	private static java.util.Random rand = new java.util.Random();
	public static int getRandomInt(int max) {
		return rand.nextInt(max);
	}
	
	public static void setSeed(long new_seed) {
		rand = new java.util.Random(new_seed);
	}
	
	
	/* a one-character long string that visually depicts your critter in the ASCII interface */
	public String toString() { return ""; }
	
	private int energy = 0;
	protected int getEnergy() { return energy; }
	
	private int x_coord;
	private int y_coord;
	private void initializePos() {
		y_coord = getRandomInt(Params.world_height);
		x_coord = getRandomInt(Params.world_width);
		return;
	}
	
	/**
	 * Determines and direction of movement for a critter calling walk or run and moves them by the specified distance (referred to
	 * as speed) in the correct direction. This method minimizes code by sharing it between walk and run.
	 * @param speed
	 * @param direction
	 */
	private void move(int speed, int direction){
			 if (direction == 0) { this.x_coord +=  speed; }
		else if (direction == 1) { this.x_coord +=  speed; this.y_coord += -speed; }
		else if (direction == 2) { 					       this.y_coord += -speed; }
		else if (direction == 3) { this.x_coord += -speed; this.y_coord += -speed; }
		else if (direction == 4) { this.x_coord += -speed; }
		else if (direction == 5) { this.x_coord += -speed; this.y_coord +=  speed; }
		else if (direction == 6) { 					       this.y_coord +=  speed; }
		else if (direction == 7) { this.x_coord +=  speed; this.y_coord +=  speed; }
		//Screen wrap-around catches! (Negative and over sizes)
		if (this.x_coord >= Params.world_width)  { this.x_coord = this.x_coord - (Params.world_width);  }
		if (this.y_coord >= Params.world_height) { this.y_coord = this.y_coord - (Params.world_height); }
		if (this.x_coord < 0) { this.x_coord = this.x_coord + (Params.world_width);  }
		if (this.y_coord < 0) { this.y_coord = this.y_coord + (Params.world_height); }
	}
	
	/**
	 * Moves the critter by 1 space in one of 7 directions on the board. When a critter first moves during a time-step they 
	 * are added to a set in CritterWorld. If the critter attempts to move again the list is checked and if the critter is 
	 * present we only reduce the critter's energy. During encounters a critter may move but we have to make sure the current 
	 * location is free, therefore we use the global boolean encounter_state to determine whether we have to check if the location is
	 * taken or not. If the location is occupied we return the critter to its original location and deduct the energy.
	 * @param direction
	 */
	protected final void walk(int direction) {
		if (!(encounter_state)){							
			if (!(CritterWorld.hasCritterMoved(this))) {
				move(1, direction);
				CritterWorld.addMovedCritter(this);
			}
			this.energy -= Params.walk_energy_cost;
		}
		else if (encounter_state){							
				int xTemp = this.x_coord;
				int yTemp = this.y_coord;
				move(1, direction);
				String key = getPositionKey();
				//If spot is taken put Critter back at original location
				if (positionLog.containsKey(key)){	
					this.x_coord = xTemp;
					this.y_coord = yTemp;
				}
				this.energy -= Params.walk_energy_cost;
		}
	}
	
	/**
	 * Moves the critter by 2 spaces in one of 7 directions on the board. When a critter first moves during a time-step they 
	 * are added to a set in CritterWorld. If the critter attempts to move again the list is checked and if the critter is 
	 * present we only reduce the critter's energy. During encounters a critter may move but we have to make sure the current 
	 * location is free, therefore we use the global boolean encounter_state to determine whether we have to check if the location is
	 * taken or not. If the location is occupied we return the critter to its original location and deduct the energy.
	 * @param direction
	 */
	protected final void run(int direction) {
		if (!(encounter_state)){
			if (!(CritterWorld.hasCritterMoved(this))) {
				move(2, direction);
				CritterWorld.addMovedCritter(this);
			}
			this.energy -= Params.run_energy_cost;
		}
		else if (encounter_state){
			int xTemp = this.x_coord;
			int yTemp = this.y_coord;
			move(2, direction);
			String key = getPositionKey();
			//If spot is taken put Critter back at original location
			if (positionLog.containsKey(key)){	
				this.x_coord = xTemp;
				this.y_coord = yTemp;
			}
			this.energy -= Params.run_energy_cost;
		}
	}
	
	/**
	 * This method determines whether the critter produces a child. If the critter calls this method and has energy
	 * greater than or equal to Params.min_reproduce_energy, we update the child (given by offspring) by splitting 
	 * the energy (round up child, round down parent), place the offspring adjacent to the parent given direction, 
	 * and add the child to the newbornCritter list in CritterWorld to be added at the end of the time-step.
	 * @param offspring
	 * @param direction
	 */
	protected final void reproduce(Critter offspring, int direction) {
		if (this.energy < Params.min_reproduce_energy){ return; }
		//Energy
		offspring.energy = this.energy / 2;					// Half of parent's energy rounded down to child
		this.energy = (this.energy / 2) + (this.energy % 2);	// Half of parent's energy rounded up to parent
		//Placement
		int x_temp = this.x_coord;
		int y_temp = this.y_coord;
		move(1, direction);						//Find birth location
		offspring.x_coord = this.x_coord;		//Set offspring to adjacent location
		offspring.y_coord = this.y_coord;	
		this.x_coord = x_temp;					//Set parent back to normal
		this.y_coord = y_temp;
		//Store to be added later
		CritterWorld.addNewborn(offspring);
	}

	public abstract void doTimeStep();
	public abstract boolean fight(String oponent);
	
	
	/**
	 * Adds Algae critters to the world every step. The amount of Algae added is
	 * specified by Params.refresh_algae_count.
	 */
	private static void createNewAlgae(){
		try{
			for (int i = 0; i < Params.refresh_algae_count; i += 1){
				makeCritter("Algae");
			}
		} catch (InvalidCritterException e) {
			System.out.println(e.toString());
			return;
		}
	}
	
	/**
	 * Calculates the key for each critter based on it's location for adding and accessing it from postionLog for 
	 * resolving encounters.
	 * @return
	 */
	private String getPositionKey() {
		int x = x_coord;
		int y = y_coord;
		return ("x" + Integer.toString(x) + "y" + Integer.toString(y));
	}
	
	/**
	 * Sets the key for the critter passed (Critter c) and adds it to the positionLog for searching for encounters and 
	 * resolving them. Returns the updated list.
	 * @param c
	 * @param positionLog
	 * @return
	 */
	private static ArrayList<Critter> getPositionCritterArray (Critter c, Map<String, ArrayList<Critter>> positionLog) {
		String positionKey = c.getPositionKey();
		ArrayList<Critter> positionList = positionLog.get(positionKey);
		return positionList;
	}
	
	/**
	 * Takes the passed set and removes all critters who are dead (energy <= 0). Also subtracts rest energy.
	 * @param critters
	 */
	private static void removeWorldDead(Set<Critter> critters) {
		Iterator<Critter> worldIt = critters.iterator();
		while (worldIt.hasNext()) {								// Iterate through world
			Critter c = (Critter) worldIt.next();
			if(c.energy <= 0) {									// Check if dead
				worldIt.remove();								// Remove from worl if dead
			}
		}
	}
	
	/**
	 * Uses the passed ArrayList size to determine if there is an encounter to resolve. If the size is greater than 1
	 * (meaning there is more than one critter with the same position) then we return true to handleEncounters and proceed
	 * to resolve the encounter. Otherwise false and continue through positions.
	 * @param positionCritterArray
	 * @return
	 */
	private static boolean hasEncounter(ArrayList<Critter> positionCritterArray) {
		if (positionCritterArray.size() > 1) {
			return true;
		}
		return false;
	}
	
	/**
	 * Give half the energy of the loser to the victor of each encounter.
	 * @param loser
	 */
	private void winEncounter(Critter loser) {
		energy += loser.energy / 2;
		return;
	}
	
	/**
	 * We check to see if a critter attempted to run from a fight by comparing a new position key and the 
	 * old key.
	 * @param positionKey
	 * @return
	 */
	private boolean ranFromFight(String positionKey) {
		String newKey = getPositionKey();
		if (newKey.equals(positionKey))
			return false;
		return true;
	}
	
	/**
	 * Resolve encounters.
	 * @param critters
	 */
	private static void resolveEncounter(ArrayList<Critter> critters) {
		String currentPositionKey = critters.get(0).getPositionKey();
			while (critters.size() > 1) {
				Critter a = critters.get(0);
				Critter b = critters.get(1);
				boolean aFight = a.fight(b.toString());
				boolean bFight = b.fight(a.toString());
				boolean aRan = a.ranFromFight(currentPositionKey);
				boolean bRan = b.ranFromFight(currentPositionKey);
				if (!(aRan || bRan)) {
					int aFightRoll = 0;
					int bFightRoll = 0;
					boolean aIsDead = a.energy <= 0;
					boolean bIsDead = b.energy <= 0;
					if (aIsDead == true) {
						a.energy = 0;
						critters.remove(0);
					} else if (bIsDead== true) {
						b.energy = 0;
						critters.remove(1);
					} else {
						if (aFight)
							aFightRoll = getRandomInt(a.energy);
						if (bFight)
							bFightRoll = getRandomInt(b.energy);
						if ((aFightRoll >= bFightRoll)) {
							a.winEncounter(b);
							b.energy = 0;
							critters.remove(1); 
						} else if (aFightRoll < bFightRoll) {
							b.winEncounter(a);
							a.energy = 0;
							critters.remove(0);
						}
					}
				} else if (aRan && bRan) {
					critters.remove(1);
					critters.remove(0);
				}else if (aRan) {
					critters.remove(0);
				} else if (bRan) {
					critters.remove(1);
				}
			}
			return;
		}
	
	/**
	 * Takes care of all instances where multiple Critters occupy the same position
	 * @param critters set of the critters in the CritterWorld
	 */
		public static void handleEncounters (Set<Critter> critters) {
			Iterator<Critter> worldIt = critters.iterator();
			while (worldIt.hasNext()) {
				Critter c = (Critter) worldIt.next();
				ArrayList<Critter> positionCritterArray = getPositionCritterArray(c, positionLog);
				//need to check not pointing to empty array list
				if (positionCritterArray != null) {
					if (hasEncounter(positionCritterArray) == true) {
						resolveEncounter(positionCritterArray);
					}
				}
			}
		}	
		
		/**
		 * 
		 * @return
		 */
		private static Map getPositionLog() {
			return positionLog;
		}
	
	/**
	 * Run a step. Each step involves resetting and updating any variables we use in other methods 
	 * (e.g., postitionLog for handling encounters), invoking doTimeStep() for each critter in our
	 * CritterWorld critter collection, remove dead critters, handle encounters, remove more dead
	 * from encounters, add newborns to the world, clear any data structures we used for storing
	 * data in other methods, and add the new algae.
	 */
	public static void worldTimeStep() {
		Set<Critter> critters = CritterWorld.getCritterList();
		Iterator<Critter> worldIt = critters.iterator();
		positionLog.clear();
		Map followPosLog = positionLog;
		while(worldIt.hasNext()) {
			Critter c = (Critter) worldIt.next();
			c.doTimeStep();
			c.energy -= Params.rest_energy_cost;
			if (c.energy > 0) {
				String positionKey = c.getPositionKey();
				ArrayList<Critter> positionList = positionLog.get(positionKey);
				if (positionList != null) {
					//add critter to list of critters
					positionList.add(c);
				} else {
					//add critter with hashcode of position
					positionList = new ArrayList<Critter>();
					positionList.add(c);
					positionLog.put(positionKey, positionList);
				}
			}
		}
	encounter_state = true;
	handleEncounters(critters);
	encounter_state = false;
	removeWorldDead(critters);
	CritterWorld.addNewbornsToPop();		// Add newborns at the end of the time step
	CritterWorld.clearMovedCritters();		// Clear list of critters that have moved
	createNewAlgae();						// Add new algae to the world
}
	
	public static void displayWorld() {
		Main.grid.getChildren().clear();
		Set<Critter> critterWorld = CritterWorld.getCritterList();
		Critter[][] display = new Critter[Params.world_width][Params.world_height];
//		for (Critter c : critterWorld) {
//			display[c.x_coord - 1][c.y_coord - 1] = c;
//		}
		Iterator worldIt = critterWorld.iterator();
		while (worldIt.hasNext()) {
			Critter c = (Critter) worldIt.next();
			display[c.x_coord] [c.y_coord] = c;
		}
		int displayHeight = Params.world_height;
		int displayWidth = Params.world_width;
		for (int y = 0; y < displayHeight; y++) {
			for (int x = 0; x < displayWidth; x++) {
				Critter toAdd = display[x][y];
				Shape s = null;
				Polygon p = new Polygon();
				if (toAdd == null) {
					s = Painter.getIcon(0);
				} else {
					switch(toAdd.viewShape()) {
						
						case CIRCLE:
							s = new Circle(Main.sceneScale/2);
							s.setFill(toAdd.viewColor());
							s.setStroke(toAdd.viewOutlineColor());
							break;
						case SQUARE:
							s = new Rectangle(Main.sceneScale, Main.sceneScale);
							s.setFill(toAdd.viewColor());
							s.setStroke(toAdd.viewOutlineColor());
							break;
						case TRIANGLE:
							Double doubleSceneScale = Double.valueOf((double) Main.sceneScale);
							p.getPoints().addAll(new Double[] {
								0.0, 0.0,
								doubleSceneScale, 0.0,
								0.0, doubleSceneScale,
							});
							p.setFill(toAdd.viewColor());
							p.setStroke(toAdd.viewOutlineColor());
							break;
						case DIAMOND:
							doubleSceneScale = Double.valueOf((double) Main.sceneScale);
							p.getPoints().addAll(new Double[] {
									doubleSceneScale/2.0, 0.0,
									doubleSceneScale, doubleSceneScale/2.0,
									doubleSceneScale/2.0, doubleSceneScale,
									0.0, doubleSceneScale/2.0,
									
							});
							p.setFill(toAdd.viewColor());
							p.setStroke(toAdd.viewOutlineColor());
							break;
						case STAR:
							doubleSceneScale = Double.valueOf((double) Main.sceneScale);
							p.getPoints().addAll(new Double[] {
									doubleSceneScale/2.0, 0.0, //point 1
									doubleSceneScale * .6, doubleSceneScale * .4,//point 2
									doubleSceneScale, doubleSceneScale/2.0, //point 3
									doubleSceneScale * .6, doubleSceneScale * .6,//point 4
									doubleSceneScale, doubleSceneScale, //point 5
									doubleSceneScale/2.0, doubleSceneScale * .8,//point 6
									0.0, doubleSceneScale,//point 7
									doubleSceneScale * .4, doubleSceneScale * .6,//point 8
									0.0, doubleSceneScale/2.0,//point 9
									doubleSceneScale * .4, doubleSceneScale * .4,//point 10
									
									
							});
							break;
					}
				}
				if (s == null) {
					Main.grid.add(p, x * Main.sceneScale, y * Main.sceneScale);
				} else {
					Main.grid.add(s, x * Main.sceneScale, y * Main.sceneScale);
				}
				
			}
		}
	}
	
	
	/**
	 * create and initialize a Critter subclass.
	 * critter_class_name must be the unqualified name of a concrete subclass of Critter, if not,
	 * an InvalidCritterException must be thrown.
	 * (Java weirdness: Exception throwing does not work properly if the parameter has lower-case instead of
	 * upper. For example, if craig is supplied instead of Craig, an error is thrown instead of
	 * an Exception.) //don't need to worry about this
	 * @param critter_class_name
	 * @throws InvalidCritterException
	 */
	
	public static void makeCritter(String critter_class_name) throws InvalidCritterException {
		try{ 
			Class<?> className = Class.forName(myPackage + "." + critter_class_name);
			Critter newCritter = (Critter) className.newInstance();
			newCritter.energy = Params.start_energy;
			newCritter.initializePos();
			CritterWorld.addCritter(newCritter);
		} catch (Exception e){		
			throw new InvalidCritterException(critter_class_name);
		}
		return;
	}
	
	/**
	 * Gets a list of critters of a specific type.
	 * @param critter_class_name What kind of Critter is to be listed.  Unqualified class name.
	 * @return List of Critters.
	 * @throws InvalidCritterException
	 */
	public static List<Critter> getInstances(String critter_class_name) throws InvalidCritterException {
		try{
			List<Critter> result = new java.util.ArrayList<Critter>();
			Set<Critter> crits = CritterWorld.getCritterList();
			Class<?> className = Class.forName(myPackage + "." + critter_class_name);
			for (Critter c : crits){
				if (className == c.getClass()) { result.add(c); }
			}
			return result;
			
		} catch (Exception e){
			throw new InvalidCritterException(critter_class_name);
		}
	}
	
	/**
	 * Prints out how many Critters of each type there are on the board.
	 * @param critters List of Critters.
	 */
	public static void runStats(List<Critter> critters) {
		System.out.print("" + critters.size() + " critters as follows -- ");
		java.util.Map<String, Integer> critter_count = new java.util.HashMap<String, Integer>();
		for (Critter crit : critters) {
			String crit_string = crit.toString();
			Integer old_count = critter_count.get(crit_string);
			if (old_count == null) {
				critter_count.put(crit_string,  1);
			} else {
				critter_count.put(crit_string, old_count.intValue() + 1);
			}
		}
		String prefix = "";
		for (String s : critter_count.keySet()) {
			System.out.print(prefix + s + ":" + critter_count.get(s));
			prefix = ", ";
		}
		System.out.println();		
	}
	
	/* the TestCritter class allows some critters to "cheat". If you want to 
	 * create tests of your Critter model, you can create subclasses of this class
	 * and then use the setter functions contained here. 
	 * 
	 * NOTE: you must make sure thath the setter functions work with your implementation
	 * of Critter. That means, if you're recording the positions of your critters
	 * using some sort of external grid or some other data structure in addition
	 * to the x_coord and y_coord functions, then you MUST update these setter functions
	 * so that they correctup update your grid/data structure.
	 */
	static abstract class TestCritter extends Critter {
		protected void setEnergy(int new_energy_value) {
			super.energy = new_energy_value;
		}
		
		protected void setX_coord(int new_x_coord) {
			super.x_coord = new_x_coord;
		}
		
		protected void setY_coord(int new_y_coord) {
			super.y_coord = new_y_coord;
		}
		
		protected int getX_coord() {
			return super.x_coord;
		}
		
		protected int getY_coord() {
			return super.y_coord;
		}
		

		/*
		 * This method getPopulation has to be modified by you if you are not using the population
		 * ArrayList that has been provided in the starter code.  In any case, it has to be
		 * implemented for grading tests to work.
		 */
		protected static List<Critter> getPopulation() {
			return population;
		}
		/*
		 * This method getBabies has to be modified by you if you are not using the babies
		 * ArrayList that has been provided in the starter code.  In any case, it has to be
		 * implemented for grading tests to work.  Babies should be added to the general population 
		 * at either the beginning OR the end of every timestep.
		 */
		protected static List<Critter> getBabies() {
			return babies;
		}
	}
	
	/**
	 * Clear the world of all critters, dead and alive
	 */
	public static void clearWorld() {
		CritterWorld.clearCritterWorld();
	}
	
	
}
