//BULLET Class
//============
//Makes a bullet based on a given coordinate, angle and speed
//Also takes care of their location and movements
//Functions include: distance, move, drawShape, and 
//                   other GET variables (x, y, deg, etc).

import java.util.*;
import java.awt.*;

public class Bullet{
	private String type;
	private double x, y, deg, v, vx, vy;
	private Polygon outline;
	
	public Bullet(double x, double y, double deg, double v){
		//x, y, coordinates
		this.x = x;
		this.y = y;
		//velocity
		this.v = v;
		//angle it's facing
		this.deg = deg;

		outline = new Polygon(); //shape of bullet
		
		//vector velocities
		vx = Math.cos(Math.toRadians(deg))*v;
		vy = Math.sin(Math.toRadians(deg))*v;
	}
	
	//DISTANCE FORMULA
	public double distance(double x1, double y1, double x2, double y2){
		return Math.pow((Math.pow(x1-x2, 2.0))+(Math.pow(y1-y2, 2.0)), 0.5);
	}
	
	//MOVEMENTS
	public void move(double mx, double my){
		
		//move based on previously calculated velocity
		x+=vx;
		y+=vy;
		
		//move relative to user's movements
		x+=mx/2;
		y+=my/2;
		
		//draw the shape
		drawShape();

	}

	private void drawShape(){
		outline.reset();

		double s = 5;   //overall size of bullet
		double w = 165; //width of bullet
		
		//polygon of 4 points makes up shape of bullet
		outline.addPoint((int)(x+Math.cos(Math.toRadians(deg))*s), (int)(y+Math.sin(Math.toRadians(deg))*s));
		outline.addPoint((int)(x+Math.cos(Math.toRadians(deg+w))*s), (int)(y+Math.sin(Math.toRadians(deg+w))*s));
		outline.addPoint((int)(x+Math.cos(Math.toRadians(deg+180))*s), (int)(y+Math.sin(Math.toRadians(deg+180))*s));
		outline.addPoint((int)(x+Math.cos(Math.toRadians(deg-w))*s), (int)(y+Math.sin(Math.toRadians(deg-w))*s));
	}
	
	//return the shape
	public Polygon draw(){
		return outline;
	}

	// GET functions
	public double getX(){return x;}
	public double getY(){return y;}
	public double getDeg(){
		return deg;
	}
}