// Snake Object
// ============
// this is the snake object where it keeps track of all the main information
// for a snake enemy. THis includes collision, coordinates, each bodypart,
// and if they are dead.
// These snakes move in circular motions and turn in circles, ( cannot turn to face
// directly at you )

import java.util.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

public class Snake{
	// these are the coordinate values along with deg it is going and speed
	private double x, y, deg, vx, vy, v;
	// the outline of the snakes
	private Polygon outline;
	// how many bodyparts or tails
	private int tail;
	// each bodypart's coordinates
	private ArrayList<double[]> bodycords;
	// each bodypart's center
	private ArrayList<Point> body;
	// death flag
	private boolean dead;

	// this creates the snake wherever indicated
	public Snake(double x, double y){
		this.x = x;
		this.y = y;
		dead = false;
		// speed and direction the snake is headed (deg)
		v = 5;
		deg = Math.toDegrees(Math.atan2(350-y, 500-x));
		vx = Math.cos(Math.toRadians(deg))*v;
		vy = Math.sin(Math.toRadians(deg))*v;
		// resets the outline
		outline = new Polygon();

		//making a tail
		bodycords = new ArrayList<double[]>(); //snake coordinates
		body = new ArrayList<Point>();         //real tail pieces
		tail = 25;
		// creating each part
		for (int i=0; i<tail*3; i++){
			double[] tmp = new double[2];
			tmp[0]=x;
			tmp[1]=y;
			bodycords.add(tmp);
		}
	}
	
	// this calculates the snake's movement
	// causing it to travel in circles
	public void move(double mx, double my){
		// the angle between you and snake
		double deg2 = Math.toDegrees(Math.atan2(350-y, 500-x));
		
		if (deg2<0){deg2+=360;} //reset angle (prevents it from getting too high
		
		//if difference is negative
		if ((deg2-deg)<0){
			//turn right or left based on which angle is closer
			//(i.e. going clockwise or counterclockwise)
			if ((deg2+360-deg)>(deg-deg2)){
				deg-=2; //turn right
				if (deg<=0){deg=360;}
			}
			else{
				deg+=2; //turn left
				if (deg>=360){deg=0;}
			}
		}
		//if difference is positive
		else if((deg2-deg)>0){
			//turn right or left based on which angle is closer
			//(i.e. going clockwise or counterclockwise)
			if ((deg+360-deg2)>(deg2-deg)){
				deg+=2; //turn left
				if (deg>=360){deg=0;}
			}
			else{
				deg-=2; //turn right
				if (deg<=0){deg=360;}
			}
		} 
		//if the angles already match up, do nothing
		else{
			deg = deg2;
		}
		
		// again this just deals with the speed
		vx = Math.cos(Math.toRadians(deg))*v;
		vy = Math.sin(Math.toRadians(deg))*v;
		// adding speed to the coordinates
		x+=vx+mx;
		y+=vy+my;
		drawShape();

		double[] tmp = new double[2];
		tmp[0]=x;
		tmp[1]=y;
		
		// adds to the back and removes the first bodycord
		bodycords.add(tmp);
		bodycords.remove(0);
		body.clear();

		for (int f=0;f<bodycords.size();f++){
			// this looks for the last tail
			if (f == bodycords.size()-1){
				mx/=5;
				my/=5;
			}
			// aligning the body with the head
			bodycords.get(f)[0] += mx;
			bodycords.get(f)[1] += my;

			//add the real tail points
			if (f%3==0){
				body.add(new Point((int)bodycords.get(f)[0], (int)bodycords.get(f)[1]));
			}
		}
	}
	// fixes the locations of each body part when the snake is flagged "dead"
	public void moveAfterDead(double mx, double my){
		for (int i=0; i<body.size(); i++){
			body.get(i).setLocation(body.get(i).getX()+mx, body.get(i).getY()+my);
		}
	}
	
	// drawing of the actual snakehead
	private void drawShape(){
		outline.reset();
		double s = 10;
		double w = 120;
		//tip point
		outline.addPoint((int)(x+Math.cos(Math.toRadians(deg))*s), (int)(y+Math.sin(Math.toRadians(deg))*s));
		outline.addPoint((int)(x+Math.cos(Math.toRadians(deg+w))*s), (int)(y+Math.sin(Math.toRadians(deg+w))*s));
		// the butt
		outline.addPoint((int)x, (int)y);
		outline.addPoint((int)(x+Math.cos(Math.toRadians(deg-w))*s), (int)(y+Math.sin(Math.toRadians(deg-w))*s));
	}
	
	// get functions
	public double getX(){return x;}
	public double getY(){return y;}
	public double getDeg(){return deg;}
	public ArrayList<double[]> getArrayList(){
		return bodycords;
	}
	public Polygon draw(){
		return outline;
	}
	// contains the head rectangle to check bullet collisions
	public Rectangle headshot (){
		Rectangle temp = new Rectangle ((int)x-10,(int)y-10,20,20);
		return temp;
	}
	// checks if guy is dead
	public void kill(){dead = true;}
	
	// removes body parts
	public void removePiece(){
		body.remove(body.size()-1);
	}
	public boolean isDead(){return dead;}
	
	public ArrayList<Point> getBody(){return body;}

}