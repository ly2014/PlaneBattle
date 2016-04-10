package com.ly.plane;

import android.app.Activity;
import android.os.Bundle;

public class MainView extends Activity{
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(new MyView(this));
        Util.lists.add(this);
	}

}
