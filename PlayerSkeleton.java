import java.util.Arrays;

public class PlayerSkeleton {
	static State s;

	public double score(int piece, int ori, int slot) {
		int[] top = s.getTop();
		int[] pbottom = State.getpBottom()[piece][ori];
		int[] ptop = State.getpTop()[piece][ori];
		int pheight = State.getpHeight()[piece][ori];

		// find height of bottom-most part after fall
		int bottomMost = top[slot] - pbottom[0];
		for (int i = 1; i < State.getpWidth()[piece][ori]; i++) {
			bottomMost = Math.max(bottomMost, top[slot + i] - pbottom[i]);
		}

		// find the maximum height of the block
		// and the number of holes created by the block
		int maxHeight = -1;
		int nHole = 0;
		for (int i = 0; i < State.getpWidth()[piece][ori]; i++) {
			maxHeight = Math.max(maxHeight, bottomMost + ptop[i]);
			nHole += bottomMost + pbottom[i] - top[slot + i];
		}

		// find out how many lines will the block clear
		int lineCleared = 0;
		for (int r = bottomMost; r < Math.min(State.ROWS, bottomMost + pheight); r++) {
			boolean full = true;
			for (int c = 0; c < State.COLS; c++) {
				if ((s.getField()[r][c] == 0)
						&& !fillSlot(r, c, slot, pbottom, ptop)) {
					full = false;
					break;
				}
			}
			if (full)
				lineCleared++;
		}

		return lineCleared - (maxHeight + bottomMost + nHole);
	}

	private boolean fillSlot(int r, int c, int slot, int[] pbottom, int[] ptop) {
		// muahahaha
		if (c < slot || c > slot + pbottom.length -1)
			return false;

		int x, ymin=0, ymax=0;
		x = slot;

		ymin = s.getTop()[x] + pbottom[c - slot];
		ymax = s.getTop()[x] + ptop[c - slot];

		if (r >= ymin && r < ymax)
			return true;

		return false;
	}

	// implement this function to have a working system
	public int[] pickMove(int[][] legalMoves) {
		int piece = s.getNextPiece();

		double maxScore = -1e12;
		int choiceOri = 0, choiceSlot = 0;
		for (int ori = 0; ori < State.getpOrients()[piece]; ori++) {
			int nPosSlot = State.COLS - State.getpWidth()[piece][ori] + 1;
			for (int slot = 0; slot < nPosSlot; slot++) {
				double curScore = score(piece, ori, slot);
				if (curScore > maxScore) {
					maxScore = curScore;
					choiceOri = ori;
					choiceSlot = slot;
				}
			}
		}

		score(piece, choiceOri, choiceSlot);
		int n[] = { choiceOri, choiceSlot };

		return n;
	}

	public static void main(String[] args) {
		s = new State();
		new TFrame(s);
		PlayerSkeleton p = new PlayerSkeleton();
		while (!s.hasLost()) {
			s.makeMove(p.pickMove(s.legalMoves()));
			s.draw();
			s.drawNext(0, 0);
			try {
				Thread.sleep(0);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("You have completed " + s.getRowsCleared()
				+ " rows.");
	}

}
