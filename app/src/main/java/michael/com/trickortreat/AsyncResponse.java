package michael.com.trickortreat;

import com.google.android.gms.maps.model.LatLng;

//Interface for getting the response from the asynctask
public interface AsyncResponse {

    void finish(LatLng latLng); //If successful
    void issue(); //If there's an issue
}
