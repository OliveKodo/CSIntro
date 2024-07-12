//package ex3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

/**
 * This class represents a 2D map as a "screen" or a raster matrix or maze over integers.
 *
 * @author boaz.benmoshe
 *id = 208040261
 */
public class Map implements Map2D {
	final int EXPLORED = 0;
	final int FREE = -2;
	private int[][] _map;
	private boolean _cyclicFlag = true;


	/**
	 * Constructs a w*h 2D raster map with an init value v.
	 *
	 * @param w
	 * @param h
	 * @param v
	 */
	public Map(int w, int h, int v) {
		init(w, h, v);
	}


	/**
	 * Constructs a square map (size*size).
	 *
	 * @param size
	 */
	public Map(int size) {
		this(size, size, 0);
	}

	/**
	 * Constructs a map from a given 2D array.
	 *
	 * @param data
	 */
	public Map(int[][] data) {
		init(data);
	}

	@Override
	public void init(int w, int h, int v) {
		this._map = new int[w][h];
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				this._map[i][j] = v;

			}

		}

	}

	@Override
	public void init(int[][] arr) throws RuntimeException {
		if (arr == null) {
			throw new RuntimeException("RuntimeException, Invalid input, arr cant be null");

		} else {
			_map = new int[arr.length][arr[0].length];
			for (int i = 0; i < arr.length; i++) {
				for (int j = 0; j < arr[0].length; j++) {
					System.out.println(Arrays.deepToString(_map));
					_map[i][j] = arr[i][j];

				}

			}
		}
	}

	@Override
	public int[][] getMap() {
		int[][] ans = null;
		int[][] copyMap = new int[this._map.length][this._map[0].length];
		for (int i = 0; i < this._map.length; i++) {
			for (int j = 0; j < this._map[0].length; j++) {
				copyMap[i][j] = this._map[i][j];
			}
		}
		return copyMap;
	}

	@Override
	public int getWidth() {
		return _map.length;
	}

	@Override
	public int getHeight() {
		return _map[0].length;
	}

	@Override
	public int getPixel(int x, int y) {
		return _map[x][y];
	}

	@Override
	public int getPixel(Pixel2D p) {
		return this.getPixel(p.getX(), p.getY());
	}

	@Override
	public void setPixel(int x, int y, int v) {
		_map[x][y] = v;
	}

	@Override
	public void setPixel(Pixel2D p, int v) {
		_map[p.getX()][p.getY()] = v;
	}

	@Override
	/**
	 * Fills this map with the new color (new_v) starting from p.
	 * https://en.wikipedia.org/wiki/Flood_fill
	 */
	public int fill(Pixel2D xy, int new_v) {
		int ans = 0; // Initialize counter for filled pixels
		int[][] distanceMap = allDistance(xy, -1).getMap(); // Create a distance map from the given pixel

		if (distanceMap != null) { // Check if the distance map was successfully created
			// Loop through the map and fill pixels with the new value
			for (int i = 0; i < getWidth(); i++) {
				for (int j = 0; j < getHeight(); j++) {
					if (distanceMap[i][j] >= 0) { // Check if the pixel is reachable
						_map[i][j] = new_v; // Fill the pixel with the new value
						ans++; // Increment the filled pixel counter
					}
				}
			}
		} else {
			System.out.println("Error: Distance map is null.");
		}

		return ans; // Return the total number of filled pixels
	}





	@Override
	/**
	 * BFS like shortest the computation based on iterative raster implementation of BFS, see:
	 * https://en.wikipedia.org/wiki/Breadth-first_search
	 */
	/**
	 * Calculates the shortest path between two pixels on the map, avoiding obstacles.
	 *
	 * @param p1        The starting pixel.
	 * @param p2        The destination pixel.
	 * @param obsColor  The color code representing the obstacles on the map.
	 * @return An array of Pixel2D objects representing the shortest path, or null if no path is found.
	 */
	public Pixel2D[] shortestPath(Pixel2D p1, Pixel2D p2, int obsColor) {
		// Get the dimensions of the map
		boolean dontNeedSteps = false;
		int width = _map.length;
		int height = _map[0].length;
		int pathLength = 1;
		Pixel2D[] ans;  // The result array containing the shortest path.
		PixelProperties[][] visited = new PixelProperties[width][height];
		initPixelPropMap(visited, width, height);

		Pixel2D[][] parent = new Pixel2D[width][height];

		Queue<Pixel2D> queue = new LinkedList<>();
		queue.add(p1); // Enqueue the first pixel
		visited[p1.getX()][p1.getY()].setState(PixelProperties.PixelState.Explored);

		// Perform breadth-first search to find the shortest path
		while (!queue.isEmpty()) {
			Pixel2D currentPixel = queue.poll();
			int currentX = currentPixel.getX();
			int currentY = currentPixel.getY();

			// Check if the current pixel is the destination
			if (currentPixel.getX() == p2.getX() && currentPixel.getY() == p2.getY()) {
				PixelProperties tracePixel = visited[currentX][currentY];

				// Backtrack to reconstruct the path
				ArrayList<Pixel2D> backwardArray = new ArrayList<>();
				backwardArray.add(currentPixel);
				while (tracePixel.getPrev() != null) {
					backwardArray.add(tracePixel.getPrev());
					pathLength++;
					if (tracePixel.getPrev().getX() == p1.getX() && tracePixel.getPrev().getY() == p1.getY()) {
						break;
					}
					tracePixel = visited[tracePixel.getPrev().getX()][tracePixel.getPrev().getY()];
				}

				ans = new Pixel2D[pathLength];

				// Reverse the path and store it in the result array
				for (int i = 0; i < pathLength; i++) {
					ans[pathLength - 1 - i] = backwardArray.get(i);
				}

				return ans; // Return the shortest path
			}

			// Check the neighbors of the current pixel
			// Check Up
			int up = currentY + 1;
			if (up > height - 1) {
				if (isCyclic()) {
					up = 0;
					CheckNeighbors(obsColor, visited, queue, currentPixel, currentX, up,dontNeedSteps);

				}
			} else {
				CheckNeighbors(obsColor, visited, queue, currentPixel, currentX, up,dontNeedSteps);
			}

			// Check Down
			int down = (currentY - 1);
			if (down < 0) {
				if (isCyclic()) {
					down = height - 1;
					CheckNeighbors(obsColor, visited, queue, currentPixel, currentX, down,dontNeedSteps);
				}
			} else {
				CheckNeighbors(obsColor, visited, queue, currentPixel, currentX, down,dontNeedSteps);
			}

			// Check Right
			int right = (currentX + 1);
			if (right > width - 1) {
				if (isCyclic()) {
					right = 0;
					CheckNeighbors(obsColor, visited, queue, currentPixel, right, currentY,dontNeedSteps);
				}
			} else {
				CheckNeighbors(obsColor, visited, queue, currentPixel, right, currentY,dontNeedSteps);
			}

			// Check Left
			int left = (currentX - 1);
			if (left < 0) {
				if (isCyclic()) {
					left = width - 1;
					CheckNeighbors(obsColor, visited, queue, currentPixel, left, currentY,dontNeedSteps);
				}
			} else {
				CheckNeighbors(obsColor, visited, queue, currentPixel, left, currentY,dontNeedSteps);
			}
		}

		// If no path is found, return null
		return null;
	}

	private void CheckNeighbors(int obsColor, PixelProperties[][] visited, Queue<Pixel2D> queue, Pixel2D currentPixel, int X, int Y, Boolean markSteps) {
		if (_map[X][Y] != obsColor && visited[X][Y].getState() != PixelProperties.PixelState.Explored) {
			int val = _map[X][Y];
			Pixel2D Down = new Index2D(X, Y, val);
			visited[X][Y].setPrev(currentPixel);


			visited[X][Y].setState(PixelProperties.PixelState.Explored);
			if (markSteps) {
				visited[X][Y].increaseStep(visited[currentPixel.getX()][currentPixel.getY()].getSteps());
			}
			queue.add(Down);
		}
	}


	@Override
	public boolean isInside(Pixel2D p) {
		if (p.getX() >= 0 && p.getX() < _map.length && p.getY() >= 0 && p.getY() < _map[0].length) {
			return true;
		}


		return false;
	}

	@Override
	public boolean isCyclic() {
		return this._cyclicFlag;
	}

	@Override
	public void setCyclic(boolean cy) {
		this._cyclicFlag = cy;
	}

	/**
	 *
	 * @param start the source (starting) point
	 * @param obsColor the color representing obstacles
	 * @return
	 */

	/**
	 * This function behave similar to the shortest path in terms of BFS,
	 * The Idea is to use the same function "CheckNeighbors" for additional purpose -> to mark the steps that took so far until we reached the pixel.
	 * The steps counting is performed through {@link PixelProperties} class, as we increase the steps for the desire pixel based on his parent(the prev pixel).
	 * */
	@Override
	public Map2D allDistance(Pixel2D start, int obsColor) {

		// Get the dimensions of the map
		boolean dontNeedSteps = true;
		int width = _map.length;
		int height = _map[0].length;
		int pathLength = 1;
		PixelProperties[][] visited = new PixelProperties[width][height];
		initPixelPropMap(visited, width, height);



		Queue<Pixel2D> queue = new LinkedList<>();
		queue.add(start); // Enqueue the first pixel
		visited[start.getX()][start.getY()].setState(PixelProperties.PixelState.Explored);

		// Perform breadth-first search to find the shortest path
		while (!queue.isEmpty()) {
			Pixel2D currentPixel = queue.poll();
			int currentX = currentPixel.getX();
			int currentY = currentPixel.getY();

			// Check the neighbors of the current pixel
			// Check Up
			int up = currentY + 1;
			if (up > height - 1) {
				if (isCyclic()) {
					up = 0;
					CheckNeighbors(obsColor, visited, queue, currentPixel, currentX, up,dontNeedSteps);

				}
			} else {
				CheckNeighbors(obsColor, visited, queue, currentPixel, currentX, up,dontNeedSteps);
			}

			// Check Down
			int down = (currentY - 1);
			if (down < 0) {
				if (isCyclic()) {
					down = height - 1;
					CheckNeighbors(obsColor, visited, queue, currentPixel, currentX, down,dontNeedSteps);
				}
			} else {
				CheckNeighbors(obsColor, visited, queue, currentPixel, currentX, down,dontNeedSteps);
			}

			// Check Right
			int right = (currentX + 1);
			if (right > width - 1) {
				if (isCyclic()) {
					right = 0;
					CheckNeighbors(obsColor, visited, queue, currentPixel, right, currentY,dontNeedSteps);
				}
			} else {
				CheckNeighbors(obsColor, visited, queue, currentPixel, right, currentY,dontNeedSteps);
			}

			// Check Left
			int left = (currentX - 1);
			if (left < 0) {
				if (isCyclic()) {
					left = width - 1;
					CheckNeighbors(obsColor, visited, queue, currentPixel, left, currentY,dontNeedSteps);
				}
			} else {
				CheckNeighbors(obsColor, visited, queue, currentPixel, left, currentY,dontNeedSteps);
			}
		}
		// Created new map, this val of this map will determine by the steps assigned to each entry of visited matrix.
		Map2D ans = new Map(new int[width][height]);
		// for loops assign the correct value of each entry, directly from the corresponding entry of visited matrix.
		for (int i = 0; i < visited.length; i++) {
			for (int j = 0; j < visited[i].length; j++) {
				ans.setPixel(i,j,visited[i][j].getSteps());
			}
		}

		return ans;
	}


	public void initPixelPropMap(PixelProperties[][] pixelProp, int width, int height) {
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				pixelProp[i][j] = new PixelProperties();
			}
		}
	}

	public static class PixelProperties {

		private Pixel2D prev;
		private PixelState state;

		private int steps;

		public PixelProperties() {
			this.prev = null;
			this.state = PixelState.Free;
			this.steps = 0;
		}

		private void increaseStep(int stepsSoFar) {
			this.steps = stepsSoFar + 1;
		}

		private int getSteps() {
			return this.steps;
		}

		public Pixel2D getPrev() {
			return this.prev;
		}


		void setPrev(Pixel2D prev) {
			this.prev = prev;
		}


		public PixelState getState() {
			return this.state;

		}


		void setState(PixelState state) {
			this.state = state;
		}

		public enum PixelState {
			Free,
			Explored,
		}



	}

	@Override
	public String toString() {
		for (int i = 0; i < _map.length; i++) {
			System.out.println(Arrays.toString(_map[i]));

		}
		return "";
	}



}


