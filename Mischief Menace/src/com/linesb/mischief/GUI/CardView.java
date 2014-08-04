package com.linesb.mischief.GUI;

import com.linesb.mischief.R;
import com.linesb.networkobject.GameCard;
import com.linesb.networkobject.GameCard.CardType;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
* Representing the graphical view of the game cards.
* 
*  
*  
* @author Linus Esbjörnsson (linuse93@hotmail.com)
* @version 1.0
*/

public class CardView extends TextView {
	
	private GameCard m_card = null;
	
	public CardView(Context context, GameCard card) {
		super(context);
		m_card = card;
	}
	
	public CardView(Context context, AttributeSet attrs, GameCard card) {
		super(context, attrs);
		m_card = card;
	}

	public CardView(Context context, AttributeSet attrs, int defStyle, GameCard card) {
		super(context, attrs, defStyle);
		m_card = card;
	}
	
	public void selectCard() {
		super.setBackgroundResource(R.drawable.cardselected);
	}
	
	public void setupCard(String size) {
		int cardWidth;
		int cardHeight;
		int textSize;
		if (size.equals("small")) {
			cardWidth = 100;
			cardHeight = 50;
			textSize = 8;
		} else if (size.equals("large")) {
			cardWidth = 350;
			cardHeight = 235;
			textSize = 15;
		} else if (size.equals("xlarge")) {
			cardWidth = 300;
			cardHeight = 175;
			textSize = 15;
		} else {
			cardWidth = 250;
			cardHeight = 150;
			textSize = 11;
		}
		final int CARD_WIDTH = cardWidth;
		final int CARD_HEIGHT = cardHeight;
		final int TEXT_SIZE = textSize;
		
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(CARD_WIDTH, CARD_HEIGHT);
		if (size.equals("small")) {
			lp.setMargins(5, 0, 5, 0);
			this.setPadding(5, 0, 5, 0);
		} else {
			lp.setMargins(10, 0, 10, 0);
			this.setPadding(10, 0, 10, 0);
		}
		this.setTextSize(TEXT_SIZE);
		if (m_card.getCardType().equals(CardType.BLACK_CARD)) {
			// Set black bg
			super.setTextColor(Color.parseColor("#ffffff"));
			super.setBackgroundResource(R.drawable.cardbg_black);
		} else {
			super.setTextColor(Color.parseColor("#000000"));
			super.setBackgroundResource(R.drawable.cardbg);
		}
		super.setText(m_card.getContent());
		super.setLayoutParams(lp);
	}
	
	public GameCard getCard() {
		return m_card;
	}
}
