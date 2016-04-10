package com.ly.plane;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class DifficultyActivity extends Activity{
	
	private SharedPreferences sp;
	private SharedPreferences.Editor editor;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.difficulty);
        Util.lists.add(this);
        
        TextView tv = (TextView)findViewById(R.id.df);
        Spinner lv = (Spinner)findViewById(R.id.level);
        
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/xk.ttf");
        tv.setTypeface(tf);
        
        String[] arr = {"难度一 ","难度二","难度三"};
        
        ArrayAdapter<String> array = new ArrayAdapter<String>(this, R.layout.level_item, arr);
        lv.setAdapter(array);
        
        sp = getSharedPreferences("score.txt", MODE_PRIVATE);
        editor = sp.edit();
        
        int position = sp.getInt("position", 0);
        lv.setSelection(position);
        
        lv.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position,
					long id) {
				if(position == 0) {
					Util.level = 1;
					editor.putInt("position", 0);
					editor.commit();
				}else if(position == 1) {
					Util.level = 2;
					editor.putInt("position", 1);
					editor.commit();
				}else if(position == 2) {
					Util.level = 3;
					editor.putInt("position", 2);
					editor.commit();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {				
			}
        	
        });
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent(DifficultyActivity.this, MainActivity.class);
			startActivity(intent);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
