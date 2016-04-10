package com.ly.plane;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Boom {

	private Bitmap bitmap;
	private int x, y;
	private int time;
	private int count;
	private boolean isDead;
	
	public Boom(Bitmap bitmap, int x, int y, int time) {
		this.bitmap = bitmap;
		this.x = x;
		this.y = y;
		this.time = time;
		count = 0;
		isDead = false;
	}
	
	public void draw(Canvas canvas, Paint paint) {
		canvas.drawBitmap(bitmap, x, y, paint);
	}
	
	public void logic() {
		if(count <= time) {
			count++;
		}else {
			isDead = true;
		}
	}

	public boolean isDead() {
		return isDead;
	}

	public void setDead(boolean isDead) {
		this.isDead = isDead;
	}
}
