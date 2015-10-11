
class Player {

    public Player() {
    }

    /**
    * Shoot!
    *
    * This is the function where you start your work.
    *
    * You will receive a variable pState, which contains information about all
    * birds, both dead and alive. Each bird contains all past moves.
    *
    * The state also contains the scores for all players and the number of
    * time steps elapsed since the last time this function was called.
    *
    * @param pState the GameState object with observations etc
    * @param pDue time before which we must have returned
    * @return the prediction of a bird we want to shoot at, or cDontShoot to pass
    */
    public Action shoot(GameState pState, Deadline pDue) {
        /*
        * Here you should write your clever algorithms to get the best action.
        * This skeleton never shoots.
        */

        int numberOfBirds = pState.getNumBirds();
        int bestBird = -1;
        int bestBirdDirection = -1;
        double bestBirdProbability = 0.75;

        for(int i = 0; i < numberOfBirds; i++) {
            Bird bird = pState.getBird(i);
            if(bird.isDead()) {
                continue;
            }

            int finsiffra = 10;

            int[] observations = new int[finsiffra];
            int numObservations = bird.getSeqLength();
            if(numObservations < finsiffra) {
                return cDontShoot;
            }

            int k = 0;
            for(int j = numObservations-finsiffra; j < numObservations; j++) {
                observations[k] = bird.getObservation(j);
                k++;
            }

            HMM hmm = new HMM(3, 3);
            hmm.estimateModel(observations);
            double[] probabilitiesOfMoves = hmm.estimateProbabilityDistributionOfNextEmission(hmm.pi);

            for(int j = 0; j < probabilitiesOfMoves.length; j++) {
                if(probabilitiesOfMoves[j] > bestBirdProbability) {
                    bestBird = i;
                    bestBirdDirection = j + 3;
                    bestBirdProbability = probabilitiesOfMoves[j];
                }
            }
        }

        // This line chooses not to shoot.
        if(bestBird == -1) {
            System.err.println("Nah we cool...");
            return cDontShoot;
        } else {
            System.err.println("SHOOT HEEEHH! "+Integer.toString(bestBirdDirection));
            return new Action(bestBird, bestBirdDirection);
        }

        // This line would predict that bird 0 will move right and shoot at it.
        // return Action(0, MOVE_RIGHT);
    }

    /**
    * Guess the species!
    * This function will be called at the end of each round, to give you
    * a chance to identify the species of the birds for extra points.
    *
    * Fill the vector with guesses for the all birds.
    * Use SPECIES_UNKNOWN to avoid guessing.
    *
    * @param pState the GameState object with observations etc
    * @param pDue time before which we must have returned
    * @return a vector with guesses for all the birds
    */
    public int[] guess(GameState pState, Deadline pDue) {
        /*
        * Here you should write your clever algorithms to guess the species of
        * each bird. This skeleton makes no guesses, better safe than sorry!
        */

        int[] lGuess = new int[pState.getNumBirds()];
        for (int i = 0; i < pState.getNumBirds(); ++i)
        lGuess[i] = Constants.SPECIES_UNKNOWN;
        return lGuess;
    }

    /**
    * If you hit the bird you were trying to shoot, you will be notified
    * through this function.
    *
    * @param pState the GameState object with observations etc
    * @param pBird the bird you hit
    * @param pDue time before which we must have returned
    */
    public void hit(GameState pState, int pBird, Deadline pDue) {
        System.err.println("HIT BIRD!!!");
    }

    /**
    * If you made any guesses, you will find out the true species of those
    * birds through this function.
    *
    * @param pState the GameState object with observations etc
    * @param pSpecies the vector with species
    * @param pDue time before which we must have returned
    */
    public void reveal(GameState pState, int[] pSpecies, Deadline pDue) {
    }

    public static final Action cDontShoot = new Action(-1, -1);
}
