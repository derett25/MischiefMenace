package com.linesb.mischief.GUI;

import java.util.Iterator;

import com.linesb.mischief.R;
import com.linesb.mischief.ConnectionState;
import com.linesb.mischief.LocalState;
import com.linesb.mischief.Utility.DialogBox;
import com.linesb.networkmessages.GameMessage;
import com.linesb.networkmessages.GameMessage.GameMessageType;
import com.linesb.networkobject.GameCard;
import com.linesb.networkobject.GameCard.CardType;
import com.linesb.networkobject.GamePlayer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
* Class that handles the Game GUI.
* 
*  
*  
* @author Linus Esbjörnsson (linuse93@hotmail.com)
* @version 1.0
*/

public class GameGUI {
	
	private LinearLayout m_userCards;
	private LinearLayout m_playedCards;
	private LinearLayout m_phrase;
	private LocalState m_state;
	private ConnectionState m_connectionState;
	private String m_nickname;
	private Activity m_activity;
	
	public GameGUI(Activity activity, LocalState state, ConnectionState connection, String nickname) {
		m_activity = activity;
		m_connectionState = connection;
		m_userCards = (LinearLayout) m_activity.findViewById(R.id.cardLayout);
		m_playedCards = (LinearLayout) m_activity.findViewById(R.id.playedLayout);
		m_phrase = (LinearLayout) m_activity.findViewById(R.id.phraseArea);
		m_state = state;
		m_nickname = nickname;
		setupButtons();
		setupCards();
		setupText();
	}
	
	public void setupButtons() {
		m_activity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				Button button = (Button) m_activity.findViewById(R.id.participantsButton);
				button.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						AlertDialog.Builder alert = new AlertDialog.Builder(m_activity);
						
						LinearLayout layoutCreateMerch = new LinearLayout(m_activity);
				        layoutCreateMerch.setOrientation(LinearLayout.VERTICAL);
				        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
						alert.setTitle("Participants");
						
						LinearLayout l = new LinearLayout(m_activity);
			    	    TextView input = new TextView(m_activity);
			    	    input.setTextSize(10);
						input.setText("Nickname");
						input.setLayoutParams(params);
						l.addView(input);
						
						input = new TextView(m_activity);
						input.setTextSize(10);
						input.setText("Points");
						input.setLayoutParams(params);
						l.addView(input);
						
						input = new TextView(m_activity);
						input.setTextSize(10);
						input.setText("CL");
						input.setLayoutParams(params);
						l.addView(input);
						
						input = new TextView(m_activity);
						input.setTextSize(10);
						input.setText("RL");
						input.setLayoutParams(params);
						l.addView(input);
						
						input = new TextView(m_activity);
						input.setTextSize(10);
						input.setText("Selecting");
						input.setLayoutParams(params);
						l.addView(input);
						
						layoutCreateMerch.addView(l);
						
						GamePlayer p;
						for(Iterator<GamePlayer> itr = m_state.getState().getPlayers().iterator(); itr.hasNext();) {
					        l = new LinearLayout(m_activity);
				    	    p = itr.next();
				    	    input = new TextView(m_activity);
				    	    input.setTextSize(10);
							input.setText(p.getName());
							input.setLayoutParams(params);
							l.addView(input);
							
							input = new TextView(m_activity);
							input.setTextSize(10);
							input.setText(Integer.toString(p.getScore()));
							input.setLayoutParams(params);
							l.addView(input);
							
							input = new TextView(m_activity);
							input.setTextSize(10);
							if (p.isCardLeader()) { input.setText("Yes"); } else { input.setText("No"); }
							input.setLayoutParams(params);
							l.addView(input);
							
							input = new TextView(m_activity);
							input.setTextSize(10);
							if (p.isRoomLeader()) { input.setText("Yes"); } else { input.setText("No"); }
							input.setLayoutParams(params);
							l.addView(input);
							
							input = new TextView(m_activity);
							input.setTextSize(10);
							if (p.isSelecting()) { input.setText("Yes"); } else { input.setText("No"); }
							input.setLayoutParams(params);
							l.addView(input);
							
							if (p.getName().equals(m_nickname)) { l.setBackgroundColor(Color.parseColor("#C9C9C9")); }
							
							layoutCreateMerch.addView(l);
				    	}
						
						alert.setView(layoutCreateMerch);
						
						alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int whichButton) {
								// OK
							}
						});
						
						alert.show();
					}
				});
				
				button = (Button) m_activity.findViewById(R.id.startGameButton);
				if (m_state.isRoomLeader() && m_state.getState().isNewGame() && m_state.isEnoughPlayers()) {
					button.setEnabled(true);
					button.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							m_connectionState.sendMessage(new GameMessage(m_nickname, GameMessageType.START_GAME));
						}
						
					});
				} else {
					button.setEnabled(false);
				}
			}
			
		});
	}
	
	public void setupCards() {
		m_activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				ContextThemeWrapper newContext = new ContextThemeWrapper(m_activity.getApplicationContext(), R.style.cardFont);
				
				m_userCards.removeAllViews();
				for(int i = 0; i < m_state.getState().getCards().size(); i++) {
					// Create a final temp variable to hold the counter
					final int temp = i;
					final GameCard c = m_state.getState().getCards().get(i);
					if (c.getCardType().equals(CardType.BLANK_CARD)) {
						c.setContent("____");
					}
		    	    final CardView cv = new CardView(newContext, c);
		    	    cv.setupCard(UIConfig.getSizeName(m_activity.getApplicationContext()));
		    	    if (m_state.isSelecting() && !m_state.isCardLeader()) {
	    	    		cv.setOnLongClickListener(new OnLongClickListener() {

	    					@Override
	    					public boolean onLongClick(View v) {
	    						cv.selectCard();
	    						if (c.getCardType().equals(CardType.BLANK_CARD)) {
	    							AlertDialog.Builder alert = new AlertDialog.Builder(m_activity);
	    							
	    							alert.setTitle("Enter card content");
	    							
	    							final EditText input = new EditText(m_activity);
	    							alert.setView(input);
	    							
	    							alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
	    								public void onClick(DialogInterface dialog, int whichButton) {
	    									String value = input.getText().toString();
	    									if (value.length() >= 1 && value.length() < 50) {
	    										// Use the temp variable to determine selected card
	    										m_connectionState.sendMessage(new GameMessage(m_nickname, GameMessageType.PLAY_CARD, temp, value));
	    									} else if (value.length() > 50) {
	    										DialogBox.createOkBox("Error", "Card content can't have more than 50 characters.", m_activity);
	    									} else {
	    										DialogBox.createOkBox("Error", "Card content must have at least 1 character.", m_activity);
	    									}
	    								}
	    							});
	    							
	    							alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	    								public void onClick(DialogInterface dialog, int whichButton) {
	    									cv.setupCard(UIConfig.getSizeName(m_activity.getApplicationContext()));
	    								}
	    							});
	    							
	    							alert.show();
	    						} else {
		    						DialogBox.createConfirmBox("Confirm card", "Are you sure you want to select this card?", m_activity, new DialogInterface.OnClickListener() {
		    				            @Override
		    				            public void onClick(DialogInterface dialog, int which) {
		    				            	// Use the temp variable to determine selected card
		    				            	m_connectionState.sendMessage(new GameMessage(m_nickname, GameMessageType.PLAY_CARD, temp));
		    				            	cv.setupCard(UIConfig.getSizeName(m_activity.getApplicationContext()));
		    				            }
		    				        }, new DialogInterface.OnClickListener() {
		    				            @Override
		    				            public void onClick(DialogInterface dialog, int which) {
		    				            	cv.setupCard(UIConfig.getSizeName(m_activity.getApplicationContext()));
		    				            }
		    				        });
	    						}
	    						return true;
	    					}
	    	    	    	
	    	    	    });
		    	    }
		    	    m_userCards.addView(cv);
		    	}
				
				m_playedCards.removeAllViews();
				for(int i = 0; i < m_state.getState().getPlayedCards().size(); i++) {
					// Create a final temp variable to hold the counter
					final int temp = i;
					final GameCard c = m_state.getState().getPlayedCards().get(i);
		    	    final CardView cv = new CardView(newContext, c);
		    	    cv.setupCard(UIConfig.getSizeName(m_activity.getApplicationContext()));
		    	    if (c.hasWon()) {
		    	    	cv.selectCard();
		    		}
		    	    if (m_state.isSelecting() && m_state.isCardLeader()) {
		    	    	cv.setOnLongClickListener(new OnLongClickListener() {

	    					@Override
	    					public boolean onLongClick(View v) {
	    						cv.selectCard();
	    						DialogBox.createConfirmBox("Confirm card", "Are you sure you want to select this card?", m_activity, new DialogInterface.OnClickListener() {
	    				            @Override
	    				            public void onClick(DialogInterface dialog, int which) {
	    				            	// Use the temp variable to determine selected card
	    				            	m_connectionState.sendMessage(new GameMessage(m_nickname, GameMessageType.SELECT_CARD, temp));
	    				            	cv.setupCard(UIConfig.getSizeName(m_activity.getApplicationContext()));
	    				            }
	    				        }, new DialogInterface.OnClickListener() {
	    				            @Override
	    				            public void onClick(DialogInterface dialog, int which) {
	    				            	cv.setupCard(UIConfig.getSizeName(m_activity.getApplicationContext()));
	    				            }
	    				        });
	    						return true;
	    					}
	    	    	    	
	    	    	    });
		    	    }
		    	    m_playedCards.addView(cv);
		    	}
				m_phrase.removeAllViews();
	    	    if (m_state.getState().getPhraseCard() != null) {
	    	    	CardView cardview = new CardView(newContext, m_state.getState().getPhraseCard());
	    	    	cardview.setupCard(UIConfig.getSizeName(m_activity.getApplicationContext()));
	    	    	m_phrase.addView(cardview);
	    	    }
			}
		});
	}
	
	public void setupText() {
		m_activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				TextView infoText = (TextView) m_activity.findViewById(R.id.infoText);
				GamePlayer player = m_state.getWinner();
				if (player == null) {
					if (m_state.getState().isNewGame()) {
						if (!m_state.isEnoughPlayers()) {
							infoText.setText("At least 3 players are needed to start the game");
						} else {
							infoText.setText("The game has not yet started");
						}
					} else if (m_state.isSelecting() && m_state.isCardLeader()) {
						infoText.setText("You are the card leader, select a played card");
					} else if (m_state.isSelecting() && !m_state.isCardLeader()) {
						infoText.setText("Select a card to play");
					} else {
						infoText.setText("Waiting...");
					}
				} else {
					infoText.setText(player.getName() + " has won!");
				}
				infoText.startAnimation(AnimationUtils.loadAnimation(m_activity, R.anim.fade_in));
			}
		});
	}
}
