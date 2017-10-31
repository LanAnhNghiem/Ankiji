package com.jishin.ankiji;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.VideoView;

import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.model.ShareVideo;
import com.facebook.share.model.ShareVideoContent;
import com.facebook.share.widget.ShareDialog;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class FbActionActivity extends AppCompatActivity {

    public static int CHOOSE_IMAGE = 1;
    public static int CHOOSE_VIDEO = 2;

    EditText edtTitle, edtDescription, edtUrl;
    Button btnChooseVideo, btnShareLink, btnShareImg, btnShareVid;
    ImageView imageTest;
    VideoView videoView;
    ShareDialog shareDialog;
    ShareLinkContent shareLinkContent;
    Bitmap bitmap;
    Uri selectVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fb_action);
        createParameters();
        shareDialog = new ShareDialog(FbActionActivity.this);
        btnShareLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ShareDialog.canShow(ShareLinkContent.class)) {
                    shareLinkContent = new ShareLinkContent.Builder()
                            .setContentTitle(edtTitle.getText().toString())
                            .setContentDescription(edtDescription.getText().toString())
                            .setContentUrl(Uri.parse(edtUrl.getText().toString()))
                            .build();
                }
                shareDialog.show(shareLinkContent);
            }
        });

        imageTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, CHOOSE_IMAGE);
            }
        });

        btnShareImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharePhoto photo = new SharePhoto.Builder()
                        .setBitmap(bitmap)
                        .build();
                SharePhotoContent content = new SharePhotoContent.Builder()
                        .addPhoto(photo)
                        .build();
                shareDialog.show(content);
            }
        });

        btnChooseVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("video/*");
                startActivityForResult(intent, CHOOSE_VIDEO);
            }
        });

        btnShareVid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShareVideo shareVideo = null;
                shareVideo = new ShareVideo.Builder()
                        .setLocalUrl(selectVideo)
                        .build();
                ShareVideoContent content = new ShareVideoContent.Builder()
                        .setVideo(shareVideo)
                        .build();
                shareDialog.show(content);
                videoView.stopPlayback();
            }
        });
    }

    public void createParameters() {
        edtTitle = (EditText) findViewById(R.id.editTxtTitle);
        edtDescription = (EditText) findViewById(R.id.editTxtDecription);
        edtUrl = (EditText) findViewById(R.id.editTxtUrl);
        imageTest = (ImageView) findViewById(R.id.imgSharePic);
        videoView = (VideoView) findViewById(R.id.vidVideoTest);
        btnShareImg = (Button) findViewById(R.id.btn_ShareImageFb);
        btnShareLink = (Button) findViewById(R.id.btn_ShareLinkFb);
        btnShareVid = (Button) findViewById(R.id.btn_ShareVideoFb);
        btnChooseVideo = (Button) findViewById(R.id.btn_ChooseVideo);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK) {
            try {
                InputStream inputStream = getContentResolver().openInputStream(data.getData());
                bitmap = BitmapFactory.decodeStream(inputStream);
                imageTest.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        if (requestCode == CHOOSE_VIDEO && resultCode == RESULT_OK) {
            selectVideo = data.getData();
            videoView.setVideoURI(selectVideo);
            videoView.start();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
