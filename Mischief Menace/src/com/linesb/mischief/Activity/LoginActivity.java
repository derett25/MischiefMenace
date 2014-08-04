package com.linesb.mischief.Activity;

import java.util.ArrayList;

import com.linesb.mischief.R;
import com.linesb.mischief.ServerEntry;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

/**
* Login activity entry point.
* 
*  
*  
* @author Linus Esbjörnsson (linuse93@hotmail.com)
* @version 1.0
*/

public class LoginActivity extends Activity implements OnClickListener {
	
	private ArrayList<ServerEntry> m_servers = new ArrayList<ServerEntry>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		m_servers.add(new ServerEntry("Server 1", "10.0.2.2", 20000));
		
		setContentView(R.layout.activity_connect);
		setupView();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.entry, menu);
		return true;
	}
	
	public void setupView() {
		Spinner spinner = (Spinner) findViewById(R.id.spinner1);
		ArrayAdapter<ServerEntry> adapter = new ArrayAdapter<ServerEntry>(this, android.R.layout.simple_spinner_item, m_servers);
		spinner.setAdapter(adapter);
		
		Button button = (Button) findViewById(R.id.button1);
		button.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		String nickname = ((EditText) findViewById(R.id.editText1)).getText().toString();
		ServerEntry server = (ServerEntry) ((Spinner) findViewById(R.id.spinner1)).getSelectedItem();
		LobbyActivity.show(this, this, nickname, server);
	}
	
	public static void show(Context context) {
		Intent intent = new Intent(context, LoginActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		context.startActivity(intent);
	}

}
