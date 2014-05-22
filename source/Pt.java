// Point Objects
// ==============
// this object allows us to create points in our game
// and use them to however we desire
// but it is mostly used for making the points to connect
// to form a grid
 
import java.util.*;

public class Pt{
	// ox and oy are the point coords
	// tx and ty are the total coords
	// x, y are the point coords
	// ax and ay are acceleration numbers
	// vx and vy are speed numbers
	private double x, y,tx, ty, ox, oy, vx, vy, ax, ay;
	// connected points,side points, up and down points
	private ArrayList<Pt> con_pts, side_pts, updown_pts;
	
	// activates every variable by giving each a value
	// (overloaded)
	public Pt(double x, double y){
		this.x = x;
		this.y = y;
		ox = x;
		oy = y;
		vx = vy = 0;
		ax = ay = 0;
		tx=0;
		ty=0;
		con_pts = new ArrayList<Pt>();
		side_pts = new ArrayList<Pt>();
		updown_pts = new ArrayList<Pt>();
	}
	public Pt(double x, double y, ArrayList<Pt> con){
		this.x = x;
		this.y = y;
		ox = x;
		oy = y;
		vx = vy = 0;
		ax = ay = 0;
		tx=0;
		ty=0;
		con_pts = con;
	}
// ================================================================
	
	// AGAIN THE DISTANCE FORMULA
	private double dist(Pt point){
		return Math.pow((Math.pow(point.getX()-x, 2.0))+(Math.pow(point.getY()-y, 2.0)), 0.5);
	}

	// simple functions of getting and setting
	public double getX(){return x;}
	public double getY(){return y;}
	public double getOX(){return ox;}
	public double getOY(){return oy;}
	public double getVY(){return vy;}
	public void setX(double newx){x = newx;}
	public void setY(double newy){y = newy;}
	public ArrayList<Pt> getCPts(){return con_pts;}

	// filters each point and adds them to the correct arraylist
	public void addConnected(Pt point){
		con_pts.add(point);
		if (point.getX()==x){
			updown_pts.add(point);
		}
		if (point.getY()==y){
			side_pts.add(point);
		}

	}
	
	// function moves each point in correlation with the movement of the character
	// in other words, since the character actually stays in one place,
	// we had to make the pts in the back move to create the illusion of movement
	public void move(){
		tx = 0;
		ty = 0;
		for (Pt point: con_pts){
			tx+=point.getX();
			ty+=point.getY();
		}
		
		// speed of the pts
		if (con_pts.size()==4){
			vx=(tx/con_pts.size()-x)/10;
			vy=(ty/con_pts.size()-y)/10;
		}
		vx+=ax;
		x+= vx*1;
		vy+=ay;
		y+=vy*1;
		//return new Pt(x+vx, y+vy, con_pts);

	}
	public String toString() {
		return x + ", " + y;
	}
}