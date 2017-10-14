package michael.com.trickortreat;


import android.content.Context;
import android.location.Address;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


//DialogFragment for displaying the reviews for a specific address

public class ShowEvents extends DialogFragment {
    private String address;

    public ShowEvents(){
        Bundle bundle = getArguments();
        this.address = bundle.getString("Address");


    }

    //
    public void onAttach(Context context){
        super.onAttach(getContext());


        DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference();
        dataRef.orderByChild("address").equalTo(address).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

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
