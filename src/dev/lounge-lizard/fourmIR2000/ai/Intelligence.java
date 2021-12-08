package dev.lounge-lizard.fourmIR2000.ai;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import dev.lounge-lizard.fourmIR2000.frame.GameFrame;
import dev.lounge-lizard.fourmIR2000.insect.Insect;
import dev.lounge-lizard.fourmIR2000.insect.Rank;
import dev.lounge-lizard.fourmIR2000.insect.Team;
import dev.lounge-lizard.fourmIR2000.util.Randomizer;
import dev.lounge-lizard.fourmIR2000.world.WorldPoint;

/**
 * Artificial Intelligence for the ennemies
 */
public final class Intelligence {

	/** The team in which the intelligence is thinking */
	private final Team team;
	
	/** The gameFrame of the game */
	private final GameFrame gameFrame;
	
	/** To know which handicap to practice during the game */
	private ArrayList<Integer> handicap;
	
	/** To know the next type of Insect to generate: 0 --> Bug, 1 --> Kamikaze */
	private int nextInsectToAdd;
	
	/** To know since when we haven't create a group or attack */
	private int timeSinceLastGroup;
	
	
	/**
	 * Constructor of the Artificial Intelligence
	 * @param team The team we want to defend
	 * @param gameFrame The gameFrame of the current game
	 */
	public Intelligence(Team team, GameFrame gameFrame) {
		this.team = team;
		this.gameFrame = gameFrame;
		handicap = new ArrayList<Integer>();
		handicap.add(2000);
		handicap.add(1000);
		handicap.add(500);
		nextInsectToAdd = -1;
		timeSinceLastGroup = 0;
	}

	/**
	 * Permits to the AI to think about the method to apply
	 */
	public void think() {
		timeSinceLastGroup++;
		// If we didn't determinate the type of the next Insect to generate, we did it
		if(nextInsectToAdd == -1) {
			nextInsectToAdd = Randomizer.getInt(2);
		}
		
		// According to the difficulty, we select the appropriated method for thinking 
		switch(gameFrame.getDifficulty()) {
			case EASY : thinkEASY(); break;
			case MEDIUM : thinkMEDIUM(); break;
			case HARD : thinkHARD(); break;
		}
	}

	/**
	 * Reflection on the possibility to increase the level of the generated insect
	 */
	private void thinkAboutVeterans() {
        // We select the appropriated method, according to the Team Rank at this moment
        switch(team.getRank()) {
	        case PRIVATE :	updateRank(Rank.SERGEANT); 	break;
	        case SERGEANT :	updateRank(Rank.CAPTAIN); 	break;
	        case CAPTAIN :	updateRank(Rank.MAJOR); 	break;
	        case MAJOR :	updateRank(Rank.COLONEL); 	break;
	        case COLONEL :	updateRank(Rank.GENERAL); 	break;
        }
	}
	

	/**
	 * Method used to think on the easy-level
	 */
	private void thinkEASY() {
		final int foodStock = team.getFoodStock();
		
		// According to the food of the team, we select the rating of Knowledge
		if(foodStock <= 1000)
			team.setPercentKnowledge(0);
		else
			team.setPercentKnowledge(25);
		// Selection of the percent of worker
		team.setPercentWorker(25);
		
		/* If we have enough stocked food, and if the difficulty level allows this, we can
	     * ask a bug to help us, in exchange of some food **/
        if(foodStock >= Team.FOOD_NEEDED_FOR_A_BUG + handicap.get(0)) {
        	//team.setFoodStock(foodStock - (Team.FOOD_NEEDED_FOR_A_BUG + handicap.get(0)));
        	team.getMaster().addBug();
        }
	}
	
	/**
	 * Method used to think on the medium-level
	 */
	private void thinkMEDIUM() {
		thinkAboutVeterans();
		
		final int foodStock = team.getFoodStock();
		final int knowledges = team.getKnowledges();
		
		// According to the food of the team, we select the rating of Knowledge
		if(foodStock <= 1000)
			team.setPercentKnowledge(0);
		else {
			if (knowledges >= Team.KNOWLEDGE_FOR_GENERALS)
				team.setPercentKnowledge(0);
			else
				team.setPercentKnowledge(75);
		}
		
		// According to the number of workers in the team, we select the rating of Workers
		if(team.nbOfAntWorkerInTeam() <= 3) {
			team.setPercentWorker(75);
		} else {
			team.setPercentWorker(25);
		}
		
		switch(nextInsectToAdd) {
		case 0:
			/* If we have enough stocked food, and if the difficulty level allows this, we can
		     * ask a bug to help us, in exchange of some food **/
			if(knowledges >= Team.KNOWLEDGE_FOR_BUGS + handicap.get(1) && foodStock >= Team.FOOD_NEEDED_FOR_A_BUG + handicap.get(1)) {
				//team.setFoodStock(foodStock - (Team.FOOD_NEEDED_FOR_A_BUG + handicap.get(1)));
				team.getMaster().addBug();
				nextInsectToAdd = -1;
			} break;
		default :
			if(knowledges >= Team.KNOWLEDGE_FOR_KAMIKAZES + handicap.get(1) && foodStock >= Team.FOOD_NEEDED_FOR_A_KAMIKAZE + handicap.get(1)) {
				//team.setFoodStock(foodStock - (Team.FOOD_NEEDED_FOR_A_BUG + handicap.get(1)));
				team.getMaster().addKamikaze();
				nextInsectToAdd = -1;
			} break;
		}
		final int nbOfAntWarriorsInTeam = team.nbOfWarriorsInTeam();
		if(nbOfAntWarriorsInTeam >= 3 && timeSinceLastGroup >= 200) {
			Team teamToFight = null;
			final List<Team> lstTeams = gameFrame.getWorld().getTeams();
			do { // We select a team while it is not ourselves and the team is dead
				teamToFight = lstTeams.get(Randomizer.getInt(lstTeams.size()));
			} while(teamToFight.equals(this.team) || teamToFight.getMaster().getEnergy() <= 0);
			timeSinceLastGroup = 0;
			createGroupAndFight(Randomizer.getInt(nbOfAntWarriorsInTeam), teamToFight.getAntHill());
		}
	}
	
	/**
	 * Method used to think on the hard-level
	 */
	private void thinkHARD() {
		thinkAboutVeterans();
		
		final int foodStock = team.getFoodStock();
		final int knowledges = team.getKnowledges();
		
		// According to the food of the team, we select the rating of Knowledge
		if(foodStock <= 2 * (Team.FOOD_NEEDED_FOR_A_BUG + handicap.get(2)))
			team.setPercentKnowledge(0);
		else {
			if (knowledges >= Team.KNOWLEDGE_FOR_GENERALS)
				team.setPercentKnowledge(0);
			else
				team.setPercentKnowledge(100);
		}
		
		// According to the number of workers in the team, we select the rating of Workers
		if(team.nbOfAntWorkerInTeam() <= 5) {
			team.setPercentWorker(100);
		} else {
			team.setPercentWorker(0);
		}
		
		switch(nextInsectToAdd) {
		case 0:
			/* If we have enough stocked food, and if the difficulty level allows this, we can
		     * ask a bug to help us, in exchange of some food **/
			if(knowledges >= Team.KNOWLEDGE_FOR_BUGS + handicap.get(2) && foodStock >= Team.FOOD_NEEDED_FOR_A_BUG + handicap.get(2)) {
				//team.setFoodStock(foodStock - (Team.FOOD_NEEDED_FOR_A_BUG + handicap.get(2)));
				team.getMaster().addBug();
				nextInsectToAdd = -1;
			} break;
		default :
			if(knowledges >= Team.KNOWLEDGE_FOR_KAMIKAZES + handicap.get(2) && foodStock >= Team.FOOD_NEEDED_FOR_A_KAMIKAZE + handicap.get(2)) {
				//team.setFoodStock(foodStock - (Team.FOOD_NEEDED_FOR_A_BUG + handicap.get(2)));
				team.getMaster().addKamikaze();
				nextInsectToAdd = -1;
			} break;
		}
		
		final int nbOfAntWarriorsInTeam = team.nbOfWarriorsInTeam();
		if(nbOfAntWarriorsInTeam >= 15 && timeSinceLastGroup >= 100) {
			Team teamToFight = null;
			final List<Team> lstTeams = gameFrame.getWorld().getTeams();
			do { // We select a team while it is not ourselves and the team is dead
				teamToFight = lstTeams.get(Randomizer.getInt(lstTeams.size()));
			} while(teamToFight.equals(this.team) || teamToFight.getMaster().getEnergy() <= 0);
			timeSinceLastGroup = 0;
			createGroupAndFight(nbOfAntWarriorsInTeam, teamToFight.getAntHill());
		}
	}


	/**
	 * Creates a group of soldier and gives the order to go to a point of the map
	 * @param nbOfSoldier The number of soldiers to select
	 * @param p The point where the group is ordered to go
	 */
	private void createGroupAndFight(int nbOfSoldier, WorldPoint p) {
		final LinkedList<Insect> lst = team.getListOfWarrior(nbOfSoldier);
		for(Insect insect : lst) {
            insect.setRefPoint(p);
		}
	}
	
	
	/**
	 * Update the rank of the team if it is possible, regarding the food and 
	 * knowledge needed.
	 * BE CAREFUL : this function assumes that you only changed of THE DIRECT UPPER RANK.
	 * @param rank	the willing rank. 
	 */
	private void updateRank(final Rank rank) {
		final int food = team.getFoodStock();
        final int knowledge = team.getKnowledges();
        int needKnown = 0;

        switch (rank) {
	        case SERGEANT: needKnown = Team.KNOWLEDGE_FOR_SERGEANTS;break;
	        case CAPTAIN: needKnown = Team.KNOWLEDGE_FOR_CAPTAINS; 	break;
	        case MAJOR: needKnown = Team.KNOWLEDGE_FOR_MAJORS; 		break;
	        case COLONEL: needKnown = Team.KNOWLEDGE_FOR_COLONELS; 	break;
	        case GENERAL: needKnown = Team.KNOWLEDGE_FOR_GENERALS; 	break;
        }
		if(knowledge >= needKnown && food >= Team.FOOD_NEEDED_FOR_VETERANS) {
    		team.setFoodStock(food - Team.FOOD_NEEDED_FOR_VETERANS);
    		team.setRank(rank);
    	}
	}
}
