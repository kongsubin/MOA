package com.handong.moa.chat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.handong.moa.R;
import com.handong.moa.data.MyData;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ImageScreenActivity extends AppCompatActivity {
    private String imgPath;
    private Uri imgUri;
    private String roomID;
    private final int GET_GALLERY_IMAGE = 200;
    private int where;

    private ImageView preImageView;
    private ImageButton deleteButton;
    private TextView sendButton;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference roodIdReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_screen);

        //* get the imgPath
        Intent imgIntent = getIntent();
        where = imgIntent.getIntExtra("type", 200);
        roomID = imgIntent.getStringExtra("room_id");
        imgPath = imgIntent.getStringExtra("img_path");
        imgUri = Uri.parse(imgPath);



        preImageView = findViewById(R.id.pre_image);
        deleteButton = findViewById(R.id.delete_button);
        sendButton = findViewById(R.id.send_button);

        if(where == GET_GALLERY_IMAGE) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imgUri);
                preImageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        }

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //1. FirebaseStorage??? ???????????? ?????? ????????????
                FirebaseStorage firebaseStorage= FirebaseStorage.getInstance();

                //2. ???????????? ????????? node??? ???????????? ??????
                SimpleDateFormat sdf= new SimpleDateFormat("yyyyMMddhhmmss");
                String filename= sdf.format(new Date())+ ".png";//?????? ???????????? ????????? ?????? 20191023142634
                //?????? ???????????? ????????? ?????? ???????????? ???????????? ???????????????. ???????????? ???????????? ?????? ????????? ????????????.

                String filePath = roomID + "/" + filename;
                StorageReference imgRef = firebaseStorage.getReference(filePath);
                UploadTask uploadTask =imgRef.putFile(imgUri);

                sendMessageFirebase(MyData.getName(), "none", filePath, MyData.uid, MyData.getPhotoUrl().toString());

                finish();
            }
        });
    }

    //send message on firebase
    private void sendMessageFirebase(String name, String content, String image, String uid, String url){
        Calendar calendar = Calendar.getInstance(); //?????? ????????? ????????? ?????? ??????
        String time = calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE);

        ChatMessageItem messageItem = new ChatMessageItem(name, content, time, image, uid, url);

        firebaseDatabase = FirebaseDatabase.getInstance();
        roodIdReference = firebaseDatabase.getReference(roomID);
        roodIdReference.push().setValue(messageItem);
    }
}