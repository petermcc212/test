package commandline;

import java.sql.*;

//this class will handle inputting and retrieving relevant information from the postgreSQL database 
//need to connect to the university database server. 

public class DatabaseCommunication {

	private static Connection connection = null;

	private static void connectToDatabase() {
		// check for the driver
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			System.err.println("Could not fine JDBC Driver");
			e.printStackTrace();
			return;
		}

		try {
			connection = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/ITGroupProject", "postgres", "password here");
		} catch (SQLException e) {
			System.err.println("Connection Failed!");
			e.printStackTrace();
			return;
		}
	}

	public static void writeGameResults(int winnerID, int draws , int noRounds, int playerWins, int AI1Wins, int AI2Wins, int AI3Wins, int AI4Wins) {
		// writes the results of the game to the database
		//winner id is 1 for player and then 2-5 for each AI player in ascending order
		if (connection != null) {
			try {
				Statement SQLStatement = connection.createStatement();
				String SQLQuery = String.format(
						"INSERT INTO game_statistics (winnerid, nodraws, totalrounds, playerwins, aiplayeronewins, aiplayertwowins, aiplayerthreewins, aiplayerfourwins)\r\n"
								+ "VALUES (%d , %d, %d, %d, %d, %d, %d, %d)",
						winnerID, draws, noRounds, playerWins, AI1Wins, AI2Wins, AI3Wins, AI4Wins);
				SQLStatement.executeUpdate(SQLQuery);
			} catch (SQLException e) {
				e.printStackTrace();
			}

		} else {
			connectToDatabase();
			writeGameResults(winnerID, draws, noRounds, playerWins, AI1Wins, AI2Wins, AI3Wins, AI4Wins);
		}
	}
	
	public static String getPreviousStatistics() {
		//use 5 different methods then combine to 1 string or return 5 strings
		if (connection != null) {
			int noGames = getNoGames();
			int AIWins = getNoAIWins();
			int humanWins = getNoHumanWins();
			double AVGDraws = getAVGDraws();
			int largestNoRounds = getLargestNoRounds();
			//basic return string for now, can be altered as required.
			String statistics = String.format("%d\t%d\t%d\t%.2f\t%d", noGames, AIWins, humanWins, AVGDraws, largestNoRounds);
			System.out.println(statistics);
			return statistics;
		} else {
			connectToDatabase();
			getPreviousStatistics();
		}
		return null;
	}
	
	private static int getNoGames() {
		int noGames = 0;
		try {
			Statement SQLStatement = connection.createStatement();
			String SQLQuery = "SELECT MAX(id)\r\n" + 
					"FROM game_statistics";
			ResultSet queryResult = SQLStatement.executeQuery(SQLQuery);
			queryResult.next();
			noGames = queryResult.getInt(1);
			return noGames;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return noGames;
	}

	private static int getNoAIWins() {
		int noAIWins = 0;
		try {
			Statement SQLStatement = connection.createStatement();
			String SQLQuery = "Select COUNT(winnerID)\r\n" + 
					"FROM game_statistics\r\n" + 
					"WHERE winnerID > 1";
			ResultSet queryResult = SQLStatement.executeQuery(SQLQuery);
			queryResult.next();
			noAIWins = queryResult.getInt(1);
			return noAIWins;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return noAIWins;
	}
	
	private static int getNoHumanWins() {
		int noHumanWins = 0;
		try {
			Statement SQLStatement = connection.createStatement();
			String SQLQuery = "SELECT COUNT(winnerID)\r\n" + 
					"FROM game_statistics\r\n" + 
					"WHERE winnerID = 1";
			ResultSet queryResult = SQLStatement.executeQuery(SQLQuery);
			queryResult.next();
			noHumanWins = queryResult.getInt(1);
			return noHumanWins;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return noHumanWins;
	}
	
	private static double getAVGDraws() {
		double AVGDraws = 0;
		try {
			Statement SQLStatement = connection.createStatement();
			String SQLQuery = "SELECT AVG(nodraws)\r\n" + 
					"FROM game_statistics";
			ResultSet queryResult = SQLStatement.executeQuery(SQLQuery);
			queryResult.next();
			AVGDraws = queryResult.getDouble(1);
			return AVGDraws;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return AVGDraws;
	}
	
	private static int getLargestNoRounds() {
		int largestNoRounds = 0;
		try {
			Statement SQLStatement = connection.createStatement();
			String SQLQuery = "SELECT MAX(totalrounds)\r\n" + 
					"FROM game_statistics";
			ResultSet queryResult = SQLStatement.executeQuery(SQLQuery);
			queryResult.next();
			largestNoRounds = queryResult.getInt(1);
			return largestNoRounds;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return largestNoRounds;
	}
	
	public static void clearHistory() {
		//this method completely clears the database table
		if (connection != null) {
			try {
				Statement SQLStatement = connection.createStatement();
				String SQLQuery = "DELETE FROM game_statistics";
				SQLStatement.executeUpdate(SQLQuery);
			} catch (SQLException e) {
				e.printStackTrace();
			}

		} else {
			connectToDatabase();
			clearHistory();
		}
	}

	public static void main(String[] args) {
		//the main can be used for testing and will be removed when 
		//implemented into the full programme
		getPreviousStatistics();
		writeGameResults(3, 1, 1, 4, 6, 1, 4, 8);
		getPreviousStatistics();
		clearHistory();
		getPreviousStatistics();
	}

}