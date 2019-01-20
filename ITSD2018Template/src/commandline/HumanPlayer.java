package commandline;

import java.util.ArrayList;
import java.util.Scanner;

public class HumanPlayer extends AbsPlayer{
	
	private ArrayList<Card> personalDeck = new ArrayList<Card>();
	private int catChoice;

	
	// call methods from super class 
	// for getTopCard
	// and givePlayerCard
	
	
	public int getCatChoice() {
		return catChoice;
	}

	public void getPlayersCatChoice(Card c) {
		//this will need system input for the human player
		viewCard(c);
		int tempCatChoice = 0;
		System.out.println("Please select your category choice between 1 and 5");
		Scanner keyboard = new Scanner(System.in);
		tempCatChoice = keyboard.nextInt();
		/*
		 * More exception handling can be added
		 */
		while(tempCatChoice < 1 && tempCatChoice > 5) {
			System.err.println("Not a valid category choice");
			System.out.println("Please enter a category choice between 1 and 5");
			tempCatChoice = keyboard.nextInt();
		}
		catChoice = tempCatChoice;
		System.out.println("You have selected category " + catChoice);
	}
	
	public void viewCard(Card c) {
		System.out.println("Here is your card to select a value from");
		System.out.println("=====================");
		System.out.println(c.getCardName());
		System.out.println("_____________________");
		System.out.println(c.getCat1Value());
		System.out.println(c.getCat2Value());
		System.out.println(c.getCat3Value());
		System.out.println(c.getCat4Value());
		System.out.println(c.getCat5Value());
		System.out.println("=====================");
	}
	
	
	public Card getTopCard() {
		Card topCard = personalDeck.get(0);
		System.out.println(topCard.getCardName() + " is your top card");
		personalDeck.remove(0);
		return topCard;
	}
	

	public void givePlayerCard(Card c) {
		System.out.println(c.getCardName() + " added to HumanPlayer");
		personalDeck.add(c);
	}
}