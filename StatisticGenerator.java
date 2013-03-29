import java.util.*;

public class StatisticGenerator{
	public final static int SAMPLE_SIZE = 100;
	
	public static void main(String[] args){
		int[] scoreList = new int[SAMPLE_SIZE];
		double avgScore = 0;
		double varScore = 0;
		
		for(int i = 0; i < SAMPLE_SIZE; i++){
			State s = new State();
			//new TFrame(s);
			PlayerSkeleton p = new PlayerSkeleton();
			while(!s.hasLost()) {
				s.makeMove(p.pickMove(s,s.legalMoves()));
				//s.draw();
				//s.drawNext(0,0);
				try {
					Thread.sleep(5);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			scoreList[i] = s.getRowsCleared();
			avgScore += scoreList[i];
			System.out.println("Iteration " + (i+1) + ": " + scoreList[i]);
		}
		
		System.out.println();
		
		avgScore = avgScore / (SAMPLE_SIZE * 1.0);
		
		for(int i = 0; i < SAMPLE_SIZE; i++){
			varScore += (scoreList[i] - avgScore)*(scoreList[i] - avgScore)*1.0;
		}
		
		varScore = varScore/SAMPLE_SIZE*1.0;
		
		System.out.println("Average: " + avgScore);
		System.out.println("Variance: " + varScore);
		System.out.println("Std dev: " + Math.sqrt(varScore));
	}
}