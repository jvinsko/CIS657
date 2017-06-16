package gvsu.vinsko.geocalculatorapp;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import org.parceler.Parcels;

public class HistoryActivity extends AppCompatActivity implements HistoryFragment.OnListFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public void onListFragmentInteraction(LocationLookup item) {
        System.out.println("Interact!");
        Intent intent = new Intent();
        Parcelable parcel = Parcels.wrap(item);
        intent.putExtra("LL", parcel);
        setResult(MainActivity.HISTORY_RESULT,intent);
        finish();
    }
}
