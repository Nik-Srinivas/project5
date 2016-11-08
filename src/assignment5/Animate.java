package assignment5;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class Animate {
	
	int animationTracker = 0;
	Timer animationTimer = new Timer();
	TimerTask animationTask = new TimerTask()  {
		public void run() {
			Critter.worldTimeStep();
			Critter.displayWorld();
			animationTracker++;
			System.out.println("Seconds passed: " + animationTracker);
		}
	};
	public void start(int animationCount) {
		try {
		for (int i = 0; i < animationCount; i++) {
			TimeUnit.MILLISECONDS.sleep(500);
			
			animationTask.run();
		}
		} catch (Exception e1) {
			System.out.println("Exception Thrown");
		}
		//animationTimer.schedule(animationTask, 1000, 1000);
	}
	
	public static void animate(int animationCount) {
		Animate animation = new Animate();
		animation.start(animationCount);
	}
	
}
