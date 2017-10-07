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


    public float getStars() {
        return stars;
    }

    public void setStars(float stars) {
        this.stars = stars;
    }

    public String getReview() {
        return comments;
    }

    public void setReview(String review) {
        this.comments = comments;
    }

    public double getLatitude(){
        return latitude;
    }
    public double getLongitude(){
        return longitude;
    }


    public String getLocality() {
        return locality;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
