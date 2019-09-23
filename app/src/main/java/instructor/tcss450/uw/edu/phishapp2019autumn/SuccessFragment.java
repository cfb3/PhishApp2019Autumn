package instructor.tcss450.uw.edu.phishapp2019autumn;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import instructor.tcss450.uw.edu.phishapp.model.Credentials;



public class SuccessFragment extends Fragment {


    private Credentials mCredentials;

    public SuccessFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCredentials = (Credentials) getArguments().getSerializable(
                    getString(R.string.keys_intent_credentials));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_success, container, false);

        return layout;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (getArguments() != null) {
            mCredentials = (Credentials) getArguments().getSerializable(
                    getString(R.string.keys_intent_credentials));

            ((TextView) getActivity().findViewById(R.id.text_display_email)).
                    setText(mCredentials.getEmail());
        }

    }


}
