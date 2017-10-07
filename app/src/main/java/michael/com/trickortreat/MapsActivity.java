package michael.com.trickortreat;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,AsyncResponse,SwipeRefreshLayout.OnRefreshListener {

    private GoogleMap map;
    private LatLng latlng;
    private LocationFinder loc;
    String locality;
    double latitude,longitude;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Intent infoIntent = getIntent(); //Gets extra data sent from MainActivity
        latitude = infoIntent.getDoubleExtra("Latitude",0);
        longitude = infoIntent.getDoubleExtra("Longitude",0);
        locality = infoIntent.getStringExtra("Locality");

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

        //Sends location details to CreateEvent dialogFragment
        bundle.putString("Address",address.getAddressLine(0));
        bundle.putString("Locality",locality);
        bundle.putDouble("Latitude",latitude);
        bundle.putDouble("Longitude",longitude);

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

    //Inherited from onSwipeRefresh
    @Override
    public void onRefresh() {
        updateUI();

    }

    public void onStart(){
        super.onStart();
        updateUI();
    }

    //Updates the map
    public void updateUI(){
        DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference();
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
