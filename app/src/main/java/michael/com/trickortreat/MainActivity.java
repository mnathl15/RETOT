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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.List;
import java.util.Map;

//1. CANCELLATION OF ASYNC THREAD
public class MainActivity extends AppCompatActivity{

    private static final int NUM_ADDRESSES = 2;
    AsyncSearch async;
    EditText search;
    TextView error;
    ProgressBar loading;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        async = new AsyncSearch(); //Declares asynctask class object





        final Button start = (Button)findViewById(R.id.start);
        search = (EditText)findViewById(R.id.locality_search);
        error = (TextView)findViewById(R.id.error);
        loading = (ProgressBar)findViewById(R.id.loading);
        loading.setVisibility(View.INVISIBLE);



        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String locality = search.getText().toString().toLowerCase();
                loading.setVisibility(View.VISIBLE); //Makes loading animation visible
                AsyncSearch async = new AsyncSearch();
                async.execute(locality);

                //1
            }
        });
    }


    //Sends LatLng to MapActivity from asyncTask

    public void sendLatlng(LatLng latlng) {
        String locality = search.getText().toString().toLowerCase();
        Intent mapIntent = new Intent(MainActivity.this,MapsActivity.class);
        mapIntent.putExtra("Latitude", latlng.latitude); //Sends the latitude and longitude to the next intent
        mapIntent.putExtra("Longitude",latlng.longitude);
        mapIntent.putExtra("Locality",locality);
        startActivity(mapIntent);
    }

    //Async Tasks the locality search because of possible frame skips
    public class AsyncSearch extends AsyncTask<String,String,String>{



        @Override
        protected String doInBackground(String ... strings) {
            Geocoder geocoder = new Geocoder(getApplicationContext());
            try {
                List<Address> addresses = geocoder.getFromLocationName(strings[0],NUM_ADDRESSES);

                /*If the address does exist, call onPostExecute(LatLng) and send data back to MainActivity
                Otherwise call another onPostExecute to say that address doesn't exist
                */
                if(addresses.size() > 0){
                    LatLng latLng = new LatLng(addresses.get(0).getLatitude(),addresses.get(0).getLongitude());
                    end(latLng);
                    //No error message displayed
                    return("");
                }
                else{
                    //Error message displayed
                    return("There was an error");
                }




            }catch(IOException io){
                io.printStackTrace();
            }
            return null;
        }

        protected void end(LatLng latLng) {
            sendLatlng(latLng);

        }


        //Declares an issue
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            loading.setVisibility(View.INVISIBLE);
            if(!result.equals("")){
                error.setText("There was an error");
            }

        }

    }

}
