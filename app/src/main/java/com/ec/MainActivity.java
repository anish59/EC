package com.ec;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ec.apis.Services;
import com.ec.fragments.HomeFragment;
import com.ec.fragments.NotificationFragment;
import com.ec.fragments.ProfileFragment;
import com.ec.helper.FunctionHelper;
import com.ec.helper.PrefUtils;
import com.ec.helper.UiHelper;
import com.ec.model.GetPostRes;
import com.ec.model.NotificationResponse;
import com.ec.widgets.CustomBottomNavigationView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private int tempBagdeCount = 2;
    private android.widget.FrameLayout fragmentHolder;
    private CustomBottomNavigationView navigation;
    private android.widget.RelativeLayout container;
    private Context context;
    private Toolbar incToolBar;
    private int pageCount = 0;
    private List<BackStackFragments> backStackFragments;
    private TextView textCartItemCount;
    private android.support.design.widget.FloatingActionButton fabAddComplain;
    private double mCartItemCount = 3;
    private View prevBadgeView;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    changeFragment(0, "navigation_home");
                    fabAddComplain.setVisibility(View.VISIBLE);
                    return true;
                case R.id.navigation_profile:
                    changeFragment(1, "navigation_profile");
                    fabAddComplain.setVisibility(View.GONE);
                    return true;
                case R.id.navigation_notifications:
                    changeFragment(2, "navigation_notifications");
                    fabAddComplain.setVisibility(View.GONE);
                    return true;
            }
            return false;
        }
    };


    @SuppressLint("ObsoleteSdkInt")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        initViews();
        init();

    }


    private void init() {

        initListener();


        backStackFragments = new ArrayList<>();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS); // for showing same    bg in notification bar
        }

        changeFragment(0, ""); // for setting home fragment at the very starting
        int navHeight = getNavHeight();
        if (navHeight > 0) {
            (findViewById(R.id.container)).setPadding(0, 0, 0, navHeight);
        }
        UiHelper.initToolbar(MainActivity.this, incToolBar, "E Smart Complain");

        AppApplication.getRetrofit().create(Services.class)
                .getNotificationData(PrefUtils.getUser(context).getUserId(), "0").enqueue(new Callback<NotificationResponse>() {
            @Override
            public void onResponse(Call<NotificationResponse> call, Response<NotificationResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getStatus() == 1) {
                    if (response.body().getData() != null && response.body().getData().size() > 0) {
                        if (!response.body().getData().get(0).getTotal().isEmpty()) {
                            setupBadge(Integer.parseInt(response.body().getData().get(0).getTotal()), 2);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<NotificationResponse> call, Throwable t) {

            }
        });

//        setupBadge(3, 2);
    }

    private void initListener() {
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        fabAddComplain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AddComplainActivity.class));
            }
        });

    }

    @SuppressLint("RestrictedApi")
    private void initViews() {
        setContentView(R.layout.activity_main);
        this.fabAddComplain = (FloatingActionButton) findViewById(R.id.fabAddComplain);
        this.incToolBar = (Toolbar) findViewById(R.id.toolbar);
        this.container = (RelativeLayout) findViewById(R.id.container);
        this.navigation = (CustomBottomNavigationView) findViewById(R.id.navigation);
        this.fragmentHolder = (FrameLayout) findViewById(R.id.fragmentHolder);
        this.mTextMessage = (TextView) findViewById(R.id.message);

        BottomNavigationViewHelper.removeShiftMode(navigation, context);
    }


    private void changeFragment(int position, String name) {

        Fragment newFragment;

        if (position == 0) {

            Bundle args = new Bundle();
            args.putString("keyName", "Home");
            HomeFragment homeFragment = new HomeFragment();
            homeFragment.setArguments(args);

            newFragment = homeFragment;

        } else if (position == 1) {

            Bundle args = new Bundle();
            args.putString("keyName", "Profile");
            ProfileFragment pf = new ProfileFragment();
            pf.setArguments(args);

            newFragment = pf;

        } else {
            Bundle args = new Bundle();
            args.putString("keyName", "Notification");
            NotificationFragment pf = new NotificationFragment();
            pf.setArguments(args);
            newFragment = pf;
        }


        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentHolder, newFragment)
//                .addToBackStack("navigation_home")
                .commit();
//        manageBackstack(position, name);
    }

    private void manageBackstack(int position, String name) {
        if (backStackFragments != null && backStackFragments.size() >= 3) {
            backStackFragments.remove(0);
        }
        backStackFragments.add(new BackStackFragments(position, name));

        getSupportFragmentManager().addOnBackStackChangedListener(
                new FragmentManager.OnBackStackChangedListener() {
                    public void onBackStackChanged() {
                       /* if (backStackFragments.size() > 1) {
                            changeFragment(backStackFragments.get(backStackFragments.size() - 2).pos, backStackFragments.get(backStackFragments.size() - 2).name);
                            backStackFragments.remove(backStackFragments.size() - 1);
                        }*/

                        changeFragment(0, "navigation_home");
                    }
                });
    }

    static class BottomNavigationViewHelper { // actually this is not required for item less than 4

        @SuppressLint("RestrictedApi")
        static void removeShiftMode(BottomNavigationView view, Context context) {
            BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
            try {
                Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
                shiftingMode.setAccessible(true);
                shiftingMode.setBoolean(menuView, false);
                shiftingMode.setAccessible(false);
                for (int i = 0; i < menuView.getChildCount(); i++) {
                    BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                    item.setShiftingMode(false);
                    item.getChildAt(1).setVisibility(View.GONE);
                    item.getChildAt(0).setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    int inDp = (int) FunctionHelper.convertDpToPixel(8, context);
                    item.getChildAt(0).setPadding(inDp, inDp, inDp, inDp);
                    // set once again checked value, so view will be updated
                    item.setChecked(item.getItemData().isChecked());
                }
            } catch (NoSuchFieldException e) {
                Log.e("ERROR NO SUCH FIELD", "Unable to get shift mode field");
            } catch (IllegalAccessException e) {
                Log.e("ERROR ILLEGAL ALG", "Unable to change value of shift mode");
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_activity, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_exit:
                finish();
                return true;
            case R.id.menuFeedBack:
                startActivity(new Intent(context, FeedBackActivity.class));
                return true;
            case R.id.menuPayTax:
                startActivity(new Intent(context, PaymentActivity.class));
                return true;
            case R.id.menuLogOut:
                PrefUtils.setLoggedIn(context, false);
                PrefUtils.setUser(context, null);
                PrefUtils.setUserLatLong(context, null);
                Intent intent = new Intent(context, SplashScreenActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @SuppressLint("ObsoleteSdkInt")
    private int getNavHeight() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT)
            return 0;
        try {

            Resources resources = getResources();
            int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
            if (resourceId > 0) {
                boolean hasMenuKey = ViewConfiguration.get(getApplicationContext()).hasPermanentMenuKey();
                boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);

                if (!hasMenuKey && !hasBackKey) {
                    return resources.getDimensionPixelSize(resourceId);
                }
            }
        } catch (Exception ex) {
            return 0;
        }
        return 0;
    }

    public class BackStackFragments {
        public int pos = 0;
        public String name;

        public BackStackFragments(int pos, String name) {
            this.pos = pos;
            this.name = name;
        }
    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    public void setupBadge(int badgeCount, int menuIndex) {
        TextView txtCount;
        BottomNavigationMenuView bottomNavigationMenuView =
                (BottomNavigationMenuView) navigation.getChildAt(0);
        View v = bottomNavigationMenuView.getChildAt(menuIndex);
        BottomNavigationItemView itemView = (BottomNavigationItemView) v;

        View badge = LayoutInflater.from(this)
                .inflate(R.layout.badge_notification, bottomNavigationMenuView, false);
        txtCount = (TextView) badge.findViewById(R.id.txtCount);

        if (badgeCount <= 99) {
            txtCount.setVisibility(View.VISIBLE);
            txtCount.setText(String.format("%d", badgeCount));
        } else {
            txtCount.setVisibility(View.VISIBLE);
            txtCount.setText("99+");
        }


        ViewTreeObserver vto = txtCount.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                txtCount.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                Log.e("ht", txtCount.getHeight() + "");
                int h = txtCount.getHeight();
                int w = txtCount.getWidth();
                int size = h > w ? h : w;
                txtCount.setWidth(size);
                txtCount.setHeight(size);
            }
        });
        if (badgeCount != 0) {
            if (prevBadgeView != null) {
                itemView.removeView(prevBadgeView);
            }
            itemView.addView(badge);
            prevBadgeView = badge;
        } else {
            if (prevBadgeView != null) {
                itemView.removeView(prevBadgeView);
            } else {
                itemView.removeView(badge);
            }
        }

    }


}
