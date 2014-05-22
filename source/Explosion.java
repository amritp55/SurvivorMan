// The Explosion Object
// ====================
// this class keeps track of the explosion caused by the bomb
// - it can be accomplished by establising a radius
//   and then checking if any enemies are within that radius
//   if they are, they are deleted within the main game.

import java.util.*;

class Explosion{
	// radius of the circle
	private double radius;
	// the coordinates of the source of the bomb
	private double x, y;
	
	// sets up under the main character
	public Explosion(){
		radius = 1;
		x = 500;
		y = 350;
	}

	//grow bigger
	public void grow(double vx, double vy){
		x+=vx;
		y+=vy;
		radius+=20;
	}
	
	// usual distance formula
	public double distance(double x1, double y1, double x2, double y2){
		return Math.pow((Math.pow(x1-x2, 2.0))+(Math.pow(y1-y2, 2.0)), 0.5);
	}

	// check collisions with
	// each type of enemies (snake, enemies, and x y's )
	public boolean contains(Enemy e){
		return distance(x, y, e.getX(), e.getY())<=radius;
	}
	public boolean contains(double ox, double oy){
		return distance(x, y, ox, oy)<=radius;
	}
	public boolean contains(Snake snk){
		return distance(x, y, snk.getX(), snk.getY())<=radius;
	}

	//get size
	public double size(){
		return radius;
	}
}