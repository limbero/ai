import java.util.*;

public class Player {
    GameState bestState;
    boolean weAreRed;
    /**
    * Performs a move
    *
    * @param pState
    *            the current state of the board
    * @param pDue
    *            time before which we must have returned
    * @return the next state the board is in after our move
    */
    public GameState play(final GameState pState, final Deadline pDue) {

        Vector<GameState> lNextStates = new Vector<GameState>();
        pState.findPossibleMoves(lNextStates);

        if (lNextStates.size() == 0) {
            // Must play "pass" move if there are no other moves possible.
            return new GameState(pState, new Move());
        }

        /**
        * Here you should write your algorithms to get the best next move, i.e.
        * the best next state. This skeleton returns a random move instead.
        */

        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;
        if (pState.getNextPlayer() == Constants.CELL_RED) {
            weAreRed = true;
        } else {
            weAreRed = false;
        }
        negaMax(pState, 10, 1, alpha, beta);
        return bestState;
    }

    public int negaMax(final GameState pState, int depth, int color, int alpha, int beta) {
        if (depth == 0 || pState.isEOG())
            return color * evaluateBoard(pState);

        Vector<GameState> lNextStates = new Vector<GameState>();
        pState.findPossibleMoves(lNextStates);

        int bestScore = Integer.MIN_VALUE;
        bestState = lNextStates.get(0);

        for (GameState lNextState : lNextStates) {
            int val = -negaMax(lNextState, depth-1, -color, -beta, -alpha);
            bestScore = val > bestScore ? val : bestScore;
            bestState = lNextState;

            alpha = val > alpha ? val : alpha;
            if (beta <= alpha)
                break;
        }
        return bestScore;
    }

    public int evaluateBoard(GameState pState) {
        if (weAreRed && pState.getNextPlayer() == Constants.CELL_RED)
            pState = pState.reversed();

        int king_multiplier = 2;

        if (pState.isRedWin())
            return Integer.MIN_VALUE;
        else if (pState.isWhiteWin())
            return Integer.MAX_VALUE;
        else if (pState.isDraw())
            return 0;

        int whitescore = 0, redscore = 0;
        for (int i=0; i < 32; i++) {
            int piece = pState.get(i);

            if (0 != (piece & Constants.CELL_WHITE)) {
                int tempscore = 1;
                if (0 != (piece & Constants.CELL_KING)) {
                    tempscore *= king_multiplier;
                }
                whitescore += tempscore;
            } else if (0 != (piece & Constants.CELL_RED)) {
                int tempscore = 1;
                if (0 != (piece & Constants.CELL_KING)) {
                    tempscore *= king_multiplier;
                }
                redscore += tempscore;
            }
        }

        return whitescore - redscore;
    }
}
