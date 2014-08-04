package com.linesb.mischief.Activity;

import com.linesb.mischief.R;
import com.linesb.mischief.ConnectionState;
import com.linesb.mischief.Game;
import com.linesb.networkmessages.GameState;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

/**
* Game activity entry point.
* 
*  
*  
* @author Linus Esbjörnsson (linuse93@hotmail.com)
* @version 1.0
*/

public class GameActivity extends Activity {
	
	private Game m_game;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_view);
		Bundle bundle = getIntent().getExtras();
		GameState gameState = (GameState) bundle.getSerializable("STATE");
		String nickname = bundle.getString("NICKNAME");
		ConnectionState connectionState = (ConnectionState) getApplicationContext();
		m_game = new Game(this, connectionState, gameState, nickname);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.entry, menu);
		return true;
	}
	
	@Override
	public void finish() {
		super.finish();
		m_game.finish();
	}
	
	public static void show(Context context, GameState state, String nickname) {
		Intent intent = new Intent(context, GameActivity.class);
		intent.putExtra("STATE", state);
		intent.putExtra("NICKNAME", nickname);
		context.startActivity(intent);
	}
}
