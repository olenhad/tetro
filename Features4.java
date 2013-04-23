/**
 * Created with IntelliJ IDEA.
 * User: omer
 * Date: 3/22/13
 * Time: 3:10 AM
 * To change this template use File | Settings | File Templates.
 */
import java.util.*;

public class Features4  {
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
//    final private static int ROWS_CLEARED_SQUARED = count++; /**/
//    final private static int MAX_HEIGHT	= count++; //took this out from fc1
//    final private static int DELTA_MAX_HEIGHT = count++;
//    final private static int MAX_HEIGHT_SQUARED = count++; /**/
    final private static int COVERED_GAPS = count++;

    // final private static int DELTA_COVERED_GAPS = count++;
    // final private static int COVERED_GAPS_SQUARED = count++; /**/
    final private static int AVG_HEIGHT = count++;
    // final private static int DELTA_AVG_HEIGHT = count++;
    // final private static int AVG_HEIGHT_SQUARED = count++; /**/
    final private static int SUM_ABS_HEIGHT_DIFF = count++;
    // final private static int DELTA_SUM_ABS_HEIGHT_DIFF = count++;
    // final private static int SUM_ABS_HEIGHT_DIFF_SQUARED = count++; /**/
    final private static int SUM_SQUARED_HEIGHT_DIFF = count++; /**/
    //final private static int STACKS_ON_EXISTING_GAP = count++; /**/
    final private static int ROW_TRANSITIONS = count++;
    final private static int COLUMN_TRANSITIONS = count++;
    //final private static int BLOCKADE = count++;
    final private static int IS_SUICIDE = count++;
//wells
     //is over a gap
    final public static int NUMBER_FEATURES = count;
    private double[] curFeatures = new double[NUMBER_FEATURES];
    private double[] pastFeatures = new double[NUMBER_FEATURES];

    double[] weights;
    public Features4(){
        Random random = new Random();

        weights =  new double[] {
            -9.601434011245342E-6, 6.220389129888194E-8, -7.25722989951555E-7, 3.87295004028969E-8, -7.113950335019432E-8, -2.128328195173797E-8, -9.528753577395118E-7, -2.057322654518275E-8, 0.21522552000444517
                };
    }
    
    public Features4(double[] w){
    	weights =  w;
    }
    
    public double[] getFeatures(State s, NextState n, int[] move) {
        evaluateFeatures(s, pastFeatures);
        evaluateFeatures(n, curFeatures);
        
        curFeatures[ROWS_CLEARED] = curFeatures[ROWS_CLEARED] - pastFeatures[ROWS_CLEARED];
    //    curFeatures[ROWS_CLEARED_SQUARED] = Math.pow(curFeatures[ROWS_CLEARED], 2);
    //    curFeatures[DELTA_MAX_HEIGHT] = curFeatures[MAX_HEIGHT] - pastFeatures[MAX_HEIGHT];
    //    curFeatures[MAX_HEIGHT_SQUARED] = Math.pow(curFeatures[MAX_HEIGHT], 2);
    //    curFeatures[DELTA_COVERED_GAPS] = curFeatures[COVERED_GAPS] - pastFeatures[COVERED_GAPS];
    //    curFeatures[COVERED_GAPS_SQUARED] = Math.pow(curFeatures[COVERED_GAPS], 2);
    //    curFeatures[DELTA_AVG_HEIGHT] = curFeatures[AVG_HEIGHT] - pastFeatures[AVG_HEIGHT];
    //    curFeatures[AVG_HEIGHT_SQUARED] = Math.pow(curFeatures[AVG_HEIGHT], 2);
    //    curFeatures[DELTA_SUM_ABS_HEIGHT_DIFF] = curFeatures[SUM_ABS_HEIGHT_DIFF] - pastFeatures[SUM_ABS_HEIGHT_DIFF];
    //    curFeatures[SUM_ABS_HEIGHT_DIFF_SQUARED] = Math.pow(curFeatures[SUM_ABS_HEIGHT_DIFF], 2);
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
       // fs[CONSTANT] = 1;
        fs[ROWS_CLEARED] = s.getRowsCleared();
        //fs[MAX_HEIGHT] = maxHeight;
        fs[AVG_HEIGHT] = (sumHeight/State.COLS);
        if (s.hasLost()) {
            fs[IS_SUICIDE] = Double.NEGATIVE_INFINITY;
        } else {
            fs[IS_SUICIDE] = 0.0;
        }
        

        fs[SUM_ABS_HEIGHT_DIFF] = 0;
        fs[SUM_SQUARED_HEIGHT_DIFF] = 0;

        for (int i = 0; i < State.COLS - 1; i++){
        	fs[SUM_ABS_HEIGHT_DIFF] += Math.abs(top[i] - top[i+1]);
            fs[SUM_SQUARED_HEIGHT_DIFF] += Math.pow(top[i] - top[i+1], 2);
        }
    }
    public void evaluateCells(State s, double[] fs, int [][] field, int[] top) {
        int coveredGaps = 0;
        int blockades = 0;
        int row_transitions = 0;
        int col_transitions = 0;
        for (int i= 0; i< State.ROWS; i++) {
            for (int j=0 ; j< State.COLS; j++) {
                if (field[i][j] == 0) {
                    if (i < top[j]) {
                        coveredGaps++;
                        blockades += top[j] - i;
                    }
                    try {
                        if (field[i][j+1] == 1) { //|| i == 0 || i == State.ROWS-1
                            row_transitions++;
                        } 
                    } catch (Exception e) {
                        //do nothign
                    }
                    try {
                        if (field[i][j-1] == 1) { //|| i == 0 || i == State.ROWS-1
                            row_transitions++;
                        }                     
                    } catch (Exception e) {
                        //do nothign
                    }
                    try {
                        if (field[i+1][j] == 1) { //|| i == 0 || i == State.ROWS-1
                            col_transitions++;
                        }                        
                    } catch (Exception e) {
                        //do nothign
                    }
                    try {
                        if (field[i-1][j] == 1) { //|| i == 0 || i == State.ROWS-1
                            col_transitions++;
                        }                      
                    } catch (Exception e) {
                        //do nothign
                    }
                }
            }
        }

        fs[COVERED_GAPS] = coveredGaps;
        fs[ROW_TRANSITIONS] = row_transitions;
        fs[COLUMN_TRANSITIONS] = col_transitions;
        //fs[BLOCKADE] = blockades;
    }

}
