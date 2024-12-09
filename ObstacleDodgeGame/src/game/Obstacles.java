package game;

import java.awt.Color;
import java.awt.Graphics;

/**
 * Represents an obstacle object in the game.
 * Obstacles can be moved and rendered within the game canvas.
 */
public class Obstacles extends Polygon {
	private static final int OBSTACLE_SIZE = 20;
	private double velocityX;
	private double velocityY;
	private int radius;

	/**
     * Constructs an obstacle object with custom points, position, rotation, velocity, and radius.
     * @param points The array of points defining the shape of the obstacle.
     * @param position The position of the obstacle.
     * @param rotation The rotation angle of the obstacle.
     * @param velocityX The horizontal velocity of the obstacle.
     * @param velocityY The vertical velocity of the obstacle.
     * @param radius The radius of the obstacle.
     */
	public Obstacles(Point[] points, Point position, double rotation, double velocityX, double velocityY, int radius) {
		super(points, position, rotation);
		this.velocityX = velocityX;
		this.velocityY = velocityY;
		this.radius = radius;
	}

	/**
     * Moves the obstacle within the game canvas.
     * @param screenWidth The width of the game canvas.
     * @param screenHeight The height of the game canvas.
     */
	public void move(int screenWidth, int screenHeight) {
		position.setX(position.getX() + velocityX);
		position.setY(position.getY() + velocityY);
		// Check if obstacle hits the right or left edge of the screen
		if (position.getX() <= 0 || position.getX() >= screenWidth - OBSTACLE_SIZE) {
			velocityX = -velocityX; // Reverse the horizontal velocity
		}
		// Check if obstacle hits the top or bottom edge of the screen
		if (position.getY() <= 0 || position.getY() >= screenHeight - OBSTACLE_SIZE) {
			velocityY = -velocityY; // Reverse the vertical velocity
		}
	}

	/**
     * Renders the obstacle on the graphics context.
     * @param brush The graphics context to render onto.
     */
	public void paint(Graphics brush) {
		brush.setColor(Color.RED);
		brush.fillOval((int) position.getX(), (int) position.getY(), OBSTACLE_SIZE, OBSTACLE_SIZE);
	}
}
