/* ----------------------------------------------------------------------------------
 * BaseFrame is designed to take a lot of the complexity out of doing graphics in Java.
 * The idea is, that rather inhetiting from JFrame directly we place a lot of common 
 * code in BaseFrame then inherit from BaseFrame. BaseFrame includes:
 *
 *	- Double Buffering - update() creates a buffer, calls paint to draw on that buffer
 *						 and copies the buffer on screen. 
 * 	- MouseMotionListener, MouseListener, KeyListener
 *	- getKeys() - returns a boolean array where each spot in the list is the ASCII value
 *				  of the keys, true if down, false if up.
 *	- getMouse()- returns an int array [mouseX, mouseY]
 *	- getButton()- returns an int that represents current button status.
 *	mx,my,mb,keys- these fields are protected so you have direct access to them from the
 *				   inherited class.
 -------------------------------------------------------------------------------------*/


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

class BaseFrame extends JFrame implements MouseMotionListener, MouseListener, KeyListener{
	protected int mx,my,mb;
	protected boolean []keys;
	protected Image dbImage;
	protected Graphics dbg;
	
	final protected int LEFT = 37;
	final protected int UP = 38;
	final protected int RIGHT = 39;
	final protected int DOWN = 40;
	final protected int SPACE = 32;
	final protected int ESC = 27;
		
    public BaseFrame() {
		super("Basic Frame");

		addKeyListener(this);
		addMouseListener (this);
		addMouseMotionListener(this);
		keys = new boolean[2000];
		
		setSize(1000,700);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
    }
	public int []getMouse(){
		int []pos=new int[2];
		pos[0]=mx;
		pos[1]=my;
		return pos;
	}
	public int getButton(){
		return mb;
	}

	public boolean []getKeys(){
		return keys;
	}

	private void updateMouse(MouseEvent e){
		mx = e.getX();
		my = e.getY();		
	}   

    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}

    public void mouseReleased(MouseEvent e) {
     	updateMouse(e);     	
     	mb = 0;
    }    
    public void mouseClicked(MouseEvent e){
    	updateMouse(e);
    	mb = 0;
    }    
    public void mouseDragged(MouseEvent e){
    	updateMouse(e);
	}
    public void mouseMoved(MouseEvent e){
    	updateMouse(e);
	}
    public void mousePressed(MouseEvent e){
    	updateMouse(e);
    	mb = e.getButton();
	}
	
    public void keyTyped(KeyEvent e) {}

    public void keyPressed(KeyEvent e) {
        keys[e.getKeyCode()] = true;
    }

    public void keyReleased(KeyEvent e) {
        keys[e.getKeyCode()] = false;
    }
    
  	public void update(){
		Graphics g = getGraphics();
		if(dbImage == null){
			dbImage = createImage(getWidth(), getHeight());
			dbg = dbImage.getGraphics();
		}
		paint(dbg);
		g.drawImage(dbImage,0,0,null);
	}

}
