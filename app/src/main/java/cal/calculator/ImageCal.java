package cal.calculator;


import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ImageCal extends AppCompatActivity {
    private Bitmap mBitmap;
    private MyPaintView myView;
    private Socket socket;
    private DataOutputStream dos;
    private DataInputStream dis;
    private String msg;
    private Handler mHandler;
    private Paint paint;
    private Path path;
    private Bitmap bitmap;
    private Canvas canvas;
    private Button sendButton;
    DataOutputStream dataOutputStream;
    DataInputStream inputStream;

    String input_message;

    File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "MyDrawings");

    private String serverIp = "118.67.135.149";
    private int serverPort = 7777;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_cal);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);


        setTitle("간단한 그림판");

        LinearLayout paintLayout = findViewById(R.id.paintLayout);
        myView = new MyPaintView(this);
        paintLayout.addView(myView);

        Button btnCancle = findViewById(R.id.btnCancle);
        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        Button btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendImageToServer(mBitmap);
            }
        });
    }
    public byte[] convertToBytes(Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    private void sendImageToServer(Bitmap bitmap){
        mHandler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                byte[] imageBytes = convertToBytes(bitmap);

                try {
                    socket = new Socket(serverIp, serverPort);
                } catch (IOException e1) {
                    Log.w("서버 접속 불가", "서버 접속 불가");
                }
                try {
                    dos = new DataOutputStream(socket.getOutputStream());
                    dis = new DataInputStream(socket.getInputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.w("버퍼", "버퍼 생성 불가");
                }
                try {
                    dos.writeInt(imageBytes.length);
                    dos.flush();

                    dos.write(imageBytes);
                    dos.flush();

                } catch(IOException e){
                    Log.w("error", "erroe occur");
                }
                try{
                    byte[] data1 = new byte[50];
                    dis.read(data1, 0, 50);
                    socket.close();

                    msg = new String(data1,"UTF-8");

                    Intent in = new Intent();
                    in.putExtra("expr", msg);
                    setResult(RESULT_OK, in);
                    finish();
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private class MyPaintView extends View {
        private Canvas mCanvas;
        private Path mPath;
        private Paint mPaint;

        public MyPaintView(Context context) {
            super(context);
            mPath = new Path();
            mPaint = new Paint();
            mPaint.setColor(Color.BLACK);
            mPaint.setAntiAlias(true);
            mPaint.setStrokeWidth(25);
            mPaint.setStyle(Paint.Style.STROKE);
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            mCanvas = new Canvas(mBitmap);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawBitmap(mBitmap, 0, 0, null);
            canvas.drawPath(mPath, mPaint);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            int x = (int) event.getX();
            int y = (int) event.getY();
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mPath.reset();
                    mPath.moveTo(x, y);
                    break;
                case MotionEvent.ACTION_MOVE:
                    mPath.lineTo(x, y);
                    break;
                case MotionEvent.ACTION_UP:
                    mPath.lineTo(x, y);
                    mCanvas.drawPath(mPath, mPaint);
                    mPath.reset();
                    break;
            }
            invalidate();
            return true;
        }


    }
}