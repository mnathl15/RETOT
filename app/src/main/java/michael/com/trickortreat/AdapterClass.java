package michael.com.trickortreat;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;




public class AdapterClass  extends RecyclerView.Adapter<AdapterClass.ViewHolder> {


    private List<Review> reviews;
    private Context context;


    public AdapterClass(Context context,List<Review> reviews){
        this.context = context;
        this.reviews = reviews;
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();



        LayoutInflater inflater = LayoutInflater.from(context);

        //Inflates the layout list to be used in this class
        View reviewView = inflater.inflate(R.layout.list,parent,false);

        //Creates a viewholder object and from the ViewHolder constructor,
        // uses the inflated view to instantiate UI components
        ViewHolder holder = new ViewHolder(reviewView);

        return holder;


    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        //Gets all the reviews from the reviews arraylist in ShowEvents and sets their texts in the list xml
        Review review = reviews.get(position);
        TextView address = holder.address;
        TextView comments = holder.comments;
        RatingBar stars = holder.stars;

        address.setText("Address: " + review.getAddress());
        comments.setText("Comments : "  + review.getComments());
        stars.setRating(review.getStars());

    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView address,comments;
        public RatingBar stars;

        public ViewHolder(View itemView){


            super(itemView);

            address = itemView.findViewById(R.id.address);
            comments = itemView.findViewById(R.id.address);
            stars = itemView.findViewById(R.id.stars);
        }
    }
}
