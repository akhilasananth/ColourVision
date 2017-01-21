package com.example.home.camera;

import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.*;
import android.util.Log;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.home.camera.colorHelper.ColorHelper;

public class MainActivity extends FragmentActivity {
    private static final String TAG = "AndroidCameraApi";
    private static final int REQUEST_CAMERA_PERMISSION = 200;

    private ColorHelper colorHelper;

    private int currentPage = 1;

    private int color1 = Color.BLACK;
    private int color2 = Color.BLACK;

    private ViewPager viewPager;
    private ScreenSlidePagerAdapter pagerAdapter;
    private ColorViewFragment colorViewFragment;

    private DBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);

        resetDatabase();

        viewPager = (ViewPager) findViewById(R.id.pager);
        pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(currentPage);

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.container);
        linearLayout.setOnTouchListener(new OnSwipeTouchListener(MainActivity.this) {
            @Override
            public void onSwipeRight() {
                if (currentPage != 0)
                    currentPage--;
                viewPager.setCurrentItem(currentPage);
            }

            @Override
            public void onSwipeLeft() {
                if (currentPage != pagerAdapter.getCount() - 1)
                    currentPage++;
                viewPager.setCurrentItem(currentPage);
            }

            @Override
            public void onSwipeTop() {

            }

            @Override
            public void onSwipeBottom() {

            }
        });

        colorViewFragment = new ColorViewFragment();
        getFragmentManager().beginTransaction().add(R.id.colorView, colorViewFragment).commit();

        colorHelper = new ColorHelper(this);
    }

    private void resetDatabase() {
        dbHandler = new DBHandler(this);

        dbHandler.onUpgrade(dbHandler.getWritableDatabase(), 0, 0);
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        int action = event.getAction();
        int keyCode = event.getKeyCode();
        int color = pagerAdapter.getColor();

        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                if (action == KeyEvent.ACTION_DOWN) {
                    pagerAdapter.updateFragments();
                    colorViewFragment.setColor1(color);
                    dbHandler.insertItem(ColorHelper.getColorName(ColorHelper.getClosestColor(color)));
                }
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                if (action == KeyEvent.ACTION_DOWN) {
                    pagerAdapter.updateFragments();
                    colorViewFragment.setColor2(color);
                    dbHandler.insertItem(ColorHelper.getColorName(ColorHelper.getClosestColor(color)));
                }
                return true;
            default:
                return super.dispatchKeyEvent(event);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                // close the app
                Toast.makeText(MainActivity.this, "Sorry!!!, you can't use this app without granting permission", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    public int getColor1() {
        return color1;
    }

    public int getColor2() {
        return color2;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        Log.e(TAG, "onPause");
        super.onPause();
    }
}