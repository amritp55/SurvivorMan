// The Main Character Object
// =========================
// this class basically keeps track of the player's character
// by containing their coordinates and the boundary rectangle
// to see if it collides with enemies.

import java.util.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class You{
	private double x, y;
	private double s;
	private Rectangle boundary;
	
	// contains your x and y values and the size of the character
	// and it's boundaries
	public You(){
		x = 500;
		y=350;
		s = 20;
		boundary = new Rectangle ((int)x-20,(int)y-20,40,40);
	}
	public double getX(){
		return x;
	}
	public double getY(){
		return y;
	}
	// resets the character
	public void setCORD(double newx, double newy){
		x = newx;
		y = newy;
	}
	public double size(){
		return s;
	}
	public Rectangle getRect(){
		return boundary;
	}
}