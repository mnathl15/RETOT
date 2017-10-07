package michael.com.trickortreat;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Map;

//1. CANCELLATION OF ASYNC THREAD
public class MainActivity extends AppCompatActivity implements AsyncResponse{

    private static final int NUM_ADDRESSES = 2;
    AsyncSearch async;
    EditText search;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        async = new AsyncSearch(); //Declares asynctask class object
        async.asyncResp = this; //Sets async.asynResp to current AsyncResponse object



        final Button start = (Button)findViewById(R.id.start);
        search = (EditText)findViewById(R.id.locality_search);



        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String locality = search.getText().toString();
                AsyncSearch async = new AsyncSearch();
                async.execute(locality);

                //1
            }
        });
    }

    //AsyncReponse interface override for retrieving LatLng object
    @Override
    public void sendLatlng(LatLng latlng) {

        String locality = search.getText().toString();
        Intent mapIntent = new Intent(MainActivity.this,MapsActivity.class);
        mapIntent.putExtra("Latitude", latlng.latitude); //Sends the latitude and longitude to the next intent
        mapIntent.putExtra("Longitude",latlng.longitude);
        mapIntent.putExtra("Locality",locality);
        startActivity(mapIntent);


    }
    //AsyncResponse interface override for issue retrieving LatLng object
    @Override
    public void issue() {

        System.out.println("Problem finding your location");

    }

    //Not needed for this class
    @Override
    public void openDialog(Address address) {}

    //Not needed for this class
    @Override
    public void sendToFirebase(Address address) {}

    //Async Tasks the locality search because of possible frame skips
    public class AsyncSearch extends AsyncTask<String,Void,Void>{


        public AsyncResponse asyncResp = null;

        @Override
        protected Void doInBackground(String ... strings) {
            Geocoder geocoder = new Geocoder(getApplicationContext());
            try {
                List<Address> addresses = geocoder.getFromLocationName(strings[0],NUM_ADDRESSES);

                /*If the address does exist, call onPostExecute(LatLng) and send data back to MainActivity
                Otherwise call another onPostExecute to say that address doesn't exist
                */
                if(addresses.size() > 0){
                    LatLng latLng = new LatLng(addresses.get(0).getLatitude(),addresses.get(0).getLongitude());
                    onPostExecute(latLng);
                }
                else{
                    onPostExecute();
                }

            }catch(IOException io){
                io.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(LatLng latLng) {
            sendLatlng(latLng);

        }


        //Declares an issue
        protected void onPostExecute(){
            issue();
        }

    }

}
