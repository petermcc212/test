package online.dwResources;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import commandline.Card;
import commandline.DatabaseCommunication;
import commandline.FileHandler;
import commandline.Player;
import online.configuration.TopTrumpsJSONConfiguration;

@Path("/toptrumps") // Resources specified here should be hosted at http://localhost:7777/toptrumps
@Produces(MediaType.APPLICATION_JSON) // This resource returns JSON content
@Consumes(MediaType.APPLICATION_JSON) // This resource can take JSON content as input
/**
 * This is a Dropwizard Resource that specifies what to provide when a user
 * requests a particular URL. In this case, the URLs are associated to the
 * different REST API methods that you will need to expose the game commands to
 * the Web page.
 * 
 * Below are provided some sample methods that illustrate how to create REST API
 * methods in Dropwizard. You will need to replace these with methods that allow
 * a TopTrumps game to be controled from a Web page.
 */
public class TopTrumpsRESTAPI {
	private static ArrayList<Card> deck = new ArrayList<Card>();
	
	private int numberOfPlayers;
	private ArrayList<Player> players;
	private ArrayList<Card> communalPile = new ArrayList<Card>();
	private int winnerID;
	private int winningIndex = 0; // current winning index
	private int catChoice = 0; // current category choice
	private int currentRoundNumber = 0;
	private boolean hasPlayerSubmitted;
	public void removePlayer(int i) {
		players.remove(i);
	}
	
	
	/**
	 * A Jackson Object writer. It allows us to turn Java objects into JSON strings
	 * easily.
	 */
	ObjectWriter oWriter = new ObjectMapper().writerWithDefaultPrettyPrinter();

	/**
	 * Contructor method for the REST API. This is called first. It provides a
	 * TopTrumpsJSONConfiguration from which you can get the location of the deck
	 * file and the number of AI players.
	 * 
	 * @param conf
	 */
	public TopTrumpsRESTAPI(TopTrumpsJSONConfiguration conf) {
			
	}

	// ----------------------------------------------------
	// Add relevant API methods here
	// ----------------------------------------------------

	@GET
	@Path("/helloJSONList")
	/**
	 * Here is an example of a simple REST get request that returns a String. We
	 * also illustrate here how we can convert Java objects to JSON strings.
	 * 
	 * @return - List of words as JSON
	 * @throws IOException
	 */
	public String helloJSONList() throws IOException {

		List<String> listOfWords = new ArrayList<String>();
		listOfWords.add("Hello");
		listOfWords.add("World!");

		// We can turn arbatory Java objects directly into JSON strings using
		// Jackson seralization, assuming that the Java objects are not too complex.
		String listAsJSONString = oWriter.writeValueAsString(listOfWords);

		return listAsJSONString;
	}

	@GET
	@Path("/launchGame")
	/**
	 * Setting up the game when game launched
	 */
	public ArrayList<Card> launchGame() throws IOException {
		
		ArrayList<Card> deck = getDeck();
		return deck;


	}
	
	@GET
	@Path("/getRoundNumber")
	public String getRoundNumber() throws IOException {
		return ""+currentRoundNumber;
	}
	
	@GET
	@Path("/numberOfPlayers")
	/**
	 * Setting the number of players
	 */
	public String numberOfPlayers(@QueryParam("Number") String Number) throws IOException {
		currentRoundNumber=0;
		ArrayList<Player> players = new ArrayList<Player>();
		players.clear();
		for (int i=0; i<players.size(); i++) {
			players.get(i).getPersonalDeck().clear();
		}
		Player.resetPlayerIDCount();
		// cast number received as an int
		numberOfPlayers = Integer.parseInt(Number);

		// create the players
		Player human = new Player();
		players.add(human);
		for (int i = 0; i < numberOfPlayers; i++) {
			Player AIPlayer = new Player();
			players.add(AIPlayer);
		}
		this.players = players;
		// Commented out until gameplay is complete
		// Collections.shuffle(players);
		return "total number of players is " + numberOfPlayers;
	}
	
	@GET
	@Path("/getDeck")
	// Method that will return deck 
	public ArrayList<Card> getDeck()throws IOException{
		FileHandler fl = new FileHandler();
		// Create File Handler reference
		// to create deck, players and shuffle
		fl.getFileData();
		deck = fl.getDeck();
		//Collections.shuffle(deck);

		return deck;
	}
	
	
	
	
	@GET
	@Path("/getPlayers")
	public ArrayList<Player> getPlayers() throws IOException{
		return players;
	}
	
	@GET
	@Path("/distributeCards")
	public String distributeCards() throws IOException{	
		for (int i = 0; i < players.size(); i++) {
			this.players.get(i).getPersonalDeck().clear();
		}
		
		for (int i = 0; i < deck.size(); i++) {
			this.players.get(i % players.size()).givePlayerCard(deck.get(i));
			System.out.println(players.get(i % players.size()).getPlayerID() + " has been given " + deck.get(i));
		}
		
		return "cards distributed";
	}
	

	@GET
	@Path("/getCardName")
	public String getCardName() throws IOException{
		for (int i=0; i<players.size(); i++) {
			if (players.get(i).getPlayerID()==1) {
				return players.get(i).getTopCard().getCardName();
			}
		}
		return "no name";
	}
	
	@GET
	@Path("/getCat1")
	public String getCat1() throws IOException{
		return players.get(0).getTopCard().getCat1Value() +"";
	}
	
	@GET
	@Path("/getCat2")
	public String getCat2() throws IOException{
		return players.get(0).getTopCard().getCat2Value() +"";
	}
	
	@GET
	@Path("/getCat3")
	public String getCat3() throws IOException{
		return players.get(0).getTopCard().getCat3Value() +"";
	}
	
	@GET
	@Path("/getCat4")
	public String getCat4() throws IOException{
		return players.get(0).getTopCard().getCat4Value() +"";
	}
	
	@GET
	@Path("/getCat5")
	public String getCat5() throws IOException{
		return players.get(0).getTopCard().getCat5Value() +"";
	}
	
	@GET
	@Path("/submit_category")
	public void submit_category(@QueryParam("categoryChoice") String categoryChoice) {
		
		catChoice = Integer.parseInt(categoryChoice);
		System.out.println();
		System.out.println("Category choice in RESTAPI: " + catChoice);
		System.out.println();
		
		hasPlayerSubmitted = true;
		
		if (players.get(winningIndex).getPlayerID() != 1) {
			catChoice = players.get(winningIndex).getAIPlayersCatChoice();
			System.out.println();
			printCatSelectedStatement(players, catChoice, winningIndex);
			System.out.println();
		} 	else {			
			
			// need to wait for players category submission
			printCatSelectedStatement(players, catChoice, winningIndex);
		}
		
		winningIndex = getWinningIndex(communalPile, catChoice);
		
		// Giving the winner the cards they won for this round
		for (int i = 0; i < communalPile.size(); i++) {
			players.get(winningIndex).givePlayerCard(communalPile.get(i));
		}
		
		//if there are cards in the communalPile give the player those
		if(communalPile.size() > 0) {
			for(int i = 0; i < communalPile.size(); i++){
				players.get(winningIndex).givePlayerCard(communalPile.get(i));
			}
			communalPile.clear();			
		}
		
		// remove top cards after round is finished
		removeCards(players);
		
		System.out.println("winning index is : " + winningIndex);
		// check players submission against others here
	}
	
	private static void removeCards(ArrayList<Player> players) {
		for (int i = 0; i < players.size(); i++) {
			try {
				players.get(i).removeTopCard();
			} catch (IndexOutOfBoundsException e) {

			}
		}
	}
	
	@GET
	@Path("/nextRound")
	public void nextRound() throws IOException{
			System.out.println();
			System.out.println();
			System.out.println("DECK SIZE: " + players.get(0).getPersonalDeck().size());
			System.out.println();
			System.out.println();
			communalPile = getTopCards(this.players);
			System.out.println("\n\n\n");
			for (int i = 0; i<communalPile.size(); i++) {
				System.out.println(communalPile.get(i));
			}
			System.out.println("\n\n\n");

			if (checkForOverallGameWin(players)) {
				winnerID = players.get(0).getPlayerID();
			}
			
			while (winningIndex >= players.size()) {
				winningIndex--;
			}
							
			currentRoundNumber++;
	}
	
	private int getWinningIndex(ArrayList<Card> communalPile, int catChoice) {
		int winningValue = 0;
		int winningIndex = 0;

		for (int j = 0; j < communalPile.size(); j++) {
			if (communalPile.get(j).getRequestedCat(catChoice) > winningValue) {
				winningValue = communalPile.get(j).getRequestedCat(catChoice);
				winningIndex = j;
			}
		}
		return winningIndex;
	}
	
	
	
	private static void printCatSelectedStatement(ArrayList<Player> players, int catChoice, int winningIndex) {
		System.out.println("Player " + players.get(winningIndex).getPlayerID() + " has selected "
				+ getCategory(catChoice) + " at " + players.get(winningIndex).getTopCard().getRequestedCat(catChoice));
	}
	
	private static String getCategory(int i) {
		String category = "";
		switch (i) {
		case 1:
			category = "Size";
			break;
		case 2:
			category = "Speed";
			break;
		case 3:
			category = "Range";
			break;
		case 4:
			category = "Firepower";
			break;
		case 5:
			category = "Cargo";
			break;

		}
		return category;
	}
	
	private static boolean checkForOverallGameWin(ArrayList<Player> players) {
		if (players.size() == 1) {
			System.out.println();
			System.out.println("Player " + players.get(0).getPlayerID() + " has won the game.");
			
			return true;
		}
		return false;
	}
	
	private static ArrayList<Card> getTopCards(ArrayList<Player> players) {
		ArrayList<Card> communalPile = new ArrayList<Card>();
		try {
			// running through each player and storing their top card in the dealers deck
			for (int i = 0; i < players.size(); i++) {
				if (players.get(i).getPersonalDeckSize() > 0) {
					communalPile.add(players.get(i).getTopCard()); // adding to communalPile
					System.out.println();
					System.out.println(players.get(i).getPlayerID() + " has given " + players.get(i).getTopCard().getCardName() + " to communalPile.");
					System.out.println();
				} else {
					// if they do not have a top card, they are removed from the game
					System.out
							.println("Player " + players.get(i).getPlayerID() + " has been removed from the game.");
					players.remove(i);
				}

			}
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
		}
		return communalPile;
	} 
	
	@GET
	@Path("/helloWord")
	/**
	 * Here is an example of how to read parameters provided in an HTML Get request.
	 * 
	 * @param Word - A word
	 * @return - A String
	 * @throws IOException
	 */
	public String helloWord(@QueryParam("Word") String Word) throws IOException {
		return "Hello " + Word;
	}
	
	///******** Database API methods ********///
	@GET
	@Path("/getTotalGames")
	public String getTotalGames() throws IOException {
		//int games = DatabaseCommunication.getNoGames();
		int games = 6;
		return ""+games;
	}
	

}
