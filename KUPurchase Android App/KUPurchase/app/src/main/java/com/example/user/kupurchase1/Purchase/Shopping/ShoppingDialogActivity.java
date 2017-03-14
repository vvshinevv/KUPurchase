package com.example.user.kupurchase1.Purchase.Shopping;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.kupurchase1.FullScreenActivity;
import com.example.user.kupurchase1.GCMUtils.QuickstartPreferences;
import com.example.user.kupurchase1.R;
import com.squareup.picasso.Picasso;

public class ShoppingDialogActivity extends Activity implements View.OnClickListener {

    private String TAGLOG = "로그: ShoppingDialogActivity: ";

    public Button bPurchaseKuProduct, bPutToCart;
    public TextView tvKuProductName, tvKuProductPrice, tvKuProductExpireDate, tvKuProductDepositStartDate, tvKuProductDepositDueDate;
    public ImageView ivKuProductImage;
    public int productManagementCode, productCount, productPrice;
    public String productName, productImageURL;

    private AlertDialog.Builder alertDialog;
    public View view;
    public Context context;

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();

        productManagementCode = intent.getIntExtra("kuProductManagementCode", -1);
        productName = intent.getStringExtra("kuProductName");
        productPrice = intent.getIntExtra("kuProductPrice", -1);
        productImageURL = intent.getStringExtra("kuProductImageURL");

        tvKuProductName.setText(intent.getStringExtra("kuProductName"));
        tvKuProductPrice.setText(String.valueOf(intent.getIntExtra("kuProductPrice", -1)));
        tvKuProductExpireDate.setText(intent.getStringExtra("kuProductExpireDate"));
        tvKuProductDepositStartDate.setText(intent.getStringExtra("kuProductDepositStartDate"));
        tvKuProductDepositDueDate.setText(intent.getStringExtra("kuProductDepositDueDate"));
        Picasso.with(this).load(intent.getStringExtra("kuProductImageURL")).into(ivKuProductImage);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_shopping_dialog);
        context = getApplicationContext();

        ivKuProductImage = (ImageView) findViewById(R.id.ivKuProductImage);
        tvKuProductName = (TextView) findViewById(R.id.tvKuProductName);
        tvKuProductPrice = (TextView) findViewById(R.id.tvKuProductPrice);
        tvKuProductExpireDate = (TextView) findViewById(R.id.tvKuProductExpireDate);
        tvKuProductDepositStartDate = (TextView) findViewById(R.id.tvKuProductDepositStartDate);
        tvKuProductDepositDueDate = (TextView) findViewById(R.id.tvKuProductDepositDueDate);


        bPurchaseKuProduct = (Button) findViewById(R.id.bPurchaseKuProduct);
        bPutToCart = (Button) findViewById(R.id.bPutToCart);

        bPutToCart.setOnClickListener(this);
        bPurchaseKuProduct.setOnClickListener(this);
        ivKuProductImage.setOnClickListener(this);

        initDialog();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bPurchaseKuProduct:
                break;

            case R.id.bPutToCart:
                removeView();
                alertDialog.show();
                break;

            case R.id.ivKuProductImage: {
                Intent intent = new Intent(this, FullScreenActivity.class);
                intent.putExtra("productImageURL", productImageURL);
                startActivity(intent);
                break;
            }
        }
    }

    private void removeView(){
        if(view.getParent()!=null) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
    }

    private void initDialog() {
        alertDialog = new AlertDialog.Builder(this);
        view = getLayoutInflater().inflate(R.layout.activity_shopping_count_dialog, null);
        final EditText etKuProductCount = (EditText) view.findViewById(R.id.etKuProductCount);
        alertDialog.setView(view);
        alertDialog.setPositiveButton("확 인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if((etKuProductCount.getText().toString()).equals("0") || (etKuProductCount.getText().toString()).equals("") ) {
                    Toast.makeText(getApplicationContext(), "장바구니에 등록되지 않았습니다.", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    productCount = Integer.parseInt(etKuProductCount.getText().toString());
                    Toast.makeText(getApplicationContext(), "장바구니를 확인해주세요.", Toast.LENGTH_LONG).show();
                    Intent shoppingComplete = new Intent(QuickstartPreferences.SHOPPING_COMPLETE);
                    shoppingComplete.putExtra("productName", productName);
                    shoppingComplete.putExtra("productCount", productCount);
                    shoppingComplete.putExtra("productPrice", productPrice);
                    shoppingComplete.putExtra("productManagementCode", productManagementCode);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(shoppingComplete);
                    finish();
                }
            }
        });
    }
}
