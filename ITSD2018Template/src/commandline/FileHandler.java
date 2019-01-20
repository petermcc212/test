package commandline;

import commandline.Card;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

public class FileHandler {

    private final static String classDirectory = System.getProperty("user.dir") + "/";
    private static String nameOfDeck = "StarCitizenDeck.txt";
    private String [] titleArray;
    private ArrayList<Card> deck = new ArrayList<>();

    // this method reads the deck, separates the titles into a fixed array
    // of strings and then creates a card object for each card in the deck
    public void getFileData(){
        FileReader fr = null;
        try{

            String filePath = classDirectory + nameOfDeck;
            fr = new FileReader(filePath);
            // make a scanner around the file reader
            Scanner s = new Scanner(fr);
            // creating an array of the titles
            if (s.hasNextLine()){
                String titles = s.nextLine();
                titleArray = titles.split(" ");

            }
            while(s.hasNextLine()){
                String description = s.next();
                int cat1 = s.nextInt();
                int cat2 = s.nextInt();
                int cat3 = s.nextInt();
                int cat4 = s.nextInt();
                int cat5 = s.nextInt();
                // add card to deck array list (as a new object)
                deck.add (new Card(description, cat1, cat2, cat3, cat4, cat5));
            }
            s.close();

        }catch(FileNotFoundException e){
            e.printStackTrace();
            System.out.println("No file found");
        }
       
    }
    // getter for deck
    public ArrayList<Card> getDeck(){
        return deck;
    }

    // Getter for title array
    public String[] getTitles(){
        return titleArray;
    }
}