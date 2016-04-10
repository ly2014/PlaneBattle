package com.ly.plane;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Player {

	private Bitmap plane;
	private int screenW, screenH;
	private int x, y;//飞机坐标
	
	public Player(Bitmap plane, int screenW, int screenH) {
		this.plane = plane;
		this.screenW = screenW;
		this.screenH = screenH;
		init();
	}
	
	private void init() {
		x = (screenW - plane.getWidth()) / 2;
		y = screenH - plane.getHeight();
	}
	
	public void draw(Canvas canvas, Paint paint) {
		canvas.drawBitmap(plane, x, y, paint);
	}
	
	public void logic() {
		if(x + plane.getWidth() >= screenW) {
			x = screenW - plane.getWidth();
		}
		if(x <= 0) {
			x = 0;
		}
		if(y + plane.getHeight() >= screenH) {
			y = screenH - plane.getHeight();
		}
		if(y <= 0) {
			y = 0;
		}
	}
	
	public boolean isCollision(Enemy en) {
		if(x > en.getX() + en.getEW()) {
			return false;
		}else if(x + plane.getWidth() < en.getX()) {
			return false;
		}else if(y > en.getY() + en.getEH()) {
			return false;
		}else if(y + plane.getHeight() < en.getY()) {
			return false;
		}
		return true;
	}
	
	public boolean isCollision(Bullet bullet) {
		if(x > bullet.getBx() + bullet.getbW()) {
			return false;
		}else if(x + plane.getWidth() < bullet.getBx()) {
			return false;
		}else if(y > bullet.getBy() + bullet.getbH()) {
			return false;
		}else if(y + plane.getHeight() < bullet.getBy()) {
			return false;
		}
		return true;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
}
