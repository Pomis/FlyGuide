package com.clintonmedbery.rajawalibasicproject;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.MotionEvent;

import org.rajawali3d.lights.DirectionalLight;
import org.rajawali3d.materials.Material;
import org.rajawali3d.materials.methods.DiffuseMethod;
import org.rajawali3d.materials.plugins.FogMaterialPlugin;
import org.rajawali3d.materials.textures.ATexture;
import org.rajawali3d.materials.textures.Texture;
import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.primitives.Plane;
import org.rajawali3d.renderer.RajawaliRenderer;

/**
 * Created by clintonmedbery on 4/6/15.
 */
public class Renderer extends RajawaliRenderer {

    public Context context;

    private DirectionalLight directionalLight;
    //private Sphere earthSphere;
    Plane plane;
    Material markerMaterial;


    public Renderer(Context context) {
        super(context);
        this.context = context;
        setFrameRate(60);
    }

    public void initScene() {
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
        new Marker(.3f, 4.4f, .1f);
        new Marker(.5f, 2.4f, .1f);
        new Marker(1.3f, 8.4f, .1f);
        plane.setMaterial(material);
        getCurrentScene().addChild(plane);
        getCurrentCamera().setZ(.6f);
        getCurrentCamera().rotate(Vector3.Axis.X, 280.0);
        plane.rotate(Vector3.Axis.Y, 180.0);
//        plane.rotate();


    }

    void initMarkerTexture() {
        markerMaterial = new Material();
        markerMaterial.enableLighting(true);
        markerMaterial.setDiffuseMethod(new DiffuseMethod.Lambert());
        markerMaterial.setColor(0);
        Texture markerTexture = new Texture("Marker", R.drawable.marker);
        try {
            markerMaterial.addTexture(markerTexture);

        } catch (ATexture.TextureException error) {
            Log.d("DEBUG", "TEXTURE ERROR");
        }
    }

    @Override
    public void onRender(final long elapsedTime, final double deltaTime) {
        super.onRender(elapsedTime, deltaTime);
        getCurrentCamera().setY(getCurrentCamera().getY() + 0.002);

    }


    public void onTouchEvent(MotionEvent event) {


    }

    public void stop() {
        stopRendering();
    }

    public void onOffsetsChanged(float x, float y, float z, float w, int i, int j) {

    }

    class Marker {
        public Plane plane;

        Marker(float mx, float my, float mz) {
            plane = new Plane(.2f, .2f, 1, 1);
            plane.setX(mx);
            plane.setY(my);
            plane.setZ(mz);
            plane.rotate(Vector3.Axis.Z, 180);
            plane.rotate(Vector3.Axis.X, 90);
            plane.setMaterial(markerMaterial);
            getCurrentScene().addChild(plane);


        }
    }
}

