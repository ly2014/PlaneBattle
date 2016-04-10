package com.ly.plane;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ScoreActivity extends Activity{
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.high_score);
        
        Util.lists.add(this);
        
        TextView tv = (TextView)findViewById(R.id.l_score);
        ListView lv = (ListView)findViewById(R.id.high_score);
        
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/xk.ttf");
        tv.setTypeface(tf);
        
        SharedPreferences sp = getSharedPreferences("score.txt", MODE_PRIVATE);
        String[] arr = { "1.   " + sp.getInt("score1", 0),
        		"2.   " + sp.getInt("score2", 0),
        		"3.   " + sp.getInt("score3", 0)};
        
        ArrayAdapter<String> array = new ArrayAdapter<String>(this, R.layout.score_item, arr);
        lv.setAdapter(array);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent(ScoreActivity.this, MainActivity.class);
			startActivity(intent);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
