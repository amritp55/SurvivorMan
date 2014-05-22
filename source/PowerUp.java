// Power Up Objects
// ================
// this object creates the different powerups
// and controls the location, their movement, what
// powerup they are, and how long they stay available

import java.util.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

// #1 = single bullet
// #2 = double bullet
// #3 = triple bullet
// #4 = extra life
// #5 = bomb available
// #6 = bullet pierce
// #7 = side shooter

public class PowerUp{
	// number of bullets being shot at one time
	// (like triple, single, double, sideshooters
	private String bulletnum;
	// num of lives
	private int lives;
	// other special flags
	private boolean bulletpierce, bombready;
	// ex and ey are power up locations, v's are speed
	private double ex, ey, deg, vx, vy, v;
	// rotation angles and size
	private double rotDeg, s;
	// how long the power up is in play
	private int timer;
	// how big the power up is
	private double maxsize;
	
	// parameters require what powerup got picked, and the location
	public PowerUp (int powerupnumber, double ex, double ey){
		// these are all the originals
		this.ex = ex;
		this.ey = ey;
		bulletnum = "single";
		lives = 0;
		bulletpierce = false;
		v = 2;
		maxsize = 15;
		s = 1;
		bombready = false;
		deg = Math.toDegrees(Math.random()*360);
		
		// now, to change the original set statuses
		// and changes them depending on powerup
		if (powerupnumber == 2){
			bulletnum = "double";
		}
		if (powerupnumber == 3){
			bulletnum = "triple";
		}
		if (powerupnumber == 4){
			lives = 1;
		}
		if (powerupnumber == 5){
			bombready = true;
		}
		if (powerupnumber == 6){
			bulletpierce = true;
		}
		if (powerupnumber == 7){
			bulletnum = "sideshooter";
		}
	}
	
	// the random power up movement on the map
	public void movement (double manx, double many, double userspeedx, double userspeedy, Rectangle bounds){
		if (s<maxsize){
			s+=1;
		}
		if (timer == 100){ // changes how far the move before changing directions
			deg = Math.toDegrees(Math.random()*360);
			timer = 0;
		}
		timer += 1;
		vx = Math.cos(Math.toRadians(deg))*v;
		vy = Math.sin(Math.toRadians(deg))*v;
		//apply velocities
		ex += vx + userspeedx;
		ey += vy + userspeedy;

		// if too close to the left side
		if ((ex<bounds.getX()&&vx<0)){
			vx*=-1;
			deg = Math.atan2(vy, vx);
		}
		// if collides with the right side
		if ((ex>(bounds.getWidth()+bounds.getX())&&vx>0)){
			vx*=-1;
			deg = Math.atan2(vy,vx);
			deg += 180;
		}
		// if collision occurs between top boundary
		if ((ey<bounds.getY()&&vy<0)){
			vy*=-1;
			deg = Math.atan2(vy, vx);
			deg += 90;
		}
		// if collision occurs between bottom boundary
		if ((ey>(bounds.getHeight()+bounds.getY())&&vy>0)){
			vx*=-1;
			deg = (Math.atan2(vy,vx));
			deg -= 90;
		}
	}
	
	// simple get functions and returns neccessary information
	public double getX(){return ex;}
	public double getY(){return ey;}
	
	public boolean getBomb(){
		return bombready;
	}
	public String getBulletNum(){
		return bulletnum;
	}
	public int addLife (){
		return lives;
	}
	public boolean bulletpierce(){
		return bulletpierce;
	}
}