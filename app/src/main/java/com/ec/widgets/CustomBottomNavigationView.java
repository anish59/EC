package com.ec.widgets;// include package path here

import android.content.Context;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.util.AttributeSet;

import java.util.HashMap;
import java.util.Map;

public class CustomBottomNavigationView extends BottomNavigationView {

    private HashMap<Integer, CustomBottomBarItem> barItemMap;

    public CustomBottomNavigationView(Context context) {
        super(context);
        init();
    }

    public CustomBottomNavigationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomBottomNavigationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        barItemMap = new HashMap<>();

        this.setOnNavigationItemSelectedListener(item -> {
            CustomBottomBarItem customBottomBarItem = barItemMap.get(item.getItemId());
            if (customBottomBarItem != null) {
                customBottomBarItem.onNavigationItemSelectedAction.onItemSelectedDo();
                return true;
            }

            return false;
        });
    }

    // Instead of registering menu items using default way, use this method to achieve it
    public void registerFragmentToBarItem(int menuItemId, Class fragmentClass, OnNavigationItemSelectedAction onNavigationItemSelectedAction) {
        barItemMap.put(menuItemId, new CustomBottomBarItem(menuItemId, fragmentClass.getName(), onNavigationItemSelectedAction));
    }

    // This method is used to set menu item of the bottom bar as ACTIVE using
    // data from BackStack passed as argument
    public void setSelectedByFragmentManager(FragmentManager fragmentManager) {
        // Getting the backStack entries count
        int backStackCount = fragmentManager.getBackStackEntryCount();
        // Getting the actual backStack entry
        FragmentManager.BackStackEntry backStackEntry = fragmentManager.getBackStackEntryAt(backStackCount - 1);
        // Select bottom bar item by fragment name retrieved from backStack
        String fragmentName = backStackEntry.getName();
        for (Object o : barItemMap.entrySet()) {
            Map.Entry barItemEntry = (Map.Entry) o;
            CustomBottomBarItem barItem = (CustomBottomBarItem) barItemEntry.getValue();
            if (barItem.fragmentClassName.equals(fragmentName)) {
                // Setting bottom bar icon as active
                this.getMenu().findItem(barItem.menuItemId).setChecked(true);
                break;
            }
        }
    }

    // Model of the menu item for this CustomBottomNavigationBar to allow the control
    // of menu items for dynamic register, interaction and control of back stack
    private class CustomBottomBarItem {
        int menuItemId;
        String fragmentClassName;
        OnNavigationItemSelectedAction onNavigationItemSelectedAction;

        CustomBottomBarItem(int menuItemId, String fragmentClassName, OnNavigationItemSelectedAction onNavigationItemSelectedAction) {
            this.menuItemId = menuItemId;
            this.fragmentClassName = fragmentClassName;
            this.onNavigationItemSelectedAction = onNavigationItemSelectedAction;
        }
    }
    
    // This interface must be implmented for each menu item to provide
    // its behavior when clicked and is used to allow a dynamic implemnetation
    // of OnNavigationItemSelectedListener instead of implementing a static
    // action for each menu item
    public interface OnNavigationItemSelectedAction {
        void onItemSelectedDo();
    }
}