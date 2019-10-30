package instructor.tcss450.uw.edu.phishapp2019autumn.ui;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import instructor.tcss450.uw.edu.phishapp.model.Credentials;
import instructor.tcss450.uw.edu.phishapp2019autumn.R;
import instructor.tcss450.uw.edu.phishapp2019autumn.utils.SendPostAsyncTask;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;


public class RegisterFragment extends Fragment {

    private Credentials mCredentials;


    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.button_register_signin)
                .setOnClickListener(this::attemptRegister);
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

            mCredentials = new Credentials.Builder(
                    emailEdit.getText().toString(),
                    password1Edit.getText().toString())
                    .build();

            //build the web service URL
            Uri uri = new Uri.Builder()
                    .scheme("https")
                    .appendPath(getString(R.string.ep_base_url))
                    .appendPath(getString(R.string.ep_register))
                    .build();

            //build the JSONObject
            JSONObject msg = mCredentials.asJSONObject();

            //instantiate and execute the AsyncTask.
            new SendPostAsyncTask.Builder(uri.toString(), msg)
                    .onPreExecute(this::handleLoginOnPre)
                    .onPostExecute(this::handleLoginOnPost)
                    .onCancelled(this::handleErrorsInTask)
                    .build().execute();
        }
    }

    /**
     * Handle onPostExecute of the AsynceTask. The result from our webservice is
     * a JSON formatted String. Parse it for success or failure.
     * @param result the JSON formatted String response from the web service
     */
    private void handleLoginOnPost(String result) {
        try {
            JSONObject resultsJSON = new JSONObject(result);
            boolean success =
                    resultsJSON.getBoolean(
                            getString(R.string.keys_json_login_success));

            if (success) {
                //Login was successful. Switch to the loadSuccessFragment.
                RegisterFragmentDirections.ActionRegisterFragmentToHomeActivity homeActivity =
                        RegisterFragmentDirections.actionRegisterFragmentToHomeActivity(mCredentials);
                homeActivity.setJwt(resultsJSON.getString(getString(R.string.keys_json_login_jwt)));
                Navigation.findNavController(getView()).navigate(homeActivity);
                getActivity().finish();
                return;
            } else {
                //Login was unsuccessful. Don’t switch fragments and
                // inform the user
                ((TextView) getView().findViewById(R.id.edit_register_email))
                        .setError("Registration Unsuccessful");
            }
            getActivity().findViewById(R.id.layout_register_wait).setVisibility(View.GONE);
        } catch (JSONException e) {
            //It appears that the web service did not return a JSON formatted
            //String or it did not have what we expected in it.
            Log.e("JSON_PARSE_ERROR",  result
                    + System.lineSeparator()
                    + e.getMessage());

            getActivity().findViewById(R.id.layout_register_wait).setVisibility(View.GONE);
            ((TextView) getView().findViewById(R.id.edit_register_email))
                    .setError("Login Unsuccessful");
        }
    }

    /**
     * Handle errors that may occur during the AsyncTask.
     * @param result the error message provide from the AsyncTask
     */
    private void handleErrorsInTask(String result) {
        Log.e("ASYNC_TASK_ERROR",  result);
    }

    /**
     * Handle the setup of the UI before the HTTP call to the webservice.
     */
    private void handleLoginOnPre() {
        getActivity().findViewById(R.id.layout_register_wait).setVisibility(View.VISIBLE);
    }

}
