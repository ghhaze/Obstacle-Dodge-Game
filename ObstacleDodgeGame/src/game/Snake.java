package game;

import java.awt.Color;
import java.awt.Graphics;

/**
 * Represents a snake object in the game.
 * The snake can be rendered and moved within the game canvas.
 */
public class Snake extends Polygon {
	private boolean forward;
	private boolean leftTurn;
	private boolean rightTurn;
	private static final double MOVE_SPEED = 3;

	/**
     * Constructs a snake object with custom shape, position, and rotation.
     * @param inShape The array of points defining the shape of the snake.
     * @param inPosition The initial position of the snake.
     * @param inRotation The initial rotation angle of the snake.
     */
	public Snake(Point[] inShape, Point inPosition, double inRotation) {
		super(inShape, inPosition, inRotation);
		forward = true; // Set forward key to true initially
		leftTurn = false; // Set left turn key to false initially
		rightTurn = false; // Set right turn key to false initially
	}

	/**
     * Renders the snake on the graphics context.
     * @param brush The graphics context to render onto.
     */
	public void paint(Graphics brush) {
		brush.setColor(Color.GREEN); // Set snake color
		Point[] snakePoints = this.getPoints();
		int[] xPoints = new int[snakePoints.length];
		int[] yPoints = new int[snakePoints.length];
		for(int i = 0; i < snakePoints.length; i++) {
			xPoints[i] = (int) snakePoints[i].getX();
			yPoints[i] = (int) snakePoints[i].getY();
		}
		brush.fillPolygon(xPoints, yPoints, snakePoints.length);

	}

	/**
     * Moves the snake based on its current direction and rotation.
     * The snake moves forward if the forward key is pressed, and can turn left or right.
     */
	public void move() {
		if (forward) {
			double newX = this.position.getX() + MOVE_SPEED * Math.cos(Math.toRadians(this.rotation));
			double newY = this.position.getY() + MOVE_SPEED * Math.sin(Math.toRadians(this.rotation));
			this.position.setX(newX);
			this.position.setY(newY);
		}
		if (leftTurn) {
			this.rotate(-5); // Rotate counter-clockwise by 5 degrees
		}
		if (rightTurn) {
			this.rotate(5); // Rotate clockwise by 5 degrees
		}
	}

	/**
     * Sets the forward movement of the snake.
     * @param forwardPressed True if the forward key is pressed, false otherwise.
     */
	public void setForward(boolean forwardPressed) {
		this.forward = forwardPressed;
	}

	/**
     * Sets the left turn movement of the snake.
     * @param leftPressed True if the left turn key is pressed, false otherwise.
     */
	public void setLeftTurn(boolean leftPressed) {
		this.leftTurn = leftPressed;
	}
	
	/**
     * Sets the right turn movement of the snake.
     * @param rightPressed True if the right turn key is pressed, false otherwise.
     */
	public void setRightTurn(boolean rightPressed) {
		this.rightTurn = rightPressed;
	}
}
