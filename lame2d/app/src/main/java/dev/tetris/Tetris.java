package dev.tetris;

import dev.tetris.game.Model;
import dev.tetris.game.ScoresCounter;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.MenuInflater;
import android.widget.TextView;
import android.widget.ImageButton;

public class Tetris extends Activity implements OnClickListener {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tetris);

        View maraButton = findViewById(R.id.mara_button);
        maraButton.setOnClickListener(this);
        
        View timeButton = findViewById(R.id.time_button);
        timeButton.setOnClickListener(this);
        
        View toyButton = findViewById(R.id.toy_button);
        toyButton.setOnClickListener(this);
        
        View scoreButton = findViewById(R.id.score_button);
        scoreButton.setOnClickListener(this);
        
        View exitButton = findViewById(R.id.exit_button);
        exitButton.setOnClickListener(this);
	}

    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.mara_button:
            Intent m = new Intent(this, TetrisGame.class);
            startActivity(m);
            break;
        case R.id.time_button:
            Intent t = new Intent(this, TetrisGame.class);
            startActivity(t);
            break;
        case R.id.toy_button:
            Intent to = new Intent(this, TetrisGame.class);
            startActivity(to);
            break;
        case R.id.score_button:
            Intent s = new Intent(this, TetrisGame.class);
            startActivity(s);
            break;
        case R.id.exit_button:
            finish();
            break;
        }
    }
}
