package com.linesb.mischief.Activity;

import com.linesb.mischief.R;
import com.linesb.mischief.Lobby;
import com.linesb.mischief.ServerEntry;
import com.linesb.mischief.Utility.DialogBox;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

/**
* Lobby activity entry point.
* 
*  
*  
* @author Linus Esbjörnsson (linuse93@hotmail.com)
* @version 1.0
*/

public class LobbyActivity extends Activity {
	
	private Lobby m_lobby;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loading_splash);
		Bundle bundle = getIntent().getExtras();
		String nickname = bundle.getString("NICKNAME");
		ServerEntry server = (ServerEntry) bundle.getSerializable("SERVER");
		m_lobby = new Lobby(this, nickname);
		m_lobby.setupLobby(server);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		m_lobby.updateListener();
	}
	
	@Override
	public void finish() {
		super.finish();
		m_lobby.finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.entry, menu);
		return true;
	}
	
	public static void show(Context context, Activity activity, String nickname, ServerEntry server) {
		if (nickname.length() >= 3 && nickname.length() <= 20) {
			Intent intent = new Intent(context, LobbyActivity.class);
			intent.putExtra("NICKNAME", nickname);
			intent.putExtra("SERVER", server);
			context.startActivity(intent);
		} else if (nickname.length() < 3) {
			DialogBox.createOkBox("Error", "Nickname must be at least 3 characters.", activity);
		} else {
			DialogBox.createOkBox("Error", "Nickname can't exceed 20 characters.", activity);
		}
	}
}
