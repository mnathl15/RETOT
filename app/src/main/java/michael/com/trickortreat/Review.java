package michael.com.trickortreat;

//Model class for a submitted review
public class Review {

    private float stars;
    private String comments,address;

    public Review(float stars,String comments,String address){
        this.stars = stars;
        this.comments = comments;
        this.address = address;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
