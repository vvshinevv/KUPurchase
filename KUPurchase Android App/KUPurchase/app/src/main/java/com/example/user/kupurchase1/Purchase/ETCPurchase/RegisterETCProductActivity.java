package com.example.user.kupurchase1.Purchase.ETCPurchase;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.kupurchase1.Purchase.MainPurchaseActivity;
import com.example.user.kupurchase1.R;
import com.example.user.kupurchase1.User;
import com.example.user.kupurchase1.UserLocalStore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class RegisterETCProductActivity extends AppCompatActivity implements View.OnClickListener{

    private String TAGLOG = "로그: RegisterETCProductActivity: ";

    private  static final int RESULT_LOAD_IMAGE = 1;
    public UserLocalStore userLocalStore;


    private EditText etETCProductTitle, etETCProductContents, etETCProductDate;
    public ImageView ivETCProductPicture, ivETCImageProductPicture;
    public TextView tvBackMainCommunityActivity, tvRegisterETCProduct, tvTextSearchDate;

    private DatePickerDialog datePickerDialog;
    public SimpleDateFormat simpleDateFormat;

    public String readyETCProductTitle, readyETCProductContents, readyETCProductDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_etcproduct);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
        SpannableString content = new SpannableString("날짜 검색");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);

        etETCProductTitle = (EditText) findViewById(R.id.etETCProductTitle);
        etETCProductContents = (EditText) findViewById(R.id.etETCProductContents);
        etETCProductDate = (EditText) findViewById(R.id.etETCProductDate);
        ivETCProductPicture = (ImageView) findViewById(R.id.ivETCProductPicture);
        ivETCImageProductPicture = (ImageView) findViewById(R.id.ivETCImageProductPicture);
        tvBackMainCommunityActivity = (TextView) findViewById(R.id.tvBackMainCommunityActivity);
        tvRegisterETCProduct = (TextView) findViewById(R.id.tvRegisterETCProduct);
        tvTextSearchDate = (TextView) findViewById(R.id.tvTextSearchDate);

        tvBackMainCommunityActivity.setOnClickListener(this);
        tvTextSearchDate.setOnClickListener(this);
        tvRegisterETCProduct.setOnClickListener(this);
        ivETCImageProductPicture.setOnClickListener(this);
        etETCProductDate.setFocusableInTouchMode(false);

        userLocalStore = new UserLocalStore(this);
        tvTextSearchDate.setText(content);

        ivETCProductPicture.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvRegisterETCProduct:

                Bitmap image;
                if((ivETCProductPicture.getDrawable()) == null) {
                    image = BitmapFactory.decodeResource(getResources(), R.drawable.no_detail_img);
                } else {
                    image = ((BitmapDrawable) ivETCProductPicture.getDrawable()).getBitmap();
                }

                submit(image);
                break;

            case R.id.ivETCImageProductPicture:
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
                break;

            case R.id.tvTextSearchDate:
                etETCProductDate.setText("");
                dialogDatePicker();
                datePickerDialog.show();
                break;

            case R.id.tvBackMainCommunityActivity:
                Intent BackMainCommunityActivity = new Intent(this, MainPurchaseActivity.class);
                startActivity(BackMainCommunityActivity);
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            ivETCProductPicture.setVisibility(View.VISIBLE);
            ivETCProductPicture.setImageDrawable(null);
            ivETCProductPicture.setImageBitmap(null);
            ivETCProductPicture.setImageURI(selectedImage);
        }
    }

    private void submit(Bitmap image) {
        if(!validateETCProductTitle()) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(RegisterETCProductActivity.this);
            dialogBuilder.setMessage("제목을 입력하세요");
            dialogBuilder.setPositiveButton("확인", null);
            dialogBuilder.show();
        } else if(!validateETCProductContents()) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(RegisterETCProductActivity.this);
            dialogBuilder.setMessage("내용을 입력하세요");
            dialogBuilder.setPositiveButton("확인", null);
            dialogBuilder.show();
        } else if(!validateETCProductDate()) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(RegisterETCProductActivity.this);
            dialogBuilder.setMessage("날짜를 입력하세요");
            dialogBuilder.setPositiveButton("확인", null);
            dialogBuilder.show();
        } else {
            Log.e(TAGLOG, "readyETCProductTitle : " + readyETCProductTitle);
            User user = userLocalStore.getLoggedInUser();
            ETCProductConstructor etcProduct = new ETCProductConstructor(user.userID, readyETCProductTitle, readyETCProductContents, readyETCProductDate);

            ETCProductServerRequests etcProductServerRequests = new ETCProductServerRequests(this);
            etcProductServerRequests.storeETCProductsListInBackground(etcProduct, image, new GetETCProductCallback() {
                @Override
                public void done(ArrayList<ETCProductConstructor> returnedETCProduct) {
                    startActivity(new Intent(RegisterETCProductActivity.this, MainPurchaseActivity.class));
                    finish();
                }
            });
        }
    }

    private void dialogDatePicker() {
        Calendar calendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                etETCProductDate.setText(simpleDateFormat.format(newDate.getTime()));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    }

    private boolean validateETCProductTitle() {
        if(etETCProductTitle.getText().toString().trim().isEmpty()) {
            requestFocus(etETCProductTitle);
            return false;
        } else {
            readyETCProductTitle = etETCProductTitle.getText().toString().trim();
            return true;
        }
    }

    private boolean validateETCProductContents() {
        if(etETCProductContents.getText().toString().trim().isEmpty()) {
            requestFocus(etETCProductContents);
            return false;
        } else {
            readyETCProductContents = etETCProductContents.getText().toString().trim();
            return true;
        }
    }

    private boolean validateETCProductDate() {
        if(etETCProductDate.getText().toString().trim().isEmpty()) {
            requestFocus(etETCProductDate);
            return false;
        } else {
            readyETCProductDate = etETCProductDate.getText().toString().trim();
            return true;
        }
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainPurchaseActivity.class);
        startActivity(intent);
        finish();
    }
}
