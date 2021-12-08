package dev.lounge-lizard.fourmIR2000;

import dev.lounge-lizard.fourmIR2000.frame.GameFrame;


public class Main {

    /**
     * Entry of the application
     * @param args  arguments given on command line
     */
    public static void main(String[] args) {
    	
        // Creation of the graphical frame
    	final GameFrame gFra = new GameFrame("Fourm'IR 2000");
        
        // Creation of the game manager
        final Game game = new Game(gFra);
        game.launchEditor();

        while (true) {
            // We wait while we are on the level editor or on the game
            while ( !game.getInitBreak() && !game.getGameBreak()) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {/* nothing */}
            }
            // If we were on the level editor, we launch the game
            if(game.getInitBreak())   
                game.launchGame();
            // If we were on the game, we return to the level editor
            else
                game.launchEditor();
        }
    }
}
