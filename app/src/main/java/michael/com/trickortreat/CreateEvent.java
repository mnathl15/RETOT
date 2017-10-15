package michael.com.trickortreat;

import android.app.DialogFragment;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.List;


public class CreateEvent extends DialogFragment {

    RatingBar rating;
    private EditText addr,comment;
    private String locality; //Locality passed from MainActivity
    private String initAddress; //Initial address passed in from MapsActivity
    private double latitude,longitude; //latlng passed from mapsactivity
    ProgressBar loading;
    TextView error;

    /*NOTE #1:  Having issue; addresses allowed on tap are being called not addresses later on, using boolean
    to say that its already been confirmed an address for now*/

    public CreateEvent() {
        // Required empty public constructor
        }




    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_create_event,container);


        Bundle bundle = getArguments();
        initAddress = bundle.getString("Address"); //Retrieves address from MapsActivity
        System.out.println("First test: " + initAddress);
        locality = bundle.getString("Locality"); //Retrieves locality from MapsActivity
        latitude = bundle.getDouble("Latitude");//Retrieves latitude from MapsActivity
        longitude = bundle.getDouble("Longitude");//Retrieves longitude from MapsActivity
        addr = rootView.findViewById(R.id.comments); //Bar to edit the address
        rating = rootView.findViewById(R.id.rating); //Rating from 0-5
        comment = rootView.findViewById(R.id.comment);
        loading = rootView.findViewById(R.id.loading);

        loading.setVisibility(View.INVISIBLE);
        error = rootView.findViewById(R.id.error);


        Button submit = rootView.findViewById(R.id.submit);
        addr.setText(initAddress);

        //Start a thread on click submit to make sure address exists
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loading.setVisibility(View.VISIBLE);
                AsyncSearch asyncThread = new AsyncSearch();
                asyncThread.execute(addr.getText().toString());

            }
        });




        return rootView;
    }




    //Async check if the altered address exists
    public class AsyncSearch extends AsyncTask<String,List<Address>,Address>{




        @Override
        protected Address doInBackground(String... strings) {
            Geocoder geocoder = new Geocoder(getActivity());

            try {
                List<Address> addresses = geocoder.getFromLocationName(strings[0], 2);
                if(addresses.size() > 0){
                    return addresses.get(0);
                }
                else{
                    return null;
                }

            }catch(IOException io){
                io.printStackTrace();
            }


            return null;
        }





        //Sends boolean from async thread to see if altered location exists
        //Sends data to firebase
        @Override
        protected void onPostExecute(Address address) {



            super.onPostExecute(address);
            String comments = comment.getText().toString();
            String addrText = addr.getText().toString();
            float stars = rating.getRating();
            boolean commentExists = !comments.isEmpty();
            boolean rated = rating.isEnabled();


            //#1
            boolean sameAddress = (addr.getText().toString()).equals(initAddress);

            //Checks if the address and comment  exists and there is a rating
            if((sameAddress || address !=null)  && commentExists && rated ){


                //Send database to firebase if address exists
                DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference();
                String key = dataRef.push().getKey();

                Review review = new Review(stars,comments,addrText,locality,latitude,longitude);
                dataRef.child(key).setValue(review);
                loading.setVisibility(View.INVISIBLE);
                dismiss();


            }
            //If there is an issue
            else{
                loading.setVisibility(View.INVISIBLE);

                if(address==null){
                    error.setText("Address does not exist");
                }
                else{
                    error.setText("There is something wrong with your form");
                }
                //SHOW ERROR MESSAGE
            }


        }
    }



}
