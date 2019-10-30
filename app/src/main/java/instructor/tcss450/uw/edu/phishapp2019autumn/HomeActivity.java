package instructor.tcss450.uw.edu.phishapp2019autumn;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;

import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import instructor.tcss450.uw.edu.phishapp2019autumn.model.BlogPost;
import instructor.tcss450.uw.edu.phishapp2019autumn.model.SetList;
import instructor.tcss450.uw.edu.phishapp2019autumn.ui.BlogFragmentDirections;
import instructor.tcss450.uw.edu.phishapp2019autumn.ui.SetListFragmentDirections;
import instructor.tcss450.uw.edu.phishapp2019autumn.utils.GetAsyncTask;

import android.view.Menu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class HomeActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    private String mJwToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_success, R.id.nav_blog, R.id.nav_set)
                .setDrawerLayout(drawer)
                .build();


        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);



        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navController.setGraph(R.navigation.mobile_navigation, getIntent().getExtras());


        HomeActivityArgs args = HomeActivityArgs.fromBundle(getIntent().getExtras());
        mJwToken = args.getJwt();
        navigationView.setNavigationItemSelectedListener(this::onNavigationSelected);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            logout();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void logout() {

        SharedPreferences prefs =
                getSharedPreferences(
                        getString(R.string.keys_shared_prefs),
                        Context.MODE_PRIVATE);
        //remove the saved credentials from StoredPrefs
        prefs.edit().remove(getString(R.string.keys_prefs_password)).apply();
        prefs.edit().remove(getString(R.string.keys_prefs_email)).apply();


        //close the app
//        finishAndRemoveTask();

        //or close this activity and bring back the Login
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
//        End this Activity and remove it from the Activity back stack.
        finish();
    }



    private boolean onNavigationSelected(final MenuItem menuItem) {
        NavController navController =
                Navigation.findNavController(this, R.id.nav_host_fragment);
        switch (menuItem.getItemId()) {
            case R.id.nav_success:
                navController.navigate(R.id.nav_success, getIntent().getExtras());
                break;
            case R.id.nav_blog:
                Uri uri = new Uri.Builder()
                        .scheme("https")
                        .appendPath(getString(R.string.ep_base_url))
                        .appendPath(getString(R.string.ep_phish))
                        .appendPath(getString(R.string.ep_blog))
                        .appendPath(getString(R.string.ep_get))
                        .build();

                new GetAsyncTask.Builder(uri.toString())
                        .onPostExecute(this::handleBlogGetOnPostExecute)
                        .addHeaderField("authorization", mJwToken) //add the JWT as a header
                        .build().execute();
                break;
            case R.id.nav_set:
                uri = new Uri.Builder()
                        .scheme("https")
                        .appendPath(getString(R.string.ep_base_url))
                        .appendPath(getString(R.string.ep_phish))
                        .appendPath(getString(R.string.ep_setlist))
                        .appendPath(getString(R.string.ep_recent))
                        .build();

                new GetAsyncTask.Builder(uri.toString())
                        .onPostExecute(this::handleSetListGetOnPostExecute)
                        .addHeaderField("authorization", mJwToken) //add the JWT as a header
                        .build().execute();
                break;
            default:
                throw new IllegalStateException("Should not be here...");
        }
        //Close the drawer
        ((DrawerLayout) findViewById(R.id.drawer_layout)).closeDrawer(GravityCompat.START);
        return true;
    }

    private void handleBlogGetOnPostExecute(final String result) {
        //parse JSON

        try {
            JSONObject root = new JSONObject(result);
            if (root.has(getString(R.string.keys_json_blogs_response))) {
                JSONObject response = root.getJSONObject(
                        getString(R.string.keys_json_blogs_response));
                if (response.has(getString(R.string.keys_json_blogs_data))) {
                    JSONArray data = response.getJSONArray(
                            getString(R.string.keys_json_blogs_data));

                    BlogPost[] blogs = new BlogPost[data.length()];

                    for(int i = 0; i < data.length(); i++) {
                        JSONObject jsonBlog = data.getJSONObject(i);

                        blogs[i] = new BlogPost.Builder(
                                jsonBlog.getString(
                                        getString(R.string.keys_json_blogs_pubdate)),
                                jsonBlog.getString(
                                        getString(R.string.keys_json_blogs_title)))
                                .addTeaser(jsonBlog.getString(
                                        getString(R.string.keys_json_blogs_teaser)))
                                .addUrl(jsonBlog.getString(
                                        getString(R.string.keys_json_blogs_url)))
                                .build();
                    }

                    MobileNavigationDirections.ActionGlobalNavBlog directions
                            = BlogFragmentDirections.actionGlobalNavBlog(blogs);

                    Navigation.findNavController(this, R.id.nav_host_fragment)
                            .navigate(directions);

                } else {
                    Log.e("ERROR!", "No data array");
                    //notify user
//                    onWaitFragmentInteractionHide();
                }
            } else {
                Log.e("ERROR!", "No response");
                //notify user
//                onWaitFragmentInteractionHide();
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("ERROR!", e.getMessage());
            //notify user
//            onWaitFragmentInteractionHide();
        }
    }

    private void handleSetListGetOnPostExecute(final String result) {
        //parse JSON

        try {
            JSONObject root = new JSONObject(result);
            if (root.has(getString(R.string.keys_json_blogs_response))) {
                JSONObject response = root.getJSONObject(
                        getString(R.string.keys_json_blogs_response));
                if (response.has(getString(R.string.keys_json_blogs_data))) {
                    JSONArray data = response.getJSONArray(
                            getString(R.string.keys_json_blogs_data));

                    SetList[] setLists = new SetList[data.length()];

                    for(int i = 0; i < data.length(); i++) {
                        JSONObject jsonSetList = data.getJSONObject(i);

                        Document doc = Jsoup.parse(jsonSetList.getString("venue"));
                        int size = doc.body().childNodeSize();

                        String text = doc.text();

                        Element a = new Element(jsonSetList.getString("venue"));

                        setLists[i] = (new SetList.Builder(jsonSetList.getString("long_date"),
                                jsonSetList.getString("location"),
                                Jsoup.parse(jsonSetList.getString("venue")).text())
                                .addArtist(jsonSetList.getString("artist"))
                                .addSongList(jsonSetList.getString("setlistdata"))
                                .addUrl(jsonSetList.getString("url"))
                                .addNotes(jsonSetList.getString("setlistnotes"))
                                .addRating(jsonSetList.getDouble("rating"))
                                .build());
                    }

                    MobileNavigationDirections.ActionGlobalSetListFragment directions
                            = SetListFragmentDirections.actionGlobalSetListFragment(setLists);

                    Navigation.findNavController(this, R.id.nav_host_fragment)
                            .navigate(directions);

                } else {
                    Log.e("ERROR!", "No data array");
                    //notify user
//                    onWaitFragmentInteractionHide();
                }
            } else {
                Log.e("ERROR!", "No response");
                //notify user
//                onWaitFragmentInteractionHide();
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("ERROR!", e.getMessage());
            //notify user
//            onWaitFragmentInteractionHide();
        }
    }
}
