import exe.ex3.game.Game;
import exe.ex3.game.GhostCL;
import exe.ex3.game.PacManAlgo;
import exe.ex3.game.PacmanGame;

import java.awt.Color;

public class Ex3Algo implements PacManAlgo {
	private int _count;
	private boolean shouldRunAway;

	public Ex3Algo(){
		_count = 0;
		shouldRunAway = false;
	}

	@Override
	public String getInfo() {
		return " The provided code implements a simple algorithm " +
				"for controlling the movement of a PacMan character in a game " +
				"this algorithm keeps track of the closest pink dots and " +
				"determines the next direction based on the current position and the found elements";
	}

	@Override
	/**
	 * This is the main method - that you should design, implement and test.
	 */
	public int move(PacmanGame game) {
		if (_count == 0 || _count == 300) {
			int code = 0;
			int[][] board = game.getGame(0);
			printBoard(board);
			int blue = Game.getIntColor(Color.BLUE, code);
			int pink = Game.getIntColor(Color.PINK, code);
			int black = Game.getIntColor(Color.BLACK, code);
			int green = Game.getIntColor(Color.GREEN, code);
			System.out.println("Blue=" + blue + ", Pink=" + pink + ", Black=" + black + ", Green=" + green);
			String pos = game.getPos(code).toString();
			System.out.println("Pacman coordinate: " + pos);
			GhostCL[] ghosts = game.getGhosts(code);
			printGhosts(ghosts);
			int up = Game.UP, left = Game.LEFT, down = Game.DOWN, right = Game.RIGHT;
		}
		_count++;

		int dir = getDirection(game);
		return dir;
	}


	private static void printBoard(int[][] b) {
		for (int y = 0; y < b[0].length; y++) {
			for (int x = 0; x < b.length; x++) {
				int v = b[x][y];
				System.out.print(v + "\t");
			}
			System.out.println();
		}
	}

	private static void printGhosts(GhostCL[] gs) {
		for (int i = 0; i < gs.length; i++) {
			GhostCL g = gs[i];
			System.out.println(i + ") status: " + g.getStatus() + ",  type: " + g.getType() + ",  pos: " + g.getPos(0) + ",  time: " + g.remainTimeAsEatable(0));
		}
	}
	private static int randomDir() {
		int[] dirs = {Game.UP, Game.LEFT, Game.DOWN, Game.RIGHT};
		int ind = (int)(Math.random()*dirs.length);
		return dirs[ind];
	}

	private static int getDirection(PacmanGame game) {
		// Get the game board from the PacmanGame object
		int[][] gameBoard = game.getGame(0);
		// Create a Map object using the game board
		Map gameMap = new Map(gameBoard);
		// Set the cyclic property of the map based on the PacmanGame object
		gameMap.setCyclic(game.isCyclic());

		// Get the current position of the main character
		String currentPosition = game.getPos(0);
		// Split the position string into x and y coordinates
		String[] positionArray = currentPosition.split("\\,");
		int xStart = Integer.parseInt(positionArray[0]);
		int yStart = Integer.parseInt(positionArray[1]);
		// Create a Pixel2D object for the current position
		Pixel2D currentPixel = new Index2D(xStart, yStart);

		// Calculate the distance map using the current position
		Map2D distanceBoard = gameMap.allDistance(currentPixel, 1);
		// Find the nearest "pink" pixel using the current position and distance map
		Pixel2D pinkPixel = findPink(currentPixel, gameMap, distanceBoard);

		// Print the distance map (for debugging purposes)
		printBoard(distanceBoard.getMap());

		// Find the shortest path from the current position to the pink pixel
		Pixel2D[] shortestPath = gameMap.shortestPath(currentPixel, pinkPixel, 1);
		// Determine the direction to move based on the shortest path
		int direction = findDir(currentPixel, shortestPath[1]);

		// Return the direction to move
		return direction;
	}

	// Helper function to find the nearest "pink" pixel
	private static Pixel2D findPink(Pixel2D cur, Map map, Map2D mapit) {
		// Initialize variables for tracking the nearest pixel
		int disNear = Integer.MAX_VALUE;
		Pixel2D near = new Index2D(0, 0);
		int w = map.getWidth();
		int h = map.getHeight();

		// Iterate over all pixels in the map
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				// Check if the pixel represents the "pink" character
				if (map.getPixel(i, j) == 3) {
					// Check if the pixel is reachable (has a valid distance)
					if (mapit.getPixel(i, j) != -1) {
						int curD = mapit.getPixel(i, j);
						// Check if the current distance is closer than the previous closest distance
						if (disNear > curD) {
							// Update the nearest pixel and distance
							disNear = curD;
							near = new Index2D(i, j);
						}
					}
				}
			}
		}

		// Return the nearest "pink" pixel
		return near;
	}


	private static int findDir(Pixel2D pose, Pixel2D dest) {
		// Check if the x-coordinates are the same
		if (pose.getX() == dest.getX()) {
			// Check if the y-coordinate of the destination is one unit above the current position
			if (pose.getY() + 1 == dest.getY()) {
				return Game.UP;
			}
			// Check if the y-coordinate of the destination is one unit below the current position
			if (pose.getY() - 1 == dest.getY()) {
				return Game.DOWN;
			}
			// Check if the y-coordinate of the destination is above the current position
			if (pose.getY() > dest.getY()) {
				return Game.UP;
			}
			// Check if the y-coordinate of the destination is below the current position
			if (pose.getY() < dest.getY()) {
				return Game.DOWN;
			}
		}
		// Check if the y-coordinates are the same
		else if (pose.getY() == dest.getY()) {
			// Check if the x-coordinate of the destination is one unit to the right of the current position
			if (pose.getX() + 1 == dest.getX()) {
				return Game.RIGHT;
			}
			// Check if the x-coordinate of the destination is one unit to the left of the current position
			if (pose.getX() - 1 == dest.getX()) {
				return Game.LEFT;
			}
			// Check if the x-coordinate of the destination is to the right of the current position
			if (pose.getX() > dest.getX()) {
				return Game.RIGHT;
			}
			// Check if the x-coordinate of the destination is to the left of the current position
			if (pose.getX() < dest.getX()) {
				return Game.LEFT;
			}
		}

		// If none of the above conditions are satisfied, return a random direction
		return randomDir();
	}

	private static GhostCL findClosestGhost(Pixel2D cur, GhostCL[] ghosts, Map2D mapit) {
		int minDistance = Integer.MAX_VALUE;
		GhostCL closestGhost = null;

		// Iterate over the ghosts array
		for (GhostCL ghost : ghosts) {
			// Get the position of the ghost
			String ghostPosition = ghost.getPos(0);
			String[] positionArray = ghostPosition.split("\\,");
			int ghostX = Integer.parseInt(positionArray[0]);
			int ghostY = Integer.parseInt(positionArray[1]);
			// Create a Pixel2D object for the ghost position
			Pixel2D ghostPixel = new Index2D(ghostX, ghostY);

			// Check if the ghost position is reachable (has a valid distance)
			if (mapit.getPixel(ghostX, ghostY) != -1) {
				int distance = mapit.getPixel(ghostX, ghostY);
				// Check if the current ghost is closer than the previous closest ghost
				if (distance < minDistance) {
					// Update the minimum distance and closest ghost
					minDistance = distance;
					closestGhost = ghost;
				}
			}
		}

		// Return the closest ghost
		return closestGhost;
	}
}














