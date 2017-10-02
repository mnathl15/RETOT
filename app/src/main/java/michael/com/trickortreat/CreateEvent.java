package michael.com.trickortreat;

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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;


public class CreateEvent extends DialogFragment implements AsyncResponse {


    public CreateEvent() {
        // Required empty public constructor
        }

    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_create_event,container);

        final EditText addr = rootView.findViewById(R.id.address);
        Bundle bundle = getArguments(); //Retrieves address from MapsActivity
        String address = bundle.getString("Address");
        Button submit = (Button)rootView.findViewById(R.id.submit);
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
    public void isAddress(boolean isAddress) {
        if(isAddress){
            //Send database to firebase
        }
        else{

        }
    }

    @Override
    public void sendLatlng(LatLng latLng) {

    }

    @Override
    public void getAddress(Address address) {

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
               isAddress(true);
           }
           else{
               isAddress(false);
           }
        }
    }



}
