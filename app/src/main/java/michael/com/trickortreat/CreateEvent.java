package michael.com.trickortreat;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;


public class CreateEvent extends DialogFragment implements AsyncResponse {

    RatingBar rating;
    private EditText addr,comment;
    private String locality; //Locality passed from MainActivity
    private double latitude,longitude; //latlng passed from mapsactivity


    public CreateEvent() {
        // Required empty public constructor
        }




    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_create_event,container);


        Bundle bundle = getArguments();
        String address = bundle.getString("Address"); //Retrieves address from MapsActivity
        locality = bundle.getString("Locality"); //Retrieves locality from MapsActivity
        latitude = bundle.getDouble("Latitude");//Retrieves latitude from MapsActivity
        longitude = bundle.getDouble("Longitude");//Retrieves longitude from MapsActivity
        addr = rootView.findViewById(R.id.address); //Bar to edit the address
        rating = rootView.findViewById(R.id.rating); //Rating from 0-5
        comment = rootView.findViewById(R.id.comment);


        Button submit = rootView.findViewById(R.id.submit);
        addr.setText(address);

        //Start a thread on click submit to make sure address exists
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AsyncSearch asyncThread = new AsyncSearch();
                asyncThread.execute(addr.getText().toString());

            }
        });




        return rootView;
    }

    //Sends boolean from async thread to see if altered location exists
    //Sends data to firebase
    @Override
    public void sendToFirebase(Address address) {
        String comments = comment.getText().toString();
        String addrText = addr.getText().toString();
        float stars = rating.getNumStars();
        boolean commentExists = !comments.isEmpty();
        boolean rated = rating.isEnabled();


        //Checks if the address and comment  exists and there is a rating
        if(address !=null && commentExists && rated){
            System.out.println(comments + " " + addrText + " " + stars);

            //Send database to firebase if address exists
            DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference();
            String key = dataRef.push().getKey();
            Review review = new Review(stars,comments,addrText,locality,latitude,longitude);
            dataRef.child(key).setValue(review);

            dismiss();



        }
        else{

        }
    }



    @Override
    public void sendLatlng(LatLng latLng) {

    }

    @Override
    public void openDialog(Address address) {

    }



    @Override
    public void issue() {

    }


    //Async check if the altered address exists
    public class AsyncSearch extends AsyncTask<String,List<Address>,Void>{




        @Override
        protected Void doInBackground(String... strings) {
            Geocoder geocoder = new Geocoder(getActivity());

            try {
                List<Address> addresses = geocoder.getFromLocationName(strings[0], 2);
                onPostExecute(addresses);

            }catch(IOException io){
                io.printStackTrace();
            }


            return null;
        }




        private void onPostExecute(List<Address> addresses) {

           if(addresses.size() >0){
               sendToFirebase(addresses.get(0));
           }
           else{
               sendToFirebase(null);
        }
        }
    }



}
