package assignment5;
import java.util.*;

public class CritterWorld {			
	
	private static Set<Critter> myCritters = new HashSet<Critter>();
	private static Set<Critter> newbornCritters = new HashSet<Critter>();
	private static Set<Critter> movedCritters = new HashSet<Critter>();
	private static int population;
	private static int newborns;

	/**
	 * Initiate CritterWorld and all fields
	 */
	public static void makeCritterWorld() {
		population = myCritters.size();
		newborns = 0;
		
	}
	
	private static void stageOneInit() {
		try {
			for (int i = 0; i < 100; i++) {
				Critter.makeCritter("Algae");
			}
		} catch (InvalidCritterException e) {
			System.out.println(e.toString());
			return;
		}
		try {
			for (int i = 0; i < 25; i++) {
				Critter.makeCritter("Craig");
			}
		} catch (InvalidCritterException e) {
			System.out.println(e.toString());
			return;
		}
	}
	
	/**
	 * Adds Critter to myCritters
	 * @param c the critter to be added to the world
	 */
	public static void addCritter(Critter c){
		myCritters.add(c);
	}
	
	/**
	 * Removes Critter to myCritters
	 * @param Critter c the critter to be removed from the world
	 */
	public static void removeCritter(Critter c){
		myCritters.remove(c);
	}
	
	/**
	 * Get set of all Critters currently in the world
	 * @return Set<Critters> the set of Critters in the world
	 */
	public static Set<Critter> getCritterList(){
		return myCritters;
	}
	

	/**
	 * Get set of all newborn critters
	 * @return Set<Critter> newbornCritters
	 */
	public static Set<Critter> getBabyList(){
		return newbornCritters;
	}
	
	/**
	 * Adds Critter to set of newborn Critters to be added to world at end of time step
	 * @param c the critter to be added to the world
	 */
	public static void addNewborn(Critter c){
		newbornCritters.add(c);					
		newborns += 1;
	}
	
	/**
	 * Add newborn critters to myCritters and clear newbornCritters
	 */
	public static void addNewbornsToPop(){
		myCritters.addAll(newbornCritters);			
		newbornCritters.clear();					
		newborns = 0;								
	}
	
	/**
	 * Adds Critter to list of movedCritters
	 * @param c the critter to be added to the list of moved critters
	 */
	public static void addMovedCritter(Critter c){
		movedCritters.add(c);
	}
	
	/**
	 * Clears set of movedCritters
	 */
	public static void clearMovedCritters(){
		movedCritters.clear();
	}
	
	/**
	 * Determines if Critter has moved this time step
	 * @param c the critter to check if moved
	 */
	public static boolean hasCritterMoved(Critter c){
		return movedCritters.contains(c);
	}
	
	public static void clearCritterWorld(){
		myCritters.clear();
	}
}	
