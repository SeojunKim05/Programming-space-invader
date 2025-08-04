package finalscsc142;


//Write your compliance statement here:
//What are your 4 extra features?
//How is your new alien different from the one described by the Alien class?
//
import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JOptionPane;

import uwcse.graphics.GWindow;
import uwcse.graphics.GWindowEvent;
import uwcse.graphics.GWindowEventAdapter;
import uwcse.graphics.Oval;
import uwcse.graphics.Rectangle;
import uwcse.graphics.TextShape;

/**
* A SpaceInvader displays a fleet of alien ships and a space ship. The player
* directs the moves of the spaceship and can shoot at the aliens.
*/

public class SpaceInvader extends GWindowEventAdapter {
	// Possible actions from the keyboard
	/** No action */
	public static final int DO_NOTHING = 0;

	/** Steer the space ship */
	public static final int SET_SPACESHIP_DIRECTION = 1;

	/** To shoot at the aliens */
	public static final int SHOOT = 2;
	
	

	// Period of the animation (in ms)
	// (the smaller the value, the faster the animation)
	private int animationPeriod = 100;

	// Current action from the keyboard
	private int action;

	// Game window
	private GWindow window;
	private String extraPoint = "";
	// The space ship
	private SpaceShip spaceShip;

	// Direction of motion given by the player
	private int dirFromKeyboard = MovingObject.LEFT;

	// The aliens
	private ArrayList<Alien> aliens;
	
	private BossAlien bossAlien;

	//add part
	private MedicKit medKit;
	
	// Is the current game over?
	//private String messageGameOver = "";
	
	
	// the constant for score
	private int initialScore;
	// to calculate the score
	private int counter= 0;
	private int countTypeSpace = 0;
	
	private TextShape leftLife;
	private TextShape currentLevel;
	
	// measure the total Alien lives at beginning
	private int totalAlienLives = 0;
	
	public static int level;
	private int spawnPerLevel;
	private int totalScore;
	private int currentGameScore;
	private Point finalBossPoint;

	/**
	 * Constructs a space invader game
	 */
	public SpaceInvader() {
		this.window = new GWindow("Space invaders", 500, 500);
		this.window.setExitOnClose();
		this.window.addEventHandler(this); // this SpaceInvader handles all of
		// the events fired by the graphics
		// window
		SpaceInvader.level = 1;
		this.spawnPerLevel = 15;

		// Display the game rules
		// we made space ship 
		String rulesOfTheGame = "Start Level : " +level + "\nSave the Earth! Destroy all of the aliens ships and the Boss Alien!\n" 
				+ "To move left, press 'a'.\n"
				+ "To move right, press 'd'.\nTo move up, press 'w'.\nTo move down, press 's'.\n"
				+ "To shoot, press the space bar.\n" + "To quit, press 'Q'."
				+ "\n (You can get extra score when you play very well)"
				+ "\n (You can recovery life when you destroy the Boss Alien)";
		JOptionPane.showMessageDialog(null, rulesOfTheGame, "Space invaders", JOptionPane.INFORMATION_MESSAGE);
		
		
		this.initializeGame();
		
		
	}

	/**
	 * Initializes the game (draw the background, aliens, and space ship)
	 */
	private void initializeGame() {
		// Clear the window
		this.window.erase();

		// Background (starry universe)
		Rectangle background = new Rectangle(0, 0, this.window.getWindowWidth(), this.window.getWindowHeight(),
				Color.black, true);
		this.window.add(background);	
		
		// Add 50 stars here and there (as small circles)
		Random rnd = new Random();
		for (int i = 0; i < 50; i++) {
			// Random radius between 1 and 3
			int radius = rnd.nextInt(3) + 1;
			// Random location (within the window)
			// Make sure that the full circle is visible in the window
			int x = rnd.nextInt(this.window.getWindowWidth() - 2 * radius);
			int y = rnd.nextInt(this.window.getWindowHeight() - 2 * radius);
			this.window.add(new Oval(x, y, 2 * radius, 2 * radius, Color.WHITE, true));
		}

		// ArrayList of aliens
		this.aliens = new ArrayList<Alien>();

		// Create the aliens as much spawnPerLevel
		// Initial location of the aliens
		// (Make sure that the space ship can fire at them)
		int x = 2 * SpaceShip.WIDTH;
		int y1 = 10 * Alien.RADIUS;
		int y2 = 50 * Alien.RADIUS;
		int y;
		
		// create Alien
		totalAlienLives = 0;
		countTypeSpace = 0;
		// create alien depends on level
		for (int i = 0; i < spawnPerLevel; i++) {
			y = y1 + (int) (Math.random() * (y2-y1));
			this.aliens.add(new Alien(this.window, new Point(x, y)));
			x += 6 * Alien.RADIUS;
		}
		// add the total alien lives 
		for(int a = 0 ; a < aliens.size() ; a ++) {
			totalAlienLives += aliens.get(a).getLives();		
		}				
		// add the boss Alien lives according to the level
		totalAlienLives += level * 5;
		
		// Create the space ship at the bottom of the window
		x = this.window.getWindowWidth() / 2;
		y = this.window.getWindowHeight() - SpaceShip.HEIGHT / 2;
		this.spaceShip = new SpaceShip(this.window, new Point(x, y));
		
		
		
		// create the Boss Alien
		int xBoss = this.window.getWindowWidth() / 2;
		int yBoss = this.window.getWindowHeight() / 5;
		this.bossAlien = new BossAlien(this.window, new Point(xBoss, yBoss));
		
		

		// the textShape position, assign the number -> get the position of text Shape
		int rightX = this.window.getWindowWidth() - 100;
		int rightY = 20;
		// present the left lives
		String content = "Left Lives : "; 
		String contentForLevel = "Current Level : "; // displays the current level
		leftLife = new TextShape(content + spaceShip.shipLives, rightX, rightY, Color.YELLOW);
		this.window.add(leftLife);
		currentLevel = new TextShape(contentForLevel + level, rightX - 20, rightY + 30, Color.YELLOW);
		this.window.add(currentLevel);
		
		medKit = new MedicKit(window, new Point(this.window.getWindowWidth() / 2, -50));
		
		// start timer events
		this.window.startTimerEvents(this.animationPeriod);
	}

	/**
	 * Moves the objects within the graphics window every time the timer fires an
	 * event
	 */
	public void timerExpired(GWindowEvent we) {
		// moving object
		if(bossAlien.isDead()) {
			finalBossPoint = bossAlien.getLocation();
		}
		moveAll();
		// Perform the action requested by the user?
		switch (this.action) {
		case SpaceInvader.SET_SPACESHIP_DIRECTION:
			this.spaceShip.setDirection(this.dirFromKeyboard);
			break;
		case SpaceInvader.SHOOT:
			this.spaceShip.shoot(this.aliens);
			if(bossAlien.getLives() > 0) {
				this.spaceShip.shootBoss(this.bossAlien);
			}
			break;
		}
		// use count ++
		counter ++;
		
		this.action = SpaceInvader.DO_NOTHING; // Don't do the same action
		// twice

		// Show the new locations of the objects
		this.updateGame();
	}

	/**
	 * Selects the action requested by the pressed key
	 */
	public void keyPressed(GWindowEvent e) {
		// Don't perform the actions (such as shoot) directly in this method.
		// Do the actions in timerExpired, so that the alien ArrayList can't be
		// modified at the same time by two methods (keyPressed and timerExpired
		// run in different threads).

		switch (Character.toLowerCase(e.getKey())) // not case sensitive
		{
		// Put here the code to move the space ship with the < and > keys
		
		case 'a': // move the space ship to the left
			this.action = SpaceInvader.SET_SPACESHIP_DIRECTION;
			dirFromKeyboard = MovingObject.LEFT;
			break;
			
		case 'd': // move the space ship to the right
			this.action = SpaceInvader.SET_SPACESHIP_DIRECTION;
			dirFromKeyboard = MovingObject.RIGHT;
			break;
			
		case 'w': // move the space ship to the up
			this.action = SpaceInvader.SET_SPACESHIP_DIRECTION;
			dirFromKeyboard = MovingObject.UP;
			break;
		case 's': // move the space ship to the down
			this.action = SpaceInvader.SET_SPACESHIP_DIRECTION;
			
			dirFromKeyboard = MovingObject.DOWN;
			break;

			
		
		case ' ': // shoot at the aliens
			this.action = SpaceInvader.SHOOT;
			countTypeSpace ++;
			break;

		case 'q': // quit the game
			System.exit(0);

		default: // no new action
			this.action = SpaceInvader.DO_NOTHING;
			break;
		}
	}

	/**
	 * Updates the game (Move aliens + space ship)
	 */
	private void updateGame() {
		// Is the game won (or lost)?
		if(spaceShip.shipLives == 0) {
			if(anotherGame("Failed! \nFailed level : "+ (level))) {
				initializeGame();	
				counter = 0;
			} else {
				System.exit(0);
			}
		}
		
		if(aliens.size() == 0 && bossAlien.isDead()) {
			initialScore = totalAlienLives * 500;
			
			currentGameScore =  initialScore - counter - 10 * countTypeSpace + 1000*spaceShip.shipLives;
			
			if(currentGameScore <= 0) {
				currentGameScore = 0;
			}
			
			// if player  shot the Alien very well they got extra score.
			if(countTypeSpace <= this.totalAlienLives * 2) {
				currentGameScore += totalAlienLives * 500;
				extraPoint = "\nYou gained the extra score!";
				
			}
			totalScore += currentGameScore;
			if(anotherGame("Congratulations, you clear the level : "+ (level ) +"! \nYour left lives : "+ spaceShip.shipLives
					+ extraPoint 
					+"\nYou gain " + currentGameScore +" score at this level."
					+ "\nYour total score : "+ totalScore +" point. "
					+ "\nYour scores continue to accumulate." )) {
				// increases level
				level++;
				// increases the number of aliens by 3 each level
				spawnPerLevel = spawnPerLevel + 3;
				// respawns the boss alien
				bossAlien.revive();
				initializeGame();
				// revert counter back to 0
				counter = 0;
				// revert extra points to 0
				extraPoint = "";
				
				
			} else {
				System.exit(0);
			}
		}
		// Put here code to end the game (= no more aliens)

		this.window.suspendRepaints(); // to speed up the drawing

		// deduct a life of both space ship and alien when collides
		spaceShip.handleCollision(aliens);
		// deduct ship lives when the boss is alive and collides
		if(bossAlien.getLives()>0) {
			spaceShip.handleCollisionBossAlien(bossAlien);
		}
		
		// collision with the medic kit
		spaceShip.medicKitCollision(medKit);
		// update the left lives
		leftLife.setText("Left lives : " + spaceShip.shipLives);

		// Displays
		this.window.resumeRepaints();
	}

	// move all the animations
	public void moveAll() {
		// Move the aliens
		for (Alien a : aliens) {
			a.move();
		}

		// Move the space ship
		this.spaceShip.move();				
		// moves boss alien and erases when it is dead
		if(bossAlien.getLives() > 0) {
			this.bossAlien.move();
		} else if(bossAlien.isDead()) {              
			bossAlien.erase();
		}
		//moves medic kit when the boss alien is dead and medic kit isn't consumed
		if(bossAlien.getLives()<=0) {
			if(medKit.getMedLife()>=1) {
				if(medKit.getLocation().y <= window.getWindowHeight()) {
					this.medKit.move();
				}
			}
		}
	}
	/**
	 * Does the player want to play again?
	 */
	public boolean anotherGame(String s) {
		// this method is useful at the end of a game if you want to prompt the
		// user
		// for another game (s would be a String describing the outcome of the
		// game
		// that just ended, e.g. "Congratulations, you saved the Earth!")
		if(aliens.size() == 0 && bossAlien.isDead()) {
			int choice = JOptionPane.showConfirmDialog(null, s + "\nDo you want to play the next Level?", "Game over",
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

			return (choice == JOptionPane.YES_OPTION);
		} else {
			int choice = JOptionPane.showConfirmDialog(null, s + "\nDo you want to play this Level again?", "Game over",
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			

			return (choice == JOptionPane.YES_OPTION);
		}
	}

	/**
	 * Starts the application
	 */
	public static void main(String[] args) {
		new SpaceInvader();
	}
}
