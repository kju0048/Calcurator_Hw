package cal.calculator;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;

public class CameraActivity extends AppCompatActivity {

    private Button btn_picture;
    private Bitmap bitmap=null;
    private Socket socket;
    private DataOutputStream dos;
    private DataInputStream dis;
    private String msg;
    private Handler mHandler;
    private ImageView iv;
    private static final int REQUEST_IMAGE_CODE = 101;
    private boolean ret = true;
    private Intent imageTakeIntent;


    private String serverIp = "118.67.135.149";
    private int serverPort = 7777;



    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        iv = findViewById(R.id.camera_image);

        if(ret) {
            ret = false;
            imageTakeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if(imageTakeIntent.resolveActivity(getPackageManager())!= null){
                startActivityForResult(imageTakeIntent,REQUEST_IMAGE_CODE);
            }
        }
    }

    public void btn_outc(View v){
        setResult(RESULT_CANCELED);
        finish();
    }

    public byte[] convertToBytes(Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    public void sendImageToServer(Bitmap bitmap){
        iv.setImageBitmap(bitmap);
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_IMAGE_CODE && resultCode == RESULT_OK){
            Bundle extras = data.getExtras();
            bitmap = (Bitmap) extras.get("data");
            sendImageToServer(bitmap);
        }
    }
}