// 	The object of game is the stay alive
// as long as possible while enemy objects
// attempt to kill you. (one touch kill)
//	you recieve powerups as u progress
// and enemies also get faster and multiply faster

// INSTRUCTIONS:
// =========================
// use mouse to aim and shoot
// use w, a, s, d keys to move
// use spacebar to bomb (when available)


import java.util.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import sun.audio.*;

// ====================================
//             MAIN LOOP
//         with delay method
// ====================================

public class SurvivorManSimple{
	public static void delay (long len)
    {
		try{
			Thread.sleep (len);
		}
		catch (InterruptedException ex){
			System.out.println(ex);
		}
    }


	//MAIN LOOP
	public static void main(String [] args)throws IOException{
		DBFrame frame = new DBFrame();
		musicplayer MusicPlayer = new musicplayer("clubbed.wav");
		String gameStat="intro";  //game starts at intro screen
		while(true){
			//musicplayer.playAudio("sound.wav");
			if (gameStat.equals("intro") || gameStat.equals ("instructions")){
				gameStat=frame.intro();
			}
			else if (gameStat.equals("main")){
				frame.dostuff();
			}

			frame.update();
		}
	}
}

// ====================================
//             VARIABLES
// ====================================

class DBFrame extends BaseFrame{
	private You sMan;
	// the character object that keeps track of x and y
	// along with the rectangle used for collision
	private UTrail trail;
	// the object that creates the yellow trail behind the character
	private String gunType;
	// the type of gun being used at the time
	private Font gamefont = new Font ("sansserif",Font.BOLD,24);
	private Font highscorefont = new Font ("sansserif",Font.BOLD,50);
	// the default font used in the game
	private int score, lives = 3;
	// variables used to keep track of the information on the bottom bar
	private double minx, miny;
	// the points the character is at
	private ArrayList<Bullet> bullets;
	// arraylist keeps track of all the bullets on the screen atm
	private ArrayList<Pt> pts;
	// arraylist keeps track of all the points on the screen (makes up grid)
	private Rectangle boundRect;
	//rectangle that covers whole grid
	private ArrayList <Enemy> enemies = new ArrayList <Enemy> ();
	// arraylist of all the enemy objects currently active in the game
	private double vx, vy;
	// the character's speed
	private double fireSpeed;
	// how frequently the bullets will fire
	private ArrayList<Snake> snakes = new ArrayList<Snake>();
	// arraylist full of snake objects active in the game
	private ArrayList<Spark> sparks;
	// arraylist of ALL the sparks until they 'fade' away
	private Image temppic, thehero;
	// the image of the character
	private double oldcirclex, oldcircley, oldcirclelevel;
	// the previous x, y, and level of the old circle
	// (used when drawing circles)
	private boolean deadcircle;
	// keeps track of whether the circle is dead or not
	private boolean deathtime = false;
	// keeps track of whether the character is dead or not
	private int fadeout = 0;
	// fades the screen out when the character dies
	private String BOMB;
	// keeps track of whether the bomb is active or not
	private Explosion explosion;
	// activation of the bomb object
	private double rotationnum;
	// the angle of rotation for the character
	private boolean bulletpierce = false;

	// activation of power up object
	private boolean poweruptime;
	private boolean activatepowerup = false;
	private int luckynum;
	private int powertemp;
	private int dispower;
	private PowerUp luckyone;
	private boolean bombavailable = false;

	//powerup images
	private Image powerup1, powerup2,powerup3, powerup4, powerup5, powerup6, powerup7;
	private Image tempP1, tempP2, tempP3, tempP4, tempP5, tempP6, tempP7;

	//introduction/instruction screen variables and images
	private Image butt1, butt2, helpbutton, playbutton, instruct, instructionscreen;
	private Rectangle helpbutt, playbutt;
	private int instructiontime = 0;
	private boolean instructionON = false;
	private Image instruct_norm, instruct_select, play_norm, play_select;
	private Rectangle play_rect, instruct_rect;

	ArrayList <String> players = new ArrayList <String> ();
	ArrayList <Integer> scores = new ArrayList <Integer> ();
	TreeMap <String,Integer> highscorelist = new TreeMap <String,Integer> ();
	TreeSet <BNode> scoresort = new TreeSet <BNode>();
	private String name;
	private boolean outfileactivate = true;

	//INTRO VARIABLES
	private String gameStat;
	private Image introscreen, title, names, smoke1;
	private double introx, introy, introw, introl;
	private double titlex, titley, namex, namey;
	private boolean clicked_in_intro;
	private double smoke1x, smoke1v, smoke2x, smoke2v;

	//enemy generate timers
	int enemyTimer;
	int randEnemyTimer;
	int realTime;
	int	realRandEnemy;



 	// activation of all the one time variables
	public DBFrame() throws IOException{
		super();
		name = "Anonymous";
		sMan = new You();
		trail = new UTrail();
		gunType = "single"; // bullet number
		minx = -250; //grid position
		miny = -400;
		boundRect = new Rectangle((int)minx, (int)miny, 1480, 1480); //bounding rectangle of play area
		fireSpeed = 5; //how often bullets fire
		BOMB = "inactive"; //bomb status
		gameStat="intro";  //game status
		explosion = new Explosion(); //explosion (of bomb)
    	temppic = new ImageIcon ("pics/thesexyguy.png").getImage(); //character image
    	thehero = temppic.getScaledInstance (40,40,Image.SCALE_SMOOTH);
		pts = new ArrayList<Pt>(); //list of points (for grid)
		bullets = new ArrayList<Bullet>(); //list of bullets
		enemies = new ArrayList <Enemy> (); //list of enemies
		sparks = new ArrayList<Spark>(); //list of sparks
		powertemp = (int)(Math.random()*7)+1;
		luckyone = new PowerUp (powertemp,Math.random()*getWidth(),Math.random()*getHeight());

		//powerup images
    	tempP1 = new ImageIcon ("pics/powerup1.png").getImage();
    	powerup1 = tempP1.getScaledInstance (40,40,Image.SCALE_SMOOTH);
    	tempP2 = new ImageIcon ("pics/powerup2.png").getImage();
    	powerup2 = tempP2.getScaledInstance (40,40,Image.SCALE_SMOOTH);
    	tempP3 = new ImageIcon ("pics/powerup3.png").getImage();
    	powerup3 = tempP3.getScaledInstance (40,40,Image.SCALE_SMOOTH);
    	tempP4 = new ImageIcon ("pics/powerup4.png").getImage();
    	powerup4 = tempP4.getScaledInstance (40,40,Image.SCALE_SMOOTH);
    	tempP5 = new ImageIcon ("pics/powerup5.png").getImage();
    	powerup5 = tempP5.getScaledInstance (40,40,Image.SCALE_SMOOTH);
    	tempP6 = new ImageIcon ("pics/powerup6.png").getImage();
    	powerup6 = tempP6.getScaledInstance (40,40,Image.SCALE_SMOOTH);
    	tempP7 = new ImageIcon ("pics/powerup7.png").getImage();
    	powerup7 = tempP7.getScaledInstance (40,40,Image.SCALE_SMOOTH);

    	/*butt1 = new ImageIcon ("playbutton.png").getImage();
    	butt2 = new ImageIcon ("helpbutton.png").getImage ();
    	playbutton = butt1.getScaledInstance (50,50,Image.SCALE_SMOOTH);
    	helpbutton = butt2.getScaledInstance (50,50,Image.SCALE_SMOOTH);
    	instruct = new ImageIcon ("instruction_screen.png").getImage();
    	instructionscreen= instruct.getScaledInstance (1000,700,Image.SCALE_SMOOTH);
    	playbutt = new Rectangle (475,500,50,50);
		helpbutt = new Rectangle (475,620,50,50);*/

		//intro pictures
		play_norm = new ImageIcon ("pics/play_norm.jpg").getImage();
		play_select = new ImageIcon ("pics/play_select.jpg").getImage();
		instruct_norm = new ImageIcon ("pics/instructions_norm.jpg").getImage();
		instruct_select = new ImageIcon ("pics/instructions_select.jpg").getImage();
		smoke1 = new ImageIcon("pics/smoke1.png").getImage();


		//buttons on intro screen
		play_rect=new Rectangle(450, 510, 115, 42);
		instruct_rect=new Rectangle(408, 575, 200, 34);

		//instruction screen picture
		instruct = new ImageIcon ("pics/instructions.jpg").getImage();
    	instructionscreen= instruct.getScaledInstance (1000,700,Image.SCALE_SMOOTH);

    	//intro variables
		introscreen = new ImageIcon ("pics/surv_main.png").getImage();
		title = new ImageIcon("pics/title.png").getImage();
		names = new ImageIcon("pics/names.png").getImage();

		introx=0;
		introy=0;
		introw=1000;
		introl=700;
		clicked_in_intro=false;

		smoke1x=-200;
		smoke1v=2;
		smoke2x=200;
		smoke2v=-2;

		//enemy generate timers
		enemyTimer=100;
		randEnemyTimer=10;
		realTime=250;
		realRandEnemy=50;

		// reading in high scores
		Scanner inFile = new Scanner (new BufferedReader(new FileReader("highscores.txt")));
		int tempnum = 0;
		int counter = 1;
		// when reading the file, the program adds all the scores
		// into one arraylist and all the players in the other
		while(inFile.hasNextLine()){
			if (counter %2==0){
				tempnum = Integer.parseInt (inFile.nextLine());
				scores.add(tempnum);
			}
			else if (counter %2 != 0){
				players.add (inFile.nextLine());
			}
			counter += 1;
		}
		inFile.close();
		// creates each player's score as a BNode
		// to map their score with their name
		// then it is added into a TREESET
		// so they can be displayed in descending order
		for (int x=0;x<players.size();x++){
			BNode temp = new BNode (players.get(x),scores.get(x));
			scoresort.add (temp);
		}

// =========================================================
//                        GAME ELEMENTS!
// =========================================================

		// CREATING AND CONNECTING THE GRID POINTS
		// =======================================
		for (int y=0; y<1500; y+=40){
			for (int x=0; x<1500; x+=40){
				Pt tmp = new Pt(x, y);
				pts.add(tmp);
				if (x!=0){
					tmp.addConnected(pts.get(pts.size()-2));
				}
				if (y!=0){
					tmp.addConnected(pts.get(pts.size()-39));
				}
			}
		}
		for (int i=0; i<pts.size(); i++){
			for (Pt point: pts.get(i).getCPts()){
				point.addConnected(pts.get(i));
			}
		} // =========================================
	}

	// DISTANCE CALCULATION METHOD
	// ===========================
	public double distance(double x1, double y1, double x2, double y2){
		return Math.pow((Math.pow(x1-x2, 2.0))+(Math.pow(y1-y2, 2.0)), 0.5);
	}

	// =========================
	// INTRODUCTION SCREEN LOOP
	// =========================
	public String intro(){

		//move smoke screen
		smoke1x+=smoke1v;
		if (smoke1x<=-200 || smoke1x>=200){
			smoke1v*=-1;
		}
		smoke2x+=smoke2v;
		if (smoke2x<=-200 || smoke2x>=200){
			smoke2v*=-1;
		}

		if (clicked_in_intro){
			//black screen fades out
			double fadespeed = 9;
			introx-=10*fadespeed;
			introy-=7*fadespeed;
			introw+=20*fadespeed;
			introl+=14*fadespeed;
			//words fly off screen
			titlex-=15;
			namex+=7;

			//change status to 'main', once intro has finished
			if (introx<=-6000){
				name = "Anonymous";
				name = JOptionPane.showInputDialog ("Name:");

				System.out.println(name.toString());
				gameStat="main";
			}
		}
		return gameStat;
	}

	//=====GENERATE ENEMIES======

	//creating random enemies
	public void genEnemy(){
		int amount = (int)(Math.random()*5+1); //creates between 1 to 5 enemies
		int type;
		double x, y;
		for (int i=0; i<amount; i++){
			x = Math.random()*1000; //random position
			y = Math.random()*700;
			while(distance(sMan.getX(), sMan.getY(), x, y)<=200){ //dont make them too close to character
				x = Math.random()*1000;
				y = Math.random()*700;
			}
			type = (int)(Math.random()*6); //random type

			//create enemies based on randomly selected type
			if (type==0){
				enemies.add(new Enemy(x, y, "square"));
			}
			if(type==1){
				enemies.add(new Enemy(x, y, "triangle"));
			}
			if(type==2){
				enemies.add(new Enemy(x, y, "diamond"));
			}
			if(type==3){
				enemies.add(new Enemy(x, y, "circle", 3));
			}
			if(type==4){
				enemies.add(new Enemy(x, y, "hexagon"));
			}
			if(type==5){
				snakes.add (new Snake (x, y));
			}

		}
	}
	//CREATING ENEMIES IN PACKS
	//=========================
	//specific generating pattern(s) for each enemy type
	public void genEnemy(int type){

		//SQUARES AND DIAMONDS
		//generate in mobs
		if (type==0 || type==2){

			//fix a certain radius and amount of enemies created
			double radius=Math.random()*20+30;
			double amount=Math.random()*20+30;

			//find a good position to create mob, away from the user
			double crowdx=Math.random()*1100-50;
			double crowdy=Math.random()*800-50;
			//keep finding spot if it's too close
			while (distance(sMan.getX(), sMan.getY(), crowdx, crowdy)>=300 || distance(sMan.getX(), sMan.getY(), crowdx, crowdy)<=150 ||
				boundRect.contains(crowdx, crowdy)==false){
				crowdx=Math.random()*1000;
				crowdy=Math.random()*700;
			}

			//create enemies around the selected spot
			for (int i=0; i<amount; i++){
				double x=Math.random()*1000;
				double y = Math.random()*700;
				//generate a spot within the radius limits
				while (distance(crowdx, crowdy, x, y)>=radius){
					x=Math.random()*1000;
					y = Math.random()*700;
				}
				//create the right type of enemy
				if(type==0){
					enemies.add(new Enemy(x, y, "square"));
				}
				else if(type==2){
					enemies.add(new Enemy(x, y, "diamond"));
				}

			}

		}

		//SNAKES
		//come from the corners
		else if (type==5){
			//make them generate from specified locations
			//at the boundaries of screen

			snakes.add (new Snake (0, 0));
			snakes.add (new Snake (500, 0));
			snakes.add (new Snake (1000, 0));
			snakes.add (new Snake (1000, 350));
			snakes.add (new Snake (1000, 500));
			snakes.add (new Snake (500, 700));
			snakes.add (new Snake (0, 700));
			snakes.add (new Snake (0, 350));
		}

		//MINEFIELD
		//2 patterns
		else if (type==4){
			//automatically grant a bomb powerup
			//(it will give the user a chance to get rid of minefield)
			activatepowerup=true;
			powertemp=5;
			luckyone = new PowerUp (powertemp,Math.random()*300+200,Math.random()*300+50);
			int pattern=(int)(Math.random()*2); //pick one of two patterns

			//first pattern
			//placed in a zig zag pattern away from user
			if (pattern==0){
				int shift=25; //shift every row over by 25 (makes zig zag pattern)
				for (int y=0; y<800; y+=70){
					shift*=-1;
					for (int x=0; x<1000; x+=100){
						//only draw if it's away from player
						if (distance(sMan.getX(), sMan.getY(), x, y)>=300 &&
							boundRect.contains(x, y)){
							enemies.add(new Enemy(x+shift, y, "hexagon"));

						}
					}
				}
			}

			//second pattern
			//make a box or mines around player
			else if (pattern==1){

				//create a top and bottom row of box
				for (int y=100; y<601; y+=500){
					for (int x=200; x<801; x+=100){
						if (boundRect.contains(x, y)){ //create mine only if it's within playing boundaries
							enemies.add(new Enemy(x, y, "hexagon"));

						}
					}
				}

				//create side columns of box
				for (int y=140; y<561; y+=100){
					for (int x=200; x<801; x+=600){
						if (boundRect.contains(x, y)){ //create mine only if it's within playing boundaries
							enemies.add(new Enemy(x, y, "hexagon"));

						}
					}
				}
			}
		}

		//TRIANGLES AND CIRCLES
		//generate randomly
		else if (type==1||type==3){
			int amount=0;
			//amount of triangles
			if (type==1){
				amount = (int)(Math.random()*20+20);
			}
			//amount of circles (less than triangles)
			else if (type==3){
				amount = (int)(Math.random()*5+5);
			}

			for (int i=0; i<amount; i++){
				//find a suitable spot away from user (radius of 150)
				double x=Math.random()*1000;
				double y=Math.random()*700;
				while (distance(sMan.getX(), sMan.getY(), x, y)<=150||boundRect.contains(x, y)==false){

					x=Math.random()*1000;
					y=Math.random()*700;
				}
				//make triangle
				if(type==1){
					enemies.add(new Enemy(x, y, "triangle"));
				}
				//make circle
				else if(type==3){
					enemies.add(new Enemy(x, y, "circle", 3));
				}
			}


		}

	}

	// ===============
	// MAIN GAME LOOP
	// ===============
	public void dostuff(){

		// DIRECTION CHANGE OF THE CHARACTER
		// =================================
		//movements
		//max should always be 5 off the limit

		vx=0;
		vy=0;
		// diagonals
		if(keys['W'] && keys ['D']){
			if (minx >= -960 && miny <= 335){
				vx= -3.5355;
				vy= 3.5355;
			}
		}
		else if(keys['W'] && keys ['A']){
			if (minx <= 480 && miny <= 335){
				vx= 3.5355;
				vy= 3.5355;
			}
		}
		else if(keys['S'] && keys ['D']){
			if (minx >= -960 && miny >= -1115){
				vx= -3.5355;
				vy= -3.5355;
			}
		}
		else if(keys['S'] && keys ['A']){
			if (minx <= 480 && miny >= -1115){
				vx= 3.5355;
				vy= -3.5355;
			}
		}
		// horizontal or vertical
		else if(keys['A']){
			if (minx <= 480){
				vx=5;
			}

		}
		else if(keys['D']){
			if (minx >= -960){
				vx=-5;
			}

		}
		else if(keys['W']){
			if (miny <= 335){
				vy=5;
			}

		}
		else if(keys['S']){
			if (miny >= -1115){
				vy=-5;
			}
		}
		//move grid according to applied speed
		minx+=vx;
		miny+=vy;

		//the boundary rectangle
		boundRect.setBounds((int)minx, (int)miny, 1480, 1480);

		// TRAIL CREATION
		// ==============
		if (vx!=0||vy!=0){
			trail.MakePrint();
		}
		trail.update(vx, vy); //fadeout with time


// =======================================================
//                 ENEMY CREATION
// =======================================================

		//make enemies (temporary test method)

		/*if (mb==3){
			enemies.add(new Enemy(Math.random()*getWidth(), Math.random()*getHeight(), "square"));
		}
		if(keys['1']){
			enemies.add(new Enemy(Math.random()*getWidth(), Math.random()*getHeight(), "triangle"));
		}
		if(keys['2']){
			enemies.add(new Enemy(Math.random()*getWidth(), Math.random()*getHeight(), "diamond"));
		}
		if(keys['3']){
			enemies.add(new Enemy(Math.random()*getWidth(), Math.random()*getHeight(), "circle", 3));
		}
		if(keys['4']){
			enemies.add(new Enemy(Math.random()*getWidth(), Math.random()*getHeight(), "hexagon"));
		}

		// makes snakes (temp)
		if (mb==2){
			Snake newsnake = new Snake (Math.random()*1000,Math.random()*700);
			snakes.add (newsnake);
			mb=0;
		}*/
		// ========================================================================

		//MAKE ENEMIES

		//generate enemy packs
		if (deathtime==false){ //create enemies if user is not dead
			enemyTimer-=1;  //countdown to next enemy spawn
		 	if ((BOMB=="inactive")&&(enemyTimer==0)){ //only create is bomb is inactive and timer reaches 0
		 		if (realTime>=130){
		 			realTime=realTime-5;
		 		}
		 		enemyTimer=(int)(Math.random()*150+realTime); //reset timer
		 		genEnemy((int)(Math.random()*6)); //set a random enemy type;
		 	}
		 	//generate random enemies
		 	randEnemyTimer-=1;  //countdown to next random enemies
		 	if (randEnemyTimer==0){ //create if timer reaches zero
		 		if (realTime>=30){
		 			realTime=realTime-1;
		 		}
		 		randEnemyTimer=realRandEnemy; //reset timer
		 		genEnemy(); //generate random enemies
		 	}
		}



		if (deadcircle== true){
			for (int c=0; c<3; c++){
				enemies.add (new Enemy (oldcirclex,oldcircley,"circle",(int)oldcirclelevel-1));
			}
			deadcircle = false;
		}

		//ENEMIES INTERACTIONS
		for (int e=enemies.size()-1;e>=0;e-=1){

    		enemies.get(e).movement(sMan.getX(), sMan.getY(), vx, vy, boundRect); //move enemies
    		enemies.get(e).collide(enemies);  //check enemy collisions with each other



    		//check if they hit explosion of bomb
    		if (explosion.contains(enemies.get(e))){
    			//make sparks according to type
 				makeSparks(enemies.get(e).getX(), enemies.get(e).getY(), enemies.get(e).getType());

 				// SCORING
	 			if (deathtime == false){
					if (enemies.get(e).getType().equals("triangle")){
						score += 10;
	 				}
		 			if (enemies.get(e).getType().equals("diamond")){
		 				score += 20;
		 			}
		 			if (enemies.get(e).getType().equals("circle")){
		 				score += 5;
		 			}
		 			if (enemies.get(e).getType().equals("hexagon")){
		 				score += 15;
		 			}
		 			if (enemies.get(e).getType().equals ("square")){
		 				score += 30;
		 			}
	 			}
	 			// REMOVING ENEMY
    			enemies.remove(e);
    		}

		}

		//ENEMY INTERACTIONS PART II
		for (int e=enemies.size()-1;e>=0;e-=1){
			// LIVESSSSSSSSSSSSSSSSS
			if (sMan.getRect().contains (enemies.get(e).getX(),enemies.get(e).getY())){
				deathtime = true;
				break;
			}
			//check if they hit bullets
 			for (int i=bullets.size()-1; i>=0; i-=1){
 				//square enemies dodge bullets
 				if (enemies.get(e).getType().equals("square")){
 					enemies.get(e).dodge(bullets.get(i));
 				}
 				if (enemies.get(e).draw().contains(bullets.get(i).getX(),bullets.get(i).getY())){
 					//make sparks according to type
 					makeSparks(bullets.get(i).getX(), bullets.get(i).getY(), enemies.get(e).getType());

 					//special case for circles
 					if (enemies.get(e).getType().equals("circle")){
 						if (enemies.get(e).getCircLevel()!=1){
 							oldcirclelevel = enemies.get(e).getCircLevel();
 							oldcirclex = enemies.get(e).getX();
 							oldcircley = enemies.get(e).getY();
 							deadcircle = true;
 							//for (int c=0; c<3; c++){
							//	enemies.add (new Enemy (enemies.get(e).getX(),enemies.get(e).getY(),"circle",
							//		enemies.get(e).getCircLevel()-1));
							//}

 						}
 					}

 					// SCORES
 					if (deathtime == false){
	 					if (enemies.get(e).getType().equals("triangle")){
	 						score += 10;
	 					}
	 					if (enemies.get(e).getType().equals("diamond")){
	 						score += 20;
	 					}
	 					if (enemies.get(e).getType().equals("circle")){
	 						score += 5;
	 					}
	 					if (enemies.get(e).getType().equals("hexagon")){
	 						score += 15;
	 					}
	 					if (enemies.get(e).getType().equals ("square")){
	 						score += 30;
	 					}
 					}


 					enemies.remove(e);

 					//remove bullets if bulletpierce is not activated
 					if (bulletpierce == false){
	 					bullets.remove(i);
 					}
 					break;
 				}
 			}



    	}

		// THE SNAKES MOVING
		for (int s=snakes.size()-1;s>=0;s-= 1){

			//if snakes are dead, they do not move, but they stay on screen
			//until all its parts are gone
			if (snakes.get(s).isDead()){
				snakes.get(s).moveAfterDead(vx, vy);
				makeSparks(snakes.get(s).getBody().get(snakes.get(s).getBody().size()-1).getX(),
					snakes.get(s).getBody().get(snakes.get(s).getBody().size()-1).getY(), "snakeTail");
				snakes.get(s).removePiece(); //remove each piece one by one

				//if all pieces are gone, remove snake
				if(snakes.get(s).getBody().size()==0){
					snakes.remove(s);
					if (deathtime == false){
						score += 50;
					}
				}
			}
			else{
				//move snake
				snakes.get(s).move (vx,vy);
				//check collisions with bomb
				if (explosion.contains(snakes.get(s))){
					makeSparks(snakes.get(s).getX(), snakes.get(s).getY(), "snakeHead");
					snakes.get(s).kill();
				}

				//check collisions with bullets
				for (int i=bullets.size()-1; i>=0; i-=1){
					//only collides with its head
					if (snakes.get(s).headshot().contains(bullets.get(i).getX(),bullets.get(i).getY())){
						makeSparks(bullets.get(i).getX(), bullets.get(i).getY(), "snakeHead");
						bullets.remove (i);
						snakes.get(s).kill();
						break;
					}
				}
			}

		}


		//=====BOMB=====
		//activate bomb
		if(deathtime==true){
			BOMB = "active";
		}
		//use spacebar to deploy
		if (bombavailable == true){
			if (keys[32]){
				BOMB = "active";
				bombavailable = false;
			}
		}
		//make the explosion grow if released
		if (BOMB.equals("active")){
			explosion.grow(vx, vy);
			//kill explosion afte it grows a certain size
			if (explosion.size()>=750){
				BOMB = "inactive";
				explosion = new Explosion();
				snakes.clear();
				enemies.clear();
			}
		}

		//SHOOT
		//the only difference between the different shooting types is
		//additional bullets added at different angles
		if (mb==1){

			//single barrel
			if (gunType.equals("single")){
				if (fireSpeed==0){ //if countdown reached zero, fire bullet
					double dist = distance(sMan.getX(), sMan.getY(), mx, my);
					double deg = Math.toDegrees(Math.atan2(my-sMan.getY()+(my-sMan.getY())/(dist/12), mx-sMan.getX()+(mx-sMan.getX())/(dist/12)));
					bullets.add(new Bullet(sMan.getX()+(mx-sMan.getX())/(dist/12), sMan.getY()+(my-sMan.getY())/(dist/12), deg, 10));
					fireSpeed=5;
				}
				else{ //countdown till next bullet fire
					fireSpeed-=1;
				}
			}

			//double barrel
			else if (gunType.equals("double")){
				if (fireSpeed==0){ //if countdown reached zero, fire bullet
					double dist = distance(sMan.getX(), sMan.getY(), mx, my);
					double deg = Math.toDegrees(Math.atan2(my-sMan.getY()+(my-sMan.getY())/(dist/12), mx-sMan.getX()+(mx-sMan.getX())/(dist/12)));
					bullets.add(new Bullet(sMan.getX()+(mx-sMan.getX())/(dist/12), sMan.getY()+(my-sMan.getY())/(dist/12), deg+5, 10));
					bullets.add(new Bullet(sMan.getX()+(mx-sMan.getX())/(dist/12), sMan.getY()+(my-sMan.getY())/(dist/12), deg-5, 10));
					fireSpeed=5;
				}
				else{ //countdown till next bullet fire
					fireSpeed-=1;
				}
			}

			//triple barrel
			else if (gunType.equals("triple")){
				if (fireSpeed==0){ //if countdown reached zero, fire bullet
					double dist = distance(sMan.getX(), sMan.getY(), mx, my);
					double deg = Math.toDegrees(Math.atan2(my-sMan.getY()+(my-sMan.getY())/(dist/12), mx-sMan.getX()+(mx-sMan.getX())/(dist/12)));
					bullets.add(new Bullet(sMan.getX()+(mx-sMan.getX())/(dist/12), sMan.getY()+(my-sMan.getY())/(dist/12), deg+5, 10));
					bullets.add(new Bullet(sMan.getX()+(mx-sMan.getX())/(dist/12), sMan.getY()+(my-sMan.getY())/(dist/12), deg-5, 10));
					bullets.add(new Bullet(sMan.getX()+(mx-sMan.getX())/(dist/12), sMan.getY()+(my-sMan.getY())/(dist/12), deg, 10));
					fireSpeed=5;
				}
				else{ //countdown till next bullet fire
					fireSpeed-=1;
				}
			}

			//sideshooter
			else if (gunType.equals("sideshooter")){
				if (fireSpeed==0){ //if countdown reached zero, fire bullet
					double dist = distance(sMan.getX(), sMan.getY(), mx, my);
					double deg = Math.toDegrees(Math.atan2(my-sMan.getY()+(my-sMan.getY())/(dist/12), mx-sMan.getX()+(mx-sMan.getX())/(dist/12)));
					bullets.add(new Bullet(sMan.getX()+(mx-sMan.getX())/(dist/12), sMan.getY()+(my-sMan.getY())/(dist/12), deg+90, 10));
					bullets.add(new Bullet(sMan.getX()+(mx-sMan.getX())/(dist/12), sMan.getY()+(my-sMan.getY())/(dist/12), deg-90, 10));
					bullets.add(new Bullet(sMan.getX()+(mx-sMan.getX())/(dist/12), sMan.getY()+(my-sMan.getY())/(dist/12), deg, 10));
					fireSpeed=5;
				}
				else{ //countdown till next bullet fire
					fireSpeed-=1;
				}
			}

		}

		//MOVE BULLETS
		for (int i=bullets.size()-1; i>=0; i-=1){
			bullets.get(i).move(vx, vy);
			//check if bullets go outside boundaries
			if (boundRect.contains(bullets.get(i).getX(), bullets.get(i).getY())==false){
				makeSparks(bullets.get(i).getX(), bullets.get(i).getY(), "edge");
				bullets.remove(i);
			}
		}

		//MOVE SPARKS
		for (int i=sparks.size()-1; i>=0; i-=1){
			sparks.get(i).move(vx, vy, boundRect);
			//delete spark if it gets too slow
			if (sparks.get(i).getVel()<=1){
				sparks.remove(i);
			}
		}





// =======================================================
//                    POWER UPS
// =======================================================
		luckyone.movement(sMan.getX(), sMan.getY(), vx, vy, boundRect);
		if (poweruptime == true){
			powertemp = (int)(Math.random()*6)+2;
			lives += luckyone.addLife();
			gunType = luckyone.getBulletNum();
			if (bulletpierce == false){
				bulletpierce = luckyone.bulletpierce();
			}
			// this accounts for if you have a bomb, getting another powerup won't take it away
			if (bombavailable == false){
				bombavailable = luckyone.getBomb();
			}
			luckyone = new PowerUp (powertemp,Math.random()*getWidth(),Math.random()*getHeight());
			poweruptime = false;
		}
		if (deathtime == true){
			bulletpierce = false;
			poweruptime = false;
			bombavailable = false;
		}
	}


	//======MAKE SPARKS======
	//according to what the bullets hit

	public void makeSparks(double x, double y, String hit){

		//random fiery coloured sparks if u hit a wall
		if (hit.equals("edge")){
			Color colour = new Color(255, (int)(Math.random()*255), 0);
			for (int i=0; i<10; i++){
				sparks.add(new Spark(x, y, Math.toRadians(Math.random()*360-180), colour));
			}
		}
		//green sparks for squares
		else if (hit.equals("square")){
			Color colour = (new Color(86,243,10));
			for (int i=0; i<20; i++){
				sparks.add(new Spark(x, y, Math.toRadians(Math.random()*360-180), colour));
			}
		}
		//orange sparks for triangles
		else if (hit.equals("triangle")){
			Color colour = (new Color(250,150,0));
			for (int i=0; i<20; i++){
				sparks.add(new Spark(x, y, Math.toRadians(Math.random()*360-180), colour));
			}
		}
		//blue sparks for diamonds
		else if (hit.equals("diamond")){
			Color colour = Color.cyan;
			for (int i=0; i<20; i++){
				sparks.add(new Spark(x, y, Math.toRadians(Math.random()*360-180), colour));
			}
		}
		//random sparks for hexagon mines
		else if (hit.equals("hexagon")){
			for (int i=0; i<300; i++){
				Color colour = (new Color((int)(Math.random()*255),(int)(Math.random()*255),(int)(Math.random()*255)));
				sparks.add(new Spark(x, y, Math.toRadians(Math.random()*360-180), colour));
			}
		}
		//shades of yellow sparks for death
		else if (hit.equals("dying")){
			for (int i=0; i<100; i++){
				Color colour = (new Color(215,215,140));
				sparks.add(new Spark(x, y, Math.toRadians(Math.random()*360-180), colour));
				colour = (new Color (255,213,66));
				sparks.add(new Spark(x, y, Math.toRadians(Math.random()*360-180), colour));
				colour = (new Color (178,143,20));
				sparks.add(new Spark(x, y, Math.toRadians(Math.random()*360-180), colour));
			}
		}
		//purple sparks for circle
		else if (hit.equals("circle")){
			Color colour = (new Color (200,0,255));
			for (int i=0; i<20; i++){
				sparks.add(new Spark(x, y, Math.toRadians(Math.random()*360-180), colour));
			}
		}
		//yellow sparks for snake head
		else if (hit.equals("snakeHead")){
			Color colour = Color.yellow.darker();
			for (int i=0; i<20; i++){
				sparks.add(new Spark(x, y, Math.toRadians(Math.random()*360-180), colour));
			}
		}
		//purple sparks for snake body
		else if (hit.equals("snakeTail")){
			Color colour = (new Color (255,0,128));
			for (int i=0; i<20; i++){
				sparks.add(new Spark(x, y, Math.toRadians(Math.random()*360-180), colour));
			}
		}
		//same as above, except brighter
		else if (hit.equals("snakeBody")){
			Color colour = (new Color (255,0,128).brighter());
			for (int i=0; i<20; i++){
				sparks.add(new Spark(x, y, Math.toRadians(Math.random()*360-180), colour));
			}
		}
	}

	public void paint(Graphics g){

		if(gameStat.equals("intro")){

			g.setColor(Color.black);
			g.fillRect(0, 0, getWidth(), getHeight());

			// drawing the grid
			for (Pt point: pts){
				g.setColor(new Color(0, 100, 0));
				if (explosion.contains(point.getX()+minx, point.getY()+miny)){
					g.setColor(new Color(200, 75, 0));
				}
				for (Pt con: point.getCPts()){
					g.drawLine((int)point.getX()+(int)minx, (int)point.getY()+(int)miny, (int)con.getX()+(int)minx, (int)con.getY()+(int)miny);
				}
			}

			//draw character
			g.drawImage(thehero,(int)(sMan.getX()-20),(int)(sMan.getY()-20), null);

			//draw smoke
			if (clicked_in_intro == false){
				g.drawImage(smoke1, (int)smoke1x, 0, null);
				g.drawImage(smoke1, (int)smoke2x, 0, null);
			}

			//draw screen
			g.drawImage(introscreen,(int)(introx), (int)(introy), (int)(introw), (int)(introl), null);
			g.drawImage(title,(int)(titlex), (int)(titley), null);
			g.drawImage(names,(int)(namex), (int)(namey), null);

			//g.drawImage (playbutton,475,500,null);
			//g.drawImage (helpbutton,475,620,null);
			if (clicked_in_intro == false){

				//draw buttons
				g.drawImage(play_norm, 450, 510, null);
				g.drawImage(instruct_norm, 408, 575, null);


				//display new image if mouse hovers over
				if (play_rect.contains(mx, my)){
					g.drawImage(play_select, 450, 510, null);
					if (mb==1){
						clicked_in_intro=true; //active intro loop (screen fade out)
					}
				}

				//display new image if mouse hovers over
				if (instruct_rect.contains(mx, my)){
					g.drawImage(instruct_select, 408, 575, null);
					if (mb==1){
						gameStat="instructions"; //go to instruction screen
					}
				}
			}



			// help button
			/*if (helpbutt.contains (mx,my)&& mb==1){
				gameStat="instructions";
			}*/
		}

		//display introduction screen if selected
		else if (gameStat.equals("instructions")){
			g.drawImage (instructionscreen,0,0,null);
			if (mb==3){ //exit with right click
				gameStat="intro";
			}
		}

		//DRAWING OF MAIN GAME
		//====================
		else if (gameStat.equals("main")){

			Graphics2D g2 = (Graphics2D) g;
			Graphics2D g3 = (Graphics2D) g;

			//delay stuff
			if (enemies.size() <= 60){
				SurvivorManSimple.delay (20);
			}
			if (enemies.size() > 60 && enemies.size() < 100){
				SurvivorManSimple.delay (2);
			}

			g.setColor(Color.black);
			g.fillRect(0, 0, getWidth(), getHeight());

			g.setClip((int)boundRect.getX()-1, (int)boundRect.getY()-1, (int)boundRect.getWidth()+2, (int)boundRect.getHeight()+2);

			// drawing the grid
			for (Pt point: pts){
				g.setColor(new Color(0, 100, 0));
				//change colour of gridlines if bomb is activated
				if (explosion.contains(point.getX()+minx, point.getY()+miny)){
					g.setColor(new Color(200, 75, 0));
				}
				//connect every point to its connecting points (makes a grid)
				for (Pt con: point.getCPts()){
					g.drawLine((int)point.getX()+(int)minx, (int)point.getY()+(int)miny, (int)con.getX()+(int)minx, (int)con.getY()+(int)miny);
				}
			}

			//drawing boundaries of the game board
			g2.setStroke(new BasicStroke(5));
			g.setColor (Color.red);
			g.drawLine ((int)minx,(int)miny,(int)minx,(int)(1480+miny)); // top left, bottom left
			g.drawLine ((int)(1480+minx),(int)miny,(int)(1480+minx),(int)(1480+miny)); // top right, bottom right
			g.drawLine ((int)minx,(int)miny,(int)(1480+minx),(int)miny); // top left to right
			g.drawLine ((int)minx,(int)(1480+miny),(int)(1480+minx),(int)(1480+miny)); // bottom left to right
			g2.setStroke(new BasicStroke(1));

			//draw trail
			for (int i=0; i<trail.getPrints().size(); i++){
				g.setColor(trail.getCols().get(i));
				g.fillRect((int)trail.getPrints().get(i).getX(), (int)trail.getPrints().get(i).getY(), 2,2);
			}

			//draw sparks
			for (int i=sparks.size()-1; i>=0; i-=1){
				g.setColor(sparks.get(i).getCol());
				//length of spark is according to its speed
				g.drawLine((int)sparks.get(i).getX(), (int)sparks.get(i).getY(),
					(int)(sparks.get(i).getX()-sparks.get(i).getVX()/2), (int)(sparks.get(i).getY()-sparks.get(i).getVY()/2));
			}

			//MAIN CHARACTER
			g2.setStroke(new BasicStroke(5));
			double dist = distance((double)mx, (double)my, sMan.getX(), sMan.getY());
			g2.setStroke (new BasicStroke(1));
			//rotate according to user input
			AffineTransform saveXform = g3.getTransform();
			AffineTransform at = new AffineTransform();
			if (keys['W'] && keys['D']){
				rotationnum = 1;
			}
			else if (keys['S'] && keys['A']){
				rotationnum = 4;
			}
			else if (keys['W'] && keys['A']){
				rotationnum = 5.5;
			}
			else if (keys['S'] && keys['D']){
				rotationnum = 2.5;
			}
			else if (keys['W']){
				rotationnum = 0;
			}
			else if (keys['D']){
				rotationnum = 1.6;
			}
			else if (keys['S']){
				rotationnum = 3.15;
			}
			else if (keys['A']){
				rotationnum = 4.7;
			}

			at.rotate(rotationnum,(int)(sMan.getX()),(int)(sMan.getY()));
			g3.transform(at);

			//draw character if he's not dead
			if (deathtime == false){
				g3.drawImage(thehero,(int)(sMan.getX()-20),(int)(sMan.getY()-20), this);
			}
			g3.setTransform(saveXform);

			// SNAKES
			g2.setStroke (new BasicStroke(2));
			for (Snake each: snakes){
				//draw the head of each snake
				if (each.isDead()==false){
					g.setColor (Color.yellow);
					g.drawPolygon (each.draw());
				}
				g.setColor (new Color (255,0,128));
				//draw the body of each snake
				for (Point eachPiece: each.getBody()){
					//bullets cannot penetrate through body
					for (int i=bullets.size()-1; i>=0; i-=1){
						Rectangle temp = new Rectangle ((int)(eachPiece.getX()-5),(int)(eachPiece.getY()-5),10,10);
						if (temp.contains(bullets.get(i).getX(), bullets.get(i).getY())){
							makeSparks(bullets.get(i).getX(), bullets.get(i).getY(), "snakeBody"); //make sparks
							bullets.remove(i); //kill the bullet
						}
					}
					//kill character if he crashes into snake
					if (sMan.getRect().contains(eachPiece)){
						deathtime = true;
					}
					//draw every piece in its body
					g.drawOval ((int)(eachPiece.getX()-5),(int)(eachPiece.getY()-5),10,10);
				}
			}

			//POWERUPS BEING DRAWN AND CALIBRATED!
			// ==============================================
			if (activatepowerup == false){
				// how often a power up shows up
				luckynum = (int)(Math.random()*200);
				//System.out.println (luckynum);
				if (dispower==0){
					activatepowerup = true;
				}
			}
			else if (activatepowerup = true){

				dispower += 1;
				if (powertemp == 1){
					g.drawImage (powerup1,(int)luckyone.getX(),(int)luckyone.getY(), null);
				}
				if (powertemp == 2){
					g.drawImage (powerup2,(int)luckyone.getX(),(int)luckyone.getY(), null);
				}
				if (powertemp == 3){
					g.drawImage (powerup3,(int)luckyone.getX(),(int)luckyone.getY(), null);
				}
				if (powertemp == 4){
					g.drawImage (powerup4,(int)luckyone.getX(),(int)luckyone.getY(), null);
				}
				if (powertemp == 5){
					g.drawImage (powerup5,(int)luckyone.getX(),(int)luckyone.getY(), null);
				}
				if (powertemp == 6){
					g.drawImage (powerup6,(int)luckyone.getX(),(int)luckyone.getY(), null);
				}
				if (powertemp == 7){
					g.drawImage (powerup7,(int)luckyone.getX(),(int)luckyone.getY(), null);
				}
				Rectangle tempcheck = new Rectangle ((int)luckyone.getX(),(int)luckyone.getY(),40,40);
				if (sMan.getRect().intersects(tempcheck)){
					poweruptime = true;
					activatepowerup = false;
					dispower = 0;
				}
				// DISPOWER IS THE AMOUNT OF TIME the user has to get the power up before it dissappears :)
				if (dispower >= 200){
					activatepowerup = false;
					powertemp = (int)(Math.random()*6)+2;
					luckyone = new PowerUp (powertemp,Math.random()*getWidth(),Math.random()*getHeight());
					dispower = 0;
				}
			}

			//ENEMIES
			//different shape and colour for each
			g2.setStroke (new BasicStroke(2));

			for (Enemy each: enemies){
				//green squares
				if (each.getType().equals("square")){
					Color colour = (new Color(86,243,10));
					g.setColor(colour);
	    			g.drawPolygon(each.draw());
				}
				//grey hexagons
				else if (each.getType().equals("hexagon")){
					g.setColor(new Color(150, 150, 150));
					g.drawPolygon (each.draw());
				}
				//orange triangles
				else if (each.getType().equals("triangle")){
					g.setColor (new Color(250,150,0));
					g.drawPolygon (each.draw());
				}
				//blue diamonds
				else if (each.getType().equals("diamond")){
					g.setColor (Color.cyan);
					g.drawPolygon (each.draw());
				}
				//purple circles
				else if (each.getType().equals("circle")){
					Color colour = (new Color(200,0,255));
					g.setColor (colour);
					g.drawOval ((int)(each.getX()-(each.getSize()+10)/2),(int)(each.getY()-(each.getSize()+10)/2),(int)each.getSize()+10,(int)each.getSize()+10);
				}
			}
			g2.setStroke (new BasicStroke(1));


			//BULLETS
			g.setColor(Color.red); //every bullet is red
			for (int i=0; i<bullets.size(); i++){
				//piercing bullets flash shades of grey
				if (bulletpierce == true){
					g2.setStroke (new BasicStroke(1));
					int col = (int)(Math.random()*150+100);
					g.setColor (new Color(col,col,col));
				}
				//draw bullet
				g.drawPolygon(bullets.get(i).draw());
			}
			g2.setStroke (new BasicStroke (1));

			// MAIN PLAYER DIEING!
			if(deathtime==true){
				SurvivorManSimple.delay (100); //make it slower
				g.setColor (new Color (0,0,0,fadeout)); //fadeout screen
				g.fillRect (0,0,1000,750);
				fadeout += 7; //slowly fades out to black
				makeSparks(500, 350, "dying"); //fancy death sparks

				//reset everything
				if (fadeout >= 255){
					fadeout = 0;
					deathtime = false;
					sMan = new You();
					minx = -250; //reset position
					miny = -400;
					lives -= 1; //subtract lives

					//go to high scores if you run out of lives
					if (lives == -1){
						gameStat = "highscore";
						deathtime=true;
					}
				}
			}

			// bottom bar
			g.setClip (0,0,1000,750);
			g.setColor (new Color (0,0,0,150));
			g.fillRect (0,660,1000,85);
			g.setColor (new Color (170,255,0,100));
			g.drawLine (0,660,1000,660);
			g.setFont (gamefont);

			//scores and lives updates at the bottom
			g.setColor (new Color (170,255,0,160));
			g.drawString ("Score: " + score,10, 685);
			g.drawString ("Lives: " + lives,880,685);
			//bomb update at the bottom
			if (bombavailable == true){
				g.setColor (Color.red);
				g.drawString ("Bomb Available! (Use Spacebar)", 350,685);
			}

		}

		//HIGHSCORES
		else if (gameStat.equals("highscore")){
			//display high scores
			g.setColor (Color.black);
			g.fillRect (0,0,1000,750);
			g.setFont (highscorefont);
			g.setColor (Color.red);
			g.drawString ("Game Over",380,80);
			g.setColor (Color.white);
			g.drawString ("High Score", 380,150);
			// if score is too low, it doesn't DESERVE
			// to be on high score
			if (score > scoresort.first().getScore()){
				BNode temp = new BNode (name,score);
				scoresort.add (temp);
			}
			int counter = 0;
			g.setFont (gamefont);
			// draws each of the scores
			for (BNode each:scoresort.descendingSet()){
				if (each.toString().equals(name)){
					g.setColor (new Color ((int)(Math.random()*255),(int)(Math.random()*255),(int)(Math.random()*255)));
				}
				else{
					g.setColor (Color.white);
				}
				g.drawString (each.toString(), 140, 200+25*counter);
				g.drawString ("" + each.getScore(),750,200+25*counter);
				counter += 1;
				if (counter > 10){
					break;
				}
			}
			// this is creating the out file
			if (outfileactivate == true){
				PrintWriter outFile = null;
				try{
					outFile = new PrintWriter (new BufferedWriter (new FileWriter ("highscores.txt")));
				}
				catch(IOException ex){
					System.out.println ("can't open file, write protected?");
				}
				// makes the BNode treeset into descending order and writes
				// the program in that way
				for (BNode each:scoresort.descendingSet()){
					outFile.println (each.toString());
					outFile.println (each.getScore());
				}
				outFile.close ();
				outfileactivate = false;
			}
		}
	}
}

//MUSIC PLAYER CLASS
//plays background music
class musicplayer{
	protected AudioStream as;

	protected String[] musicFiles;

	public musicplayer(String n) throws IOException{

 		playAudio(n);
	}
	public void playAudio(String musicname) throws IOException{
		InputStream in = new FileInputStream(musicname);
		as = new AudioStream(in);
		AudioPlayer.player.start(as);
	}
	public void stop(){
		AudioPlayer.player.stop(as);
	}
}
