/*
 * Jonah Harris - Critter 2 of 2
 * 
 * Critter1 acts as a cheetah; that is, it fights any critters for food. It reproduces at
 * energy level 200 as it needs a lot of energy to keep running. This critter runs in a 
 * straight line either left or right, looking for prey. When the stats method is called, 
 * data is output showing the total cheetahs going left, right, and how many laps all the 
 * combined cheetahs have ran based on the world width.
 * INVOKES LOOK DURING DOTIMESTEP() 
 */
package assignment5;

public class Critter2 extends Critter {
	 
	int dir;
	int run_calls;
	
	@Override
	public String toString() { return "2"; }
	
	public Critter2() {
		dir = getRandomInt(2);
		if (dir == 0) { dir = 0; }
		else if (dir == 1){ dir = 4; }
		run_calls = 0;
	}
	
	@Override
	public void doTimeStep() {
		if (look(dir, true) == null) {
			run(dir);
			run_calls += 1;	
		}
		if (getEnergy() > 200){
			Critter2 child = new Critter2();
			reproduce(child, getRandomInt(8));
		}
	}

	@Override
	public boolean fight(String not_used) {	return true; }
	
	@Override
	public CritterShape viewShape() { return CritterShape.CIRCLE;}
	
	@Override
	public javafx.scene.paint.Color viewOutlineColor() { return javafx.scene.paint.Color.BLACK; }
	
	@Override
	public javafx.scene.paint.Color viewColor() { return javafx.scene.paint.Color.GOLD; }
	
	@Override
	public javafx.scene.paint.Color viewFillColor() { return viewColor(); }
	
	public static void runStats(java.util.List<Critter> cheetahs) {
		int total_left = 0;
		int total_right = 0;
		int total_runs = 0;
		int total_laps = 0;
		int runs_per_lap = Params.world_width / 2;
		for (Object obj : cheetahs){
			Critter2 c = (Critter2) obj;
			if (c.dir == 0) { total_right += 1; }
			if (c.dir == 4) { total_left  += 1; }
			total_runs += c.run_calls;
		}
		total_laps = total_runs / runs_per_lap;
		System.out.print("" + cheetahs.size() + " total Critter2    ");
		System.out.print("" + total_right + " Total Right   ");
		System.out.print("" + total_left + " Total Left   ");
		System.out.print("" + total_laps + " Total Laps Ran   ");
		System.out.println("RAWWWWWR!!!   ");
	}
}
