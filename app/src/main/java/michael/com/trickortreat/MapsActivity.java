package michael.com.trickortreat;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;

import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import android.support.v7.app.AlertDialog;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap map;
    private LatLng latlng;

    String locality;
    double latitudeTown,longitudeTown; //latitudes of locality



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);





        Intent infoIntent = getIntent(); //Gets extra data sent from MainActivity
        latitudeTown = infoIntent.getDoubleExtra("Latitude",0);
        longitudeTown = infoIntent.getDoubleExtra("Longitude",0);
        locality = infoIntent.getStringExtra("Locality");

        this.latlng = new LatLng(latitudeTown,longitudeTown);




        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }







    @Override
    public void onMapReady(final GoogleMap map) {
        this.map = map;

        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng,19.0f));

        /*
        On screen click, open a dialogfragment that will allow user to create
        info on the house
        */
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latlng) {

                MapClickAsync mapClickThread = new MapClickAsync(); //Need to create a new async thread each time
                mapClickThread.execute(latlng); //Asynchronous

            }
        });
        /*
        When user clicks a marker,
        a dialogfragment will pop up with all the the reviews from that location
         */
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker marker) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);


                //When marker is clicked, a dialog pops up to see if user wants to submit a review or look at reviews
                builder.setTitle("Trick or Treat?").setPositiveButton("Add review", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                MapClickAsync mapClickThread = new MapClickAsync(); //Need to create a new async thread each time
                                mapClickThread.execute(marker.getPosition());
                            }
                        }).setNegativeButton("View reviews", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        MarkerClickAsync markClickAsync = new MarkerClickAsync();
                        markClickAsync.execute(marker.getPosition());
                    }
                     }).setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }}).create();
                builder.show();





                return false;
            }
        });

    }



    public void onResume(){
        super.onResume();

        updateUI();

    }

    public void onStart(){
        super.onStart();

        updateUI();
    }

    //Updates the map
    public void updateUI(){

        final DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference();
        dataRef.orderByChild("locality").equalTo(locality).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Review review  = dataSnapshot.getValue(Review.class);
                LatLng latlng = new LatLng(review.getLatitude(),review.getLongitude());

                //Adds reviews that are in your locality
                map.addMarker(new MarkerOptions().position(latlng).title(review.getAddress()));


            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




    }


    //AsyncTask to translate map click into an address
    public class MapClickAsync extends AsyncTask<LatLng,Void,Address>{


        @Override
        protected Address doInBackground(LatLng... latlngs) {

            Geocoder geocoder = new Geocoder(getApplicationContext());
            try {
                List<Address> addresses = geocoder.getFromLocation(latlngs[0].latitude, latlngs[0].longitude, 2);
                Address address = addresses.get(0);
                return address;
            }catch(IOException io){
                io.printStackTrace();
            }

            return null;
        }


        protected void onPostExecute(Address address) {
            CreateEvent createEvent = new CreateEvent();

            Bundle bundle = new Bundle();

            double latitude = address.getLatitude(); //Latitude of clicked address
            double longitude = address.getLongitude(); //Longitude of clicked address



            //Sends location details to CreateEvent dialogFragment
            bundle.putString("Address",address.getAddressLine(0));
            bundle.putString("Locality",locality);
            bundle.putDouble("Latitude",latitude);
            bundle.putDouble("Longitude",longitude);

            createEvent.setArguments(bundle);
            //In case the fragment is already there
            if(!createEvent.isAdded()){
                createEvent.show(getFragmentManager(),"DialogFragment");
            }

            //When its cancelled or dismissed, receive information and place it on map


        }
    }
    //AsyncTask to get address when clicking on a marker,
    public class MarkerClickAsync extends AsyncTask<LatLng,Void,Address>{
        @Override
        protected Address doInBackground(LatLng... latlngs) {

            Geocoder geocoder = new Geocoder(getApplicationContext());
            try {
                List<Address> addresses = geocoder.getFromLocation(latlngs[0].latitude, latlngs[0].longitude, 2);
                Address address = addresses.get(0);
                System.out.println("Here address: " + address.getAddressLine(0));
                return address;
            }catch(IOException io){
                io.printStackTrace();
            }

            return null;
        }

        //Sends address to ShowEvents dialogFragment
        protected void onPostExecute(Address address) {
            Bundle bundle = new Bundle();
            bundle.putString("Address",address.getAddressLine(0));
            /*OptionsDialog opt = new OptionsDialog();

            if(!opt.isAdded()){
                opt.setArguments(bundle);
                opt.show(getSupportFragmentManager(),"Options");
            }*/

           ShowEvents showEvents = new ShowEvents();
            showEvents.setArguments(bundle);
            //In case the fragment is already there
            if(!showEvents.isAdded()){
                showEvents.show(getSupportFragmentManager(),"Events");
            }


        }


    }



}
