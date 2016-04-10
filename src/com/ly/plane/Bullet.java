package com.ly.plane;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Bullet {
	private Bitmap bitmap;
	private int bx, by;
	private int bW, bH;
	private int speed;
	private int type;
	private int screenH;
	private static final int BULLET1 = 1;
	private static final int BULLET2 = 2;
	private static final int BULLET3 = 3;
	private static final int BULLET4 = 4;
	private static final int BULLET5 = 5;
	private boolean isDead;
	
	public Bullet(Bitmap bitmap, int type, int bx, int by, int screenH) {
		this.bitmap = bitmap;
		this.type = type;
		this.bx = bx;
		this.by = by;
		this.screenH = screenH;
		init();
	}

	private void init() {
		bW = bitmap.getWidth();
		bH = bitmap.getHeight();
		switch(type) {
		case BULLET1 :
			speed = 10;
			break;
		case BULLET2 :
			speed = 21;
			break;
		case BULLET3 :
			speed = 16;
			break;
		case BULLET4 :
			speed = 11;
			break;
		case BULLET5 :
			speed = 11;
			break;
		}
	}
	
	public void draw(Canvas canvas, Paint paint) {
		canvas.drawBitmap(bitmap, bx, by, paint);
	}
	
	public void logic() {
		switch(type) {
		case BULLET1 :
			by -= speed;
			if(by < 0) {
				isDead = true;
			}
			break;
		case BULLET2 :
			by += speed;
			if(by > screenH) {
				isDead = true;
			}
			break;
		case BULLET3 :
			by += speed;
			if(by > screenH) {
				isDead = true;
			}
			break;
		case BULLET4 :
			by += speed;
			if(by > screenH) {
				isDead = true;
			}
			break;
		case BULLET5 :
			by += speed;
			if(by > screenH) {
				isDead = true;
			}
			break;
		}
	}
	
	public int getBx() {
		return bx;
	}

	public int getBy() {
		return by;
	}

	public boolean isDead() {
		return isDead;
	}

	public void setDead(boolean isDead) {
		this.isDead = isDead;
	}

	public int getbW() {
		return bW;
	}

	public int getbH() {
		return bH;
	}
	
}
