package com.ly.plane;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Background {
	
	private static final int speed = 3;//背景移动速度
	private Bitmap bg;//背景图
	private int bg1y, bg2y;//背景纵坐标
	private float scaleX, scaleY;//缩放比例

	public Background(Bitmap bg, int screenW, int screenH) {
		this.bg = bg;
		scaleX = (float)screenW / bg.getWidth();
		scaleY = (float)screenH / bg.getHeight();
		bg1y = 0;
		bg2y = -screenH;
	}
	
	public void draw(Canvas canvas, Paint paint) {
		canvas.save();
		canvas.scale(scaleX, scaleY, 0, 0);//缩放背景图
		//绘制背景图
		canvas.drawBitmap(bg, 0, bg1y, paint);
		canvas.drawBitmap(bg, 0, bg2y, paint);
		canvas.restore();
	}
	
	public void logic() {
		if(bg1y > bg2y) {
			bg1y += speed;
			bg2y = bg1y - bg.getHeight();
		}else {
			bg2y += speed;
			bg1y = bg2y - bg.getHeight();
		}
		if(bg1y >= bg.getHeight()) {
			bg1y = bg2y - bg.getHeight();
		}else if(bg2y >= bg.getHeight()){
			bg2y = bg1y - bg.getHeight();
		}
	}
	
}
