package com.example.user.kupurchase1.Purchase;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.user.kupurchase1.GCMUtils.QuickstartPreferences;
import com.example.user.kupurchase1.MainActivity;
import com.example.user.kupurchase1.Purchase.Shopping.GetShoppingCallback;
import com.example.user.kupurchase1.Purchase.Shopping.MakeJsonShoppingData;
import com.example.user.kupurchase1.Purchase.Shopping.ShoppingAdapter;
import com.example.user.kupurchase1.Purchase.Shopping.ShoppingCarts;
import com.example.user.kupurchase1.Purchase.Shopping.ShoppingServerRequests;
import com.example.user.kupurchase1.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainPurchaseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private String TAGLOG = "로그: MainPurchaseActivity: ";

    private ActionBarDrawerToggle toggle;
    private DrawerLayout drawer;
    private Toolbar toolbar;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    public ArrayList<ShoppingCarts> shoppingCartsArrayList;
    public ShoppingAdapter shoppingAdapter;
    public String shoppingJsonString = null;
    public RecyclerView recyclerView;
    public Context mContext;
    public static Activity mainPurchaseActivity;

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void onResume() {
        super.onResume();

        mainPurchaseActivity = MainPurchaseActivity.this;

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawer.isDrawerOpen(GravityCompat.END)) {
                    drawer.closeDrawer(GravityCompat.END);
                } else {
                    drawer.openDrawer(GravityCompat.END);
                }
            }
        });
        toggle.syncState();

        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(QuickstartPreferences.SHOPPING_COMPLETE));
    }

    public void registerBroadcastReceiver() {
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();

                if(action.equals(QuickstartPreferences.SHOPPING_COMPLETE)) {
                    String productName = intent.getStringExtra("productName");
                    int productCount = intent.getIntExtra("productCount", -1);
                    int productPrice = intent.getIntExtra("productPrice", -1);
                    int productManagementCode = intent.getIntExtra("productManagementCode", -1);
                    shoppingAdapter.addItem(productName, productCount, productPrice, productManagementCode);
                    LocalBroadcastManager.getInstance(context).unregisterReceiver(mRegistrationBroadcastReceiver);
                }
            }
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_purchase);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mContext = this;
        registerBroadcastReceiver();

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tlTab_Layout);
        tabLayout.addTab(tabLayout.newTab().setText("KU 공동구매"));
        tabLayout.addTab(tabLayout.newTab().setText("기타 공동구매"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        shoppingCartsArrayList = new ArrayList<>();

        final ViewPager viewPager = (ViewPager) findViewById(R.id.vpView_Pager);
        final MainPurchaseAdapter mainPurchaseAdapter = new MainPurchaseAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(mainPurchaseAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        initView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        int menuToUse = R.menu.menu_nav_drawer;

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(menuToUse, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        toggle.onOptionsItemSelected(item);

        if (item != null && item.getItemId() == R.id.btnMyMenu) {
            if (drawer.isDrawerOpen(GravityCompat.END)) {
                drawer.closeDrawer(GravityCompat.END);
            } else {
                drawer.openDrawer(GravityCompat.END);
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return false;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        } else {
            alert();
        }
    }

    private void initView() {
        ShoppingCarts shoppingCarts = new ShoppingCarts();
        shoppingCarts.setShoppingCartsArrayList(shoppingCartsArrayList);

        recyclerView = (RecyclerView) findViewById(R.id.card_recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        shoppingAdapter = new ShoppingAdapter(shoppingCartsArrayList, getApplicationContext());
        recyclerView.setAdapter(shoppingAdapter);
        if(shoppingJsonString != null) {
            try {
                JSONObject jsonObject = new JSONObject(shoppingJsonString);
                JSONArray jsonArray = jsonObject.getJSONArray("products");
                for(int i = 0 ; i < jsonArray.length() ; i++) {
                    JSONObject productObject = jsonArray.getJSONObject(i);
                    shoppingCarts.setKuProductName(productObject.getString("productName"));
                    shoppingCarts.setKuProductPrice(productObject.getInt("productPrice"));
                    shoppingCarts.setKuProductCount(productObject.getInt("productCount"));
                    shoppingCartsArrayList.add(shoppingCarts);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        shoppingAdapter.notifyDataSetChanged();
        initSwipe();
    }

    private void initSwipe() {
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();

                if(direction == ItemTouchHelper.LEFT) {
                    shoppingAdapter.removeItem(position);
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                Bitmap icon;
                Paint p = new Paint();
                if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    View itemView = viewHolder.itemView;

                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    if(dX < 0) {
                        p.setColor(Color.parseColor("#D32F2F"));
                        RectF background = new RectF((float) itemView.getRight() + dX,
                                (float) itemView.getTop(),(float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background, p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.trash_icon);
                        RectF icon_dest = new RectF((float) itemView.getRight() - 2*width ,(float) itemView.getTop() + width,(float) itemView.getRight() - width,(float)itemView.getBottom() - width);
                        c.drawBitmap(icon, null,icon_dest,p);
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    public void BackMainActivity(View v) {
        alert();
    }

    public void alert() {

        if(!checkPurchase()) {
            Intent intent = new Intent(MainPurchaseActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            dialogBuilder.setMessage("장바구니 내역이 삭제됩니다. 종료하시겠습니까?").setCancelable(false).setPositiveButton("확인",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(MainPurchaseActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    return;
                }
            });
            AlertDialog alertDialog = dialogBuilder.create();
            alertDialog.show();
        }
    }

    private Boolean checkPurchase() {
        MakeJsonShoppingData makeJsonShoppingData = new MakeJsonShoppingData(shoppingCartsArrayList, getApplicationContext());
        shoppingJsonString = makeJsonShoppingData.makeJsonShoppingData();

        try {
            JSONObject jsonObject = new JSONObject(shoppingJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray("products");

            if(jsonArray.length() == 0) {
                return false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return true;
    }

    public void PurchaseKuProducts(View v) {


        if(!checkPurchase()) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainPurchaseActivity.this);
            dialogBuilder.setMessage("주문내역이 없습니다.");
            dialogBuilder.setPositiveButton("확인", null);
            dialogBuilder.show();
        } else {
            ShoppingServerRequests shoppingServerRequests = new ShoppingServerRequests(this);
            shoppingServerRequests.storeShoppingDataInBackground(shoppingJsonString, new GetShoppingCallback() {
                @Override
                public void done(String returnedResultData) {

                    if(returnedResultData != null) {
                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainPurchaseActivity.this);
                        dialogBuilder.setMessage("주문이 완료되었습니다. 주문조회를 확인하세요.");
                        dialogBuilder.setPositiveButton("확인", null);
                        dialogBuilder.show();
                        shoppingJsonString = "";
                    } else {
                        Toast.makeText(getApplication(), "서버 오류로 주문에 실패하였습니다.", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
}
