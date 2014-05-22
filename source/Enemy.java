// ENEMY class
// ===========
// This class is the enemy object, which
// determines the co-ordinates of each enemy,
// the direction they are going,
// and the outline of the shape.

import java.util.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

// Enemy overload (the circles are different because they
// have different sizes when they get killed)

public class Enemy{
	// variables
	private double ex, ey, deg, vx, vy, v;
	// ex and ey are the coordinates of the enemy
	// deg is the direction it is going, and vx vy
	// are the vector velocities of v
	private double rotDeg, s;
	// rotDeg rotates deg to the desired rotDeg, s is the size
	private String type;
	// type of shape
	private Polygon outline;
	// how to draw the shape
	private int timer, grower;
	// timer keeps track of time, grower increases their size
	private double maxsize;
	// doesn't allow shape to grow any largers
	private int circLevel;
	//level of circles (size)
	private Rectangle circlecontainer;
	// checks to see if enemy collides with anything

	public Enemy (double ex, double ey, String type, int level){
		circLevel = level;
		this.ex = ex;
		this.ey = ey;
		this.type = type;

		// enemy velocity
		if (type.equals("triangle")){
			v = 4;
		}
		else if (type.equals("diamond")){
			v = 3;
		}
		else{
			v = 2;
		}

		outline = new Polygon(); //shape of enemy
		rotDeg = Math.random()*360; //rotational degree
		s= 1; //size of enemy
		
		if (type.equals ("circle")){
			if (circLevel==1){
				maxsize = 10;
			}
			else if (circLevel==2){
				maxsize = 20;
			}
			else if (circLevel==3){
				maxsize = 30;
			}
		}
		else{
			maxsize = 15;
		}
		// if it is a triangle or circle, they move randomly
		if(type.equals("triangle") || type.equals("circle")){
			deg=(int)(Math.random()*360-180);

		}
	}

	public Enemy (double ex, double ey, String type){
		this.ex = ex;
		this.ey = ey;
		this.type = type;

		// enemy velocity
		if (type.equals("triangle")){
			v = 4;
		}
		else if (type.equals("diamond")){
			v = 3;
		}
		else{
			v = 2;
		}

		outline = new Polygon(); //shape of enemy
		rotDeg = Math.random()*360; //rotational degree
		s= 1; //size of enemy
		
		if (type.equals ("circle")){
			maxsize = 20;
		}
		else{
			maxsize = 15;
		}
		if(type.equals("triangle") || type.equals("circle")){
			deg=(int)(Math.random()*360-180);

		}
		

	}
	
	// ==============================================================================
	
	

	//Simple distance formula
	public double distance(double x1, double y1, double x2, double y2){
		return Math.pow((Math.pow(x1-x2, 2.0))+(Math.pow(y1-y2, 2.0)), 0.5);
	}

	//enemy movements
	public void movement (double manx, double many, double userspeedx, double userspeedy, Rectangle bounds){
		// grows the enemy
		if (s<maxsize){
			s+=1;
		}
		if (s>maxsize){
			s-=1;
		}
		if (type.equals("square")){
			deg = Math.toDegrees(Math.atan2((many-ey), (manx-ex))); //find the degree it needs to point
			//find x and y velocities according to degree
			vx = Math.cos(Math.toRadians(deg))*v;
			vy = Math.sin(Math.toRadians(deg))*v;
			//apply velocities
			ex += vx + userspeedx;
			ey += vy + userspeedy;

			//changing rotational degree (allows enemy to spin)
			rotDeg+=3;
			if (rotDeg>=360){
				rotDeg = 0;
			}
		}
		if (type.equals("hexagon")){
			// doesn't move and just grows bigger as time goes on
			maxsize+=grower;
			if (maxsize>30 || maxsize<10){
				grower*=-1;
			}
			// changes the x and y of enemy
			ex += userspeedx;
			ey += userspeedy;
			
			// simply rotates the shape
			rotDeg+=12;
			if (rotDeg>=360){
				rotDeg = 0;
			}
		}
		if (type.equals("triangle")||type.equals("circle")){
			if (timer == 100){ // changes how far the move before changing directions
				deg = Math.toDegrees(Math.random()*360);
				timer = 0;
			}
			timer += 1;
			//vectors of the speed
			vx = Math.cos(Math.toRadians(deg))*v;
			vy = Math.sin(Math.toRadians(deg))*v;
			//apply velocities
			ex += vx + userspeedx;
			ey += vy + userspeedy;

			rotDeg+=8;
			if (rotDeg>=360){
				rotDeg = 0;
			}

			//if enemy hits the left wall
			if ((ex<bounds.getX()&&vx<0)){
				vx*=-1;
				deg = Math.atan2(vy, vx);
			}
			// if enemy hits the right wall
			if ((ex>(bounds.getWidth()+bounds.getX())&&vx>0)){
				vx*=-1;
				deg = Math.atan2(vy,vx);
				deg += 180;
			}
			// if enemy hits the top wall
			if ((ey<bounds.getY()&&vy<0)){
				vy*=-1;
				deg = Math.atan2(vy, vx);
				deg += 90;
			}
			// if enemy hits the bottom wall
			if ((ey>(bounds.getHeight()+bounds.getY())&&vy>0)){
				vx*=-1;
				deg = (Math.atan2(vy,vx));
				deg -= 90;
			}
		}
		if (type.equals("diamond")){
			deg = Math.toDegrees(Math.atan2((many-ey), (manx-ex))); //find the degree it needs to point
			//find x and y velocities according to degree
			vx = Math.cos(Math.toRadians(deg))*v;
			vy = Math.sin(Math.toRadians(deg))*v;
			//apply velocities
			ex += vx + userspeedx;
			ey += vy + userspeedy;

			//changing rotational degree (allows enemy to spin)
			rotDeg+=1;
			if (rotDeg>=360){
				rotDeg = 0;
			}
		}

		drawShape();
	}
	// dodge function for the green square enemies
	
	public void dodge(Bullet bullet){
		Polygon path = new Polygon();
		// draws a rect in the math of the bullet
		// and basically bounces the enemy away if it's
		// inside the rectangle
		double d = 25;
		// thickness of the polygon
		double bullAngle = bullet.getDeg(); // bullet angle
		double realAngle = Math.atan2(ey-bullet.getY(), ex-bullet.getX());
		// real angle is between bullet and enemy
		if (bullAngle<0){
			bullAngle+=Math.PI*2;
		}
		if (realAngle<0){
			realAngle+=Math.PI*2;
		}
		
		// rectangle points
		path.addPoint((int)(bullet.getX()+Math.cos(bullAngle+Math.PI/2)*d), (int)(bullet.getY()+Math.sin(bullAngle+Math.PI/2)*d));
		path.addPoint((int)(bullet.getX()+Math.cos(bullAngle-Math.PI/2)*d), (int)(bullet.getY()+Math.sin(bullAngle-Math.PI/2)*d));
		path.addPoint((int)((bullet.getX()+Math.cos(bullAngle-Math.PI/2)*d)+Math.cos(bullAngle)*150), (int)((bullet.getY()+Math.sin(bullAngle-Math.PI/2)*d)+Math.sin(bullAngle)*150));
		path.addPoint((int)((bullet.getX()+Math.cos(bullAngle+Math.PI/2)*d)+Math.cos(bullAngle)*150), (int)((bullet.getY()+Math.sin(bullAngle+Math.PI/2)*d)+Math.sin(bullAngle)*150));
		
		if (path.contains(ex, ey)){
			if (bullAngle<realAngle){
				ex+=Math.cos(bullAngle+Math.PI/2)*3;
				ey+=Math.sin(bullAngle+Math.PI/2)*3;
			}
			else{
				ex+=Math.cos(bullAngle-Math.PI/2)*3;
				ey+=Math.sin(bullAngle-Math.PI/2)*3;
			}

		}
	}

	//check collisions with each other
	public void collide(ArrayList<Enemy> enemies){
		for (int e=enemies.size()-1;e>=0;e-=1){
			double dist = distance(ex, ey, enemies.get(e).getX(), enemies.get(e).getY());
			if (dist<=maxsize*2 && dist!=0){
				//enemies.get(e).setX(enemies.get(e).getX()+(enemies.get(e).getX()-ex)/(dist/3));
                //enemies.get(e).setY(enemies.get(e).getY()+(enemies.get(e).getY()-ey)/(dist/3));
                ex+=(ex-enemies.get(e).getX())/(dist/3);
                ey+=(ey-enemies.get(e).getY())/(dist/3);
			}
		}
	}

	//Draw outline of polygon
	public void drawShape(){
		if (type.equals("square")){
			outline.reset();
			outline.addPoint((int)(ex+Math.cos(Math.toRadians(rotDeg))*s), (int)(ey+Math.sin(Math.toRadians(rotDeg))*s));
			outline.addPoint((int)(ex+Math.cos(Math.toRadians(rotDeg+90))*s), (int)(ey+Math.sin(Math.toRadians(rotDeg+90))*s));
			outline.addPoint((int)(ex+Math.cos(Math.toRadians(rotDeg+180))*s), (int)(ey+Math.sin(Math.toRadians(rotDeg+180))*s));
			outline.addPoint((int)(ex+Math.cos(Math.toRadians(rotDeg+270))*s), (int)(ey+Math.sin(Math.toRadians(rotDeg+270))*s));
		}
		else if (type.equals ("triangle")){
			outline.reset();
			outline.addPoint ((int)(ex+Math.cos(Math.toRadians(rotDeg))*s),(int)(ey+Math.sin(Math.toRadians(rotDeg))*s));
			outline.addPoint ((int)(ex+Math.cos(Math.toRadians(rotDeg+120))*s),(int)(ey+Math.sin(Math.toRadians(rotDeg+120))*s));
			outline.addPoint ((int)(ex+Math.cos(Math.toRadians(rotDeg+240))*s),(int)(ey+Math.sin(Math.toRadians(rotDeg+240))*s));
		}
		else if (type.equals ("hexagon")){
			outline.reset();
			outline.addPoint ((int)(ex+Math.cos(Math.toRadians(rotDeg+60))*s),(int)(ey+Math.sin(Math.toRadians(rotDeg+60))*s));
			outline.addPoint ((int)(ex+Math.cos(Math.toRadians(rotDeg+120))*s),(int)(ey+Math.sin(Math.toRadians(rotDeg+120))*s));
			outline.addPoint ((int)(ex+Math.cos(Math.toRadians(rotDeg+180))*s),(int)(ey+Math.sin(Math.toRadians(rotDeg+180))*s));
			outline.addPoint ((int)(ex+Math.cos(Math.toRadians(rotDeg+240))*s),(int)(ey+Math.sin(Math.toRadians(rotDeg+240))*s));
			outline.addPoint ((int)(ex+Math.cos(Math.toRadians(rotDeg+300))*s),(int)(ey+Math.sin(Math.toRadians(rotDeg+300))*s));
			outline.addPoint ((int)(ex+Math.cos(Math.toRadians(rotDeg+360))*s),(int)(ey+Math.sin(Math.toRadians(rotDeg+3))*s));
		}
		else if (type.equals ("diamond")){
			outline.reset();
			outline.addPoint((int)(ex-5+Math.cos(Math.toRadians(0))*s), (int)(ey+Math.sin(Math.toRadians(0))*s));
			outline.addPoint((int)(ex+Math.cos(Math.toRadians(90))*s), (int)(ey+8+Math.sin(Math.toRadians(90))*s));
			outline.addPoint((int)(ex+5+Math.cos(Math.toRadians(180))*s), (int)(ey+Math.sin(Math.toRadians(180))*s));
			outline.addPoint((int)(ex+Math.cos(Math.toRadians(270))*s), (int)(ey-8+Math.sin(Math.toRadians(270))*s));
		}
		else if (type.equals("circle")){
			outline.reset();
			outline.addPoint((int)(ex+Math.cos(Math.toRadians(rotDeg+45))*s), (int)(ey+Math.sin(Math.toRadians(rotDeg+45))*s));
			outline.addPoint((int)(ex+Math.cos(Math.toRadians(rotDeg+135))*s), (int)(ey+Math.sin(Math.toRadians(rotDeg+135))*s));
			outline.addPoint((int)(ex+Math.cos(Math.toRadians(rotDeg+225))*s), (int)(ey+Math.sin(Math.toRadians(rotDeg+225))*s));
			outline.addPoint((int)(ex+Math.cos(Math.toRadians(rotDeg+315))*s), (int)(ey+Math.sin(Math.toRadians(rotDeg+315))*s));
		}
	}

	//return the shape of enemy
	public Polygon draw(){
		return outline;
	}
	public String getType(){
		return type;
	}
	public double getX(){return ex;}
	public double getY(){return ey;}
	public void setX(double newx){ex = newx;}
	public void setY(double newy){ey = newy;}
	public void setSize(double size){
		maxsize = size;
	}
	public double getSize(){return s;}
	public int getCircLevel(){return circLevel;}


}