package tadakazu1972.tinyquest2;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity implements Runnable, android.view.View.OnTouchListener {
    private MainActivity mainActivity;
    private View view;
    private SurfaceView surfaceView;
    private Thread thread;
    private volatile boolean isThreadRun;
    public int deviceWidth; //Device's size
    public int deviceHeight; //Device's size
    final float VIEW_WIDTH = 320.0f;
    final float VIEW_HEIGHT = 320.0f;
    private int width;
    private int height;
    private float scale;
    private float scaleX;
    private float scaleY;
    protected Map map;
    protected float mapx;
    protected float mapy;
    protected float m;
    protected int baseX; // for drawing Map Array's baseX
    protected int baseY; // for drawing Map Array's baseY
    private Bitmap[] sBg = new Bitmap[4];
    private Bitmap[] sMap = new Bitmap[14];
    private Bitmap[] sArthur = new Bitmap[8];
    private Bitmap[] sDamage = new Bitmap[7];
    protected MyChara myChara;
    private int touchDirection;
    boolean repeatFlg;
    private Button btnUp;
    private Button btnRight;
    private Button btnDown;
    private Button btnLeft;
    protected static final int DN = 4; //ダメージ表現最大数
    protected Damage[] damage = new Damage[DN]; //ダメージ表現
    protected Paint paintDamage = new Paint();
    protected Paint paintMonsterHp = new Paint();
    protected int damageIndex;
    //ダメージ時にモンスターHP表示して３秒後に消すタイマー
    private Timer monsterHpTimer = null;
    private TimerTask monsterHpTimerTask = null;
    private Handler monsterHpTimerHandler = new Handler();
    //開発中パラメータ表示用
    protected Paint paint0 = new Paint();

    public MainActivity(){
        super();
        // Create MAP DATA
        mapx = 0.0f;
        mapy = 0.0f;
        map = new Map();
        m = 0.0f;
        baseX = 0;
        baseY = 0;
        // Create MyChara
        myChara = new MyChara();
        touchDirection=0;
        repeatFlg=false;
        // Create Damage
        for (int i=0;i<damage.length;i++){
            damage[i] = new Damage();
        }
        damageIndex=0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = this;
        view = this.getWindow().getDecorView();
        setContentView(R.layout.activity_main);
        surfaceView = (SurfaceView)findViewById(R.id.MainSurfaceView);
        getDisplaySize();
        createButton();
        //タイマー生成
        monsterHpTimer = new Timer();
        monsterHpTimerTask = new monsterHpTimerTask();
        monsterHpTimer.schedule(monsterHpTimerTask, 3000, 3000); //3秒後
    }

    @Override
    public void onResume(){
        super.onResume();
        // Create Sprite
        createSprite();
        isThreadRun = true;
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void onPause(){
        super.onPause();
        isThreadRun = false;
        while(true){
            try{
                thread.join();
                break;
            } catch(InterruptedException e){

            }
        }
        thread = null;
    }

    @Override
    public void run(){
        SurfaceHolder holder = surfaceView.getHolder();
        while(isThreadRun){
            if(holder.getSurface().isValid()){
                synchronized (this){
                    // Draw Screen
                    draw(holder);
                }
                // Moving
                moveCharacters();
            }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent e){
        //onTouchは画面を押した時と離した時の両方のイベントを取得する
        int action = e.getAction();
        switch(action){
            //ボタンから指が離れた時
            case MotionEvent.ACTION_UP:
                //連続イベントフラグをfalse
                repeatFlg = false;
                touchDirection = 0;
                //ゲームスタート
                //if (mainSurfaceView.gs==0) mainSurfaceView.gs=1;
                break;
            case MotionEvent.ACTION_DOWN:
                switch(v.getId()){
                    case R.id.btnUp:
                        myChara.base_index = 6;
                        touchDirection = 1;
                        break;
                    case R.id.btnRight:
                        touchDirection = 2;
                        break;
                    case R.id.btnDown:
                        myChara.base_index = 0;
                        touchDirection = 3;
                        break;
                    case R.id.btnLeft:
                        touchDirection = 4;
                        break;
                }
        }
        return false;
    }

    public void getDisplaySize(){
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        deviceWidth = size.x;
        deviceHeight = size.y;
        scaleX = deviceWidth / VIEW_WIDTH;
        scaleY = deviceHeight / VIEW_HEIGHT;
        scale = scaleX > scaleY ? scaleY : scaleX;
    }

    public void createButton(){
        /*OnClickだと動きっぱなしになるので不採用
        view.findViewById(R.id.btnUp).setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                myChara.base_index = 6;
                touchDirection = 1;
            }
        });
        view.findViewById(R.id.btnRight).setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                touchDirection = 2;
            }
        });
        view.findViewById(R.id.btnDown).setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                myChara.base_index = 0;
                touchDirection = 3;
            }
        });
        view.findViewById(R.id.btnLeft).setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v){
                touchDirection = 4;
            }
        });*/
        //OnTouchのほうがボタンを離すと止まる動きを実現できるのでこちらを採用
        // attach button from res/layout/activity_main.xml
        btnUp = (Button)findViewById(R.id.btnUp);
        btnRight = (Button)findViewById(R.id.btnRight);
        btnDown = (Button)findViewById(R.id.btnDown);
        btnLeft = (Button)findViewById(R.id.btnLeft);
        // set OnTouchListener
        btnUp.setOnTouchListener(this);
        btnRight.setOnTouchListener(this);
        btnDown.setOnTouchListener(this);
        btnLeft.setOnTouchListener(this);
    }

    public void createSprite(){
        Resources res = this.getResources();
        if (sBg[0] == null) {
            // Back Ground
            sBg[0] = BitmapFactory.decodeResource(res, R.drawable.black);
            sBg[1] = BitmapFactory.decodeResource(res, R.drawable.rock2);
            sBg[2] = BitmapFactory.decodeResource(res, R.drawable.back04);
            sBg[3] = BitmapFactory.decodeResource(res, R.drawable.back05);
            // Map
            sMap[0] = BitmapFactory.decodeResource(res, R.drawable.greenfield);
            sMap[1] = BitmapFactory.decodeResource(res, R.drawable.dessert);
            sMap[2] = BitmapFactory.decodeResource(res, R.drawable.grass01);
            sMap[3] = BitmapFactory.decodeResource(res, R.drawable.road01);
            sMap[4] = BitmapFactory.decodeResource(res, R.drawable.flooring);
            sMap[5] = BitmapFactory.decodeResource(res, R.drawable.redbrick);
            sMap[6] = BitmapFactory.decodeResource(res, R.drawable.water);
            sMap[7] = BitmapFactory.decodeResource(res, R.drawable.water);
            sMap[8] = BitmapFactory.decodeResource(res, R.drawable.tree);
            sMap[9] = BitmapFactory.decodeResource(res, R.drawable.brickl);
            sMap[10] = BitmapFactory.decodeResource(res, R.drawable.brickm);
            sMap[11] = BitmapFactory.decodeResource(res, R.drawable.brick);
            sMap[12] = BitmapFactory.decodeResource(res, R.drawable.door_close);
            sMap[13] = BitmapFactory.decodeResource(res, R.drawable.door_open);
            // Arthur
            sArthur[0] = BitmapFactory.decodeResource(res, R.drawable.arthur01);
            sArthur[1] = BitmapFactory.decodeResource(res, R.drawable.arthur02);
            sArthur[2] = BitmapFactory.decodeResource(res, R.drawable.arthur03);
            sArthur[3] = BitmapFactory.decodeResource(res, R.drawable.arthur04);
            sArthur[4] = BitmapFactory.decodeResource(res, R.drawable.arthur05);
            sArthur[5] = BitmapFactory.decodeResource(res, R.drawable.arthur06);
            sArthur[6] = BitmapFactory.decodeResource(res, R.drawable.arthur07);
            sArthur[7] = BitmapFactory.decodeResource(res, R.drawable.arthur08);
            //sDamage = new Bitmap[7];
            sDamage[0] = BitmapFactory.decodeResource(res,R.drawable.star01);
            sDamage[1] = BitmapFactory.decodeResource(res,R.drawable.star02);
            sDamage[2] = BitmapFactory.decodeResource(res,R.drawable.star03);
            sDamage[3] = BitmapFactory.decodeResource(res,R.drawable.star04);
            sDamage[4] = BitmapFactory.decodeResource(res,R.drawable.star05);
            sDamage[5] = BitmapFactory.decodeResource(res,R.drawable.star06);
            sDamage[6] = BitmapFactory.decodeResource(res,R.drawable.star07);
        }
        //ダメージポイント表示用Paint設定
        paintDamage.setTextSize(12);
        paintDamage.setColor(Color.WHITE);
        paintDamage.setTextAlign(Paint.Align.CENTER);
        paintDamage.setTypeface(Typeface.DEFAULT_BOLD);
        //ダメージ時モンスターHP表示用Paint設定
        paintMonsterHp.setTextSize(12);
        paintMonsterHp.setColor(Color.RED);
        paintMonsterHp.setTextAlign(Paint.Align.CENTER);
        //paintMonsterHp.setTypeface(Typeface.DEFAULT_BOLD);
        //開発時各種パラメーター確認用
        paint0.setTextSize(12);
        paint0.setColor(Color.WHITE);
        paint0.setTextAlign(Paint.Align.LEFT);
        paint0.setTypeface(Typeface.DEFAULT_BOLD);
    }

    public class monsterHpTimerTask extends TimerTask {
        @Override
        public void run(){
            monsterHpTimerHandler.post( new Runnable(){
                public void run(){
                }
            });
        }
    }

    public void draw(SurfaceHolder holder){
        Canvas canvas = holder.lockCanvas();
        if (canvas != null){
            //Adjust Screen Size
            canvas.scale(scale, scale);
            // Draw Back
            for (int y=0;y<11;y++){
                for (int x=0;x<10;x++){
                    canvas.drawBitmap(sBg[3], x*32.0f, y*32.0f, null);
                }
            }
            // Draw MAP
            // Draw Next Map
            // 上辺
            if (baseY>0){
                for (int x=0;x<10;x++) canvas.drawBitmap(sMap[map.MAP[baseY-1][baseX+x]], x*32.0f+mapx, -32.0f+mapy, null);
            }
            // 右辺
            if (baseX<19){
                for (int y=0;y<10;y++) canvas.drawBitmap(sMap[map.MAP[baseY+y][baseX+10]], 320.0f+mapx, y*32.0f+mapy, null);
            }
            // 左辺
            if (baseX>0){
                for (int y=0;y<10;y++) canvas.drawBitmap(sMap[map.MAP[baseY+y][baseX-1]], -32.0f+mapx, y*32.0f+mapy, null);
            }
            // 下辺
            if (baseY<19){
                for (int x=0;x<10;x++) canvas.drawBitmap(sMap[map.MAP[baseY+10][baseX+x]], x*32.0f+mapx, 320.0f+mapy, null);
            }
            // Draw Main Map
            for (int y=0;y<10;y++){
                for (int x=0;x<10;x++){
                    canvas.drawBitmap(sMap[map.MAP[baseY+y][baseX+x]], x*32.0f+mapx, y*32.0f+mapy, null);
                }
            }
            // Draw MyChara
            int i = myChara.base_index + myChara.index / 10;
            if (i > 7) i = 0;
            canvas.drawBitmap(sArthur[i], myChara.x, myChara.y, null);
            // Draw Damage
            for (Damage aDamage : damage){
                if ( aDamage.visible ==1 ){
                    int di = aDamage.index / 10;
                    if ( di > 6 ) di = 6;
                    //ダメージ表現
                    canvas.drawBitmap(sDamage[di], aDamage.x, aDamage.y, null);
                    //ダメージポイント表示
                    canvas.drawText(String.valueOf(aDamage.point), aDamage.x + 16.0f, aDamage.py, paintDamage);
                }
            }

            // Draw Text
            canvas.drawText("mapx="+String.valueOf(mapx), 20, 10, paint0);
            canvas.drawText("mapy="+String.valueOf(mapy), 20, 20, paint0);
            canvas.drawText("x="+String.valueOf(myChara.x), 20, 30, paint0);
            canvas.drawText("y="+String.valueOf(myChara.y), 20, 40, paint0);
            canvas.drawText("wx="+String.valueOf(myChara.wx), 20, 50, paint0);
            canvas.drawText("wy="+String.valueOf(myChara.wy), 20, 60, paint0);
            canvas.drawText("baseX="+String.valueOf(baseX), 20, 70, paint0);
            canvas.drawText("baseY="+String.valueOf(baseY), 20, 80, paint0);
        }
        holder.unlockCanvasAndPost(canvas);
    }

    public void moveCharacters(){
        myChara.move( touchDirection, map, this);
        for (Damage aDamage : damage){
            aDamage.move();
        }
    }
}
