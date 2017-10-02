package michael.com.trickortreat;

import android.location.Address;

import com.google.android.gms.maps.model.LatLng;

//Interface for getting the response from the asynctask
public interface AsyncResponse {

    void sendLatlng(LatLng latLng); //If successful send latlng to main thread
    void getAddress(Address address); //Sends closest location from map click to main thread
    void isAddress(boolean isAddress); //Checks if an actual address
    void issue(); //If there's an issue
}
