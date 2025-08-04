package finalscsc142;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;

import uwcse.graphics.GWindow;
import uwcse.graphics.Line;
import uwcse.graphics.Rectangle;
import uwcse.graphics.Shape;
import uwcse.graphics.Triangle;

/**
 * The space ship
 */
public class SpaceShip extends MovingObject {
	/** Height of a space ship */
	public static final int HEIGHT = 40;

	/** Width of a space ship */
	public static final int WIDTH = 20;
	
	public static final int INITIAL_LIFE = 5;

	/** Is the space ship shooting with its laser? */
	private boolean isShooting;

	// ship lives start 5
	public int shipLives;
	
	//add part
	private MedicKit medKit;

	/**
	 * Constructs this SpaceShip
	 */
	public SpaceShip(GWindow window, Point center) {
		super(window, center);
		this.direction = MovingObject.LEFT;
		// give 5 life
		shipLives = INITIAL_LIFE;
		// Draw this SpaceShip
		this.draw();
	}

	/**
	 * Moves this SpaceShip. The space ship should be constantly moving. Select a
	 * new direction if the space ship can't move in the current direction of
	 * motion.
	 */
	public void move() {

		// A space ship moves left or right
		if (this.direction != MovingObject.LEFT && this.direction != MovingObject.RIGHT
				&& this.direction != MovingObject.UP && this.direction != MovingObject.DOWN) {
			throw new IllegalArgumentException("Invalid space ship direction");
		}

		// move this SpaceShip
		boolean isMoveOK = false;
		// Distance covered by the space ship in one step
		int step = this.boundingBox.getWidth() / 2;

		do {
			int x = this.center.x;
			int y = this.center.y;
			switch (this.direction) {
			case LEFT:
				x -= step;
				break;
				
			case RIGHT:
				x += step;
				break;

			// move the spaceship up and down
			case UP:
				y -= step;
				break;
				
			case DOWN:
				y += step;
				break;
			}

			// Is the new position in the window?
			if (x + this.boundingBox.getWidth() / 2 >= this.window.getWindowWidth())
			// past the right edge
			{
				isMoveOK = false;
				this.direction = MovingObject.LEFT;
			} else if (x - this.boundingBox.getWidth() / 2 <= 0) // past the
			// left edge
			{
				isMoveOK = false;
				this.direction = MovingObject.RIGHT;
			} else if (y + this.boundingBox.getWidth() / 2 >= this.window.getWindowHeight()) {
				isMoveOK = false;
				this.direction = MovingObject.UP;
			} else if (y - this.boundingBox.getHeight() / 2 <= 0) {
				isMoveOK = false;
				this.direction = MovingObject.DOWN;
			} else // it is in the window
			{
				if (this.direction == MovingObject.RIGHT || this.direction == MovingObject.LEFT) {
					isMoveOK = true;
					this.center.x = x;
				} else {
					isMoveOK = true;
					this.center.y = y;
				}

			}

		} while (!isMoveOK);

		// Show the new location of this SpaceShip
		this.erase();
		this.draw();
	}

	/**
	 * Shoots at the aliens If an alien is hit, decrease its number of lives or
	 * remove it from the array list if it is dead.
	 * 
	 * @param aliens the ArrayList of aliens
	 */
	public void shoot(ArrayList<Alien> aliens) {
		this.isShooting = true;

		for (int i = aliens.size() - 1; i >= 0; i--) {
			Alien a = aliens.get(i);
			int xLeft = a.getBoundingBox().getX();
			int xRight = a.getBoundingBox().getX() + a.getBoundingBox().getWidth();
			
			// add yTop variable. Now spaceShip can move upward or downward so that if the Alien is under the space ship then it is not been hit.
			int yTop = a.getBoundingBox().getY();
			if (xLeft <= center.x && center.x <= xRight && center.y > yTop) { // now we can move space ship up and down, we have to consider y component
				// the alien is hit
				a.isShot();
				if (a.isDead()) {
					aliens.remove(i);
				}
			}
		}

		
	}
	/**
	 * Shoots at the boss alien and if it is hit, decrease its number of lives 
	 * remove when boss alien has 0 lives left and is dead
	 * 
	 * @param bossAlien
	 */
	public void shootBoss(BossAlien bossAlien) {
		// when shooting
		this.isShooting = true;
		BossAlien b = bossAlien;
		// get bounding Box position
		int xBossLeft = b.getBoundingBox().getX();
		int xBossRight = b.getBoundingBox().getX() + b.getBoundingBox().getWidth();
		int yBossTop = b.getBoundingBox().getY();
		// when space ship is shooting, if the x position of space ship interact the bounding box of Boss alien
		if(xBossLeft <= center.x && center.x <= xBossRight && center.y > yBossTop) {    // when the space ship is over than object, it doesn't work
			b.isShot();		// deducts a life of boss alien
			if(b.getLives()==0) { 	// when dead, erase from the window
				bossAlien.erase();
			}
		}
		
	}
	/*
	 * when an alien collides with the space ship, the lives of both alien and the space ship
	 * deducts. 
	 * 
	 * if the alien had 3 remaining lives, it will deduct three lives from the space ship and the alien dies
	 * 
	 * if the alien is dead from the collision and has no more lives left, removes alien
	 */
	public void handleCollision(ArrayList<Alien> aliens) {

        for (Alien alien : aliens) {
            if (alien.getBoundingBox().intersects(this.boundingBox)) {
                shipLives--; // Deduct life from spaceship
                alien.isShot(); // Deduct life from alien
                if (alien.isDead()) {
                    aliens.remove(alien); // Remove dead alien from the list
                }
                break; // Exit the loop after handling the collision
            }
        }
    }
	/* collision with boss alien when alive. the collision with boss alien deducts lives of
	 * the space ship only
	 * 
	 */
	public void handleCollisionBossAlien(BossAlien bossAlien) {
		if (bossAlien.getBoundingBox().intersects(this.boundingBox)) {
			this.shipLives--;	// ship lives deduction
		}
	}
	
	/*
	 * collision with the medic kit and reverts the space ship lives back to 5 when collided
	 */
	public void medicKitCollision(MedicKit medicKit) {
		if (medicKit.getBoundingBox().intersects(this.boundingBox)) {
			this.shipLives = 5;		// ship lives restore
			MedicKit.medLife --;	// removes the medic kit from the window after a collision
			medicKit.erase();
		}
	}

	/**
	 * Draws this SpaceShip in the graphics window
	 */
	protected void draw() {
		this.shapes = new Shape[5];

		// Body of the space ship
		Rectangle body = new Rectangle(this.center.x - SpaceShip.WIDTH / 2, this.center.y - SpaceShip.HEIGHT / 2,
				SpaceShip.WIDTH, SpaceShip.HEIGHT, Color.cyan, true);

		this.shapes[0] = body;

		// Cone on top
		int x1 = body.getX();
		int y1 = body.getY();
		int x2 = x1 + body.getWidth();
		int y2 = y1;
		int x3 = body.getCenterX();
		int y3 = y1 - body.getWidth();
		this.shapes[1] = new Triangle(x1, y1, x2, y2, x3, y3, Color.pink, true);
		// Show the laser beam if the rocket is shooting
		if (this.isShooting) {
			this.shapes[4] = new Line(x3, y3, x3, 0, Color.yellow);
			this.isShooting = false;
		}

		// Wings on the sides
		x1 = body.getX();
		y1 = body.getY() + body.getHeight();
		x2 = body.getX() - body.getWidth() / 2;
		y2 = y1;
		x3 = x1;
		y3 = y1 - body.getWidth() / 2;
		this.shapes[2] = new Triangle(x1, y1, x2, y2, x3, y3, Color.red, true);
		x1 = body.getX() + body.getWidth();
		x2 = x1 + body.getWidth() / 2;
		x3 = x1;
		this.shapes[3] = new Triangle(x1, y1, x2, y2, x3, y3, Color.red, true);

		// The bounding box of this SpaceShip
		this.boundingBox = this.shapes[0].getBoundingBox();

		// Put everything in the window
		for (int i = 0; i < this.shapes.length; i++)
			if (this.shapes[i] != null)
				this.window.add(this.shapes[i]);

		this.window.doRepaint();
	}
}
