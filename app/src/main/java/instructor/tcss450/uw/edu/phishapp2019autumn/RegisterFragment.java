package instructor.tcss450.uw.edu.phishapp2019autumn;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import instructor.tcss450.uw.edu.phishapp.model.Credentials;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RegisterFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class RegisterFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_register, container, false);

        view.findViewById(R.id.button_register_signin)
                .setOnClickListener(this::attemptRegister);

        return view;
    }

    private void attemptRegister(final View theButton) {

        EditText emailEdit = getActivity().findViewById(R.id.edit_register_email);
        EditText password1Edit = getActivity().findViewById(R.id.edit_register_password1);
        EditText password2Edit = getActivity().findViewById(R.id.edit_register_password2);

        boolean hasError = false;
        if (emailEdit.getText().length() == 0) {
            hasError = true;
            emailEdit.setError("Field must not be empty.");
        }  else if (emailEdit.getText().toString().chars().filter(ch -> ch == '@').count() != 1) {
            hasError = true;
            emailEdit.setError("Field must contain a valid email address.");
        }
        if (password1Edit.getText().length() == 0) {
            hasError = true;
            password1Edit.setError("Field must not be empty.");
        } else if (password1Edit.getText().length() < 5) {
            hasError = true;
            password1Edit.setError("Password must be longer than 5 characters.");
        }

        if (password2Edit.getText().length() == 0) {
            hasError = true;
            password2Edit.setError("Field must not be empty.");
        } else if (password2Edit.getText().length() < 5) {
            hasError = true;
            password2Edit.setError("Password must be longer than 5 characters.");
        }

        if (!password1Edit.getText().toString().equals(password2Edit.getText().toString())) {
            hasError = true;
            password1Edit.setError("Passwords must match.");
        }

        if (!hasError) {
            /************************
             * Send credentials to web service then wait for the results.
             */

            //remove when ysiung AsyncTask and web service.
            Credentials credentials = new Credentials.Builder(
                    emailEdit.getText().toString(),
                    password1Edit.getText().toString())
                    .build();

            mListener.onRegisterSuccess(credentials);
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onRegisterSuccess(Credentials theCredentials);
    }
}
