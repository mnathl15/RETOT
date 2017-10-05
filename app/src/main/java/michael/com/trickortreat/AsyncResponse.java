package michael.com.trickortreat;

import android.location.Address;

import com.google.android.gms.maps.model.LatLng;

//Interface for getting the response from the asynctask
public interface AsyncResponse {

    void sendLatlng(LatLng latLng); //If successful send latlng to main thread
    void openDialog(Address address); //Sends closest location from map click to main thread
    void sendToFirebase(Address address); //Checks if an actual address, boolean as param to make sure address exists
    void issue(); //If there's an issue
}
