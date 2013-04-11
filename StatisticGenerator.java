import java.util.*;

public class StatisticGenerator {
	public final static int SAMPLE_SIZE = 100;
	public final static int BREED_ITERATION = 10000;
	public static double HIGH_P_THRES = 88;
	public static double LOW_P_THRES = 99;

	public static Random randGen = new Random();

	public static double[] getStats(double[] weights) {
		int[] scoreList = new int[SAMPLE_SIZE];
		double avgScore = 0, varScore = 0, stdDevScore = 0;

		for (int i = 0; i < SAMPLE_SIZE; i++) {
			State s = new State();
			PlayerSkeleton p = new PlayerSkeleton(weights);
			while (!s.hasLost()) {
				s.makeMove(p.pickMove(s, s.legalMoves()));
			}
			scoreList[i] = s.getRowsCleared();
			avgScore += scoreList[i];
		}

		avgScore = avgScore / ((double) SAMPLE_SIZE);
		for (int i = 0; i < SAMPLE_SIZE; i++) {
			varScore += (scoreList[i] - avgScore) * (scoreList[i] - avgScore)
					* 1.0;
		}
		varScore = varScore / SAMPLE_SIZE * 1.0;
		stdDevScore = Math.sqrt(varScore);
	
		return new double[] { avgScore, varScore, stdDevScore };
	}

	static boolean better(double[] score1, double[] score2) {
		return score1[0] > score2[0];
	}

	public static void main(String[] args){
		double[] wRef1 =  new double[] {
                1946.70,		/* CONSTANT */
                388.43,			/* ROWS_CLEARED */
                -3.36,			/* MAX_HEIGHT */
                -4.82,			/* DELTA_MAX_HEIGHT */
                -68.40,			/* COVERED_GAPS */
                -111.74,		/* DELTA_COVERED_GAPS */
                -10.92,			/* AVG_HEIGHT */
                379.08,			/* DELTA_AVG_HEIGHT */ 
                -22.02,			/* SUM_ABS_HEIGHT_DIFF */
                -20.79			/* DELTA_SUM_ABS_HEIGHT_DIFF */ 
        		};
		double[] wHand =  new double[] {
                0,		/* CONSTANT */
                100,			/* ROWS_CLEARED */
                -5,			/* MAX_HEIGHT */
                -5,			/* DELTA_MAX_HEIGHT */
                -150,		/* COVERED_GAPS */
                -200,		/* DELTA_COVERED_GAPS */
                -5,			/* AVG_HEIGHT */
                -100,			/* DELTA_AVG_HEIGHT */ 
                -30,		/* SUM_ABS_HEIGHT_DIFF */
                -20			/* DELTA_SUM_ABS_HEIGHT_DIFF */ 
        		};
		double[] gen1 = {486.675, 100.0, -3.36, -4.82, -68.4, -200.0, -5.0, -100.0, -30.0, -20.0};
		double[] gen2 = {105.43253815742321, 30.83704520472885, -0.9297627843871829, -1.1841151106423722, -68.4, -200.0, -5.0, -100.0, -30.0, -20.0};
		double[] gen3 = {32.48581083059224, 27.23001322384344, -0.25561010883341245, -1.1841151106423722, -68.4, -200.0, -5.0, -100.0, -30.0, -20.0};
		double[] gen4 = {0.9266257041073647, 0.837484482310802, -0.005298893029129517, -0.043717150606703115, -1.5733348100669897, -5.404692237085569, -0.13511509864342033, -2.414533892953383, -1.0021306562633423, -0.6055850494278614};
		double[] w1 = gen3, w2 = gen4;
		double[] goodPlayer = null, badPlayer = null, goodScore = null, badScore = null;
		
		double bestScore = 0;
		double[] bestPlayer = null;
		for(int i=0;i < BREED_ITERATION; i++){
			System.out.println("Starting generation " + i + "...");
			double[] score1 = getStats(w1);
			double[] score2 = getStats(w2);
			
			if(score1[0] > bestScore){
				bestScore = score1[0];
				bestPlayer = w1;
			}
			if(score2[0] > bestScore){
				bestScore = score2[0];
				bestPlayer = w2;
			}
			
			boolean changed = false;
			if(score1[0] < 150){
				w1 = bestPlayer;
				changed = true;
			}
			if(score2[0] < 150){
				w2 = bestPlayer;
				changed = true;
			}
			if(changed) continue;
			
			if(better(score1,score2)) {
				goodPlayer = w1; 
				goodScore = score1;
				badPlayer = w2;
				badScore = score2;
			} else {
				goodPlayer = w2; 
				goodScore = score2;
				badPlayer = w1;
				badScore = score1;
			}

			//HIGH_P_THRES = goodScore[0] / badScore[0] * 100.0;
			//LOW_P_THRES = 100 - HIGH_P_THRES - 1;
						
			for(int j=0;j<10;j++){
				int p = randGen.nextInt(101);
				w1[j] = p <= HIGH_P_THRES ? (goodPlayer[j] * (HIGH_P_THRES/100) + badPlayer[j] * ((100-LOW_P_THRES)/100))  : 
						(p <= LOW_P_THRES ? (badPlayer[j] * (HIGH_P_THRES/100) + goodPlayer[j] * ((100-LOW_P_THRES)/100)) : 
						randGen.nextGaussian() * goodPlayer[j]);
				p = randGen.nextInt(101);
				w2[j] = p <= HIGH_P_THRES ? (goodPlayer[j] * (HIGH_P_THRES/100) + badPlayer[j] * ((100-LOW_P_THRES)/100)) : 
						(p <= LOW_P_THRES ? (badPlayer[j] * (HIGH_P_THRES/100) + goodPlayer[j] * ((100-LOW_P_THRES)/100)) :
							randGen.nextGaussian() * goodPlayer[j]);
			}
			
			printStats(goodPlayer, badPlayer, goodScore, badScore);
			System.out.println("Best player of avg score: " + bestScore);
			System.out.println("stats: " + Arrays.toString(bestPlayer));
		}
		
		System.out.println("Results after " + BREED_ITERATION + " generations:");
		printStats(goodPlayer, badPlayer, goodScore, badScore);
		
		System.out.println("Best player of avg score: " + bestScore);
		System.out.println("stats: " + Arrays.toString(bestPlayer));
	}
	
	static void printStats(double[] goodPlayer, double[] badPlayer, double[] goodScore, double[] badScore){
		System.out.print("GoodPlayer stats:\nweights: ");
		for(int i=0;i<10;i++){
			System.out.print(goodPlayer[i] + ", ");
		}
		System.out.println("\nAvg Score: " + goodScore[0]);
		System.out.println("Variance: " + goodScore[1]);
		System.out.println("Std Dev: " + goodScore[2]);
		
		System.out.print("\nBadPlayer stats:\nweights: ");
		for(int i=0;i<10;i++){
			System.out.print(badPlayer[i] + ", ");
		}
		System.out.println("\nAvg Score: " + badScore[0]);
		System.out.println("Variance: " + badScore[1]);
		System.out.println("Std Dev: " + badScore[2]);
		System.out.println();
	}
}