package michael.com.trickortreat;

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
    LocationFinder loc2;


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


                loc.execute(latlng); //Asynchronous

            }
        });
        //LatLng sydney = new LatLng(-34, 151);

        //map.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    @Override
    public void isAddress(boolean isAddress) {

    }

    ///Inherited from AsyncResponse interface
    @Override
    public void sendLatlng(LatLng latLng) {


    }

    ///Inherited from AsyncResponse interface
    //Sends address to CreateEvent DialogFragment
    @Override
    public void getAddress(Address address) {
        System.out.println("The address is: " + address.getAddressLine(0));
        CreateEvent createEvent = new CreateEvent();
        Bundle bundle = new Bundle();
        bundle.putString("Address",address.getAddressLine(0));
        createEvent.setArguments(bundle);
        createEvent.show(getFragmentManager(),"DialogFragment");

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

            getAddress(address);
        }
    }

}
