import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GameState {
	private int size; // The number of stones
	private boolean[] stones; // Game state: true for available stones, false for taken ones
	private int lastMove; // The last move
	private int noTaken; // The number of removed stones to determine whose turn it is

	/**
	 * Class constructor specifying the number of stones.
	 */
	public GameState(int size) {

		this.size = size;

		// For convenience, we use 1-based index, and set 0 to be unavailable
		this.stones = new boolean[this.size + 1];
		this.stones[0] = false;

		// Set default state of stones to available
		for (int i = 1; i <= this.size; ++i) {
			this.stones[i] = true;
		}

		// Set the last move be -1
		this.lastMove = -1;
	}

	/**
	 * Copy constructor
	 */
	public GameState(GameState other) {
		this.size = other.size;
		this.stones = Arrays.copyOf(other.stones, other.stones.length);
		this.lastMove = other.lastMove;
	}

	/**
	 * This method is used to compute a list of legal moves
	 *
	 * @return This is the list of state's moves
	 */
	public List<Integer> getMoves() {
		// TODO Add your code here

		// All possible factors and multiples of the last move are legal.
		// For example, if the last move is a, then the set of possible moves can be
		// expressed as {x∣(xmoda=0)∨(amodx=0)}, assuming x has not been taken yet.

		List<Integer> moves = new ArrayList<Integer>();
		int x = this.getLastMove();

		// If it is the first turn, it should be the odd number strictly less than n/2
		if (x == -1) {
			int upper = (this.size / 2) + (this.size % 2);
			for (int i = 1; i < upper; i += 2) {
				moves.add(i);
			}
		} else {
			for (int i = 1; i <= this.size; i++) {
				if (this.getStone(i)) {
					if ((x % i == 0) || (i % x == 0))
						moves.add(i);
				}
			}
		}
		return moves;
	}

	/**
	 * This method is used to generate a list of successors using the getMoves()
	 * method
	 *
	 * @return This is the list of state's successors
	 */
	public List<GameState> getSuccessors() {
		return this.getMoves().stream().map(move -> {
			var state = new GameState(this);
			state.removeStone(move);
			return state;
		}).collect(Collectors.toList());
	}

	/**
	 * This method is used to evaluate a game state based on the given heuristic
	 * function
	 *
	 * @return int This is the static score of given state
	 */
	public double evaluate() {
		// TODO Add your code here

		// Not sure if it is just List or ArrayList
		List<Integer> successors = this.getMoves(); // if stone 1 is still available, score is 0
		boolean maxPlayer = false; // to check whose turn it is

		if (this.noTaken % 2 == 0)
			maxPlayer = true;

		// At an end game state where Player 1 (MAX) wins: 1.0
		// At an end game state where Player 2 (MIN) wins: -1.0

		if (successors.size() == 0) {
			if (maxPlayer)
				return -1;
			else
				return 1;
		}

		// If it is Player1(MAX)’s turn
		// If stone 1 is not taken yet, return a value of 0 (because the current state
		// is a relatively neutral one for both players)

		if (maxPlayer) {
			if (this.getStone(1))
				return 0;

			// If the last move was 1, count the number of the possible successors (i.e.,
			// legal moves).
			// If the count is odd, return 0.5; otherwise, return -0.5.

			int last = this.getLastMove();

			if (last == 1) {
				if (successors.size() % 2 == 1) // if odd
					return 0.5;
				else
					return -0.5;

			} else if (Helper.isPrime(last)) {
				// If last move is a prime, count the multiples of that prime in all possible
				// successors. If the count is odd, return 0.7; otherwise, return -0.7.
				int count = 0; // include lastMove
				for (int i = 1; i < this.getSize() + 1; i++)
					if (this.getStone(i) && i % last == 0)
						count++;
				if (count % 2 == 1)
					return 0.7;
				else
					return -0.7;
			} else {
				int maxPrime = Helper.getLargestPrimeFactor(last);
				// If the last move is a composite number (i.e., not prime), find the largest
				// prime that can divide last move, count the multiples of that prime, including
				// the prime number itself if it hasn’t already been taken, in all the possible
				// successors. If the count is odd, return 0.6; otherwise, return -0.6.
				int count = 0;
				for (int i = 1; i < this.getSize() + 1; i++)
					if (this.getStone(i) && i % maxPrime == 0)
						count++;
				if (count % 2 == 1) // if odd
					return 0.6;
				else
					return -0.6;
			}
		} else { // If it is Player2(MIN)’s turn
			// perform the same checks but return the opposite (negation) of the values
			// specified.
			if (this.getStone(1))
				return 0;

			int last = this.getLastMove();

			if (last == 1) {
				if (successors.size() % 2 == 1) // if odd
					return -0.5;
				else
					return 0.5;

			} else if (Helper.isPrime(last)) {
				// If last move is a prime, count the multiples of that prime in all possible
				// successors. If the count is odd, return 0.7; otherwise, return -0.7.
				int count = 0; // include lastMove
				for (int i = 1; i < this.getSize() + 1; i++)
					if (this.getStone(i) && i % last == 0)
						count++;
				if (count % 2 == 1)
					return -0.7;
				else
					return 0.7;
			} else {
				int maxPrime = Helper.getLargestPrimeFactor(last);
				// If the last move is a composite number (i.e., not prime), find the largest
				// prime that can divide last move, count the multiples of that prime, including
				// the prime number itself if it hasn’t already been taken, in all the possible
				// successors. If the count is odd, return 0.6; otherwise, return -0.6.
				int count = 0;
				for (int i = 1; i < this.getSize() + 1; i++)
					if (this.getStone(i) && i % maxPrime == 0)
						count++;
				if (count % 2 == 1) // if odd
					return -0.6;
				else
					return 0.6;
			}
		}

	}

	/**
	 * This method is used to take a stone out
	 *
	 * @param idx Index of the taken stone
	 */
	public void removeStone(int idx) {
		this.stones[idx] = false;
		this.lastMove = idx;
		this.noTaken += 1; // To keep track whose turn it is
	}

	/**
	 * These are get/set methods for a stone
	 *
	 * @param idx Index of the taken stone
	 */
	public void setStone(int idx) {
		this.stones[idx] = true;
	}

	public boolean getStone(int idx) {
		return this.stones[idx];
	}

	/**
	 * These are get/set methods for lastMove variable
	 *
	 * @param move Index of the taken stone
	 */
	public void setLastMove(int move) {
		this.lastMove = move;
	}

	public int getLastMove() {
		return this.lastMove;
	}

	/**
	 * This is get method for game size
	 *
	 * @return int the number of stones
	 */
	public int getSize() {
		return this.size;
	}

	/**
	 * This is get method for the number of taken stones
	 *
	 * @return int the number of stones taken
	 */
	public int getTaken() {
		return this.noTaken;
	}

}
