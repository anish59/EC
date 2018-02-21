package com.ec;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ec.fragments.HomeFragment;
import com.ec.fragments.ProfileFragment;
import com.ec.helper.FunctionHelper;
import com.ec.helper.UiHelper;
import com.ec.widgets.CustomBottomNavigationView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private android.widget.FrameLayout fragmentHolder;
    private CustomBottomNavigationView navigation;
    private android.widget.RelativeLayout container;
    private Context context;
    private Toolbar incToolBar;
    private int pageCount = 0;
    private List<BackStackFragments> backStackFragments;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    changeFragment(0, "navigation_home");
                    return true;
                case R.id.navigation_notifications:
                    changeFragment(1, "navigation_notifications");
                    Log.e("pageCount", pageCount + "");
                    return true;
                case R.id.navigation_setting:
                    changeFragment(2, "navigation_setting");
                    Log.e("pageCount", pageCount + "");
                    return true;
            }
            return false;
        }
    };
    private android.support.design.widget.FloatingActionButton fabAddComplain;

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
            ProfileFragment pf = new ProfileFragment();
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
}
