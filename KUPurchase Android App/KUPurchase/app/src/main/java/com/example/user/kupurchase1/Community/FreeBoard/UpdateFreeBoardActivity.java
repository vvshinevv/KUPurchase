package com.example.user.kupurchase1.Community.FreeBoard;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.kupurchase1.Community.MainCommunityActivity;
import com.example.user.kupurchase1.R;
import com.example.user.kupurchase1.User;
import com.example.user.kupurchase1.UserLocalStore;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

public class UpdateFreeBoardActivity extends AppCompatActivity implements View.OnClickListener{

    private String TAGLOG = "로그: UpdateFreeBoardActivity: ";

    private  static final int RESULT_LOAD_IMAGE = 1;

    private EditText etFreeBoardTitle, etFreeBoardContents;
    public TextView tvBackMainCommunityActivity, tvUpdateFreeBoard;
    public ImageView ivFreeBoardSelectionImage, ivFreeBoardNewImage, ivFreeBoardImage;

    public String freeBoardTitle, freeBoardContents, freeBoardImageURL, isImageEmpty;
    public int freeBoardManagementCode;

    public String readyFreeBoardTitle, readyFreeBoardContents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_freeboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        etFreeBoardTitle = (EditText) findViewById(R.id.etFreeBoardTitle);
        etFreeBoardContents = (EditText) findViewById(R.id.etFreeBoardContents);
        tvBackMainCommunityActivity = (TextView) findViewById(R.id.tvBackMainCommunityActivity);
        tvUpdateFreeBoard = (TextView) findViewById(R.id.tvUpdateFreeBoard);
        ivFreeBoardSelectionImage = (ImageView) findViewById(R.id.ivFreeBoardSelectionImage);
        ivFreeBoardNewImage = (ImageView) findViewById(R.id.ivFreeBoardNewImage);
        ivFreeBoardImage = (ImageView) findViewById(R.id.ivFreeBoardImage);

        ivFreeBoardSelectionImage.setOnClickListener(this);
        tvBackMainCommunityActivity.setOnClickListener(this);
        tvUpdateFreeBoard.setOnClickListener(this);

        Intent intent = getIntent();
        freeBoardTitle = intent.getStringExtra("freeBoardTitle");
        freeBoardContents = intent.getStringExtra("freeBoardContents");
        freeBoardManagementCode = intent.getIntExtra("freeBoardManagementCode", -1);
        freeBoardImageURL = intent.getStringExtra("freeBoardImageURL");
        Log.e(TAGLOG, "freeBoardImageURL : " + freeBoardImageURL);

        initView();
    }

    private void initView() {
        etFreeBoardTitle.setText(freeBoardTitle);
        etFreeBoardContents.setText(freeBoardContents);

        if(freeBoardImageURL.equals("ImageEmpty")) {
            ivFreeBoardImage.setVisibility(View.GONE);
        } else {
            Picasso.with(this).load(freeBoardImageURL).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    ivFreeBoardImage.setVisibility(View.VISIBLE);
                    ivFreeBoardImage.setImageBitmap(bitmap);
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            });
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvUpdateFreeBoard:
                Bitmap image;
                if ((ivFreeBoardNewImage.getDrawable()) == null && (ivFreeBoardImage.getDrawable()) == null) { // 사진이 없으면
                    image = BitmapFactory.decodeResource(getResources(), R.drawable.no_detail_img);
                    isImageEmpty = "ImageEmpty";
                    Log.e(TAGLOG, "1");
                } else if((ivFreeBoardNewImage.getDrawable()) != null) {
                    image = ((BitmapDrawable) ivFreeBoardNewImage.getDrawable()).getBitmap();
                    isImageEmpty = "ImageNotEmpty";
                    Log.e(TAGLOG, "2");

                } else {
                    image = ((BitmapDrawable) ivFreeBoardImage.getDrawable()).getBitmap();
                    isImageEmpty = "ImageNotEmpty";
                    Log.e(TAGLOG, "3");

                }

                submit(image);
                break;

            case R.id.tvBackMainCommunityActivity:
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
            ivFreeBoardNewImage.setImageDrawable(null);
            ivFreeBoardNewImage.setImageBitmap(null);
            ivFreeBoardNewImage.setImageURI(selectedImage);
        }
    }


    private void submit(Bitmap bitmap) {
        if(!validateFreeBoardTitle()) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(UpdateFreeBoardActivity.this);
            dialogBuilder.setMessage("제목을 입력하세요");
            dialogBuilder.setPositiveButton("확인", null);
            dialogBuilder.show();
        } else if(!validateFreeBoardContents()) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(UpdateFreeBoardActivity.this);
            dialogBuilder.setMessage("내용을 입력하세요");
            dialogBuilder.setPositiveButton("확인", null);
            dialogBuilder.show();
        } else {
            UserLocalStore userLocalStore = new UserLocalStore(this);
            User user = userLocalStore.getLoggedInUser();
            FreeBoardConstructor freeBoard = new FreeBoardConstructor(readyFreeBoardTitle, readyFreeBoardContents,
                    freeBoardManagementCode, isImageEmpty, freeBoardImageURL, user.userID);

            FreeBoardServerRequests freeBoardServerRequests = new FreeBoardServerRequests(this);
            freeBoardServerRequests.updateFreeBoardListInBackground(bitmap, freeBoard, new GetFreeBoardCallback() {
                @Override
                public void done(ArrayList<FreeBoardConstructor> returnedFreeBoard) {
                    FreeBoardContentsActivity freeBoardContentsActivity = (FreeBoardContentsActivity) FreeBoardContentsActivity.freeBoardContentsActivity;
                    freeBoardContentsActivity.finish();

                    MainCommunityActivity mainCommunityActivity = (MainCommunityActivity) MainCommunityActivity.mainCommunityActivity;
                    mainCommunityActivity.finish();

                    Intent intent = new Intent(getApplicationContext(), MainCommunityActivity.class);
                    startActivity(intent);
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
        finish();
    }
}
