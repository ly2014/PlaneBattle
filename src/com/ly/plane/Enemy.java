package com.ly.plane;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Enemy {
	private int type;//敌机种类
	private static final int TYPE1 = 1;
	private static final int TYPE2 = 2;
	private static final int TYPE3 = 3;
	private static final int TYPE4 = 4;
	private Bitmap bitmap;
	private int x, y;
	private int screenW, screenH;
	private int eW, eH;
	private int speed;
	private int hp;
	private boolean isDead;
	
	public Enemy(Bitmap bitmap, int type, int x, int y, int screenW, int screenH) {
		this.bitmap = bitmap;
		this.type = type;
		this.x = x;
		this.y = y;
		this.screenW = screenW;
		this.screenH = screenH;
		init();
	}
	
	private void init() {
		isDead = false;
		eW = bitmap.getWidth();
		eH = bitmap.getHeight();
		switch(type) {
		case TYPE1 :
			speed = 20;
			hp = 1;
			break;
		case TYPE2 :
			speed = 15;
			hp = 2;
			break;
		case TYPE3 :
			speed = 10;
			hp = 3;
			break;
		case TYPE4 :
			speed = 10;
			hp = 4;
			break;
		}
	}
	
	public void draw(Canvas canvas, Paint paint) {
		canvas.drawBitmap(bitmap, x,  y, paint);
	}
	
	public void logic() {
		switch(type) {
		case TYPE1 :
			if(isDead == false) {
				speed -= 1;
				y += speed;
			}
			if(y <= -200) {
				isDead = true;
			}
			break;
		case TYPE2 :
			if(isDead == false) {
				x += speed / 2;
				y += speed;
				if(x > screenW) {
					isDead = true;
				}
			}
			break;
		case TYPE3 :
			if(isDead == false) {
				x -= speed / 2;
				y += speed;
				if(x < -50) {
					isDead = true;
				}
			}
			break;
		case TYPE4 :
			if(isDead == false) {
				y += speed;
				if(y > screenH) {
					isDead = true;
				}
			}
		}
	}
	
	public boolean isCollision(Bullet bullet) {
		if(x > bullet.getBx() + bullet.getbW()) {
			return false;
		}else if(x + eW < bullet.getBx()) {
			return false;
		}else if(y > bullet.getBy() + bullet.getbH()) {
			return false;
		}else if(y + eH < bullet.getBy()) {
			return false;
		}
		hp--;
		return true;
	}
	
	public int getEW() {
		return eW;
	}
	
	public int getEH() {
		return eH;
	}
	
	public int getType() {
		return type;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public boolean isDead() {
		return isDead;
	}
	
	public void setDead(boolean isDead) {
		this.isDead = isDead;
	}

	public int getHp() {
		return hp;
	}

}
