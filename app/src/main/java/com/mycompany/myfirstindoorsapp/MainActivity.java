package com.mycompany.myfirstindoorsapp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.customlbs.coordinates.GeoCoordinate;
import com.customlbs.library.IndoorsException;
import com.customlbs.library.IndoorsFactory;
import com.customlbs.library.IndoorsLocationListener;
import com.customlbs.library.callbacks.LoadingBuildingStatus;
import com.customlbs.library.model.Building;
import com.customlbs.library.model.Zone;
import com.customlbs.shared.Coordinate;
import com.customlbs.surface.library.IndoorsSurfaceFactory;
import com.customlbs.surface.library.IndoorsSurfaceFragment;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Sample Android project, powered by indoo.rs :)
 *
 * @author original author "indoo.rs | Philipp Koenig"
 * @author modified by Victor Wang
 */
public class MainActivity extends FragmentActivity implements IndoorsLocationListener {

    private IndoorsSurfaceFragment indoorsFragment;
    private String result = "";
    private int positionNum = 0;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IndoorsFactory.Builder indoorsBuilder = new IndoorsFactory.Builder();
        IndoorsSurfaceFactory.Builder surfaceBuilder = new IndoorsSurfaceFactory.Builder();
        indoorsBuilder.setContext(this);

        // TODO: replace this with your API-key
        indoorsBuilder.setApiKey("6ae0a01d-5902-4d6e-bf92-97da09dbbd8a");
        // TODO: replace 12345 with the id of the building you uploaded to
        // our cloud using the MMT
        indoorsBuilder.setBuildingId((long) 799963497);
        // callback for indoo.rs-events
        indoorsBuilder.setUserInteractionListener(this);
        surfaceBuilder.setIndoorsBuilder(indoorsBuilder);
        indoorsFragment = surfaceBuilder.build();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(android.R.id.content, indoorsFragment, "indoors");
        transaction.commit();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.background:
                Toast.makeText(this, result, Toast.LENGTH_LONG).show();

                Intent sendIntent = new Intent(Intent.ACTION_SEND);
                sendIntent.setData(Uri.parse("mailto:"));
                sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Location Data");
                sendIntent.putExtra(Intent.EXTRA_EMAIL, "vwang1111@gmail.com");
                sendIntent.putExtra(Intent.EXTRA_TEXT, result);
                sendIntent.setType("text/html");
                startActivity(sendIntent);
                result = "";

                return true;

            case R.id.toast:
                Coordinate geoCoordinate = indoorsFragment.getCurrentUserPosition();
                if (geoCoordinate != null) {
                    Toast.makeText(
                            this,
                            "Position " + (++positionNum) + " recorded at: " + geoCoordinate.x + ","
                                    + geoCoordinate.y, Toast.LENGTH_SHORT).show();

                    //write a string with the data
                    result += geoCoordinate.x;
                    result += ",";
                    result += geoCoordinate.y;
                    result += ",";
                } else {
                    Toast.makeText(this, "No user location detected", Toast.LENGTH_SHORT).show();
                }
                
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void positionUpdated(Coordinate userPosition, int accuracy) {
        /*
        GeoCoordinate geoCoordinate = indoorsFragment.getCurrentUserGpsPosition();

        if (geoCoordinate != null) {
            Toast.makeText(
                    this,
                    "User is located at " + geoCoordinate.getLatitude() + ","
                            + geoCoordinate.getLongitude(), Toast.LENGTH_SHORT).show();
        }
        */
        //((TextView)findViewById(R.id.currPos)).setText(userPosition.x + "," + userPosition.y);
    }

    @Override
    public void loadingBuilding(LoadingBuildingStatus loadingBuildingStatus) {
    }

    public void buildingLoaded(Building building) {
        // indoo.rs SDK successfully loaded the building you requested and
        // calculates a position now
        Toast.makeText(
                this,
                "Building is located at " + building.getLatOrigin() / 1E6 + ","
                        + building.getLonOrigin() / 1E6, Toast.LENGTH_SHORT).show();
    }

    public void onError(IndoorsException indoorsException) {
        Toast.makeText(this, indoorsException.getMessage(), Toast.LENGTH_LONG).show();
    }

    public void changedFloor(int floorLevel, String name) {
        // user changed the floor
    }

    public void leftBuilding(Building building) {
        // user left the building
    }

    public void loadingBuilding(int progress) {
        // indoo.rs is still downloading or parsing the requested building
    }

    public void orientationUpdated(float orientation) {
        // user changed the direction he's heading to
    }

    public void enteredZones(List<Zone> zones) {
        // user entered one or more zones
        /*
        Toast.makeText(
                this,
                "User entered " + zones.get(0).getName(), Toast.LENGTH_SHORT).show();
                */
    }

    @Override
    public void buildingLoadingCanceled() {
        // Loading of building was cancelled
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.mycompany.myfirstindoorsapp/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.mycompany.myfirstindoorsapp/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
