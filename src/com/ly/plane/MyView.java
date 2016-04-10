package com.ly.plane;

import java.util.Random;
import java.util.Vector;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

public class MyView extends SurfaceView implements Callback, Runnable{

	private SurfaceHolder holder;
	private Context context;
	private int screenW, screenH;
	private Bitmap bg, plane, e1, e2, e3, e4, bullet, bullet2, bm1, bm2, bm3, pause;
	private Canvas canvas;
	private Paint paint;
	private Thread thread;
	private boolean flag;
	private Background background;
	private Player player;
	private Vector<Enemy> enemies;
	private Vector<Bullet> bullets, eBullets;
	private Vector<Boom> booms;
	private int bTime;
	private int eTime;
	private int count = 1;
	private Random random;
	private int goal;
	private SoundPool sp;
	private int boom;
	private MediaPlayer media;
	private Typeface tf;
	private Handler handler;
	
	public MyView(Context context) {
		super(context);
		this.context = context;
		holder = getHolder();
		holder.addCallback(this);
		sp = new SoundPool(5, AudioManager.STREAM_MUSIC, 100);
		boom = sp.load(context, R.raw.boom, 1);
		media = MediaPlayer.create(context, R.raw.game);
		media.setLooping(true);
		tf = Typeface.createFromAsset(context.getAssets(), "fonts/xk.ttf");
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if(msg.what == 0x101) {
					popupWindow(MyView.this);
					setHighScore(goal);
				}
			}
		};
	}

	@Override
	public void run() {
		while(flag) {
			myDraw();
			logic();
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		screenW = this.getWidth();
		screenH = this.getHeight();
		paint = new Paint();
		bg = BitmapFactory.decodeResource(getResources(), R.drawable.bg);
		plane = BitmapFactory.decodeResource(getResources(), R.drawable.plane);
		e1 = BitmapFactory.decodeResource(getResources(), R.drawable.enemy_1);
		e2 = BitmapFactory.decodeResource(getResources(), R.drawable.enemy_2);
		e3 = BitmapFactory.decodeResource(getResources(), R.drawable.enemy_3);
		e4 = BitmapFactory.decodeResource(getResources(), R.drawable.enemy_4);
		bullet = BitmapFactory.decodeResource(getResources(), R.drawable.bullet);
		bullet2 = BitmapFactory.decodeResource(getResources(), R.drawable.bullet2);
		bm1 = BitmapFactory.decodeResource(getResources(), R.drawable.boom1);
		bm2 = BitmapFactory.decodeResource(getResources(), R.drawable.boom2);
		bm3 = BitmapFactory.decodeResource(getResources(), R.drawable.boom3);
		pause = BitmapFactory.decodeResource(getResources(), R.drawable.pause);
		background = new Background(bg, screenW, screenH);
		player = new Player(plane, screenW, screenH);
		enemies = new Vector<Enemy>();
		bullets = new Vector<Bullet>();
		eBullets = new Vector<Bullet>();
		booms = new Vector<Boom>();
		random = new Random();
		flag = true;
		goal = 0;
		thread = new Thread(this);
		thread.start();
		media.start();
		switch(Util.level) {
		case 1 :
			eTime = 50;
			bTime = 50;
			break;
		case 2 :
			eTime = 35;
			bTime = 40;
			break;
		case 3 :
			eTime = 20;
			bTime = 30;
			break;
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		flag = false;
		media.stop();
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int x = (int)event.getX();
		int y = (int)event.getY();
		switch(event.getAction()) {
		case MotionEvent.ACTION_MOVE :
			player.setX(x - plane.getWidth() / 2);
			player.setY(y - plane.getHeight() / 2);
			break;
		case MotionEvent.ACTION_DOWN :
			if(x >= 0 && x <= pause.getWidth() && y >= 0 && y <= pause.getHeight()) {
				flag = false;
				initPopWindow(this);
			}
			break;
		}
		return true;
	}
	
	public void myDraw() {
		try {
			canvas = holder.lockCanvas();
			background.draw(canvas, paint);
			drawGoal(canvas, paint);
			drawPause(canvas, paint);
			player.draw(canvas, paint);
			for(int i = 0; i < enemies.size(); i++) {
				enemies.elementAt(i).draw(canvas, paint);
			}
			for(int i = 0; i < bullets.size(); i++) {
				bullets.elementAt(i).draw(canvas, paint);
			}
			for(int i = 0; i < eBullets.size(); i++) {
				eBullets.elementAt(i).draw(canvas, paint);
			}
			for(int i = 0; i < booms.size(); i++) {
				booms.elementAt(i).draw(canvas, paint);
			}
		}catch(Exception e) {
			
		}finally {
			if(canvas != null) {
				holder.unlockCanvasAndPost(canvas);
			}
		}
	}
	
	public void logic() {
		background.logic();
		player.logic();
		for(int i = 0; i < enemies.size(); i++) {
			Enemy en = enemies.elementAt(i);
			if(en.isDead()) {
				enemies.removeElementAt(i);
			}else {
				en.logic();
			}
			if(player.isCollision(en)) {
				booms.addElement(new Boom(bm3, player.getX(), player.getY(), 15));
				flag = false;
				myDraw();
				sp.play(boom, 1f, 1f, 0, 0, 1);
				handler.sendEmptyMessage(0x101);
			}
		}
		count++;
		if(count % eTime == 0) {
			int type = random.nextInt(4) + 1;
			int x = random.nextInt(screenW - 48);
			switch(type) {
			case 1 :
				enemies.addElement(new Enemy(e1, 1, x, 0, screenW, screenH));
				break;
			case 2 :
				enemies.addElement(new Enemy(e2, 2, x, 0, screenW, screenH));
				break;
			case 3 :
				enemies.addElement(new Enemy(e3, 3, x, 0, screenW, screenH));
				break;
			case 4 :
				enemies.addElement(new Enemy(e4, 4, x, 0, screenW, screenH));
				break;
			default :
				break;
			}
		}
		if(count % bTime == 0) {
			for(int i = 0; i < enemies.size(); i++) {
				Enemy en = enemies.elementAt(i);
				int bt = 0;
				switch(en.getType()) {
				case 1 :
					bt = 2;
					break;
				case 2 :
					bt = 3;
					break;
				case 3 :
					bt = 4;
					break;
				case 4 :
					bt = 5;
					break;
				}
				eBullets.add(new Bullet(bullet2, bt, en.getX() + (en.getEW() - bullet.getWidth()) / 2, en.getY() + en.getEH() - bullet.getHeight(), screenH));
			}
		}
		for(int i = 0; i < eBullets.size(); i++) {
			Bullet b = eBullets.elementAt(i);
			if(b.isDead()) {
				eBullets.removeElement(b);
			}else {
				b.logic();
			}
		}
		if(count % 10 == 0) {
			bullets.add(new Bullet(bullet, 1, player.getX() + 50, player.getY() - 20, screenH));
		}
		for(int i = 0; i < bullets.size(); i++) {
			Bullet b = bullets.elementAt(i);
			if(b.isDead()) {
				bullets.removeElement(b);
			}else {
				b.logic();
			}
		}
		for(int i = 0; i < eBullets.size(); i++) {
			if(player.isCollision(eBullets.elementAt(i))) {
				booms.addElement(new Boom(bm3, player.getX(), player.getY(), 5));
				flag = false;
				myDraw();
				sp.play(boom, 1f, 1f, 0, 0, 1);
				handler.sendEmptyMessage(0x101);
			}
		}
		for(int i = 0; i < bullets.size(); i++) {
			Bullet b = bullets.elementAt(i);
			for(int j = 0; j < enemies.size(); j++) {
				if(enemies.elementAt(j).isCollision(b)) {
					Enemy en = enemies.elementAt(j);
					if(en.getHp() == 0) {
						en.setDead(true);
						goal += en.getType();
						if(en.getType() == 1 || en.getType() == 2) {
							booms.add(new Boom(bm1, en.getX(), en.getY(), 15));
						}else {
							booms.add(new Boom(bm2, en.getX(), en.getY(), 15));
						}
						sp.play(boom, 1f, 1f, 0, 0, 1);
					}
					b.setDead(true);
				}
			}
		}
		for(int i = 0; i < booms.size(); i++) {
			Boom b = booms.elementAt(i);
			if(b.isDead()) {
				booms.removeElement(b);
			}else {
				b.logic();
			}
		}
	}
	
	private void drawGoal(Canvas canvas, Paint paint) {
		String s = "得分：" + goal;
		Rect r = new Rect();
		paint.setTypeface(tf);
		paint.setTextSize(40);
		paint.setColor(Color.WHITE);
		paint.getTextBounds(s, 0, s.length(), r);
		canvas.drawText(s, screenW - r.left - r.right - 15, -r.top + 15, paint);
	}
	
	private void drawPause(Canvas canvas, Paint paint) {
		canvas.drawBitmap(pause, 0, 0, paint);
	}

	private void initPopWindow(View v) {
		View view = LayoutInflater.from(context).inflate(R.layout.pop, null);
		Button b1 = (Button)view.findViewById(R.id.back);
		Button b2 = (Button)view.findViewById(R.id.restart);
		Button b3 = (Button)view.findViewById(R.id.b_main);
		b1.setTypeface(tf);
		b2.setTypeface(tf);
		b3.setTypeface(tf);
		final PopupWindow pop = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT, true);
		pop.showAtLocation(v, Gravity.CENTER, 0, 0);
		
		b1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				flag = true;
				thread = new Thread(MyView.this);
				thread.start();
				pop.dismiss();
			}			
		});
		
		b2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				flag = true;
				enemies.removeAllElements();
				bullets.removeAllElements();
				eBullets.removeAllElements();
				booms.removeAllElements();
				goal = 0;
				player.setX((screenW - plane.getWidth()) / 2);
				player.setY(screenH - plane.getHeight());
				thread = new Thread(MyView.this);
				thread.start();
				pop.dismiss();
			}			
		});
		
		b3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, MainActivity.class);
				context.startActivity(intent);
			}			
		});
	}
	
	private void popupWindow(View v) {
		View view = LayoutInflater.from(context).inflate(R.layout.end_pop, null, false);
		Button b1 = (Button)view.findViewById(R.id.e_restart);
		Button b2 = (Button)view.findViewById(R.id.eb_goal);
		Button b3 = (Button)view.findViewById(R.id.e_back);
		TextView score = (TextView)view.findViewById(R.id.y_goal);
		TextView end = (TextView)view.findViewById(R.id.end);
		score.setTypeface(tf);
		score.setText("你的得分：" + goal);
		end.setTypeface(tf);
		b1.setTypeface(tf);
		b2.setTypeface(tf);
		b3.setTypeface(tf);
		final PopupWindow pop = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT, true);
		pop.showAtLocation(v, Gravity.CENTER, 0, 0);
		
		b1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				flag = true;
				enemies.removeAllElements();
				bullets.removeAllElements();
				eBullets.removeAllElements();
				booms.removeAllElements();
				goal = 0;
				player.setX((screenW - plane.getWidth()) / 2);
				player.setY(screenH - plane.getHeight());
				thread = new Thread(MyView.this);
				thread.start();
				pop.dismiss();
			}			
		});
		
		b2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, ScoreActivity.class);
				context.startActivity(intent);
			}			
		});
		
		b3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, MainActivity.class);
				context.startActivity(intent);
			}			
		});
	}
	
	private void setHighScore(int score) {
		SharedPreferences sp = context.getSharedPreferences("score.txt", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		
		int[] arr = {sp.getInt("score1", 0),
				sp.getInt("score2", 0),
        		sp.getInt("score3", 0)};
		
		if(score > arr[0]) {
			editor.putInt("score1", score);
			editor.putInt("score2", arr[0]);
			editor.putInt("score3", arr[1]);
			editor.commit();
		}else if(score > arr[1]) {
			editor.putInt("score2", score);
			editor.putInt("score3", arr[2]);
			editor.commit();
		}else if(score > arr[2]) {
			editor.putInt("score3", score);
			editor.commit();
		}
	}
}
