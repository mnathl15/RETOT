package michael.com.trickortreat;

import com.google.android.gms.maps.model.LatLng;

//Model class for a submitted review
public class Review {

    private float stars;
    private String comments,address,locality;
    private double latitude,longitude;



    public Review(){
        //No arg constructor
    }

    public Review(float stars,String comments,String address,String locality,double latitude,double longitude){
        this.stars = stars;
        this.comments = comments;
        this.address = address;
        this.locality = locality;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    //Second constructor to show events
    public Review(float stars,String comments, String address){
        this.stars = stars;
        this.comments = comments;
        this.address = address;
    }


    public float getStars() {
        return stars;
    }



    public String getComments() {
        return comments;
    }



    public double getLatitude(){
        return latitude;
    }
    public double getLongitude(){
        return longitude;
    }
    public String getLocality(){return locality;}




    public String getAddress() {
        return address;
    }


}
