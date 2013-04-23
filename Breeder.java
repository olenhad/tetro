import java.util.*;

public class Breeder {
	public final static int SAMPLE_SIZE = 100;
	public final static int BREED_ITERATION = 100;
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
		
		int NGEN = 8;
		double[][] gen = new double[NGEN][];
		gen[0] = new double[] {-9.770756747569378, 7.536096948211782, -0.15344104005358817, 0.35994719334166974, -3.1346459740890284, -9.165803729592603, -0.2283504662987085, 3.999204134104953, -1.5007260553728094, -0.91653199687301};
		//gen[1] = new double[] {486.675, 100.0, -3.36, -4.82, -68.4, -200.0, -5.0, -100.0, -30.0, -20.0};
		//gen[2] = new double[] {105.43253815742321, 30.83704520472885, -0.9297627843871829, -1.1841151106423722, -68.4, -200.0, -5.0, -100.0, -30.0, -20.0};
		//gen[1] = new double[] {32.48581083059224, 27.23001322384344, -0.25561010883341245, -1.1841151106423722, -68.4, -200.0, -5.0, -100.0, -30.0, -20.0};
		gen[1] = new double[] {0.9266257041073647, 0.837484482310802, -0.005298893029129517, -0.043717150606703115, -1.5733348100669897, -5.404692237085569, -0.13511509864342033, -2.414533892953383, -1.0021306562633423, -0.6055850494278614};
		gen[2] = new double[] {0.021596608846013502, 0.014249672906482562, -1.3661955331280978E-4, -9.534315780956981E-4, -0.03915677305256805, -0.09241272364259767, -0.0031985161670923736, -0.05915856517255117, 0.03467310044516623, -0.02260961512491643};
		gen[3] = new double[] {3.158594275364314, 2.6475732539183303, -0.024852962134660882, 0.08716397231688221, -6.650529659289416, -19.44599315581701, -0.4861498288954254, -9.722996577908505, -2.9168989733725503, -1.9445993155817016};
		gen[4] = new double[] {2.5019225255160737, 2.0971427744287094, -0.019686031306864884, 0.0690425824722024, -5.267884543123147, -15.403171178722655, -0.38507927946806647, -7.701585589361327, -2.310475676808397, -1.5403171178722659};
		
		// avg 1036 stddev 1013
		gen[5] = new double[] {-7.720365371906888, 5.940915554719386, -0.12124613960138278, 0.27983173173592785, -2.477321983110656, -7.227672112968805, -0.14518808292558213, 3.1585370155208734, -1.1860124403007994, -0.7243385364234114};
		// 868 676
		gen[6] = new double[] {-6.784655270236987, 5.236380532976169, -0.10674959177950814, 0.24581475242154946, -1.4093078526900575, -6.414398381783404, -0.12911666396094648, 2.755367234728835, -1.053712254027337, -0.6434737625468806};
		//942 906
		gen[7] = new double[] {-5.374125439554717, 4.147737020170424, -0.08455635164854841, 0.19470986539310936, -1.1233123227966717, -5.080844958210634, -0.10227330952346571, 2.1825263866287106, -0.8346454764150536, -0.5096955673133842};
		
		double[] w1 = gen[NGEN - 1], w2 = gen[0];
		double[] goodPlayer = null, badPlayer = null, goodScore = null, badScore = null;
		
		double bestScore = 0;
		double[] bestPlayer = null;
		for(int i=0;i < BREED_ITERATION; i++){
			System.out.println("Starting generation " + i + "...");
			System.out.println("\nBest player so far of avg score: " + bestScore);
			System.out.println("stats: " + Arrays.toString(bestPlayer));
			System.out.println();
			double[] score1 = getStats(w1);
			double[] score2 = getStats(w2);
			
			if(score1[0] > bestScore){
				bestScore = score1[0];
				bestPlayer = Arrays.copyOf(w1, 10);
			}
			if(score2[0] > bestScore){
				bestScore = score2[0];
				bestPlayer = Arrays.copyOf(w2, 10);;
			}
			
			boolean changed = false;
			if(score1[0] < bestScore/5){
				w1 = Arrays.copyOf(bestPlayer, 10);
				w2 = Arrays.copyOf(gen[randGen.nextInt(NGEN)], 10);
				changed = true;
			}
			if(score2[0] < bestScore/5){
				w1 = Arrays.copyOf(gen[randGen.nextInt(NGEN)], 10);
				w2 = Arrays.copyOf(bestPlayer, 10);
				changed = true;
			}
			if(changed) continue;
			
			if(better(score1,score2)) {
				goodPlayer = Arrays.copyOf(w1, 10); 
				goodScore = score1;
				badPlayer = Arrays.copyOf(w2, 10);
				badScore = score2;
			} else {
				goodPlayer = Arrays.copyOf(w2, 10);
				goodScore = score2;
				badPlayer = Arrays.copyOf(w1, 10);
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