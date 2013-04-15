import java.util.*;

public class StatisticGenerator{
	public final static int SAMPLE_SIZE = 1000;
	public static double[] w = 
		{-7.720365371906888, 
		5.940915554719386, 
		-0.12124613960138278, 
		0.27983173173592785, 
		-2.477321983110656, 
		-7.227672112968805, 
		-0.14518808292558213, 
		3.1585370155208734, 
		-1.1860124403007994, 
		-0.7243385364234114};

	public static void main(String[] args){
		int[] scoreList = new int[SAMPLE_SIZE];
		double avgScore = 0;
		double varScore = 0;

		for(int i = 0; i < SAMPLE_SIZE; i++){
			State s = new State();
			PlayerSkeleton p = new PlayerSkeleton(w);
			while(!s.hasLost()) {
				s.makeMove(p.pickMove(s,s.legalMoves()));
			}

			scoreList[i] = s.getRowsCleared();
			avgScore += scoreList[i];
			System.out.println("Iteration " + (i+1) + ": " + scoreList[i]);
		}

		System.out.println();

		avgScore = avgScore / (SAMPLE_SIZE * 1.0);

		int max = Integer.MIN_VALUE, min = Integer.MAX_VALUE;
		for(int i = 0; i < SAMPLE_SIZE; i++){
			max = Math.max(scoreList[i], max);
			min = Math.min(scoreList[i], min);
			varScore += (scoreList[i] - avgScore)*(scoreList[i] - avgScore)*1.0;
		}

		varScore = varScore/SAMPLE_SIZE*1.0;

		System.out.println("Max: " + max + " Min: " + min);
		System.out.println("Average: " + avgScore);
		Arrays.sort(scoreList);
		System.out.println("Median: " + scoreList[SAMPLE_SIZE/2]);
		System.out.println("Variance: " + varScore);
		System.out.println("Std dev: " + Math.sqrt(varScore));
	}
}