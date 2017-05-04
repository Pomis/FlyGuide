package com.clintonmedbery.rajawalibasicproject;

import android.app.ActionBar;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.rajawali3d.surface.IRajawaliSurface;
import org.rajawali3d.surface.RajawaliSurfaceView;


public class DrawerActivity extends AppCompatActivity implements GoogleMap.OnMarkerClickListener,
        NavigationView.OnNavigationItemSelectedListener, View.OnTouchListener, OnMapReadyCallback {

    Renderer renderer;
    NavigationView navigationView;
    ScrollListener scrollListener;
    GestureDetector scrollDetector;
    private GoogleMap mMap;
    RajawaliSurfaceView surface;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        initRenderer();
        initHamburger();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decor = getWindow().getDecorView();
                decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        }

        updateAgo();

        scrollListener = new ScrollListener();
        scrollDetector = new GestureDetector(this, scrollListener);

        findViewById(R.id.map).setVisibility(View.GONE);

        FloatingActionButton myFab = (FloatingActionButton) findViewById(R.id.floating_button);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (findViewById(R.id.map).getVisibility() == View.GONE) {
                    ((FloatingActionButton) findViewById(R.id.floating_button)).setImageResource(R.drawable.ic_3d_rotation_white_48dp);
                    findViewById(R.id.map).setVisibility(View.VISIBLE);
                } else {
                    ((FloatingActionButton) findViewById(R.id.floating_button)).setImageResource(R.drawable.ic_map_white_48dp);
                    findViewById(R.id.map).setVisibility(View.GONE);
                }

                // Obtain the SupportMapFragment and get notified when the map is ready to be used.
                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map);
                mapFragment.getMapAsync(DrawerActivity.this);
            }
        });
    }

    private void initHamburger() {
        ImageView ham = (ImageView) findViewById(R.id.iv_hamburger);
        ham.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

                drawer.openDrawer(GravityCompat.START);

            }
        });
    }

    private void initRenderer() {
        surface = (RajawaliSurfaceView) findViewById(R.id.rjv_renderer);
        surface.setFrameRate(60.0);
        surface.setRenderMode(IRajawaliSurface.RENDERMODE_WHEN_DIRTY);

        renderer = new Renderer(this);
        surface.setSurfaceRenderer(renderer);
        surface.setOnTouchListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    int kmFrom = 1504;
    int kmTo = 1300;
    void updateAgo() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            kmFrom++;
                            kmTo--;
                            ((TextView)findViewById(R.id.tv_from)).setText(kmFrom+"km\n1:23 AGO");
                            ((TextView)findViewById(R.id.tv_to)).setText(kmTo+"km\n01:12 AGO");
                            updateAgo();
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        scrollDetector.onTouchEvent(event);

        if(event.getAction() == MotionEvent.ACTION_DOWN)
        {
            renderer.getObjectAt(event.getX(), event.getY());
            renderer.onTouchEvent(event);
        }

        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng latLng1 = new LatLng(46.9767, 6.80);
        LatLng latLng2 = new LatLng(46.95, 6.70);
        LatLng latLng3 = new LatLng(46.93, 6.90);
        LatLng latLng4 = new LatLng(46.99, 6.60);
        LatLng latLng5 = new LatLng(46.92, 6.65);
        LatLng latLng6 = new LatLng(46.954, 7);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng1, 10));

        Bitmap bitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.city)).getBitmap();
        Bitmap cityMarker = Bitmap.createScaledBitmap(bitmap, 98, 143, false);

        bitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.nature)).getBitmap();
        Bitmap natureMarker = Bitmap.createScaledBitmap(bitmap, 98, 143, false);

        bitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.water)).getBitmap();
        Bitmap waterMarker = Bitmap.createScaledBitmap(bitmap, 98, 143, false);

        MarkerOptions[] markers = new MarkerOptions[] {
                new MarkerOptions().position(latLng1).title("nature").icon(BitmapDescriptorFactory.fromBitmap(natureMarker)),
                new MarkerOptions().position(latLng2).title("nature").icon(BitmapDescriptorFactory.fromBitmap(natureMarker)),
                new MarkerOptions().position(latLng3).title("water").icon(BitmapDescriptorFactory.fromBitmap(waterMarker)),
                new MarkerOptions().position(latLng6).title("water").icon(BitmapDescriptorFactory.fromBitmap(waterMarker)),
                new MarkerOptions().position(latLng4).title("city").icon(BitmapDescriptorFactory.fromBitmap(cityMarker)),
                new MarkerOptions().position(latLng5).title("city").icon(BitmapDescriptorFactory.fromBitmap(cityMarker)),
        };

        for (MarkerOptions m : markers) {
            mMap.addMarker(m);
        }

        mMap.setOnMarkerClickListener(this);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (marker.getTitle().equals("nature")) {
            startActivity(new Intent(getApplication(), NatureActivity.class));
        } else {
            startActivity(new Intent(getApplication(), SightActivity.class));
        }

        return false;
    }

    public class ScrollListener extends
            GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            renderer.xDistance = distanceX;
            renderer.yDistance = distanceY;
            renderer.scroll = true;
            System.out.println("onScroll: distanceX = " + distanceX + "; distanceY = " + distanceY);
            return super.onScroll(e1, e2, distanceX, distanceY);
        }
    }
}
