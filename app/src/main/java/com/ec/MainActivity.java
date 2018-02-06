package com.ec;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ec.fragments.ProfileFragment;
import com.ec.helper.FunctionHelper;
import com.ec.helper.UiHelper;

import java.lang.reflect.Field;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private android.widget.FrameLayout fragmentHolder;
    private BottomNavigationView navigation;
    private android.widget.RelativeLayout container;
    private Context context;
    private Toolbar incToolBar;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    changeFragment(0);
                    return true;
                case R.id.navigation_notifications:
                    changeFragment(1);
                    return true;
                case R.id.navigation_setting:
                    changeFragment(2);
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
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        changeFragment(0); // for setting home fragment at the very starting

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            w.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }

        UiHelper.initToolbar(MainActivity.this, incToolBar, "EC");
    }

    @SuppressLint("RestrictedApi")
    private void initViews() {
        setContentView(R.layout.activity_main);
        this.incToolBar = (Toolbar) findViewById(R.id.toolbar);
        this.container = (RelativeLayout) findViewById(R.id.container);
        this.navigation = (BottomNavigationView) findViewById(R.id.navigation);
        this.fragmentHolder = (FrameLayout) findViewById(R.id.fragmentHolder);
        this.mTextMessage = (TextView) findViewById(R.id.message);

        BottomNavigationViewHelper.removeShiftMode(navigation, context);
    }

    private void changeFragment(int position) {

        Fragment newFragment = null;

        if (position == 0) {

            Bundle args = new Bundle();
            args.putString("keyName", "Home");
            ProfileFragment pf = new ProfileFragment();
            pf.setArguments(args);

            newFragment = pf;

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

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentHolder, newFragment)
                .commit();

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


}
