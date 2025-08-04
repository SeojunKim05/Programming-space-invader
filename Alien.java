package finalscsc142;

import java.awt.Color;
import java.awt.Point;

import uwcse.graphics.GWindow;
import uwcse.graphics.Line;
import uwcse.graphics.Oval;
import uwcse.graphics.Rectangle;
import uwcse.graphics.Shape;

/**
 * The representation and display of an Alien
 */

public class Alien extends MovingObject {
	// Size of an Alien
	public static final int RADIUS = 5;
	
	// possible colors of the alien
	public static final Color[] colors = {Color.RED, Color.GREEN, Color.BLUE};

	// Number of lives in this Alien
	// When 0, this Alien is dead
	public int lives;
	
	
	private int deltaX;
	private int deltaY;

	/**
	 * Creates an alien in the graphics window
	 * 
	 * @param window the GWindow this Alien belongs to
	 * @param center the center Point of this Alien
	 */
	public Alien(GWindow window, Point center) {
		super(window, center);
		this.lives = (int) (Math.random() * 3 + 1);
		this.deltaX = getRandomDeltaX(); // Get initial random movement in x-direction
        this.deltaY = getRandomDeltaY(); // Get initial random movement in y-direction

		// Display this Alien
		this.draw();
	}


	/**
	 * The alien is being shot. Decrement its number of lives and erase it from the
	 * graphics window if it is dead.
	 */
	public void isShot() {
		lives --;
		if(lives == 0 ) {
			erase();
		}
	}

	public int getLives() {
		return lives;
	}
	/**
	 * Is this Alien dead?
	 */
	public boolean isDead() {
		return this.lives == 0;
	}

	/**
	 * Returns the location of this Alien
	 */
	public Point getLocation() {
		return this.center;
	}

	/**
	 * Moves this Alien As a start make all of the aliens move downward. If an alien
	 * reaches the bottom of the screen, it reappears at the top.
	 */
	public void move() {
		// Update the y-coordinate of the center point to move the alien down
        int newY = center.y + deltaY;
        center = new Point(center.x, newY);

        // Update the x-coordinate of the center point to move the alien randomly to the sides
        int newX = center.x + deltaX;
        center = new Point(newX, center.y);
		
		// move the individual shapes
        shapes[0].moveBy(deltaX, deltaY);
        shapes[1].moveBy(deltaX, deltaY);
        shapes[2].moveBy(deltaX, deltaY);
		
		// if the alien is past the bottom of the window, move back to the top
		if (newY >= window.getWindowHeight() + shapes[2].getHeight() / 2) {
			center.y = 10 * Alien.RADIUS;
			newX = (int) (Math.random() * (window.getWindowWidth() - shapes[2].getWidth()) + shapes[2].getWidth() / 2);
            center = new Point(newX, -shapes[2].getHeight() / 2);
            deltaX = getRandomDeltaX(); // Get new random movement in x-direction
            deltaY = getRandomDeltaY(); // Get new random movement in y-direction
		}
		
		// check if the alien has reached the edge of the window
        if (newX <= 0 || newX >= window.getWindowWidth()) {
            // switch direction
            deltaX = -deltaX;}
		this.erase();
		this.draw();

	}
	// returns random delta x for random speed of x value side ways
    private int getRandomDeltaX() {
        // generate a random number between -1 and 1 
        return (int) ((Math.random() * 5) - 1) * (SpaceInvader.level / 3 + 1);
    }
    
    // returns random delta y for random speed of y value downwards
    private int getRandomDeltaY() {
        // generate a random number between 1 and 5 
        return (int) ((Math.random() * 7) + 1) * (SpaceInvader.level / 3 + 1);
    }

	/**
	 * Displays this Alien in the graphics window
	 */
	protected void draw() {
		if(lives == 0) {
			return;
		}
		
		// pick the color (according to the number of lives left)
		Color color = colors[lives - 1];

		// Graphics elements for the display of this Alien
		// A circle on top of an X
		this.shapes = new Shape[3];
		this.shapes[0] = new Line(this.center.x - 2 * RADIUS, this.center.y - 2 * RADIUS, this.center.x + 2 * RADIUS,
				this.center.y + 2 * RADIUS, color);
		this.shapes[1] = new Line(this.center.x + 2 * RADIUS, this.center.y - 2 * RADIUS, this.center.x - 2 * RADIUS,
				this.center.y + 2 * RADIUS, color);
		this.shapes[2] = new Oval(this.center.x - RADIUS, this.center.y - RADIUS, 2 * RADIUS, 2 * RADIUS, color, true);
			

		for (int i = 0; i < this.shapes.length; i++)
			this.window.add(this.shapes[i]);
		
		// Bounding box of this Alien
		this.boundingBox = new Rectangle(this.center.x - 2 * RADIUS, this.center.y - 2 * RADIUS, 4 * RADIUS,
				4 * RADIUS);

		this.window.doRepaint();
	}
}
