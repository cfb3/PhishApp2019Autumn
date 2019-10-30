package instructor.tcss450.uw.edu.phishapp2019autumn.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import instructor.tcss450.uw.edu.phishapp.model.Credentials;
import instructor.tcss450.uw.edu.phishapp2019autumn.R;
import instructor.tcss450.uw.edu.phishapp2019autumn.utils.SendPostAsyncTask;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;


/**

 */
public class LoginFragment extends Fragment {

    private Credentials mCredentials;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);



        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.button_login_signin)
                .setOnClickListener(this::attemptLogin);


        view.findViewById(R.id.button_login_register)
                .setOnClickListener(b -> {
                    NavController nc = Navigation.findNavController(getView());

                    if (nc.getCurrentDestination().getId() != R.id.loginFragment) {
                        nc.navigateUp();
                    }
                    nc.navigate(R.id.action_loginFragment_to_registerFragment);
                });

    }

    @Override
    public void onStart() {
        super.onStart();

        //TODO Added

        SharedPreferences prefs =
                getActivity().getSharedPreferences(
                        getString(R.string.keys_shared_prefs),
                        Context.MODE_PRIVATE);
        //retrieve the stored credentials from SharedPrefs
        if (prefs.contains(getString(R.string.keys_prefs_email)) &&
                prefs.contains(getString(R.string.keys_prefs_password))) {

            final String email = prefs.getString(getString(R.string.keys_prefs_email), "");
            final String password = prefs.getString(getString(R.string.keys_prefs_password), "");
            //Load the two login EditTexts with the credentials found in SharedPrefs
            EditText emailEdit = getActivity().findViewById(R.id.edit_login_email);
            emailEdit.setText(email);
            EditText passwordEdit = getActivity().findViewById(R.id.edit_login_password);
            passwordEdit.setText(password);

            doLogin(new Credentials.Builder(
                    emailEdit.getText().toString(),
                    passwordEdit.getText().toString())
                    .build());

        }
    }

    private void doLogin(Credentials credentials) {
        //build the web service URL
        Uri uri = new Uri.Builder()
                .scheme("https")
                .appendPath(getString(R.string.ep_base_url))
                .appendPath(getString(R.string.ep_login))
                .build();

        //build the JSONObject
        JSONObject msg = credentials.asJSONObject();

        mCredentials = credentials;

        Log.d("JSON Credentials", msg.toString());

        //instantiate and execute the AsyncTask.
        //Feel free to add a handler for onPreExecution so that a progress bar
        //is displayed or maybe disable buttons.
        new SendPostAsyncTask.Builder(uri.toString(), msg)
                .onPreExecute(this::handleLoginOnPre)
                .onPostExecute(this::handleLoginOnPost)
                .onCancelled(this::handleErrorsInTask)
                .build().execute();
    }



    private void saveCredentials(final Credentials credentials) {
        SharedPreferences prefs =
                getActivity().getSharedPreferences(
                        getString(R.string.keys_shared_prefs),
                        Context.MODE_PRIVATE);
        //Store the credentials in SharedPrefs
        prefs.edit().putString(getString(R.string.keys_prefs_email), credentials.getEmail()).apply();
        prefs.edit().putString(getString(R.string.keys_prefs_password), credentials.getPassword()).apply();
    }


    /**
     *
     * @param theButton
     */
    private void attemptLogin(final View theButton) {

        EditText emailEdit = getActivity().findViewById(R.id.edit_login_email);
        EditText passwordEdit = getActivity().findViewById(R.id.edit_login_password);

        boolean hasError = false;
        if (emailEdit.getText().length() == 0) {
            hasError = true;
            emailEdit.setError("Field must not be empty.");
        }  else if (emailEdit.getText().toString().chars().filter(ch -> ch == '@').count() != 1) {
            hasError = true;
            emailEdit.setError("Field must contain a valid email address.");
        }
        if (passwordEdit.getText().length() == 0) {
            hasError = true;
            passwordEdit.setError("Field must not be empty.");
        }

        if (!hasError) {

//            Bundle args = new Bundle();
//            args.putSerializable(getString(R.string.keys_intent_credentials),
//                    new Credentials.Builder(
//                    emailEdit.getText().toString(),
//                    passwordEdit.getText().toString())
//                    .build());
//
//            NavController nc = Navigation.findNavController(getView());
////            if (nc.getCurrentDestination().getId() != R.id.loginFragment) {
////                nc.navigateUp();
////            }
//
//            nc.navigate(R.id.action_loginFragment_to_homeActivity, args);
//
//            LoginFragmentDirections.ActionLoginFragmentToHomeActivity homeActivity =
//                    LoginFragmentDirections.actionLoginFragmentToHomeActivity(new Credentials.Builder(
//                            emailEdit.getText().toString(),
//                            passwordEdit.getText().toString())
//                            .build());
//            Navigation.findNavController(getView()).navigate(homeActivity);

//            Credentials credentials = new Credentials.Builder(
//                    emailEdit.getText().toString(),
//                    passwordEdit.getText().toString())
//                    .build();
//
//            //build the web service URL
//            Uri uri = new Uri.Builder()
//                    .scheme("https")
//                    .appendPath(getString(R.string.ep_base_url))
//                    .appendPath(getString(R.string.ep_login))
//                    .build();
//
//            //build the JSONObject
//            JSONObject msg = credentials.asJSONObject();
//
//            mCredentials = credentials;
//
//            //instantiate and execute the AsyncTask.
//            new SendPostAsyncTask.Builder(uri.toString(), msg)
//                    .onPreExecute(this::handleLoginOnPre)
//                    .onPostExecute(this::handleLoginOnPost)
//                    .onCancelled(this::handleErrorsInTask)
//                    .build().execute();

            doLogin(new Credentials.Builder(
                    emailEdit.getText().toString(),
                    passwordEdit.getText().toString())
                    .build());

        }
    }

    /**
     * Handle onPostExecute of the AsyncTask. The result from our webservice is
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
                saveCredentials(mCredentials);

                //Login was successful. Switch to the SuccessFragment.
                LoginFragmentDirections.ActionLoginFragmentToHomeActivity homeActivity =
                        LoginFragmentDirections.actionLoginFragmentToHomeActivity(mCredentials);
                homeActivity.setJwt(resultsJSON.getString(getString(R.string.keys_json_login_jwt)));
                Navigation.findNavController(getView()).navigate(homeActivity);
                getActivity().finish();
                return;
            } else {
                //Login was unsuccessful. Don’t switch fragments and
                // inform the user
                ((TextView) getView().findViewById(R.id.edit_login_email))
                        .setError("Login Unsuccessful");
            }
            getActivity().findViewById(R.id.layout_login_wait).setVisibility(View.GONE);
        } catch (JSONException e) {
            //It appears that the web service did not return a JSON formatted
            //String or it did not have what we expected in it.
            Log.e("JSON_PARSE_ERROR",  result
                    + System.lineSeparator()
                    + e.getMessage());

            getActivity().findViewById(R.id.layout_login_wait).setVisibility(View.GONE);
            ((TextView) getView().findViewById(R.id.edit_login_email))
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
        getActivity().findViewById(R.id.layout_login_wait).setVisibility(View.VISIBLE);
    }



}
