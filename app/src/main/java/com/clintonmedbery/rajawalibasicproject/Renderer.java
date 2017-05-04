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
import org.rajawali3d.math.Quaternion;
import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.primitives.Plane;
import org.rajawali3d.renderer.RajawaliRenderer;
import org.rajawali3d.util.ObjectColorPicker;
import org.rajawali3d.util.OnObjectPickedListener;


public class Renderer extends RajawaliRenderer implements OnObjectPickedListener, SensorEventListener {

    private ObjectColorPicker mPicker;
    private Context context;

    private DirectionalLight directionalLight;
    //private Sphere earthSphere;
    private Plane plane;
    private Material markerMaterial;
    private Material materialCity, materialNature, materialWater;

    float xDistance, yDistance;
    boolean scroll = false;

    Vector3 xAxis;
    Vector3 yAxis;
    Vector3 zAxis;

    SensorManager mSensorManager;
    Sensor mAccelerometer;


    Renderer(Context context) {
        super(context);
        this.context = context;
        setFrameRate(60);

        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    public void initScene() {
        mPicker = new ObjectColorPicker(this);
        mPicker.setOnObjectPickedListener(this);

        directionalLight = new DirectionalLight(1f, .2f, -1.0f);
        directionalLight.setColor(1.0f, 1.0f, 1.0f);
        directionalLight.setPower(4);
        try {
            getCurrentScene().setSkybox(R.drawable.skybox);

        } catch (ATexture.TextureException e) {
            e.printStackTrace();
        }
        getCurrentScene().setBackgroundColor(0xd4dded);
        getCurrentScene().addLight(directionalLight);
        getCurrentScene().setFog(new FogMaterialPlugin.FogParams(FogMaterialPlugin.FogType.LINEAR, 0xd4dded, 1f, 12f));

        Material material = new Material();
        material.enableLighting(true);
        material.setDiffuseMethod(new DiffuseMethod.Lambert());
        material.setColor(0);

        Texture earthTexture = new Texture("Earth", R.drawable.mapa);
        try {
            material.addTexture(earthTexture);

        } catch (ATexture.TextureException error) {
            Log.d("DEBUG", "TEXTURE ERROR");
        }

        initMarkerTexture();
        plane = new Plane(20, 15, 1, 1);
        plane.setY(6);

        new Marker(.5f, 2.4f, .15f, false, materialNature);
        new Marker(.9f, 4.7f, .15f, false, materialNature);
        new Marker(-.59f, 4.2f, .15f, false, materialNature);
        new Marker(.8f, 2.4f, .15f, false, materialNature);
        new Marker(-.3f, 8.4f, .15f, false, materialCity);
        new Marker(-1.7f, 7.1f, .15f, false, materialCity);
        new Marker(1.1f, 7.5f, .15f, false, materialCity);
        new Marker(1.3f, 11.4f, .15f, false, materialWater);
        new Marker(-1.3f, 1.4f, .15f, false, materialWater);
        new Marker(-2.4f, 10.4f, .15f, false, materialWater);
        new Marker(3.5f, 13.4f, .15f, false, materialWater);
        new Marker(0, 0, 0, true, materialWater);

        plane.setMaterial(material);
        getCurrentScene().addChild(plane);
        getCurrentCamera().setZ(.8f);
        getCurrentCamera().rotate(Vector3.Axis.X, 280.0);
        plane.rotate(Vector3.Axis.Y, 180.0);

        xAxis = getCurrentCamera().getOrientation().getXAxis();
        yAxis = getCurrentCamera().getOrientation().getYAxis();
        zAxis = getCurrentCamera().getOrientation().getZAxis();

        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    private void initMarkerTexture() {
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
    }

    @Override
    public void onRender(final long elapsedTime, final double deltaTime) {
        getCurrentCamera().setY(getCurrentCamera().getY() + 0.0025);
        //plane.setY(plane.getY() - 0.0025);

        if(scroll) {

            //Method to rotate the 3D model
            Quaternion x = new Quaternion(yAxis, -xDistance / 10);
            Quaternion y = new Quaternion(xAxis, yDistance / 10);
            getCurrentCamera().rotate(x);
            //getCurrentCamera().rotate(y);

            scroll = false;
        }

        super.onRender(elapsedTime, deltaTime);
    }


    public void onTouchEvent(MotionEvent event) {

    }

    public void onOffsetsChanged(float x, float y, float z, float w, int i, int j) {

    }

    public void getObjectAt(float x, float y) {
        mPicker.getObjectAt(x, y);
    }

    @Override
    public void onObjectPicked(Object3D object) {

        if (object.getName().startsWith("marker") && !object.getName().equals("marker" + (i - 1))) {
            int num = Integer.valueOf(object.getName().split("marker")[1]);
            if (num <= 2)
                getContext().startActivity(new Intent(getContext(), NatureActivity.class));
            else
                getContext().startActivity(new Intent(getContext(), SightActivity.class));

        }
    }

    private int i = 0;

    float X, Y, xdist, ydist;
    boolean init = false;

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() != Sensor.TYPE_ACCELEROMETER) return;

        if (X == event.values[0] || Y == event.values[2]) return;

        if (!init) {
            X = event.values[0];
            Y = event.values[0];
            //init = true;
            return;
        }

        xdist = X - event.values[0];
        ydist = Y - event.values[2];

        X = event.values[0];
        Y = event.values[0];

        System.out.println(xdist + "; " + ydist);

        //Method to rotate the 3D model
        Quaternion x = new Quaternion(yAxis, xdist / 5);
        Quaternion y = new Quaternion(xAxis, -ydist / 5);
        //plane.rotate(x.multiply(y));
        getCurrentCamera().rotate(x);
        getCurrentCamera().rotate(y);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private class Marker {
        Plane plane;

        Marker(float mx, float my, float mz, boolean empty, Material material) {
            plane = new Plane(.2f, .3f, 1, 1);
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

