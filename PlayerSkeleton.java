import java.util.*;
import java.lang.*;

public class PlayerSkeleton {
	int piece;
	int[] pWidth;
	int[] pHeight;
	int[][] pBottom;
	int[] top;
	int[] difference;

	//implement this function to have a working system
	public int[] pickMove(State s, int[][] legalMoves) {
		System.out.println("making a move for piece " + s.getNextPiece());
		piece = s.getNextPiece();
		pBottom = s.getpBottom()[piece];
		pWidth = s.getpWidth()[piece];
		pHeight = s.getpHeight()[piece];

		top = s.getTop();
		difference = new int[top.length];
		difference[0] = -1; //or zero?
		for (int i = 1; i < top.length; i++) {
			difference[i] = top[i] - top[i-1];
		}
		return findMatch();
		// }
	}
	//try a see Pattern lower, possibly with match pattern lower
	public int[] seePattern(int[] pattern, int[] row) {
		int[] matched = new int[row.length-pattern.length];
		for (int i = 0; i < row.length - pattern.length; i++) {
			matched[i] = 0;
			for (int j = 0; j < pattern.length; j++) {
				if (pattern[j] == row[i+j]) {
					if (j == (pattern.length - 1)) {
						matched[i] = 1; //mark the first column entry as positive
					}
					continue;
				} else {
					break;
				}
			}
		}
		//re-sizing the match
		int count = 0;
		for (int i = 0; i < matched.length; i++) {
			if (matched[i] == 1) {
				count ++;
			}
		}

		if (count == 0) {
			return new int[] {-1};
		}

		int[] new_matched = new int[count];
		count = 0;
		for (int i = 0; i < matched.length; i++) {
			if (matched[i] == 1) {
				new_matched[count] = i;
				count ++;
			}
		}
		return new_matched;
	}

	public int checkPattern(int[] matched) {
		int count = 0;
		for (int i = 0; i < matched.length; i++) {
			if (matched[i] == 1) {
				count ++;
			}
		}
		return count;
	}

	public int findlowest() {
		int min = 40; //highest column is 31?
		int space = 9;
		for (int i = 0; i < top.length; i++) {
			if (top[i] <= min) {
				min = top[i];
				space = i;
			}
		}
		return space;
	}

	public int[] placeLowest() {
		int lowheight = 44;
		int col = 0;
		int orient = 0;
		for (int i = 0; i < top.length; i++) {
			for (int j = 0; j < pWidth.length; j++) {
				if (((pWidth[j]+i-1) < 10) && ((pHeight[j]+top[i]) < lowheight)) {//find the column with an orientation that will result in the lowest height
					lowheight = pHeight[j]+top[i];
					col = i;
					orient = j;
				}
			}
		}
		return new int[]{orient, col};
	}

	public int[] greatestDifference() {
		int max = 0;
		int space = 0;
		for (int i = 1; i < difference.length; i++) {
			if (Math.abs(difference[i]) >= max) {
				max = difference[i];
				space = i;
			}
		}
		return new int[]{max, space};
	}

	public String printPattern(int[] pattern) {
		String pat = "";
		for (int i = 0; i < pattern.length; i++) {
			pat = pat + pattern[i] + " ";
		}
		return pat;
	}
	public int matchPattern(int[] pattern) {//, int[] top) { //, int orientation) {
		int[] see = seePattern(pattern,difference);
		//figure out how to match a particular pattern to the base
		if (see.length == 1) {
			if (see[0] == -1) {
				return -1;
			} else {
				System.out.println("pattern [" + printPattern(pattern) + "] matched only on one occurrence");
				return see[0];
			}
		} else {//more than one possible fit
			// if (piece == 0) {
			// 	return an even space.
			// }
			//pick one randomly, for now
			//return see[(int)Math.random()*see.length];
			//find the lowest best fit
			int maxHeight = 33;
			int best = see[(int)Math.random()*see.length]; 
			for (int i = 0; i < see.length; i++) {
				if (top[see[i]] < maxHeight) {
					maxHeight = top[see[i]];
					best = see[i];
				}
			}
			return best;
		}
			
	}

	public int[] minusAbsolute(int[] values, int add) {
		int[] valueadd = new int[values.length];
		for (int i = 0; i < values.length; i++) {
			valueadd[i] = values[i] + add;
		}
		return valueadd;
	}

	public boolean matches(int[] pattern1, int[] pattern2) {//assumes they are the same length/pattern1 is shorter than pattern2
		boolean bool = true;
		for (int i = 0; i < pattern1.length; i++) {
			if (pattern1[i] == pattern2[i]){
				continue;
			} else {
				bool = false;
				return bool;
			}
		}
		return bool;
	}
	public void printcase(char cl, int sth, int space, int orient) {
		System.out.println("System has passed case " + cl + " of case " + sth + " with space = " + space + " and orient = " + orient);
	}

	public int globalDiff() {
		int max = 0;
		int min = 44;
		for (int i = 0; i < top.length; i++) {
			if (top[i] > max){
				max = top[i];
			}
			if (top[i] < min) {
				min = top[i];
			}
		}
		return (max-min);
	}
	public int[] findMatch() {
		int orient = 0;
		int space = -1;
		int count = 0;
		int greatHeight = 40;
		int[] pair = new int[2];
		//look for maximum difference column, attempt to fit piece there
		int[] maxDiff = greatestDifference();
		//if (globalDiff() > 3) { //quite a large gap, attempt to fill it first
		//	return placeLowest();
		// 	//between place lowest, and find lowest?
		// 	pair = placeLowest();
		// 	orient = pair[0];
		// 	space = pair[1];
		// 	System.out.println("max diff was very high ..placed orient = " + orient + " space = " + space);
		// 	return pair;
		// } else 
		// 	System.out.println("max diff was very high ..");
		//} else {
			if (space < 0) {
				//pair = placeLowest();
				int lowestcol = findlowest();
				System.out.println("from findlowest, lowest column currenttly is: " + lowestcol);
				int width = 0;
				for (int j = 0; j < pWidth.length; j++) {
					width = pWidth[j];
					for (int i = lowestcol-width+1; i < lowestcol+width; i++) {
						if ((i >= 0) && ((i + width - 1) < top.length)) {//or do a try?
							if (matches(pBottom[j], Arrays.copyOfRange(minusAbsolute(top, pBottom[j][0] - top[i]), i, i+width))) {
								orient = j;
								space = i;
								//greatHeight = 
								System.out.println("frm findlowest: space = " + space + " orient = " + orient);
							}
						}
					}
				}
				System.out.println("System exits findlowest with: space = " + space + " orient = " + orient);
				//make sure it isn't blocking off a big hole
				// int holes = 0;
				// int peak = 0;
				// int base_col = space;
				// for (int i = space; i < pWidth[orient] + space; i++) {
				// 	if (top[i] > peak) {
				// 		top[i] = peak;
				// 	}
				// }
				// for (int i = space; i < pWidth[orient] + space; i++) {
				// 	if (top[i] > peak) {
				// 		top[i] = peak;
				// 	}
				// }
				// int[] minused = Arrays.copyOfRange(minusAbsolute(top, pBottom[j][0] - top[i]), i, i+width)

				// for (int i = space; i < pWidth[orient] + space; i++) {
				// 	if (pBottom[i]) {
				// 		top[i] = peak;
				// 	}
				// }
				//compare findlowest and placelowest
				// if ((top[pair[1]] + pHeight[pair[0]]) > (top[space] + pHeight[orient])) {
				// 	System.out.println("used place lowest");
				// 	return pair;
				// } else {
				// 	System.out.println("used find lowest");
				// 	return new int[] {orient, space};
				// }
			}
		//} else {
			switch(piece) {
				//one issue right now is counts can be more negative (or positive), there is no maximum
				//possible to look for greatest (pos and neg) depth + match pattern at specific indices - 2b implemented l8r
				case 0: 
					if (space < 0) {space = matchPattern(new int[] {0});}
					System.out.println("System has passed case a of case 0 with space = " + space);
					break;
					//printcase('a', 0, space, orient);
				case 1: 
					count = -5; //-4
					while ((space < 0) && (count < 1)) {
						space = matchPattern(new int[] {count});
						count ++;
					} // could also implement the vertical to put to the sides of the board
					System.out.println("System has passed case a of case 1 with space = " + space);
					if (space < 0) {
						orient = 1;
						space = matchPattern(new int[] {0, 0, 0}) - 1;
					}			
					System.out.println("System has passed case b of case 1 with space = " + space);
					break;
				case 2:
					count = -4; //-3
					while ((space < 0) && (count < 0)) {
						space = matchPattern(new int[] {count, 0});
						count ++;
					}
					System.out.println("System has passed case a of case 2 with space = " + space);
	 				if (space < 0) {
						orient = 1;
						space = matchPattern(new int[] {1, 0}) - 1;
					}
					System.out.println("System has passed case b of case 2 with space = " + space);
					if (space < 0) {
						orient = 2;
						space = matchPattern(new int[] {-2}) - 1;
					}
					System.out.println("System has passed case c of case 2 with space = " + space);
					count = 3; //2
					while ((space < 0) && (count > -1)) {
						orient = 3;
						space = matchPattern(new int[] {0, 0, count}) - 1;
						count --;
					}
					System.out.println("System has passed case d of case 2 with space = " + space);
					// if (space == -1) {
					// 	orient = 3;
					// 	space = matchPattern(new int[] {0, 0});
					// }
					break;
				case 3:
					count = -4; //-3
					while ((space < 0) && (count < 0)) {
						space = matchPattern(new int[] {0, count}) - 1;
						count ++;
					}
					System.out.println("System has passed case a of case 3 with space = " + space);
					if (space < 0) {
						orient = 2;
						space = matchPattern(new int[] {2}) - 1;
					}
					System.out.println("System has passed case b of case 3 with space = " + space);
					if (space < 0) {
						orient = 3;
						space = matchPattern(new int[] {0, -1}) - 1;
					}
					System.out.println("System has passed case c of case 3 with space = " + space);
					count = -3; //-2
					while ((space < 0) && (count < 1)) {
						orient = 1;
						space = matchPattern(new int[] {count, 0, 0});
						count ++;
					}
					System.out.println("System has passed case d of case 3 with space = " + space);
					break;
				case 4:
					if (space < 0) { orient = 1; space = matchPattern(new int[] {-1, 1}) - 1;}
					System.out.println("System has passed case a of case 4 with space = " + space);
					count = -4; //-3
					while ((space < 0) && (count < 0)) {
						orient = 0;
						space = matchPattern(new int[] {count, 1});
						count ++;
					}
					System.out.println("System has passed case b of case 4 with space = " + space);
					count = 4; //3
					while ((space < 0) && (count > -1)) {
						orient = 2;
						space = matchPattern(new int[] {-1, count}) - 1;
						count --;
					}	
					System.out.println("System has passed case c of case 4 with space = " + space);			
					if (space < 0) {
						orient = 3;
						space = matchPattern(new int[] {0, 0}) - 1;
					}
					System.out.println("System has passed case d of case 4 with space = " + space);
					break;
				case 5:
					if (space < 0) {
						space = matchPattern(new int[] {0, 1}) - 1;
					}
					System.out.println("System has passed case a of case 5 with space = " + space);
					// count = -4; //-2
					// while ((space < 0) && (count < 1)) {
					// 	orient = 1;
					// 	space = matchPattern(new int[] {count, -1});
					// 	count ++;
					// }
					if ((space < 0)) {
						orient = 1;
						space = matchPattern(new int[] {-1}) - 1;
					}
					System.out.println("System has passed case b of case 5 with space = " + space);
					break;
				case 6:
					if (space < 0) {space = matchPattern(new int[] {-1, 0}) - 1;}
					System.out.println("System has passed case a of case 6 with space = " + space);
					if (space < 0) {
						orient = 1;
						space = matchPattern(new int[] {1}) - 1;
					}
					System.out.println("System has passed case b of case 6 with space = " + space);
					break;
			}
		//}
		if (space < 0) {
			// switch (piece) {//non-perfect matches
			// 	case 0:
			// 	case 1:
			// 	case 2:
			// 	case 3:
			// 	case 4:
			// 	case 5:
			// 	case 6:

			// }
				//randomly place 
			System.out.println("piece " + piece + " was lowest placed");
			// space = findlowest(); //or lowest minus 2, needs to ensre can fit length and sides
			// //space = (int)Math.random()*10;
			// 	//randomly orient
			// orient = (int)Math.random()*pBottom.length;
			pair = placeLowest();
			orient = pair[0];
			space = pair[1];
		}
	
		System.out.println("orient = " + orient + " and slot = " + space);
		return new int[]{orient, space};
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
				Thread.sleep(300);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("You have completed "+s.getRowsCleared()+" rows.");
	}
	
}

//thigns to do: find difference in height. 
