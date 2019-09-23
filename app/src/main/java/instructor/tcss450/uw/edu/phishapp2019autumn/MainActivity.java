package instructor.tcss450.uw.edu.phishapp2019autumn;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import instructor.tcss450.uw.edu.phishapp.model.Credentials;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements
    LoginFragment.OnFragmentInteractionListener,
    RegisterFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState == null) {
            if (findViewById(R.id.frame_main_fragment_container) != null) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.frame_main_fragment_container, new LoginFragment())
                        .commit();
            }
        }
    }

    @Override
    public void onLoginSuccess(Credentials theCredentials, String theJwt) {
        SuccessFragment fragment = new SuccessFragment();
        Bundle args = new Bundle();
        args.putSerializable(getString(R.string.keys_intent_credentials), theCredentials);
        fragment.setArguments(args);
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_main_fragment_container, fragment)
                .addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onRegisterClicked() {
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_main_fragment_container, new RegisterFragment())
                .addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onRegisterSuccess(Credentials theCredentials) {
        SuccessFragment fragment = new SuccessFragment();
        Bundle args = new Bundle();
        args.putSerializable(getString(R.string.keys_intent_credentials), theCredentials);
        fragment.setArguments(args);
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_main_fragment_container, fragment)
                .addToBackStack(null);
        transaction.commit();
    }
}
