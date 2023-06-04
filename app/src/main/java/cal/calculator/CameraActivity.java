package cal.calculator;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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
    private ImageView imageView;
    private Button btn_picture;

    private static final int REQUEST_IMAGE_CODE = 101;

    File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "MyDrawings");

    private String serverIp = "192.168.0.7";
    private int serverPort = 7777;



    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        imageView = findViewById(R.id.camera_image);
        btn_picture = findViewById(R.id.btn_camera);
        btn_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePicture();
            }
        });
    }
    public byte[] convertToBytes(Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    public void sendImageToServer(Bitmap bitmap){
        byte[] imageBytes = convertToBytes(bitmap);

        new Thread(new Runnable(){
            @Override
            public void run(){
                try {
                    Socket socket = new Socket(serverIp, serverPort);
                    DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                    dataOutputStream.writeInt(imageBytes.length);
                    dataOutputStream.write(imageBytes);
                    dataOutputStream.close();

                    DataInputStream inputStream = new DataInputStream(socket.getInputStream());
                    String[] array = getImageToServer(inputStream);

                    socket.close();

                    Intent intent = new Intent();
                    intent.putExtra("textarray",array);
                    setResult(RESULT_OK, intent);
                    finish();



                } catch (IOException e) {
                    e.printStackTrace();

                }
            }
        }).start();
    }
    public static String[] getImageToServer(DataInputStream inputStream) throws IOException {
        int len = inputStream.readInt();

        if(len>0){
            String[] textArray = new String[len];

            for(int i=0; i<len; i++){
                int textlen = inputStream.readInt();
                byte[] textBytes = new byte[textlen];
                inputStream.readFully(textBytes);
                String text = new String(textBytes);
                textArray[i] = text;
            }
            return textArray;
        }
        return null;

    }
    public void takePicture(){
        Intent imageTakeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if(imageTakeIntent.resolveActivity(getPackageManager())!= null){
            startActivityForResult(imageTakeIntent,REQUEST_IMAGE_CODE);
        }

    }
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_IMAGE_CODE && resultCode == RESULT_OK){
            Bundle extras = data.getExtras();
            Bitmap bitmap = (Bitmap) extras.get("data");
            sendImageToServer(bitmap);
            imageView.setImageBitmap(bitmap);

        }
    }
}