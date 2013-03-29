/**
 * Created with IntelliJ IDEA.
 * User: omer
 * Date: 3/22/13
 * Time: 3:10 AM
 * To change this template use File | Settings | File Templates.
 */
public class Features{
    private static int count = 0;
    
    /*
     * Ivan	: Check ref1.pdf page 33
     * 		  I'm going to implement the features listed over there
     * 		  I'm also going to change the features ordering to match the list
     */
    
    /*
     * 	NOTES ON FEATURES
     * 
     *  DELTA prefix: Abs(Feature(State) - Feature(NextState))
     *  
     *  ABS_HEIGHT_DIFF: Absolute difference between heights of adjacent columns
     *  Example: Column 1 have height 8, column 2 have height 10
     *  		 ABS_HEIGHT_DIFF between col 1 and col 2 = 2
     *  SUM_ABS_HEIGHT_DIFF: Sum of ABS_HEIGHT_DIFF of col 1-2, col 2-3, ..., col 9-10
     */
    
    final private static int CONSTANT	= count++;
    final private static int ROWS_CLEARED = count++;
    final private static int MAX_HEIGHT	= count++;
    final private static int DELTA_MAX_HEIGHT = count++;
    final private static int COVERED_GAPS = count++;
    final private static int DELTA_COVERED_GAPS = count++;
    final private static int AVG_HEIGHT = count++;
    final private static int DELTA_AVG_HEIGHT = count++;
    final private static int SUM_ABS_HEIGHT_DIFF = count++;
    final private static int DELTA_SUM_ABS_HEIGHT_DIFF = count++;

    final public static int NUMBER_FEATURES = count;
    private double[] curFeatures = new double[NUMBER_FEATURES];
    private double[] pastFeatures = new double[NUMBER_FEATURES];

    double[] weights = new double[NUMBER_FEATURES];
    {
    	// "Handcoded Player"
    	/*
    	 	weights = new double[] {
            0,
            2,
            0,
            -1,
            0,
            -4,
            0,
            0,
            0,
            -1
        	};
    	 */
    	
    	// "Learned Player"
    	/**/
        weights = new double[] {
            1946.70,
            388.43,
            -3.36,
            -4.82,
            -68.40,
            -111.74,
            -10.92,
            379.08,
            -22.02,
            -20.79
        };
        /**/
    }
    public double[] getFeatures(State s, NextState n, int[] move) {
        evaluateFeatures(s, pastFeatures);
        evaluateFeatures(n, curFeatures);
        
        curFeatures[ROWS_CLEARED] = curFeatures[ROWS_CLEARED] - pastFeatures[ROWS_CLEARED];
        curFeatures[DELTA_MAX_HEIGHT] = curFeatures[MAX_HEIGHT] - pastFeatures[MAX_HEIGHT];
        curFeatures[DELTA_COVERED_GAPS] = curFeatures[COVERED_GAPS] - pastFeatures[COVERED_GAPS];
        curFeatures[DELTA_AVG_HEIGHT] = curFeatures[AVG_HEIGHT] - pastFeatures[AVG_HEIGHT];
        curFeatures[DELTA_SUM_ABS_HEIGHT_DIFF] = curFeatures[SUM_ABS_HEIGHT_DIFF] - pastFeatures[SUM_ABS_HEIGHT_DIFF];
        return curFeatures;
    }
    public double score(State s, NextState ns, int[] move) {
        double[] f = this.getFeatures(s,ns,move);
        double total = 0;
        for (int i =0 ;i < f.length; i++) {
            double score = f[i]*weights[i];
            total+=score;
        }
        return total;
    }
    public void evaluateFeatures(State s, double[] fs) {
        int[][] field = s.getField();
        int[] top = s.getTop();
        double maxHeight = 0,
               minHeight = Integer.MAX_VALUE,
               sumHeight = 0;

        for (int i =0;i < State.COLS; i++) {
            maxHeight = Math.max(maxHeight, top[i]);
            minHeight = Math.min(minHeight, top[i]);
            sumHeight += top[i];
        }
        evaluateCells(s,fs,field,top);
        fs[CONSTANT] = 1;
        fs[ROWS_CLEARED] = s.getRowsCleared();
        fs[MAX_HEIGHT] = maxHeight;
        fs[AVG_HEIGHT] = (sumHeight/State.COLS);

        fs[SUM_ABS_HEIGHT_DIFF] = 0;
        
        for (int i = 0; i < State.COLS - 1; i++){
        	fs[SUM_ABS_HEIGHT_DIFF] += Math.abs(top[i] - top[i+1]);
        }
    }
    public void evaluateCells(State s, double[] fs, int [][] field, int[] top) {
        int coveredGaps = 0;
        for (int i= 0; i< State.ROWS; i++) {
            for (int j=0 ; j< State.COLS; j++) {
                if (i < top[j] && field[i][j] == 0)
                    coveredGaps++;

            }
        }
        fs[COVERED_GAPS] = coveredGaps;
    }

}
