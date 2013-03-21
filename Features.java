/**
 * Created with IntelliJ IDEA.
 * User: omer
 * Date: 3/22/13
 * Time: 3:10 AM
 * To change this template use File | Settings | File Templates.
 */
public class Features{
    private static int count = 0;
    final private static int MAX_HEIGHT	= count++;
    final private static int COVERED_GAPS= count++;
    final private static int DELTA_ROWS_COMPLETED= count++;
    final private static int DELTA_AVG_HEIGHT = count++;
    final private static int MAX_MIN_DIFF = count++;

    final public static int NUMBER_FEATURES = count;
    private double[] curFeatures = new double[NUMBER_FEATURES];
    private double[] pastFeatures = new double[NUMBER_FEATURES];

    double[] weights = new double[NUMBER_FEATURES];
    {
        weights = new double[] {
            -0.5,
            -1.0,
            17,
            -0.2,
            -1.0
        };
    }
    public double[] getFeatures(State s, NextState n, int[] move) {
        evaluateFeatures(s, pastFeatures);
        evaluateFeatures(n, curFeatures);

        curFeatures[DELTA_AVG_HEIGHT] = curFeatures[DELTA_AVG_HEIGHT] - pastFeatures[DELTA_AVG_HEIGHT];
        curFeatures[DELTA_ROWS_COMPLETED] = n.getRowsCleared() - s.getRowsCleared();
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
        fs[MAX_HEIGHT] = maxHeight;
        fs[MAX_MIN_DIFF] = maxHeight - minHeight;
        fs[DELTA_AVG_HEIGHT] = (sumHeight/State.COLS);

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
