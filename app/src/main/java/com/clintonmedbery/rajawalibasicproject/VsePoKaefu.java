package com.clintonmedbery.rajawalibasicproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;
import android.view.MotionEvent;
import org.rajawali3d.lights.DirectionalLight;
import org.rajawali3d.materials.Material;
import org.rajawali3d.materials.methods.DiffuseMethod;
import org.rajawali3d.materials.textures.ATexture;
import org.rajawali3d.materials.textures.AlphaMapTexture;
import org.rajawali3d.materials.textures.NormalMapTexture;
import org.rajawali3d.materials.textures.Texture;
import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.primitives.Plane;
import org.rajawali3d.primitives.Sphere;
import org.rajawali3d.terrain.SquareTerrain;
import org.rajawali3d.terrain.Terrain;
import org.rajawali3d.terrain.TerrainGenerator;
import org.rajawali3d.util.ObjectColorPicker;
import org.rajawali3d.vr.renderer.VRRenderer;
import java.util.ArrayList;
import java.util.List;


public class VsePoKaefu extends VRRenderer {
    private ObjectColorPicker mPicker;
    private Context context;

    private DirectionalLight directionalLight;
    //private Sphere earthSphere;
    private Plane plane;
    private Material markerMaterial;
    private Material materialCity, materialNature, materialWater;

    Vector3 xAxis;
    Vector3 yAxis;
    Vector3 zAxis;

    private Terrain terrain;
    private Sphere lookatSphere;

    List<Plane> markers = new ArrayList<>();


    @Override
    public void onOffsetsChanged(float xOffset, float yOffset, float xOffsetStep,
                                 float yOffsetStep, int xPixelOffset, int yPixelOffset) {

    }

    @Override
    public void onTouchEvent(MotionEvent event) {

    }

    VsePoKaefu(Context context) {
        super(context);
        this.context = context;
        setFrameRate(60);
    }

    @Override
    protected void initScene() {
        mPicker = new ObjectColorPicker(this);
        //mPicker.setOnObjectPickedListener(this);

        directionalLight = new DirectionalLight(1f, .2f, -1.0f);
        directionalLight.setColor(1.0f, 1.0f, 1.0f);
        directionalLight.setPower(4);


        DirectionalLight light = new DirectionalLight(0.2f, -1f, 0f);
        light.setPower(.7f);
        getCurrentScene().addLight(light);

        light = new DirectionalLight(0.2f, 1f, 0f);
        light.setPower(1f);
        getCurrentScene().addLight(light);

        getCurrentCamera().setFarPlane(1000);

        getCurrentScene().setBackgroundColor(0xdddddd);


        createTerrain();

        try {
            getCurrentScene().setSkybox(R.drawable.posx, R.drawable.negx, R.drawable.posy,
                    R.drawable.negy, R.drawable.posz, R.drawable.negz);

        } catch (ATexture.TextureException e) {
            e.printStackTrace();
        }
//        getCurrentScene().setBackgroundColor(0xd4dded);
//        getCurrentScene().addLight(directionalLight);
//        getCurrentScene().setFog(new FogMaterialPlugin.FogParams(FogMaterialPlugin.FogType.LINEAR, 0xd4dded, 1f, 12f));

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
        //plane.setY(-50);


        new Marker(0, -3, 8, false, materialNature);
        new Marker(-1.5f, -3, 7, false, materialNature);
        new Marker(-2.2f, -3, 6, false, materialNature);
        new Marker(1.5f, -3, 5, false, materialNature);
        new Marker(-0.8f, -3, 4, false, materialCity);
        new Marker(-0.3f, -3, 4.5f, false, materialCity);
        new Marker(1.3f, -3, 5.5f, false, materialCity);
        new Marker(2.3f, -3, 6.5f, false, materialWater);
        //new Marker(-1.3f, 1.4f, .15f, false, materialWater);
        //new Marker(-2.4f, 10.4f, .15f, false, materialWater);
        //new Marker(3.5f, 13.4f, .15f, false, materialWater);
        //new Marker(0, 0, 0, true, materialWater);

        plane.setMaterial(material);
        getCurrentScene().addChild(plane);
        //getCurrentCamera().setZ(.8f);
        //getCurrentCamera().rotate(Vector3.Axis.X, 280.0);
        //plane.rotate(Vector3.Axis.Y, 180.0);

        lookatSphere = new Sphere(1, 12, 12);
        Material sphereMaterial = new Material();
        sphereMaterial.setDiffuseMethod(new DiffuseMethod.Lambert());
        sphereMaterial.enableLighting(true);
        lookatSphere.setMaterial(sphereMaterial);
        lookatSphere.setColor(Color.YELLOW);
        lookatSphere.setPosition(0, 0, -6);
        getCurrentScene().addChild(lookatSphere);
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

    public void createTerrain() {

        Bitmap bmp = BitmapFactory.decodeResource(getContext().getResources(),
                R.drawable.mapa);

        try {
            SquareTerrain.Parameters terrainParams = SquareTerrain.createParameters(bmp);
            // -- set terrain scale
            terrainParams.setScale(4f, 54f, 4f);
            // -- the number of plane subdivisions
            terrainParams.setDivisions(128);
            // -- the number of times the textures should be repeated
            terrainParams.setTextureMult(4);
            //
            // -- Terrain colors can be set by manually specifying base, middle and
            //    top colors.
            //
            // --  terrainParams.setBasecolor(Color.argb(255, 0, 0, 0));
            //     terrainParams.setMiddleColor(Color.argb(255, 200, 200, 200));
            //     terrainParams.setUpColor(Color.argb(255, 0, 30, 0));
            //
            // -- However, for this example we'll use a bitmap
            //
            //terrainParams.setColorMapBitmap(bmp);
            //
            // -- create the terrain
            //
            terrain = TerrainGenerator.createSquareTerrainFromBitmap(terrainParams, true);
            //terrain = new Terrain();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //
        // -- The bitmap won't be used anymore, so get rid of it.
        //
        bmp.recycle();

        //
        // -- A normal map material will give the terrain a bit more detail.
        //
        Material material = new Material();
        material.enableLighting(true);
        material.useVertexColors(true);
        material.setDiffuseMethod(new DiffuseMethod.Lambert());
        try {
            Texture groundTexture = new Texture("ground", R.drawable.ground);
            groundTexture.setInfluence(.5f);
            material.addTexture(groundTexture);
            material.addTexture(new NormalMapTexture("groundNormalMap", R.drawable.groundnor));
            material.setColorInfluence(0);
        } catch (ATexture.TextureException e) {
            e.printStackTrace();
        }

        //
        // -- Blend the texture with the vertex colors
        //
        material.setColorInfluence(.5f);
        terrain.setY(-100);
        terrain.setMaterial(material);

        getCurrentScene().addChild(terrain);
    }

    @Override
    protected void onRender(long ellapsedRealtime, double deltaTime) {
        //getCurrentCamera().setY(getCurrentCamera().getY() + 0.0025);
        //plane.setY(plane.getY() - 0.0025);

//        if(scroll) {
//
//            //Method to rotate the 3D model
//            Quaternion x = new Quaternion(yAxis, -xDistance / 10);
//            Quaternion y = new Quaternion(xAxis, yDistance / 10);
//            getCurrentCamera().rotate(x);
//            getCurrentCamera().rotate(y);
//
//            scroll = false;
//        }

        super.onRender(ellapsedRealtime, deltaTime);

        for (Plane marker : markers) {
            boolean isLookingAt = isLookingAtObject(marker);

            if(isLookingAt) {
                Material pick = new Material();
                pick.setColor(Color.BLUE);
                try {
                    AlphaMapTexture alphaTex = new AlphaMapTexture("camdenTown", R.drawable.mask);
                    alphaTex.setInfluence(.5f);
                    materialNature.addTexture(alphaTex);
                    materialNature.setColorInfluence(0);

                } catch (ATexture.TextureException e) {
                    e.printStackTrace();
                }

                marker.setMaterial(pick);
                System.out.println("Каааааааааааааааееееееееееееееефффффффффффффффф!!!!!!!!!!!!!!!");
            } else {
                //marker.setColor(Color.YELLOW);
            }
        }
    }

    private int i = 0;
    private class Marker {
        Plane plane;

        Marker(float mx, float my, float mz, boolean empty, Material material) {
            plane = new Plane(.6f, .9f, 1, 1);
            plane.setX(mx);
            plane.setY(my);
            plane.setZ(mz);
            //plane.rotate(Vector3.Axis.Z, 180);
            //plane.rotate(Vector3.Axis.X, 90);
            plane.setMaterial(material);
            plane.setDoubleSided(true);
            plane.setName("marker" + i++);
            if (empty) {
                plane.setVisible(false);
            }
            getCurrentScene().addChild(plane);
            mPicker.registerObject(plane);
            markers.add(plane);
        }
    }
}
