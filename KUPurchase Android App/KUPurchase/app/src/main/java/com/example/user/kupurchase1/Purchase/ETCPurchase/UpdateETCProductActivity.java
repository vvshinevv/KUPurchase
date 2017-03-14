package com.example.user.kupurchase1.Purchase.ETCPurchase;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.kupurchase1.Purchase.MainPurchaseActivity;
import com.example.user.kupurchase1.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class UpdateETCProductActivity extends AppCompatActivity implements View.OnClickListener{

    private String TAGLOG = "로그: UpdateETCProductActivity: ";

    private  static final int RESULT_LOAD_IMAGE = 1;

    public String userID, imagePath, titleOfETCProduct, productExpireDateOfPurchase, productDetailInform, uploadProductsDate;
    public int productManagementCode, userManagementCode;

    public EditText etETCProductTitle, etETCProductDate, etETCProductContents;
    public ImageView ivETCImageProductPicture, ivETCProductPicture, ivETCProductNewPicture;
    public TextView tvBackETCProductContentActivity, tvUpdateETCProduct, tvTextSearchDate;

    private DatePickerDialog datePickerDialog;
    public SimpleDateFormat simpleDateFormat;

    public String readyETCProductTitle, readyETCProductContents, readyETCProductDate;

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");
        imagePath = intent.getStringExtra("imagePath");
        titleOfETCProduct = intent.getStringExtra("titleOfETCProduct");
        productExpireDateOfPurchase = intent.getStringExtra("productExpireDateOfPurchase");
        productDetailInform = intent.getStringExtra("productDetailInform");
        uploadProductsDate = intent.getStringExtra("uploadProductsDate");
        productManagementCode = intent.getIntExtra("productManagementCode", -1);
        userManagementCode = intent.getIntExtra("userManagementCode", -1);

        etETCProductTitle.setText(titleOfETCProduct);
        etETCProductDate.setText(productExpireDateOfPurchase);
        etETCProductContents.setText(productDetailInform);

        Picasso.with(this).load(imagePath).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                ivETCProductPicture.setImageBitmap(bitmap);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_etcproduct);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
        SpannableString content = new SpannableString("날짜 검색");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);

        etETCProductTitle = (EditText) findViewById(R.id.etETCProductTitle);
        etETCProductDate = (EditText) findViewById(R.id.etETCProductDate);
        etETCProductContents = (EditText) findViewById(R.id.etETCProductContents);

        ivETCImageProductPicture = (ImageView) findViewById(R.id.ivETCImageProductPicture);
        ivETCProductPicture = (ImageView) findViewById(R.id.ivETCProductPicture);
        ivETCProductNewPicture = (ImageView) findViewById(R.id.ivETCProductNewPicture);

        tvBackETCProductContentActivity = (TextView) findViewById(R.id.tvBackETCProductContentActivity);
        tvUpdateETCProduct = (TextView) findViewById(R.id.tvUpdateETCProduct);
        tvTextSearchDate = (TextView) findViewById(R.id.tvTextSearchDate);

        ivETCImageProductPicture.setOnClickListener(this);
        tvBackETCProductContentActivity.setOnClickListener(this);
        tvUpdateETCProduct.setOnClickListener(this);
        tvTextSearchDate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivETCImageProductPicture:
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
                break;

            case R.id.tvBackETCProductContentActivity:
                finish();
                break;

            case R.id.tvUpdateETCProduct: {
                Bitmap image;
                if ((ivETCProductNewPicture.getDrawable()) == null) {

                    image = ((BitmapDrawable) ivETCProductPicture.getDrawable()).getBitmap();
                } else {
                    image = ((BitmapDrawable) ivETCProductNewPicture.getDrawable()).getBitmap();
                }
                submit(image);
                break;
            }

            case R.id.tvTextSearchDate:
                etETCProductDate.setText("");
                dialogDatePicker();
                datePickerDialog.show();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            ivETCProductNewPicture.setImageDrawable(null);
            ivETCProductNewPicture.setImageBitmap(null);
            ivETCProductNewPicture.setImageURI(selectedImage);
        }
    }

    private void submit(Bitmap image) {
        if (!validateETCProductTitle()) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(UpdateETCProductActivity.this);
            dialogBuilder.setMessage("제목을 입력하세요");
            dialogBuilder.setPositiveButton("확인", null);
            dialogBuilder.show();
        } else if (!validateETCProductContents()) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(UpdateETCProductActivity.this);
            dialogBuilder.setMessage("내용을 입력하세요");
            dialogBuilder.setPositiveButton("확인", null);
            dialogBuilder.show();
        } else if (!validateETCProductDate()) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(UpdateETCProductActivity.this);
            dialogBuilder.setMessage("날짜를 입력하세요");
            dialogBuilder.setPositiveButton("확인", null);
            dialogBuilder.show();
        } else {
            ETCProductConstructor etcProductConstructor = new ETCProductConstructor(uploadProductsDate, userID, readyETCProductTitle, readyETCProductContents,
                    readyETCProductDate, imagePath, userManagementCode, productManagementCode);

            ETCProductServerRequests etcProductServerRequests = new ETCProductServerRequests(this);
            etcProductServerRequests.updateETCProductsListInBackground(etcProductConstructor, image, new GetETCProductCallback() {
                @Override
                public void done(ArrayList<ETCProductConstructor> returnedETCProduct) {
                    ETCProductContentsActivity etcProductContentsActivity = (ETCProductContentsActivity)ETCProductContentsActivity.etcProductContentsActivity;
                    etcProductContentsActivity.finish();

                    Intent intent = new Intent(getApplicationContext(), MainPurchaseActivity.class);
                    startActivity(intent);
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
}
