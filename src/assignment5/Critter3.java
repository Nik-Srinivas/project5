package assignment5;

import assignment5.Critter.CritterShape;

	/*
	 * Alex Lostak ajl3287
	 * Meet the Lion. The king of the jungle he stakes his claim and does not let it go.
	 * Walking back and forth across his spot he paces, willing to tear apart anything 
	 * that comes his way. Killed will track the total kills the lion gets.
	 * */
	public class Critter3  extends Critter {
		private int killed = 0;
		private boolean dirToggle = false;
		public String toString() { return "3"; }
		
		public boolean fight(String not_used) { 
			killed++;
			return true; 
		}
		
		@Override
		public CritterShape viewShape() { return CritterShape.CIRCLE;}
		
		@Override
		public javafx.scene.paint.Color viewOutlineColor() { return javafx.scene.paint.Color.ORANGERED; }
		
		@Override
		public javafx.scene.paint.Color viewColor() { return javafx.scene.paint.Color.DARKORANGE; }
		
		@Override
		public javafx.scene.paint.Color viewFillColor() { return viewColor(); }
		
		
		public void doTimeStep() {
			if (dirToggle) {
				walk(0);
				dirToggle = false;
			} else {
				walk(1);
				dirToggle = true;
			}
		}
		
		public static void runStats(java.util.List<Critter> lions) {
			int totalKilled = 0;
			for (Critter l : lions) {
				Critter3 realL = (Critter3) l;
				totalKilled += realL.killed;
			}
			System.out.println(lions.size() + " lions are protecting their land and have eaten " + totalKilled + " critters in doing so");
		}

	}
