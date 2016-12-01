package android.gns.umass.edu.gnsclient.home;

import android.gns.umass.edu.gnsclient.fragments.HomeFragment;
import android.gns.umass.edu.gnsclient.fragments.GNSClientActionListener;
import android.gns.umass.edu.gnsclient.fragments.LogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.gns.umass.edu.gnsclient.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.InetSocketAddress;

import edu.umass.cs.gnsclient.client.GNSClient;
import edu.umass.cs.gnsclient.client.GNSCommand;
import edu.umass.cs.gnsclient.client.util.GuidEntry;
import edu.umass.cs.gnsclient.client.util.GuidUtils;

public class GNSClientActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GNSClientActionListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private CoordinatorLayout coordinatorLayout;
    private FloatingActionButton fab;
    private TabLayout tabLayout;
    private HomeFragment homeFragment;
    private LogFragment logFragment;

    GNSClient gnsClient = null;
    GuidEntry guidEntry = null;

    private static String ACCOUNT_ALIAS = "admin@gns.name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gnsclient);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_content);

        homeFragment = HomeFragment.newInstance();
        logFragment = LogFragment.newInstance();

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Custom implementation
            }
        });
        fab.hide();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                prepareForShowingTab(position);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_gnsclient, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    void prepareForShowingTab(int position) {
        switch (position) {
            case 0: fab.hide(); break;
            case 1: fab.show(); break;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position) {
                case 0: return homeFragment;
                case 1: return logFragment;
                default: return homeFragment;
            }

        }


        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "GNS Client";
                case 1:
                    return "Log";
            }
            return null;
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        // if id == R.id.nav_camera, do something

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void runClientTest() {
        Snackbar.make(coordinatorLayout, "Running test..", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
        connectGNSClient();
        createGNSAccountGuid();
        writeJson();
        readJson();
    }


    private void connectGNSClient() {
        if (gnsClient != null) {
            InetSocketAddress reconfiguratorAddress = new InetSocketAddress("10.0.0.38", 2178);
            try {
                gnsClient = new GNSClient(reconfiguratorAddress);
            } catch (IOException exception) {
                logFragment.logException(exception);
            }
            logFragment.log("Client connected to GNS!");
        }
    }

    private void createGNSAccountGuid() {
        if (gnsClient == null) {
            logFragment.log("GNS Client not connected, aborting..");
        } else {
            try {
                guidEntry =  GuidUtils.lookupOrCreateAccountGuid(gnsClient, ACCOUNT_ALIAS, "password", true);
            } catch (Exception e) {
                logFragment.logException(e);
            }
        }
    }

    private void writeJson () {
        JSONObject json = null;
        try {
            json = new JSONObject("{\"occupation\":\"busboy\","
                    + "\"friends\":[\"Joe\",\"Sam\",\"Billy\"],"
                    + "\"gibberish\":{\"meiny\":\"bloop\",\"einy\":\"floop\"},"
                    + "\"location\":\"work\",\"name\":\"frank\"}");
            gnsClient.execute(GNSCommand.update(guidEntry, json));
            logFragment.log("Updating :");
            logFragment.log(json.toString(2));
        } catch (Exception e) {
            logFragment.logException(e);
        }
    }

    private void readJson() {
        JSONObject result = null;
        try {
            result = gnsClient.execute(GNSCommand.read(guidEntry))
                    .getResultJSONObject();
            logFragment.log("Reading back: ");
            logFragment.log(result.toString(2));
        } catch (Exception e) {
            logFragment.logException(e);
        }

    }

    @Override
    protected void onDestroy() {
        if (gnsClient != null) {
            gnsClient.close();
        }
        super.onDestroy();
    }
}
