import java.util.List;

public class AlphaBetaPruning {
	private int bestMove; // best move for current player
	private double value; // value of the move
	private int count; // the number of nodes visited
	private int leaf; // the number of nodes evaluated
	private int maxDepth; // the max depth it searched
	private int fullDepth; // depth to search from the argument

	public AlphaBetaPruning() {
	}

	/**
	 * This function will print out the information to the terminal, as specified in
	 * the homework description.
	 */
	public void printStats() {
		// TODO Add your code here
		
		// Need to divide these into half because alphabeta is called twice.
		// 1. To get the value 2. To get the best move
		this.leaf = this.leaf / 2;
		this.count = this.count / 2;
		double branching = (double) (this.count - 1) / (this.count - this.leaf);

		String branch = String.format("Avg Effective Branching Factor: %.1f", branching); // to round up
		System.out.println("Move: " + this.bestMove);
		System.out.println("Value: " + this.value);
		System.out.println("Number of Nodes Visited: " + this.count);
		System.out.println("Number of Nodes Evaluated: " + this.leaf);
		System.out.println("Max Depth Reached: " + this.maxDepth);
		System.out.println(branch);

	}

	/**
	 * This function will start the alpha-beta search
	 * 
	 * @param state This is the current game state
	 * @param depth This is the specified search depth
	 */
	public void run(GameState state, int depth) {
		// TODO Add your code here

		double alpha = Double.NEGATIVE_INFINITY; // initial value of alpha
		double beta = Double.POSITIVE_INFINITY; // initial value of alpha
		boolean maxPlayer = false; // Check whose turn it is
		if (state.getTaken() % 2 == 0)
			maxPlayer = true;
		this.fullDepth = depth;

		// Run AlphaBeta pruning
		this.value = alphabeta(state, depth, alpha, beta, maxPlayer);
		this.bestMove = (int) getBest(state, depth, maxPlayer);

	}

	/**
	 * This method is used to implement alpha-beta pruning for both 2 players
	 * 
	 * @param state     This is the current game state
	 * @param depth     Current depth of search
	 * @param alpha     Current Alpha value
	 * @param beta      Current Beta value
	 * @param maxPlayer True if player is Max Player; Otherwise, false
	 * @return double This is the number indicating score of the best next move
	 */
	private double alphabeta(GameState state, int depth, double alpha, double beta, boolean maxPlayer) {
		// TODO Add your code here
		List<Integer> successors = state.getMoves();
		double v = 0;
	
		this.count++; // Increase the count whenever alphabeta is called

		// Keep track of the max depth searched
		if (this.fullDepth - depth > this.maxDepth)
			this.maxDepth = this.fullDepth - depth;

		if (successors.size() == 0) {

			this.leaf++; // Increase when it reaches the leaf

			if (maxPlayer == true) {
				return -1;
				// if at game end and max player playing, return lose score
			} else {
				return 1;
				// if at game end and max player playing, return lose score
			}
		}
		if (depth == 0) {

			 this.leaf++; // Increase when it reaches the leaf

			return state.evaluate();
		}

		if (maxPlayer == true) {

			// If Max Player's turn
			v = Double.NEGATIVE_INFINITY;

			for (Integer successor : successors) {

				GameState newState = new GameState(state);
				// Copy and create a new state
				newState.removeStone(successor); // update the last move

				v = Math.max(v, alphabeta(newState, depth - 1, alpha, beta, !maxPlayer));

				if (v >= beta) {
					
					return v; // prune
				}
				alpha = Math.max(alpha, v);
			}

		} else {
			
			// If Min Player's turn
			v = Double.POSITIVE_INFINITY;
			
			for (Integer successor : successors) {

				GameState newState = new GameState(state);
				// Copy and get a new state
				newState.removeStone(successor); // update last move

				v = Math.min(v, alphabeta(newState, depth - 1, alpha, beta, true));

				if (v <= alpha) {
					
					return v; // prune
				}
				beta = Math.min(beta, v);
			}
		}

		return v;
	}

	/**
	 * This method is used to get the best next move from the current state
	 * 
	 * @param state     This is the current game state
	 * @param depth     Current depth of search
	 * @param maxPlayer True if player is Max Player; Otherwise, false
	 * @return double The best move for the current player
	 */
	public double getBest(GameState state, int depth, boolean maxPlayer) {
		double move = -1; // the best next move
		double alpha = Double.NEGATIVE_INFINITY; // initial value of alpha
		double beta = Double.POSITIVE_INFINITY; // initial value of alpha
		
		this.count++; // Increase the count when this method is called

		// Getting successors of the given state
		List<Integer> successors = state.getMoves();
		// Check if depth is 0 or it is at the terminal state
		if (depth == 0 || successors.size() == 0) {
			return move;
		}

		if (maxPlayer == true) {
			
			double tempV = Double.NEGATIVE_INFINITY;
			for (Integer successor : successors) {
				GameState newState = new GameState(state);// Copy the state
				newState.removeStone(successor); // Update the last move
				double v = alphabeta(newState, depth - 1, alpha, beta, false);

				if (v > tempV) {
					move = successor; // Update the move
					tempV = v; // Update v
				} else if (v == tempV) {
					move = Math.min(successor, move);
					// Update the move
				}
				if (v >= beta)
					return move;// prune
				alpha = Math.max(alpha, v);
			}
		} else {
			// If Min player's turn
			double tempV = Double.POSITIVE_INFINITY;
			for (Integer successor : successors) {
				GameState newState = new GameState(state); // Copy the state

				newState.removeStone(successor); // Update the last move

				double v = alphabeta(newState, depth - 1, alpha, beta, true);

				if (v < tempV) {
					move = successor;
					tempV = v; // Update v
				} else if (v == tempV) {
					move = Math.min(successor, move); // Update the move
				}
				if (v <= alpha)
					return move; // prune
				beta = Math.min(beta, v);
			}
		}
		return move;
	}

}
