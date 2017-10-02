package michael.com.trickortreat;

import android.app.DialogFragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


public class CreateEvent extends DialogFragment {


    public CreateEvent() {
        // Required empty public constructor


    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Bundle bundle = getArguments();
        String address = bundle.getString("Address");

        Toast.makeText(getActivity(),"The address here is " + address,Toast.LENGTH_LONG).show();


    }
}
