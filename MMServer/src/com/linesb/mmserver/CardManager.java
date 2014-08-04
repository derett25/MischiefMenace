package com.linesb.mmserver;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import com.linesb.networkobject.GameCard;
import com.linesb.networkobject.GameCard.CardType;

/**
* Utility class that handles card logic and IO.
* 
*  
*  
* @author Linus Esbjörnsson (linuse93@hotmail.com)
* @version 1.0
*/

public class CardManager {
	
	private static ArrayList<GameCard> m_whiteCards = new ArrayList<GameCard>();
	private static ArrayList<GameCard> m_blackCards = new ArrayList<GameCard>();
	
	public static void loadCards() {
		final String FILE_PATH = "cards.txt";
		
		try(BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
	        String line = br.readLine();

	        while (line != null) {
	            String[] splitLine = line.split(",", 2);
	            int type = Integer.parseInt(splitLine[0]);
	            if (type == 0) {
	            	splitLine[1] = splitLine[1].replace("@!", "____");
	            	m_blackCards.add(new GameCard(CardType.BLACK_CARD, splitLine[1]));
	            } else {
	            	m_whiteCards.add(new GameCard(CardType.WHITE_CARD, splitLine[1]));
	            }
	            line = br.readLine();
	        }
	        
	    } catch (IOException e) {
	    	e.printStackTrace();
	    }
	}
	
	public static GameCard getRandomCard(CardType type, ArrayList<Player> players) {
		Random randomGenerator = new Random();
		if (type.equals(CardType.BLACK_CARD)) {
			if (m_blackCards.size() > 0) {
			    int index = randomGenerator.nextInt(m_blackCards.size());
			    return m_blackCards.get(index);
			}
		} else {
			int index = randomGenerator.nextInt(14);
			if (index == 5) {
				return new GameCard(CardType.BLANK_CARD, "");
			} else {
				if (m_whiteCards.size() > 0 && players != null) {
					do {
						index = randomGenerator.nextInt(m_whiteCards.size());
					} while (!checkUniqueness(m_whiteCards.get(index), players));
				    return m_whiteCards.get(index);
				}
			}
		}
		
		return null;
	}
	
	private static boolean checkUniqueness(GameCard card, ArrayList<Player> players) {
		Iterator<Player> itr = players.iterator();
		while (itr.hasNext()) {
			Player p = itr.next();
			Iterator<GameCard> cardItr = p.getCards().iterator();
			while (cardItr.hasNext()) {
				GameCard c = cardItr.next();
				if (c.equals(card)) {
					return false;
				}
			}
		}
		return true;
	}
	
}
