package com.clintonmedbery.rajawalibasicproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.MotionEvent;

import org.rajawali3d.Object3D;
import org.rajawali3d.lights.DirectionalLight;
import org.rajawali3d.materials.Material;
import org.rajawali3d.materials.MaterialManager;
import org.rajawali3d.materials.methods.DiffuseMethod;
import org.rajawali3d.materials.plugins.FogMaterialPlugin;
import org.rajawali3d.materials.textures.ATexture;
import org.rajawali3d.materials.textures.AlphaMapTexture;
import org.rajawali3d.materials.textures.Texture;
import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.primitives.Plane;
import org.rajawali3d.renderer.RajawaliRenderer;
import org.rajawali3d.util.ObjectColorPicker;
import org.rajawali3d.util.OnObjectPickedListener;


public class Renderer extends RajawaliRenderer implements OnObjectPickedListener, SensorEventListener {

    ObjectColorPicker mPicker;
    public Context context;

    private DirectionalLight directionalLight;
    //private Sphere earthSphere;
    Plane plane;
    Material markerMaterial;
    Material materialCity, materialNature, materialWater;

    SensorManager mSensorManager;
    Sensor mAccelerometer;
    float X, Y;


    public Renderer(Context context) {
        super(context);
        this.context = context;
        setFrameRate(60);

        SensorManager mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    public void initScene() {
        mPicker = new ObjectColorPicker(this);
        mPicker.setOnObjectPickedListener(this);

        directionalLight = new DirectionalLight(1f, .2f, -1.0f);
        directionalLight.setColor(1.0f, 1.0f, 1.0f);
        directionalLight.setPower(4);
//        try {
//            getCurrentScene().setSkybox(R.drawable.earthtruecolor_nasa_big);
//        } catch (ATexture.TextureException e) {
//            e.printStackTrace();
//        }
        getCurrentScene().setBackgroundColor(0xd4dded);
        getCurrentScene().addLight(directionalLight);

        getCurrentScene().setFog(new FogMaterialPlugin.FogParams(FogMaterialPlugin.FogType.LINEAR, 0xd4dded, 1f, 16f));
        Material material = new Material();
        material.enableLighting(true);
        material.setDiffuseMethod(new DiffuseMethod.Lambert());
        material.setColor(0);
        Texture earthTexture = new Texture("Earth", R.drawable.earthtruecolor_nasa_big);
        try {
            material.addTexture(earthTexture);

        } catch (ATexture.TextureException error) {
            Log.d("DEBUG", "TEXTURE ERROR");
        }
        initMarkerTexture();
        plane = new Plane(60, 40, 1, 1);

        Texture city = new Texture("Marker", R.drawable.city);
        Texture nature = new Texture("Marker", R.drawable.nature);
        Texture water = new Texture("Marker", R.drawable.water);

        new Marker(.3f, 4.4f, .1f, false, materialCity);
        new Marker(.5f, 2.4f, .1f, false, materialNature);
        new Marker(1.3f, 8.4f, .1f, false, materialWater);
        new Marker(0, 0, 0, true, materialWater);

        plane.setMaterial(material);
        getCurrentScene().addChild(plane);
        getCurrentCamera().setZ(.6f);
        getCurrentCamera().rotate(Vector3.Axis.X, 280.0);
        plane.rotate(Vector3.Axis.Y, 180.0);
//        plane.rotate();

        mSensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);

        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    void initMarkerTexture() {
        markerMaterial = new Material();
        markerMaterial.enableLighting(true);
        markerMaterial.setDiffuseMethod(new DiffuseMethod.Lambert());
        markerMaterial.setColor(0);

        Texture markerTexture = new Texture("Marker", R.drawable.city);
        materialCity = new Material();
        try {
            AlphaMapTexture alphaTex = new AlphaMapTexture("camdenTown", R.drawable.mask);
            alphaTex.setInfluence(.5f);
            materialCity.addTexture(alphaTex);
            materialCity.setColorInfluence(0);
            materialCity.addTexture(markerTexture);

        } catch (ATexture.TextureException e) {
            e.printStackTrace();
        }
        materialCity.setColorInfluence(.5f);

        markerTexture = new Texture("Marker", R.drawable.nature);
        materialNature = new Material();
        try {
            AlphaMapTexture alphaTex = new AlphaMapTexture("camdenTown", R.drawable.mask);
            alphaTex.setInfluence(.5f);
            materialNature.addTexture(alphaTex);
            materialNature.setColorInfluence(0);
            materialNature.addTexture(markerTexture);

        } catch (ATexture.TextureException e) {
            e.printStackTrace();
        }
        materialNature.setColorInfluence(.5f);

        markerTexture = new Texture("Marker", R.drawable.water);
        materialWater = new Material();
        try {
            AlphaMapTexture alphaTex = new AlphaMapTexture("camdenTown", R.drawable.mask);
            alphaTex.setInfluence(.5f);
            materialWater.addTexture(alphaTex);
            materialWater.setColorInfluence(0);
            materialWater.addTexture(markerTexture);

        } catch (ATexture.TextureException e) {
            e.printStackTrace();
        }
        materialWater.setColorInfluence(.5f);
    }

    @Override
    public void onRender(final long elapsedTime, final double deltaTime) {
        super.onRender(elapsedTime, deltaTime);
        getCurrentCamera().setY(getCurrentCamera().getY() + 0.002);
    }


    public void onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_SCROLL) {
            System.out.println("ACTION_MOVE");
        }
    }

    public void pleaseStop() {
        stopRendering();
        getCurrentScene().clearChildren();
    }

    public void onOffsetsChanged(float x, float y, float z, float w, int i, int j) {

    }

    @Override
    public void onObjectPicked(Object3D object) {

        if (object.getName().startsWith("marker") && !object.getName().equals("marker" + (i - 1))) {
            getContext().startActivity(new Intent(getContext(), SightActivity.class));
        }
    }

    public void getObjectAt(float x, float y) {
        mPicker.getObjectAt(x, y);
    }

    int i = 0;

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() != Sensor.TYPE_ACCELEROMETER) return;
        double FILTERING_FACTOR = 0;

        X = (float) (-event.values[1] * FILTERING_FACTOR + X
                * (1.0 - FILTERING_FACTOR));
        Y = (float) (event.values[0] * FILTERING_FACTOR + Y
                * (1.0 - FILTERING_FACTOR));
        double posx = X * .2f;
        double posy = Y * .2f;
        getCurrentCamera().setRotation(posx, posy, 0);
        double currentPosx = getCurrentCamera().getPosition().x;
        double currentPosy = getCurrentCamera().getPosition().y;
        //getCurrentCamera().setLookAt(currentPosx, currentPosy, 0);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    class Marker {
        public Plane plane;

        Marker(float mx, float my, float mz, boolean empty, Material material) {
            plane = new Plane(.2f, .2f, 1, 1);
            plane.setX(mx);
            plane.setY(my);
            plane.setZ(mz);
            plane.rotate(Vector3.Axis.Z, 180);
            plane.rotate(Vector3.Axis.X, 90);
            plane.setMaterial(material);
            plane.setDoubleSided(true);
            plane.setName("marker" + i++);
            if (empty) {
                plane.setVisible(false);
            }
            getCurrentScene().addChild(plane);
            mPicker.registerObject(plane);
        }
    }
}

