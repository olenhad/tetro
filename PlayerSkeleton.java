
public class PlayerSkeleton {

    //implement this function to have a working system
    public int pickMove(State s, int[][] legalMoves) {
        //legalmoves[index of legal moves][ORIENT & SLOT]
        int move = 0;
        double heuristics = 0.0;
        double max = Double.NEGATIVE_INFINITY;
        int[] dummytop = new int[s.getTop().length];
        int[] backuptop= s.getTop(); //get state's top

        //create dummy top for simulation purpose
        for(int i=0;i<backuptop.length;i++){
            dummytop[i]=backuptop[i];
        }

        int[][] backupfield= s.getField(); //get the state's field
        int[][] dummyfield = new int[backupfield.length][backupfield[0].length];

        //create a dummy field for simulation purpose
        for(int i=dummyfield.length-1;i>-1;i--){
            for(int j=0;j<dummyfield[i].length;j++){
                dummyfield[i][j]=backupfield[i][j];
                //System.out.print(dummyfield[i][j]);
            }
            //System.out.println();
        }
        
        
        int nextPiece = s.getNextPiece();
        System.out.println("Piece no. ="+nextPiece);
        int slot;
        int orient;
        int[][][] pBottom = s.getpBottom();
        int[][][] pTop = s.getpTop();
        int[][] pWidth = s.getpWidth();
        int[][] pHeight = s.getpHeight();
        int lost=0;
        boolean movesConsidered=true;
        System.out.println("Legal Moves= "+legalMoves.length);

        
        for (int z = 0; z < legalMoves.length; z++) {
            //evaluate this moves with heuristics
            /////////////////////////////////////////////////////////
            orient = legalMoves[z][0];
            slot = legalMoves[z][1];
            //System.out.println("slot= "+slot);
            //System.out.println("orient= "+orient);
            //height if the first column makes contact
            int height = dummytop[slot] - pBottom[nextPiece][orient][0];
            //for each column beyond the first in the piece
            for (int c = 1; c < pWidth[nextPiece][orient]; c++) {
                height = Math.max(height, dummytop[slot + c] - pBottom[nextPiece][orient][c]);
            }

            //check if game ended
		if(height+pHeight[nextPiece][orient] >= 21) {
			lost = 1;
			movesConsidered=false;
		}

            //for each column in the piece - fill in the appropriate blocks
            for (int i = 0; i < pWidth[nextPiece][orient]; i++) {

                //from bottom to top of brick
                for (int h = height + pBottom[nextPiece][orient][i]; h < height + pTop[nextPiece][orient][i]; h++) {
                    if(movesConsidered && h>=0){ //if placing a piece cause overflow, don't consider that move
                        dummyfield[h][i + slot] = 1; //filled field marked with 1 here instead of turn
                    }
                }
            }

            //adjust top
            for (int c = 0; c < pWidth[nextPiece][orient]; c++) {
                dummytop[slot + c] = height + pTop[nextPiece][orient][c];
            }

            int rowsCleared = 0;

            //check for full rows - starting at the top
            for (int r = height + pHeight[nextPiece][orient] - 1; r >= height; r--) {
                //check all columns in the row
                boolean full = true;
                for (int c = 0; c < 10; c++) {
                    if(movesConsidered && r>=0){
                        if (dummyfield[r][c] == 0) {
                            full = false;
                            break;
                        }
                    }
                }
                //if the row was full - remove it and slide above stuff down
                if (full && movesConsidered) {
                    rowsCleared++;
                    //for each column
                    for (int c = 0; c < 10; c++) {

                        //slide down all bricks
                        for (int i = r; i < dummytop[c]; i++) {
                            if(i<20 && i>=0){
                                dummyfield[i][c] = dummyfield[i + 1][c];
                            }
                        }
                        //lower the top
                        dummytop[c]--;
                        while (dummytop[c] >= 1 && dummytop[c]<21 && dummyfield[dummytop[c] - 1][c] == 0) {
                            dummytop[c]--;
                        }
                    }
                }
            }

            //check for holes
            int holes=0;
            int blockades=0;
            for (int c = 0; c < 10; c++) {
                if (dummytop[c] < 20) {
                    for (int i = dummytop[c]; i > -1; i--) {
                        if (dummyfield[i][c] == 0 && i < dummytop[c]) {
                            holes++;
                            //check for blockade(s) above the hole
                            for (int k = i + 1; k < dummytop[c]; k++) {
                                if (dummyfield[k][c] != 0) {
                                    blockades++;
                                } else {
                                    k = 20; //stop counting blocks
                                }
                            }
                        }
                    }
                }
            }
            ////////////////////////////////////////////////////////////////
            System.out.println(z);
            if(movesConsidered){
                System.out.println("entered");
                int maxTop = 0;
                for (int k = 0; k < dummytop.length; k++) {
                    if (dummytop[k] > maxTop) {
                        maxTop = dummytop[k];
                    }
                }
                System.out.println("maxTop= "+maxTop);
                heuristics = ((-3.78) * maxTop) + ((1.6) * rowsCleared)+ ((-2.31)*holes)+((-0.59)*blockades);
                if (Double.compare(heuristics,max)>0) {
                    max = heuristics;
                    move = z;
                }
            }

            //print debugger
            /*
            System.out.println("After simulating a move");
            for (int i = dummyfield.length - 1; i > -1; i--) {
                for (int j = 0; j < dummyfield[i].length; j++) {
                    System.out.print(dummyfield[i][j]);
                }
                System.out.println();
            }*/
            
            //restore dummyfield to original after simulation
            //System.out.println("Dummyfield after copying backupfield");
            for (int i = dummyfield.length - 1; i > -1; i--) {
                for (int j = 0; j < dummyfield[i].length; j++) {
                    dummyfield[i][j] = backupfield[i][j];
                    //System.out.print(dummyfield[i][j]);
                }
                //System.out.println();
            }

            //restore dummytop to original after simulation
            for (int i = 0; i < backuptop.length; i++) {
                dummytop[i] = backuptop[i];
            }
            movesConsidered=true;
            lost=0;
        }
        System.out.println("Move= "+move);
        return move;
    }
	
	public static void main(String[] args) {
		State s = new State();
		new TFrame(s);
		PlayerSkeleton p = new PlayerSkeleton();
                int piececount=0;
		while(!s.hasLost()) {
			s.makeMove(p.pickMove(s,s.legalMoves()));
			piececount++;
                        s.draw();
			s.drawNext(0,0);
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
                System.out.println("Total piece dropped: "+piececount);
		System.out.println("You have completed "+s.getRowsCleared()+" rows.");
	}
	
}
