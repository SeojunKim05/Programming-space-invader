package finalscsc142;


import java.awt.Color;
import java.awt.Point;

import uwcse.graphics.GWindow;
import uwcse.graphics.Line;
import uwcse.graphics.Oval;
import uwcse.graphics.Rectangle;
import uwcse.graphics.Shape;

public class MedicKit extends MovingObject{
	
	public static final int BOSS_RADIUS = 15;
	//movement of Y value
	private int deltaY;
	//lives remaining of the medic kit
	public static int medLife;
	
	/*
	 *  Dropped medic kit when the boss dies, it can be picked up once and refills the ship's lives back to 5
	 *  Moves straight down
	 */
	
	public MedicKit(GWindow window, Point center) { 
		super(window, center);
		// TODO Auto-generated constructor stub
		// available for one time collision only
		this.medLife = 1;
		// y value speed
		this.deltaY = 5;
		draw();
	}
	
	public void move() {
		// Moves straight down
		int newY = center.y + deltaY;
		center = new Point(center.x, newY);
		// System.out.println(medLife);
		shapes[0].moveBy(0, deltaY);
        shapes[1].moveBy(0, deltaY);
        shapes[2].moveBy(0, deltaY);
        
        this.erase();
        this.draw();
	}
	
	// returns the lives remaining of the kit
	public int getMedLife() {
		return this.medLife;
	}
	
	// checks if it is dead
	public boolean isDead() {
		return this.medLife == 0;
	}
	
	// graphics of the medic kit
	protected void draw() {
		this.shapes = new Shape[3];
		this.shapes[0] = new Line(this.center.x - 2 * BOSS_RADIUS, this.center.y - 2 * BOSS_RADIUS, this.center.x + 2 * BOSS_RADIUS,
				this.center.y + 2 * BOSS_RADIUS, Color.GREEN);
		this.shapes[1] = new Line(this.center.x + 2 * BOSS_RADIUS, this.center.y - 2 * BOSS_RADIUS, this.center.x - 2 * BOSS_RADIUS,
				this.center.y + 2 * BOSS_RADIUS, Color.GREEN);
		this.shapes[2] = new Oval(this.center.x - BOSS_RADIUS, this.center.y - BOSS_RADIUS, 2 * BOSS_RADIUS, 2 * BOSS_RADIUS, Color.GREEN, true);
		for (int i = 0; i < this.shapes.length; i++)
			this.window.add(this.shapes[i]);
		
	// Bounding box of this medic kit
		this.boundingBox = new Rectangle(this.center.x - 2*BOSS_RADIUS, this.center.y - 2 *BOSS_RADIUS, 2* BOSS_RADIUS,2*BOSS_RADIUS);
		
		
		this.window.doRepaint();
	}

    

}
