
import java.util.*;
public class PlayerSkeleton {

    double[] curFeatures = new double[Features.NUMBER_FEATURES];
    double[] prevFeautures;
    private Features featureFn;
    private NextState ns = null;

    public PlayerSkeleton(){
    	featureFn = new Features();
    }
    
    public PlayerSkeleton(double[] w){
    	featureFn = new Features(w);
    }
    
	//implement this function to have a working system
	public int pickMove(State s, int[][] legalMoves) {
		if (ns == null) ns = new NextState();
        ns.resetState(s);
        int maxMove = maxUtilityMove(s,legalMoves,curFeatures);

        return maxMove;
                
	}
	public int maxUtilityMove(State s, int[][] legalMoves, double[] features) {
        double maxScore = Double.NEGATIVE_INFINITY;
        int start = (int)(Math.random()*legalMoves.length);
        int move = (start+1)%(legalMoves.length);
        int maxMove = move;
        double score;
        while (move != start) {
            ns.makeMove(move);
            if (! ns.hasLost()) {
                score = featureFn.score(s,ns, legalMoves[move]);
                if (maxScore < score) {
                    maxMove = move;
                    maxScore = score;
                }
            }
            ns.resetState(s);
            move = (move+1)%(legalMoves.length);
        }
        return maxMove;
    }
	public static void main(String[] args) {
		State s = new State();
		new TFrame(s);
		PlayerSkeleton p = new PlayerSkeleton();
		while(!s.hasLost()) {
			s.makeMove(p.pickMove(s,s.legalMoves()));
			s.draw();
			s.drawNext(0,0);
			try {
				Thread.sleep(0);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("You have completed "+s.getRowsCleared()+" rows.");
	}
	
}
