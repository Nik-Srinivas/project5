package assignment5;

import assignment5.Critter.CritterShape;

/*
 * Jonah Harris - Critter 1 of 2
 * 
 * Critter1 acts as a bunny; that is, it only fights algae critters for food and flees from other critters
 * in fights. It reproduces at the minimum provided energy rate! (WOW) and continues in a single 
 * direction to find algae. Stats displays the total number of Critter1's, the total offspring born, and 
 * the total amount of algae eaten.
 */

public class Critter1 extends Critter {
	
	int dir;
	int baby_bunnies;
	int algae_eaten;
	
	@Override
	public String toString() { return "1"; }
	
	public Critter1() {
		dir = Critter.getRandomInt(8);
		baby_bunnies = 0;
		algae_eaten = 0;
	}
	
	public void doTimeStep() {
		run(dir);
		if (getEnergy() >= Params.min_reproduce_energy){
			Critter2 child = new Critter2();
			reproduce(child, getRandomInt(8));
			baby_bunnies += 1;
		}
	}



	@Override
	public boolean fight(String opponent) { 
		if (opponent.equals("@")) { algae_eaten += 1; return true; }
		else { return false; }
	}
	
	@Override
	public CritterShape viewShape() { return CritterShape.TRIANGLE;}
	
	@Override
	public javafx.scene.paint.Color viewOutlineColor() { return javafx.scene.paint.Color.HOTPINK; }
	
	@Override
	public javafx.scene.paint.Color viewColor() { return javafx.scene.paint.Color.GAINSBORO; }
	
	@Override
	public javafx.scene.paint.Color viewFillColor() { return viewColor(); }
	
	public static void runStats(java.util.List<Critter> bunnies) {
		int total_baby_bunnies = 0;
		int total_algae_eaten = 0;
		for (Object obj : bunnies){
			Critter1 b = (Critter1) obj;
			total_baby_bunnies = b.baby_bunnies;	
			total_algae_eaten = b.algae_eaten;
		}
		System.out.print("" + bunnies.size() + " total Critter1    ");
		System.out.print("" + total_baby_bunnies + " Total Baby Critter1's   ");
		System.out.print("" + total_algae_eaten + " Total Algae Eaten   ");
		System.out.println("OM NOM NOM!!!  ^^^ ");
	}

}
