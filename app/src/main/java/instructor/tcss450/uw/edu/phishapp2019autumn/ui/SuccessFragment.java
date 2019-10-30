package instructor.tcss450.uw.edu.phishapp2019autumn.ui;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import instructor.tcss450.uw.edu.phishapp.model.Credentials;
import instructor.tcss450.uw.edu.phishapp2019autumn.HomeActivityArgs;
import instructor.tcss450.uw.edu.phishapp2019autumn.R;


public class SuccessFragment extends Fragment {


    private Credentials mCredentials;

    public SuccessFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_success, container, false);

        return layout;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        HomeActivityArgs args = HomeActivityArgs.fromBundle(getArguments());
        Credentials credentials = args.getCredentials();
        ((TextView) getActivity().findViewById(R.id.text_display_email)).
                setText(credentials.getEmail());
        String jwt = args.getJwt();
        Log.d("JWT", jwt);
    }

}
