package michael.com.trickortreat;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,AsyncResponse {

    private GoogleMap map;
    private LatLng latlng;
    private LocationFinder loc;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Intent infoIntent = getIntent(); //Gets extra data sent from MainActivity
        double latitude = infoIntent.getDoubleExtra("Latitude",0);
        double longitude = infoIntent.getDoubleExtra("Longitude",0);
        this.latlng = new LatLng(latitude,longitude);


        loc = new LocationFinder();
        loc.asyncResp = this;

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    //Code to add info from createevent to a marker
    protected void onStart(){
        super.onStart();
        System.out.println("RESUMED");
        Bundle bundle = new Bundle();
        boolean reviewMade = bundle.getBoolean("Review");

        double latitude = bundle.getDouble("Latitude");
        double longitude = bundle.getDouble("Longitude");
        String addrText = bundle.getString("Address");

        if(reviewMade){
            System.out.println("REVIEW HAS BEEN CREATED");
            LatLng latLng = new LatLng(latitude,longitude);
            map.addMarker(new MarkerOptions().position(latLng)).setTitle(addrText);
        }


    }






    @Override
    public void onMapReady(final GoogleMap map) {
        this.map = map;

        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng,18.0f));

        /*
        On screen click, open a dialogfragment that will allow user to create
        info on the house
        */
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latlng) {

                LocationFinder locThread = new LocationFinder(); //Need to create a new async thread each time
                locThread.execute(latlng); //Asynchronous

            }
        });
        //LatLng sydney = new LatLng(-34, 151);

        //map.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    @Override
    public void sendToFirebase(Address address) {

    }

    ///Inherited from AsyncResponse interface
    @Override
    public void sendLatlng(LatLng latLng) {


    }

    ///Inherited from AsyncResponse interface
    //Sends address to CreateEvent DialogFragment
    @Override
    public void openDialog(final Address address) {
        CreateEvent createEvent = new CreateEvent();
        Bundle bundle = new Bundle();
        bundle.putString("Address",address.getAddressLine(0));
        createEvent.setArguments(bundle);
        createEvent.show(getFragmentManager(),"DialogFragment");
        //When its cancelled or dismissed, receive information and place it on map
        createEvent.onCancel(new DialogInterface() {
            @Override
            public void cancel() {

            }

            @Override
            public void dismiss() {

            }
        });

    }

    ///Inherited from AsyncResponse interface
    @Override
    public void issue() {

    }


    public class LocationFinder extends AsyncTask<LatLng,Void,Void>{


        public AsyncResponse asyncResp = null;
        @Override
        protected Void doInBackground(LatLng... latlngs) {

            Geocoder geocoder = new Geocoder(getApplicationContext());
            try {
                List<Address> addresses = geocoder.getFromLocation(latlngs[0].latitude, latlngs[0].longitude, 2);
                Address address = addresses.get(0);
                onPostExecute(address);
            }catch(IOException io){
                io.printStackTrace();
            }

            return null;
        }


        private void onPostExecute(Address address) {

            //Opens the dialog fragment for creating a review
            openDialog(address);
        }
    }

}
