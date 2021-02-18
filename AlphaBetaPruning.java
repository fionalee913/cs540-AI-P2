import java.util.List;

public class AlphaBetaPruning {
	final double alpha = Double.NEGATIVE_INFINITY;
	final double beta = Double.POSITIVE_INFINITY;
	private int bestMove = 0;
	private double value = 0.0;
	private int evaluated = 0;
	private int visited = 0;
	private int maxdepth = 0;
	private int exploreddepth =0;
	private double branch = 0.0;
	
    public AlphaBetaPruning() {
    	
    }

    /**
     * This function will print out the information to the terminal,
     * as specified in the homework description.
     */
    public void printStats() {
    	System.out.println("Move: " + bestMove); // + move
    	System.out.println("Value: " + value); // + value
    	System.out.println("Number of Nodes Visited: " + visited); // + total # of visited nodes
    	System.out.println("Number of Nodes Evaluated: " + evaluated); // + # evaluated nodes (either end game state or depth reached
    	System.out.println("Max Depth Reached: " + exploreddepth); // + max depth (root depth = 0)
    	System.out.println("Avg Effective Branching Factor: " + branch); // + the average number of successors that are not pruned 
    }

    /**
     * This function will start the alpha-beta search
     * @param state This is the current game state
     * @param depth This is the specified search depth
     */
    public void run(GameState state, int depth) {
    	maxdepth = depth;
    	value = alphabeta(state, 0, alpha, beta, state.maxPlayer()); // starting from root = last taken; if none has been taken, root = -1
    	branch = (double)(visited - 1)/(visited - evaluated);
    }

    /**
     * This method is used to implement alpha-beta pruning for both 2 players
     * @param state This is the current game state
     * @param depth Current depth of search
     * @param alpha Current Alpha value
     * @param beta Current Beta value
     * @param maxPlayer True if player is Max Player; Otherwise, false
     * @return int This is the number indicating score of the best next move
     */
    private double alphabeta(GameState state, int curdepth, double alpha, double beta, boolean maxPlayer) {
    	if(maxPlayer) {
    		return maxValue(state, alpha, beta, curdepth);
    	}
        return minValue(state, alpha, beta, curdepth);
    }
    
    private double maxValue(GameState state, double alpha, double beta, int curdepth) {
    	visited++;
    	int b = 0;
    	if(state.getMoves().isEmpty() || curdepth == maxdepth) { // if terminal: evaluate the value and return
    		evaluated++;
    		exploreddepth = Math.max(curdepth, exploreddepth);
    		return state.evaluate(); 
    	}
    	double score = Double.NEGATIVE_INFINITY;
    	List<GameState> succ = state.getSuccessors();
    	for(int i = 0; i < succ.size(); i++) {
    		GameState s = succ.get(i);
    		double childScore = minValue(s, alpha, beta, curdepth+1);
    		if (childScore > score) {
    			score = childScore;
    		}
    		if(childScore >= beta) {
    			return score;
    		}
    		if(childScore > alpha) {
    			alpha = childScore;
    			b = state.getMoves().get(i);
    		}
    	}
    	bestMove = b;
    	return score;
    }
    
    private double minValue(GameState state, double alpha, double beta, int curdepth) {
    	visited++;
    	int b = 0;
    	if(state.getMoves().isEmpty() || curdepth == maxdepth) {
    		evaluated++;
    		exploreddepth = Math.max(curdepth, exploreddepth);
    		return (-1)*state.evaluate();
    	}
    	double score = Double.POSITIVE_INFINITY;
    	List<GameState> succ = state.getSuccessors();
    	for(int i= 0; i < succ.size(); i++) {
    		GameState s = succ.get(i);
    		double child = maxValue(s, alpha, beta, curdepth+1);
    		if (child < score) { // child score < cur score		
    			score = child;    			
    		}
    		if(child <= alpha) { // child score <= alpha
    			return score;
    		}
    		if(child < beta) { // child score < beta
    			beta = child;
    			b = state.getMoves().get(i);
    		}
    	}
    	bestMove = b; // update the best next move
    	return score; // return cur score
    }
}
