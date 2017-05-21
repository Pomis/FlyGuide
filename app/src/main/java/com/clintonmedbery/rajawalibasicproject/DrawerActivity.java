package com.clintonmedbery.rajawalibasicproject;

import android.app.ActionBar;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
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
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.vr.sdk.widgets.pano.VrPanoramaView;

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
    Marker plane;
    Polyline polyline;


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
//                if (findViewById(R.id.map).getVisibility() == View.GONE) {
//                    ((FloatingActionButton) findViewById(R.id.floating_button)).setImageResource(R.drawable.ic_3d_rotation_white_48dp);
//                    findViewById(R.id.map).setVisibility(View.VISIBLE);
//                } else {
//                    ((FloatingActionButton) findViewById(R.id.floating_button)).setImageResource(R.drawable.ic_map_white_48dp);
//                    findViewById(R.id.map).setVisibility(View.GONE);
//                }
//
//                // Obtain the SupportMapFragment and get notified when the map is ready to be used.
//                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                        .findFragmentById(R.id.map);
//                mapFragment.getMapAsync(DrawerActivity.this);
                startActivity(new Intent().setAction("com.rajawali3d.examples.examples.vr_ar.VR_MAP"));
            }
        });
        FloatingActionButton myFab2 = (FloatingActionButton) findViewById(R.id.floating_button_map);
        myFab2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (findViewById(R.id.map).getVisibility() == View.GONE) {
                    ((FloatingActionButton) findViewById(R.id.floating_button_map)).setImageResource(R.drawable.ic_3d_rotation_white_48dp);
                    findViewById(R.id.map).setVisibility(View.VISIBLE);
                } else {
                    ((FloatingActionButton) findViewById(R.id.floating_button_map)).setImageResource(R.drawable.ic_map_white_48dp);
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

        if (id == R.id.nav_mys7) {
            // Handle the camera action
        } else if (id == R.id.nav_buy) {

        } else if (id == R.id.nav_best) {

        } else if (id == R.id.nav_reg) {

        } else if (id == R.id.nav_table) {

        } else if (id == R.id.nav_tickets) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    int kmFrom = 2445;
    int kmTo = 999;

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
                            ((TextView) findViewById(R.id.tv_from)).setText(kmFrom + "km\n04:03 AGO");
                            ((TextView) findViewById(R.id.tv_to)).setText(kmTo + "km\n00:51 AGO");
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

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            renderer.getObjectAt(event.getX(), event.getY());
            renderer.onTouchEvent(event);
        }

        return true;
    }

    boolean wasPaused = false;


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng latLng1 = new LatLng(47.05, 6.80);
        LatLng latLng2 = new LatLng(46.80, 6.70);
        LatLng latLng3 = new LatLng(46.93, 6.90);
        LatLng latLng4 = new LatLng(47.03, 6.60);
        LatLng latLng5 = new LatLng(46.89, 6.65);
        LatLng latLng6 = new LatLng(46.974, 7);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng1, 10));

        Bitmap bitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.city)).getBitmap();
        Bitmap cityMarker = Bitmap.createScaledBitmap(bitmap, 98, 143, false);

        bitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.nature)).getBitmap();
        Bitmap natureMarker = Bitmap.createScaledBitmap(bitmap, 98, 143, false);

        bitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.water)).getBitmap();
        Bitmap waterMarker = Bitmap.createScaledBitmap(bitmap, 98, 143, false);

        MarkerOptions[] markers = new MarkerOptions[]{
                new MarkerOptions().position(latLng1).title("nature").icon(BitmapDescriptorFactory.fromBitmap(natureMarker)),
                new MarkerOptions().position(latLng2).title("nature").icon(BitmapDescriptorFactory.fromBitmap(natureMarker)),
                //new MarkerOptions().position(latLng3).title("water").icon(BitmapDescriptorFactory.fromBitmap(waterMarker)),
                new MarkerOptions().position(latLng6).title("water").icon(BitmapDescriptorFactory.fromBitmap(waterMarker)),
                new MarkerOptions().position(latLng4).title("city").icon(BitmapDescriptorFactory.fromBitmap(cityMarker)),
                new MarkerOptions().position(latLng5).title("city").icon(BitmapDescriptorFactory.fromBitmap(cityMarker)),
        };

        for (MarkerOptions m : markers) {
            mMap.addMarker(m);
        }
        mMap.setOnMarkerClickListener(this);

        LatLng CDG = new LatLng(49.0097, 2.5477);
        LatLng TSF = new LatLng(45.6484, 12.1944);

        PolylineOptions way = new PolylineOptions()
                .add(CDG)
                .add(new LatLng(47.06, 6.40))
                .add(new LatLng(46.93, 7.10))
                .add(TSF);
        polyline = mMap.addPolyline(way);
        polyline.setColor(R.color.primaryColor);

        bitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.plane)).getBitmap();
        Bitmap plane_bitmap = Bitmap.createScaledBitmap(bitmap, 152, 144, false);
        plane_bitmap = rotateBitmap(plane_bitmap, 15);

        if (!wasPaused) {
            plane = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(47.01, 6.50))
                    .title("plane")
                    .icon(BitmapDescriptorFactory.fromBitmap(plane_bitmap)));
        }

        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
//                for (double y = polyline.getPoints().get(0).longitude; y < polyline.getPoints().get(1).longitude; y += 0.001) {
//                    double x = -(2.0330000000000013 * y - 213.58342141000003) / 4.2523;
//
//                    //plane.setPosition(new LatLng(x, y));
//                }

                if (!wasPaused) {
                    wasPaused = true;
                    animateMarker(plane, new LatLng(46.88, 7.20), false);
                }
            }
        });
    }

    public void animateMarker(final Marker marker, final LatLng toPosition,
                              final boolean hideMarker) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = mMap.getProjection();
        Point startPoint = proj.toScreenLocation(marker.getPosition());
        final LatLng startLatLng = proj.fromScreenLocation(startPoint);
        final long duration = 50000;

        final Interpolator interpolator = new LinearInterpolator();

        //marker.setRotation(angleDeg);

        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / duration);
                double lng = t * toPosition.longitude + (1 - t)
                        * startLatLng.longitude;
                double lat = t * toPosition.latitude + (1 - t)
                        * startLatLng.latitude;

                marker.setPosition(new LatLng(lat, lng));

                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                } else {
                    if (hideMarker) {
                        marker.setVisible(false);
                    } else {
                        marker.setVisible(true);
                    }
                }
            }
        });
    }

    public static Bitmap rotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (marker.getTitle().equals("nature")) {
            startActivity(new Intent(getApplication(), NatureActivity.class).setAction(Intent.ACTION_VIEW).putExtra("inputType", VrPanoramaView.Options.TYPE_STEREO_OVER_UNDER).setData(Uri.parse("natura.jpg")))
            ;
        } else {
            startActivity(new Intent(getApplication(), SightActivity.class).setAction(Intent.ACTION_VIEW).putExtra("inputType", VrPanoramaView.Options.TYPE_STEREO_OVER_UNDER).setData(Uri.parse("city.jpg")));
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
