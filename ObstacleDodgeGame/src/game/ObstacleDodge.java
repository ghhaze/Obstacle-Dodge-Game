package game;

/*
CLASS: YourGameNameoids
DESCRIPTION: Extending Game, YourGameName is all in the paint method.
NOTE: This class is the metaphorical "main method" of your program,
      it is your control center.

*/
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Timer;

class ObstacleDodge extends Game implements KeyListener, GameEventListener, Interface {
	static int counter = 0;
	private boolean running = true;
	private Snake snake;
	private Scoreboard scoreboard;
	private CollisionHandler collisionHandler;
	private GameEventListener gameEventListener;
	private int SNAKE_SIZE = 20;
	private int FOOD_SIZE = 10;
	private Food food;
	private int score;
	private List<Obstacles> obstacles;
	private static final int NUM_OBSTACLES = 20;
	private static final int OBSTACLE_SIZE = 15;
	private static final int OBSTACLE_RADIUS = 15;

	/**
	 * Constructor for the ObstacleDodge class. Initializes the game components such
	 * as snake, scoreboard, food, and obstacles.
	 */
	public ObstacleDodge() {
		super("ObstacleDodge!", 800, 600);
		this.setFocusable(true);
		this.requestFocus();
		this.addKeyListener(this);
		Timer timer = new Timer(100, e -> {
			if (running) {
				snake.move();
				checkCollisions();
			}else {
				running = false;
			}
		});
		timer.start();
		scoreboard = new Scoreboard();
		food = new Food(generateRandomPosition(), FOOD_SIZE); // Initialize food
		food.setPosition(generateRandomPosition());
		Point[] snakePoints = { new Point(0, 0), new Point(SNAKE_SIZE, 0), new Point(SNAKE_SIZE, SNAKE_SIZE),
				new Point(0, SNAKE_SIZE) };
		snake = new Snake(snakePoints, new Point(400, 300), 0);
		Point[] foodPoints = { new Point(0, 0), new Point(FOOD_SIZE, 0), new Point(FOOD_SIZE, FOOD_SIZE),
				new Point(0, FOOD_SIZE) };
		food = new Food(foodPoints, generateRandomPosition(), 0);
		score = 0;
		obstacles = new ArrayList<>();
		for (int i = 0; i < NUM_OBSTACLES; i++) {
			// Create obstacles at random positions
			Point[] obstaclePoints = { new Point(0, 0), new Point(OBSTACLE_SIZE, 0),
					new Point(OBSTACLE_SIZE, OBSTACLE_SIZE), new Point(0, OBSTACLE_SIZE) };
			double velocityX = Math.random() * 2 - 1; // Random velocity along X-axis (-1 to 1)
			double velocityY = Math.random() * 2 - 1; // Random velocity along Y-axis (-1 to 1)
			obstacles.add(
					new Obstacles(obstaclePoints, generateRandomPosition(), 0, velocityX, velocityY, OBSTACLE_RADIUS));
		}
	}

	/**
	 * Paints the game components onto the graphics context. This method is
	 * responsible for rendering the snake, food, obstacles, and score onto the
	 * screen.
	 * 
	 * @param brush The graphics context to paint onto.
	 */
	public void paint(Graphics brush) {
		brush.setColor(Color.black);
		brush.fillRect(0, 0, width, height);
		try {
			if (snake != null) {
				snake.move();
			}
		} catch (NullPointerException e) {
			System.out.println("NullPointerException occurred: " + e.getMessage());
		}
		if (collisionHandler != null) {
			collisionHandler.checkCollisions(snake, food, obstacles);
		}
		// sample code for printing message for debugging
		// counter is incremented and this message printed
		// each time the canvas is repainted
		// counter++;
		// brush.setColor(Color.white);
		// brush.drawString("Counter is " + counter, 10, 10);
		if (snakeHitsEdge()) {
			// Game over condition
			stopGame();
			brush.setColor(Color.RED);
			brush.drawString("Game Over", width / 2 - 40, height / 2);
			brush.drawString("Score: " + scoreboard.getScore(), width / 2 - 30, height / 2 + 20);
			return;
		} else {
			// Check for collision with food
			if (snake.collides(food)) {
				// Snake has collided with food, generate new food position
				food.setPosition(generateRandomPosition());
				scoreboard.incrementScore();// Increment score counter
				food.paint(brush);
			}
			if (gameEventListener != null) {
				gameEventListener.onGameEvent(GameEvent.FOOD_EATEN);
			}
			for (Obstacles obstacle : obstacles) {
				if (snake.collides(obstacle)) {
					// Snake has collided with an obstacle, stop the game
					stopGame();
					brush.setColor(Color.RED);
					brush.drawString("Game Over", width / 2 - 40, height / 2);
					brush.drawString("Score: " + scoreboard.getScore(), width / 2 - 30, height / 2 + 20);
					return; // Exit the method
				}
				if (gameEventListener != null) {
					gameEventListener.onGameEvent(GameEvent.OBSTACLE_COLLISION);
				}
			}
			// Update positions of obstacles and paint them
			for (Obstacles obstacle : obstacles) {
				obstacle.move(width, height); // Move the obstacle
				obstacle.paint(brush); // Paint the obstacle
			}
		}

		brush.setColor(Color.WHITE);
		brush.drawString("Score: " + scoreboard.getScore(), 10, 20);
		for (Obstacles obstacle : obstacles) {
			obstacle.move(width, height); // Move the obstacle and handle bouncing
			obstacle.paint(brush); // Paint the obstacle
		}
		snake.paint(brush);
		food.paint(brush);
	}

	/**
	 * Checks if the snake hits the edge of the game canvas.
	 * 
	 * @return True if the snake hits the edge, false otherwise.
	 */
	public boolean snakeHitsEdge() {
		Point[] points = snake.getPoints();
		for (Point point : points) {
			if (point.getX() < 0 || point.getX() >= width || point.getY() < 0 || point.getY() >= height) {
				return true; // Snake hits the edge
			}
			if (gameEventListener != null) {
				gameEventListener.onGameEvent(GameEvent.SNAKE_COLLISION);
			}
		}
		return false; // Snake does not hit the edge
	}

	/**
	 * Generates a random position within the game canvas. This method is used to
	 * generate random positions for food and obstacles.
	 * 
	 * @return A Point object representing the random position.
	 */
	private Point generateRandomPosition() {
		int x = (int) (Math.random() * (width / FOOD_SIZE)) * FOOD_SIZE;
		int y = (int) (Math.random() * (height / FOOD_SIZE)) * FOOD_SIZE;
		return new Point(x, y);
	}

	/**
	 * Handles key presses to control the snake's movement.
	 * 
	 * @param e The KeyEvent object representing the key press event.
	 */
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if (keyCode == KeyEvent.VK_UP) {
			snake.setForward(true); // Forward key pressed
		} else if (keyCode == KeyEvent.VK_LEFT) {
			snake.setLeftTurn(true); // Left turn key pressed
		} else if (keyCode == KeyEvent.VK_RIGHT) {
			snake.setRightTurn(true); // Right turn key pressed
		}
	}

	/**
	 * Handles key releases.
	 * 
	 * @param e The KeyEvent object representing the key release event.
	 */
	public void keyReleased(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if (keyCode == KeyEvent.VK_UP) {
			snake.setForward(false); // Forward key released
		} else if (keyCode == KeyEvent.VK_LEFT) {
			snake.setLeftTurn(false); // Left turn key released
		} else if (keyCode == KeyEvent.VK_RIGHT) {
			snake.setRightTurn(false); // Right turn key released
		}
	}

	/**
	 * Empty method required by the KeyListener interface.
	 * 
	 * @param e The KeyEvent object representing the key typed event.
	 */
	public void keyTyped(KeyEvent e) {
	}

	/**
	 * Stops the game when it's over and notifies the game event listener. This
	 * method is called when the game ends either by hitting an obstacle or the edge
	 * of the canvas.
	 */
	public void stopGame() {
		on = false;
		if (gameEventListener != null) {
			gameEventListener.onGameEvent(GameEvent.GAME_OVER);
		}
	}

	/**
	 * Inner class representing the scoreboard. The scoreboard keeps track of the
	 * player's score.
	 */
	private class Scoreboard {
		private int score;
		/**
		 * Constructor for the Scoreboard class.
		 */
		public Scoreboard() {
			this.score = 0;
		}
		/**
		 * Increments the score by one. This method is called when the snake eats food.
		 */
		public void incrementScore() {
			score++;
		}
		/**
		 * Gets the current score.
		 * 
		 * @return The current score.
		 */
		public int getScore() {
			return score;
		}
	}

	/**
	 * Inner class representing the collision handler. The collision handler detects
	 * collisions between the snake, food, and obstacles.
	 */
	private class CollisionHandler {

		/**
		 * Checks for collisions between the snake, food, and obstacles. This method is
		 * responsible for handling collisions and updating game state accordingly.
		 * 
		 * @param snake     The snake object.
		 * @param food      The food object.
		 * @param obstacles The list of obstacle objects.
		 */
		public void checkCollisions(Snake snake, Food food, List<Obstacles> obstacles) {
			// Implement collision detection and handling logic here
			// For example:
			if (snake.collides(food)) {
				// Handle collision between snake and food
				food.setPosition(generateRandomPosition());
				scoreboard.incrementScore();
			}
			for (Obstacles obstacle : obstacles) {
				if (snake.collides(obstacle)) {
					// Handle collision between snake and obstacle
					stopGame();
					// Other collision handling logic...
				}
			}
		}
	}

	/**
	 * Handles the game over event by printing the score.
	 * 
	 * @param score The final score of the game.
	 */

	public void setGameEventListener(GameEventListener listener) {
		this.gameEventListener = listener;
	}

	/**
	 * Main method to instantiate and start the ObstacleDodge game.
	 */
	public static void main(String[] args) {
		ObstacleDodge a = new ObstacleDodge();
		a.repaint();
	}

	@Override
	public void onGameEvent(GameEvent event) {
		GameEventListener gameEventListener = (GameEvent e) -> {
			switch (e) {
			case SNAKE_COLLISION:
				System.out.println("Snake collided with an obstacle.");
				break;
			case FOOD_EATEN:
				System.out.println("Snake ate the food.");
				break;
			case OBSTACLE_COLLISION:
				System.out.println("Snake collided with an obstacle.");
				break;
			case GAME_OVER:
				System.out.println("Game Over!");
				break;
			default:
				System.out.println("Unknown game event.");
				break;
			}
		};

		gameEventListener.onGameEvent(event);
	}

	@Override
	public void checkCollisions() {
	}

}