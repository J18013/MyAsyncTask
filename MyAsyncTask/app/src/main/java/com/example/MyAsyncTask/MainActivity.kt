package com.example.MyAsyncTask

import android.content.Context
import android.content.pm.ActivityInfo
//import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
//import android.os.SystemClock
import kotlin.properties.Delegates
import android.util.Log
import android.view.SurfaceHolder
import android.view.View
import android.view.View.OnClickListener
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() ,SensorEventListener,SurfaceHolder.Callback, OnClickListener{
    //センサーの変数宣言
    private var mSensorManager:SensorManager by Delegates.notNull<SensorManager>()
    private var mAccSensor:Sensor by Delegates.notNull<Sensor>()
    private var mHolder: SurfaceHolder by Delegates.notNull<SurfaceHolder>()
    private var mSurfaceWidth : Int by Delegates.notNull<Int>()
    private var mSurfaceHeight : Int by Delegates.notNull<Int>()

    //ボール
    private var mFrom : Long by Delegates.notNull<Long>()
    private var mTo : Long by Delegates.notNull<Long>()
    private  var mVX : Float by Delegates.notNull<Float>()
    private  var mVY : Float by Delegates.notNull<Float>()
    private  var mBallx :Float by Delegates.notNull<Float>()
    private  var mBally : Float by Delegates.notNull<Float>()
    private var RADIUS = 50.0f
    private var COEF = 1000.0f


    //衝突判定
    private var flg = false

    //ゴール座標
    private var left : Int = 10
    private var top : Int = 100
    private  var right : Int = 300
    private  var bottom : Int= 200




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mFrom = System.currentTimeMillis()

        setContentView(R.layout.activity_main)
        button.setOnClickListener(this)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT


        //センサーマネージャの値を取得
        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        //加速度計のセンサーの値を取得する
        mAccSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        mHolder = surfaceView.holder
        mHolder.addCallback(this)




    }

    override fun onClick(v: View?) {
        if (v != null) {
            if(v.id == R.id.button)
            {
                //
                //
                MyAsyncTask(this).execute("Param1")
            }
        }
    }

    //センサー制度が変更されたとき
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    override fun onSensorChanged(event: SensorEvent?) {

        flg = (left <= mBallx + RADIUS && right >= mBallx - RADIUS
                && top <= mBally + RADIUS && bottom >= mBally - RADIUS)



        drawCanvas()
        Log.d("Sensormanager","----------")
        Log.d("x",event?.values!![0].toString())
        Log.d("y",event?.values!![1].toString())
        Log.d("z",event?.values!![2].toString())

        /*float mBallｘ;//ボールの現在のX座標
　        float mBally;//ボールの現在のY座標
　        float mVX;//ボールのX軸方向へのスピード
　        float mVY;//ボールのY軸方向へのスピード
　        long mFrom;//前回センサーから加速度を取得した時間
　        long mTo;//今回センサーから加速度を取得した時間
*/
        var x = -event?.values!![0]
        var y = event?.values!![1]
       // var z = event?.values!![2]




        mTo = System.currentTimeMillis()

        var t = (mTo - mFrom).toFloat()
        t /= 1000.0f

        var dx = mVX * t + x*t*t / 2.0f
        var dy = mVY * t + y*t*t /2.0f

        mBallx += dx * COEF
        mBally += dy * COEF
        mVX += x * t
        mVY += y * t



        if(mBallx - RADIUS < 0 && mVX < 0){
            mVX = -mVX / 1.5f
            mBallx = RADIUS
        }else if(mBallx + RADIUS > mSurfaceWidth && mVX > 0){
            mVX = -mVX / 1.5f
            mBallx = mSurfaceWidth - RADIUS
        }
        if(mBally - RADIUS < 0 && mVY < 0){
            mVY = -mVY / 1.5f
            mBally = RADIUS
        }else if(mBally + RADIUS > mSurfaceHeight && mVY > 0){
            mVY = -mVY / 1.5f
            mBally = mSurfaceHeight - RADIUS
        }

        mFrom = System.currentTimeMillis()






    }


   /* override fun onResume() {
        super.onResume()
        mSensorManager.registerListener(this,mAccSensor,SensorManager.SENSOR_DELAY_GAME)
    }

    override fun onPause() {
        super.onPause()
        mSensorManager.unregisterListener(this)

    }
*/
    //Surfaceが作られたとき

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
        mSurfaceWidth = width
        mSurfaceHeight = height

        mBallx =  width /2.0f
        mBally = height /2.0f
        mVX = 0.0f
        mVY = 0.0f


    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
            mSensorManager.registerListener(this,mAccSensor,SensorManager.SENSOR_DELAY_GAME)
    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
        mSensorManager.unregisterListener(this)
    }

    private fun drawCanvas(){
        var c = mHolder.lockCanvas()
        c.drawColor(Color.YELLOW)

        var paint :Paint = Paint()
        paint.color = Color.argb(255, 255, 0, 255)
        c.drawCircle(mBallx,mBally,RADIUS,paint)


        paint.color = Color.argb(0,0,0,255)
       // c.drawColor(Color.BLUE)
        var rect : Rect = Rect(left,top,right,bottom)
        c.drawRect(rect,paint)


        if(flg){
       // if(true){
            c.drawText("HelloText", 100f, 100f, paint)
        }

        mHolder.unlockCanvasAndPost(c)//描画だと思われ

    }
}




