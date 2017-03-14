package com.example.user.kupurchase1.Community.FreeBoard;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.kupurchase1.Community.MainCommunityActivity;
import com.example.user.kupurchase1.R;
import com.example.user.kupurchase1.User;
import com.example.user.kupurchase1.UserLocalStore;

import java.util.ArrayList;

public class RegisterFreeBoardActivity extends AppCompatActivity implements View.OnClickListener{

    private String TAGLOG = "로그: RegisterFreeBoardActivity: ";

    private  static final int RESULT_LOAD_IMAGE = 1;

    public UserLocalStore userLocalStore;

    private EditText etFreeBoardTitle, etFreeBoardContents;
    public TextView tvBackMainCommunityActivity, tvRegisterFreeBoard;
    public ImageView ivFreeBoardSelectionImage, ivFreeBoardImage;
    public String readyFreeBoardTitle, readyFreeBoardContents, isImageEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_freeboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        etFreeBoardTitle = (EditText) findViewById(R.id.etFreeBoardTitle);
        etFreeBoardContents = (EditText) findViewById(R.id.etFreeBoardContents);
        tvBackMainCommunityActivity = (TextView) findViewById(R.id.tvBackMainCommunityActivity);
        tvRegisterFreeBoard = (TextView) findViewById(R.id.tvRegisterFreeBoard);
        ivFreeBoardImage = (ImageView) findViewById(R.id.ivFreeBoardImage);
        ivFreeBoardSelectionImage = (ImageView) findViewById(R.id.ivFreeBoardSelectionImage);

        tvBackMainCommunityActivity.setOnClickListener(this);
        tvRegisterFreeBoard.setOnClickListener(this);
        ivFreeBoardSelectionImage.setOnClickListener(this);

        userLocalStore = new UserLocalStore(this);
        ivFreeBoardImage.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvRegisterFreeBoard:


                Bitmap image;
                if((ivFreeBoardImage.getDrawable()) == null) {
                    image = BitmapFactory.decodeResource(getResources(), R.drawable.no_detail_img);
                    isImageEmpty="ImageEmpty";
                } else {
                    image = ((BitmapDrawable) ivFreeBoardImage.getDrawable()).getBitmap();
                    isImageEmpty="ImageNotEmpty";
                }
                submit(image);
                break;

            case R.id.tvBackMainCommunityActivity:
                Intent BackMainCommunityActivity = new Intent(this, MainCommunityActivity.class);
                startActivity(BackMainCommunityActivity);
                finish();
                break;

            case R.id.ivFreeBoardSelectionImage:
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            ivFreeBoardImage.setVisibility(View.VISIBLE);
            ivFreeBoardImage.setImageDrawable(null);
            ivFreeBoardImage.setImageBitmap(null);
            ivFreeBoardImage.setImageURI(selectedImage);
        }
    }

    private void submit(Bitmap bitmap) {
        if(!validateFreeBoardTitle()) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(RegisterFreeBoardActivity.this);
            dialogBuilder.setMessage("제목을 입력하세요");
            dialogBuilder.setPositiveButton("확인", null);
            dialogBuilder.show();
        } else if(!validateFreeBoardContents()) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(RegisterFreeBoardActivity.this);
            dialogBuilder.setMessage("내용을 입력하세요");
            dialogBuilder.setPositiveButton("확인", null);
            dialogBuilder.show();
        } else {
            User user = userLocalStore.getLoggedInUser();
            FreeBoardConstructor freeBoard = new FreeBoardConstructor(user.userID, readyFreeBoardTitle, readyFreeBoardContents, isImageEmpty);

            FreeBoardServerRequests freeBoardServerRequests = new FreeBoardServerRequests(this);
            freeBoardServerRequests.storeFreeBoardListInBackground(bitmap, freeBoard, new GetFreeBoardCallback() {
                @Override
                public void done(ArrayList<FreeBoardConstructor> returnedFreeBoard) {
                    startActivity(new Intent(RegisterFreeBoardActivity.this, MainCommunityActivity.class));
                    finish();
                }
            });
        }
    }

    private boolean validateFreeBoardTitle() {
        if(etFreeBoardTitle.getText().toString().trim().isEmpty()) {
            requestFocus(etFreeBoardTitle);
            return false;
        } else {
            readyFreeBoardTitle = etFreeBoardTitle.getText().toString().trim();
            return true;
        }
    }

    private boolean validateFreeBoardContents() {
        if(etFreeBoardContents.getText().toString().trim().isEmpty()) {
            requestFocus(etFreeBoardContents);
            return false;
        } else {
            readyFreeBoardContents = etFreeBoardContents.getText().toString().trim();
            return true;
        }
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    public void onBackPressed() {
        Intent intent = new Intent(this, MainCommunityActivity.class);
        startActivity(intent);
        finish();
    }
}
