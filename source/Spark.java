//SPARK Class
//===========
//makes badass sparks that spray outwards when 
//you shoot an enemy or shoot a wall.
//consists of rapidly moving lines that fade out and
//get shorter as they decrease in velocity (for spark effect)


import java.util.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

public class Spark{
	private Color col;
	private double v, vx, vy, x, y;
	private double deg;
	
	public Spark(double x, double y, double deg, Color col){
		v = Math.random()*30+10; //random velocity
		this.x = x;
		this.y = y;
		this.deg = deg;          //direction it's facing
		vx = Math.cos(deg)*v;    //velocities
		vy = Math.sin(deg)*v;
		this.col = col; //colour

	}


	//Spark movements
	public void move(double mx, double my, Rectangle bounds){

		//if it goes off screen, change its direction (bounce off):
		
		if ((x<bounds.getX()&&vx<0)||(x>bounds.getWidth()+bounds.getX()&&vx>0)){
			vx*=-1;  //change vector velocity
			deg = Math.atan2(vy, vx); //then calculate a new angle
			v*=0.9;  //make it slower when it bounces
		}
		if ((y<bounds.getY()&&vy<0)||(y>bounds.getHeight()+bounds.getY()&&vy>0)){
			vy*=-1;
			deg = Math.atan2(vy, vx);
			v*=0.9;
		}



		v*=0.94;       //velocity gets slower
		
		//change x and y velocities according to velocity
		vx = Math.cos(deg)*v;
		vy = Math.sin(deg)*v;
		
		//update coordinate
		x+=vx+mx;
		y+=vy+my;
		
		//update colour (fades out)
		col = new Color((int)(col.getRed()*0.98), (int)(col.getGreen()*0.98), (int)(col.getBlue()*0.98));
	}

	public double getVel(){return v;}
	public double getX(){return x;}
	public double getY(){return y;}
	public double getVX(){return vx;}
	public double getVY(){return vy;}
	public Color getCol(){return col;}

}