package michael.com.trickortreat;


import android.content.Context;
import android.location.Address;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.GridLayoutManager;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


//DialogFragment for displaying the reviews for a specific address

public class ShowEvents extends DialogFragment {
    private String address;
    private String textAddress;
    ArrayList<Review> reviews;
    AdapterClass adapter;

    public ShowEvents(){



    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_show_events,container);

        RecyclerView recyclerView = rootView.findViewById(R.id.recycler);
        reviews =new ArrayList<>();

        //Links reviews arraylist to adapter and uses that adapter for the recyclerview
        adapter = new AdapterClass(getContext(),reviews);
        recyclerView.setAdapter(adapter);
        //Uses a gridlayout
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return rootView;
    }


    public void onAttach(Context context){
        super.onAttach(getContext());

        Bundle bundle = getArguments();
        //Retrieves address from MapActivity
        address = bundle.getString("TextAddress");

        textAddress = bundle.getString("TextAddress");

        DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference();

        //Searches for all reviews from selected marker
        dataRef.orderByChild("address").equalTo(address).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Review review = dataSnapshot.getValue(Review.class);
                review.setTextAddress(textAddress);

                //If a review found, add it to the arraylist and notifies the adapter of the change
                reviews.add(review);
                adapter.notifyDataSetChanged();



            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }




}
