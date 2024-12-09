package game;

import java.awt.Color;
import java.awt.Graphics;

/**
 * Represents a food object in the game.
 * Foods can be rendered and positioned within the game canvas.
 */
public class Food extends Polygon {
	private static final int FOOD_SIZE = 10; // Size of the food
	private int size;

	/**
     * Constructs a food object with custom points, position, and rotation.
     * @param points The array of points defining the shape of the food.
     * @param position The position of the food.
     * @param rotation The rotation angle of the food.
     */
	public Food(Point[] points, Point position, double rotation) {
		super(points, position, rotation);
	}

	/**
     * Constructs a food object with a default rectangular shape.
     * @param position The position of the food.
     * @param size The size of the food.
     */
	public Food(Point position, int size) {
		super(new Point[] { new Point(0, 0), new Point(size, 0), new Point(size, size), new Point(0, size) }, position,
				0);
		this.size = size;
	}

	/**
     * Renders the food on the graphics context.
     * @param brush The graphics context to render onto.
     */
	public void paint(Graphics brush) {
		brush.setColor(Color.yellow);
		// Get the array of points for the food
		Point[] foodPoints = this.getPoints();
		// Convert foodPoints into arrays of x and y coordinates
		int[] xPoints = new int[foodPoints.length];
		int[] yPoints = new int[foodPoints.length];
		for (int i = 0; i < foodPoints.length; i++) {
			xPoints[i] = (int) foodPoints[i].getX();
			yPoints[i] = (int) foodPoints[i].getY();
		}
		// Draw the food on the canvas
		brush.fillOval((int) position.getX(), (int) position.getY(), FOOD_SIZE, FOOD_SIZE);
	}

	/**
     * Sets the position of the food.
     * @param newPosition The new position of the food.
     */
	public void setPosition(Point newPosition) {
		this.position = newPosition;
	}
}