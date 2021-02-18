import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GameState {
    private int size;			 // The number of stones
    private int taken;			 // The number of stones already taken
    private boolean[] stones;    // Game state: true for available stones, false for taken ones
    private int lastMove;        // The last move
    private Helper helper  = new Helper();	// Helper to check prime number

    /**
     * Class constructor specifying the number of stones.
     */
    public GameState(int size) {

        this.size = size;
        //  For convenience, we use 1-based index, and set 0 to be unavailable
        this.stones = new boolean[(int) (this.size + 1)];
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
    	// 1st move: odd# stone
    	// remove the multiple/factor of last move
    	ArrayList<Integer> list = new ArrayList<Integer>();
    	if(taken == 0) {
    		for(int i=1; i < (double)size/2.0 ; i+=2) {
    			list.add(i);
    		}
    	}
    	else {
    		for(int i = 1; i <= size; i++) {
    			if(stones[i]) {
    				if(i % getLastMove() == 0 || getLastMove() % i == 0) {
    					list.add(i);
    				}
    			}
    		}
    	}
        return list;
    }


    /**
     * This method is used to generate a list of successors
     * using the getMoves() method
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
     * This method is used to evaluate a game state based on
     * the given heuristic function
     *
     * @return int This is the static score of given state
     */
    public double evaluate() { // compute the result in Max player's turn, and negate it in Min player's turn
    	int count = 0;
    	List<Integer> succ = getMoves();
    	int lastmove = getLastMove();
    	if(succ.isEmpty()) { // if no legal moves for the current state, the current player loses
    		return -1.0;
    	}
    	if(stones[1]) { // if 1 has not been taken, return 0
    		return 0;
    	}
    	if(lastmove == 1) { // if last move was 1, count legal moves: if odd return 0.5, else return -0.5
    		count = succ.size();
    		if(count % 2 == 0) {
    			return -0.5;
    		}
    		return 0.5;
    	}
    	if(helper.isPrime(lastmove)) { // if last move was prime, count multiples: if odd return 0.7, else return -0.7
    		for(int i = 0; i < succ.size(); i++) {
    			if(succ.get(i) % lastmove == 0) {
    				count++;
    			}
    		}
    		if(count % 2 == 0) {
    			return -0.7;
    		}
    		return 0.7;
    	}
    	else { // if last move was not prime, find largest prime factor and count multiples: if odd return 0.6, else return -0.6
    		int lpf = helper.getLargestPrimeFactor(lastmove);
    		for(int i = 0; i < succ.size(); i++) {
    			if(succ.get(i) % lpf == 0) {
    				count++;
    			}
    		}
    		if(count % 2 == 0) {
    			return -0.6;
    		}
    		return 0.6;
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
        this.taken++;
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
        return (int) this.size;
    }
    
    /**
     * This is maxPlayer method for player turn
     * @return true if it's max player's turn
     */
    public boolean maxPlayer() {
    	boolean flag;
    	if((this.taken % 2) == 0) {
    		flag = true;
    	}
    	else {
    		flag = false;
    	}
    	return flag;
    }

}	
