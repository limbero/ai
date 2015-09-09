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

        miniMax(pState, 9, true, alpha, beta);
        return bestState;
    }

    public int miniMax(final GameState pState, int depth, boolean maximizingPlayer, int alpha, int beta) {
        if (depth == 0 || pState.isEOG())
            return evaluateBoard(pState);

        Vector<GameState> lNextStates = new Vector<GameState>();
        pState.findPossibleMoves(lNextStates);
        GameState bestChild = lNextStates.get(0);

        if (maximizingPlayer) {
            int bestScore = Integer.MIN_VALUE;

            for (GameState lNextState : lNextStates) {
                int score = miniMax(lNextState, depth-1, false, alpha, beta);
                if (score > bestScore) {
                    bestScore = score;
                    bestChild = lNextState;
                }

                alpha = score > alpha ? score : alpha;
                if (beta <= alpha)
                    break;
            }
            bestState = bestChild;
            return bestScore;
        }
        else {
             int bestScore = Integer.MAX_VALUE;
             for (GameState lNextState : lNextStates) {
                int score = miniMax(lNextState, depth-1, true, alpha, beta);
                if (score < bestScore) {
                    bestScore = score;
                    bestChild = lNextState;
                }

                beta = score < beta ? score : beta;
                if (beta <= alpha)
                    break;
            }
            bestState = bestChild;
            return bestScore;
        }
    }

    public int evaluateBoard(GameState pState) {
        int king_value = 5;

        if (weAreRed) {
            if (pState.isRedWin())
                return Integer.MAX_VALUE;
            else if (pState.isWhiteWin())
                return Integer.MIN_VALUE;
        } else {
            if (pState.isRedWin())
                return Integer.MIN_VALUE;
            else if (pState.isWhiteWin())
                return Integer.MAX_VALUE;
        }

        int score = 0;
        for (int i = 0; i < 32; i++) {
            int piece = pState.get(i);

            if (weAreRed) {
                if (0 != (piece & Constants.CELL_WHITE)) {
                    score -= 1;
                    if (0 != (piece & Constants.CELL_KING)) {
                        score -= king_value;
                    }

                } else if (0 != (piece & Constants.CELL_RED)) {
                    score += 1;
                    if (0 != (piece & Constants.CELL_KING)) {
                        score += king_value;
                    }
                }
            } else {
                if (0 != (piece & Constants.CELL_WHITE)) {
                    score += 1;
                    if (0 != (piece & Constants.CELL_KING)) {
                        score += king_value;
                    }

                } else if (0 != (piece & Constants.CELL_RED)) {
                    score -= 1;
                    if (0 != (piece & Constants.CELL_KING)) {
                        score -= king_value;
                    }
                }
            }
         }

        return score;
    }
}
