//UTRAIL Class
//============
//makes a simple trail behind the guy
//consists of golden circles that fade out
//Functions include: MakePrint, update, getPrints, getCols

import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class UTrail{
	ArrayList<Point> prints;
	ArrayList<Color> cols;
	private double fade;
	public UTrail(){
		prints = new ArrayList<Point>(); //list of points (for the trail)
		cols = new ArrayList<Color>();  //list of colours (for each point)
		fade = 0.96;  //how fast it fades away
	}
	
	//DISTANCE FORMULA
	public double distance(double x1, double y1, double x2, double y2){
		return Math.pow((Math.pow(x1-x2, 2.0))+(Math.pow(y1-y2, 2.0)), 0.5);
	}
	
	//MAKE PRINTS
	public void MakePrint(){
		
		//make 3 randomly placed points within a certain distance
		//(i.e. around the guy only)
		for (int i=0; i<3; i++){
			double x = Math.random()*20+490;
			double y = Math.random()*20+340;
			while(distance(x, y, 500, 350)>=10){
				x = Math.random()*20+490;
				y = Math.random()*20+340;
			}
			prints.add(new Point((int)x, (int)y));
			cols.add(new Color(245, 170, 0));
		}

	}
	
	//UPDATE
	public void update (double vx, double vy){
		
		//set their locations according to the user's speed
		//(they should be relative to your current position)
		for (int i=prints.size()-1; i>=0; i-=1){
			prints.get(i).setLocation(prints.get(i).getX()+vx, prints.get(i).getY()+vy);
			//fade colour
			cols.set(i, new Color((int)(cols.get(i).getRed()*fade), (int)(cols.get(i).getGreen()*fade),(int)(cols.get(i).getBlue()*fade)));
			//remove if they get too dark
			if (cols.get(i).getRed()<=10){
				prints.remove(i);
				cols.remove(i);
			}
		}
	}
	public ArrayList<Point> getPrints(){return prints;} //get list of points
	public ArrayList<Color> getCols(){return cols;} //get list of colors
}