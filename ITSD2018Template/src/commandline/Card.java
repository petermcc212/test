package commandline;

import java.util.ArrayList;
import java.util.Collections;

public class Card {
	private String cardName;
	private int cat1Value;
	private int cat2Value;
	private int cat3Value;
	private int cat4Value;
	private int cat5Value;
	private ArrayList<Integer> catValues = new ArrayList<Integer>();
	private int highestValue;
	private ArrayList<String> catNames = new ArrayList<String>();
	
	

	
	public Card(String cardName, int value1, int value2, int value3, int value4, int value5, ArrayList<String> catNames) {
		
		// This will contain constructor info
		// to form new card object
		this.cardName = cardName;
		this.cat1Value = value1;
		this.cat2Value = value2;
		this.cat3Value = value3;
		this.cat4Value = value4;
		this.cat5Value = value5;
		catValues.add(value1);
		catValues.add(value2);
		catValues.add(value3);
		catValues.add(value4);
		catValues.add(value5);
		highestValue = Collections.max(catValues);
		this.catNames = catNames;
	}
	
	public void viewCard() {
		System.out.println("Here is your card to select a value from");
		System.out.println("=====================");
		System.out.println(getCardName());
		System.out.println("_____________________");
		System.out.println("Category 1: \t" + catNames.get(1) + "\t" + "\t" + cat1Value);
		System.out.println("Category 2: \t" + catNames.get(2) + "\t" + "\t" + cat2Value);
		System.out.println("Category 3: \t" + catNames.get(3) + "\t" + "\t" + cat3Value);
		System.out.println("Category 4: \t" + catNames.get(4) + "\t" + cat4Value);
		System.out.println("Category 5: \t" + catNames.get(5) + "\t" + "\t" + cat5Value);
		System.out.println("=====================");
	}
	
	public String toString() {
		String cardDescription = getCardName();
		cardDescription += "\n" + catNames.get(1) + "\t" + "\t" + cat1Value;
		cardDescription += "\n" + catNames.get(2) + "\t" + "\t" + cat2Value;
		cardDescription += "\n" + catNames.get(3) + "\t" + "\t" + cat3Value;
		cardDescription += "\n" + catNames.get(4) + "\t" + "\t" + cat4Value;
		cardDescription += "\n" + catNames.get(5) + "\t" + "\t" + cat5Value;
		return cardDescription;
	}
	
	public int getHighestIndex() {
		int valueNumber = catValues.indexOf(highestValue);
		return valueNumber + 1;
	}
	
		
	public String getCardName() {
		return cardName;
	}

	public int getCat1Value() {
		return cat1Value;
	}

	public int getCat2Value() {
		return cat2Value;
	}

	public int getCat3Value() {
		return cat3Value;
	}

	public int getCat4Value() {
		return cat4Value;
	}

	public int getCat5Value() {
		return cat5Value;
	}

	public int getRequestedCat(int i) {
		int catValue = 0;
		
		switch (i) {
			case 1: catValue = getCat1Value();
				break;
			case 2: catValue = getCat2Value();
				break;
			case 3: catValue = getCat3Value();
				break;
			case 4: catValue = getCat4Value();
				break;
			case 5: catValue = getCat5Value();
				break;
		}
		return catValue;						
	}
	
	public ArrayList<String> getCatNames(){
		return catNames;
	}

	
	

}
