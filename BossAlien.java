package finalscsc142;

import java.awt.Color;
import java.awt.Point;

import uwcse.graphics.GWindow;
import uwcse.graphics.Line;
import uwcse.graphics.Oval;
import uwcse.graphics.Rectangle;
import uwcse.graphics.Shape;

/*
 * 	A Boss Alien that has a lot of health and drops a healthpack when dies
 */
public class BossAlien extends MovingObject {

	public static final int BOSS_HEIGHT = 30;
	public static final int BOSS_WIDTH = 30;
	public static final int BOSS_RADIUS = 20;
	public static final Color[] colors = {Color.RED, Color.GREEN, Color.BLUE,Color.CYAN,Color.YELLOW};
	
	private int deltaX; // left and right motion
	private int Y = 100; // y coordinate fixed at 100
	
	// made a boss alien lives, increases by 5 each level
	private int bossLives;
	
	//add part
	private MedicKit medKit;
	private SpaceShip spaceShip;

	
	public BossAlien(GWindow window, Point center) {
		super(window, center);
		this.deltaX = 5; // x speed at 5
		revive(); 
		this.draw(); // draws boss alien
	}	

	// This method resets the boss alien lives and increases by 5 each level
	public void revive() {
		this.bossLives = SpaceInvader.level * 5;
	}

	public void dead() {
		this.bossLives = 0;
	}
	
	// Drops medic kit that refills ship lives back to 5 when picked up by the space ship
	protected void dropMedicKit(SpaceShip spaceShip) {
        if (window != null && center != null) {
            medKit = new MedicKit(window, center);
            //if the medic kit and ship collides
            if (medKit.getBoundingBox().intersects(spaceShip.getBoundingBox())) {
                spaceShip.shipLives = 5;
                medKit.erase();                
            }
        }
    }

	// when the boss Alien is shot, it reduce the boss Alien's lives
	public void isShot() {
		bossLives --;
		if(bossLives == 0 ) {
			erase();
	    }
		
	}
	
	// return the boss Alien lives
	public int getLives() {
		return bossLives;
	}
	
	// check the boss lives remaining is zero
	public boolean isDead() {
		return this.bossLives == 0;
	}
	
	// get location of the boss alien
	public Point getLocation() {
		return this.center;
	}
	
	public void move() {
		int newY = 100;
        center = new Point(center.x, newY);
        // Update the x-coordinate of the center point to move the alien randomly
        int newX = center.x + deltaX;
        center = new Point(newX, Y);
		shapes[0].moveBy(deltaX, Y);
	    shapes[1].moveBy(deltaX, Y);
	    shapes[2].moveBy(deltaX, Y);
		// Is the new position in the window?

        if (newX <= 0 || newX >= window.getWindowWidth()) {
	         // Switch direction by negating the deltaX
	         deltaX = -deltaX;}
		// Show the new location of this SpaceShip
		this.erase();
		if(bossLives > 0) {
			this.draw();
		}
	}

	

	protected void draw() {
		
		// the 3 shape draw the appearance of Alien , and the other is drawing the stamina bar
		this.shapes = new Shape[3 + bossLives];
		this.shapes[0] = new Line(this.center.x - 2 * BOSS_RADIUS, this.center.y - 2 * BOSS_RADIUS, this.center.x + 2 * BOSS_RADIUS,
				this.center.y + 2 * BOSS_RADIUS, Color.RED);
		this.shapes[1] = new Line(this.center.x + 2 * BOSS_RADIUS, this.center.y - 2 * BOSS_RADIUS, this.center.x - 2 * BOSS_RADIUS,
				this.center.y + 2 * BOSS_RADIUS, Color.RED);
		this.shapes[2] = new Oval(this.center.x - BOSS_RADIUS, this.center.y - BOSS_RADIUS, 2 * BOSS_RADIUS, 2 * BOSS_RADIUS, Color.YELLOW, true);
		
		for(int i = 3; i< this.shapes.length; i++) { 
			shapes[i] = new Rectangle(this.center.x - 2* BOSS_RADIUS + (i-3)*(4*BOSS_RADIUS)/(SpaceInvader.level * 5),
					// health bar
					// first bar starts at left and next bar should start at end of the first bar so that add the width of the bar
					this.center.y - 2 * BOSS_RADIUS - 20,       // y initial position
					(4*BOSS_RADIUS)/(SpaceInvader.level * 5) , // width is the total width divide level*5
					10,  								// height = 10
					Color.RED, true);
		}									

		// add the shapes in the window
		for (int i = 0; i < this.shapes.length; i++)
			this.window.add(this.shapes[i]);
		
	// Bounding box of this Alien
		this.boundingBox = new Rectangle(this.center.x - 2*BOSS_RADIUS, this.center.y - 2 *BOSS_RADIUS, 2* BOSS_RADIUS,2*BOSS_RADIUS);
		this.window.doRepaint();
	}

	

	

}
