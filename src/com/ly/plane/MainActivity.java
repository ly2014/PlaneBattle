package com.ly.plane;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Util.lists.add(this);
        
        Button b1 = (Button)findViewById(R.id.start);
        Button b2 = (Button)findViewById(R.id.difficulty);
        Button b3 = (Button)findViewById(R.id.b_score);
        Button b4 = (Button)findViewById(R.id.exit);
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/xk.ttf");
        b1.setTypeface(tf);
        b2.setTypeface(tf);
        b3.setTypeface(tf);
        b4.setTypeface(tf);
        
        b1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(MainActivity.this, MainView.class);
				startActivity(intent);
			}
        });
        
        b2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(MainActivity.this, DifficultyActivity.class);
				startActivity(intent);
			}
        });
        
        b3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(MainActivity.this, ScoreActivity.class);
				startActivity(intent);
			}
        });
        
        b4.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				for(int i = 0; i < Util.lists.size(); i++) {
					Util.lists.get(i).finish();
				}
			}
        });
    }
    
    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK) {
			for(int i = 0; i < Util.lists.size(); i++) {
				Util.lists.get(i).finish();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
