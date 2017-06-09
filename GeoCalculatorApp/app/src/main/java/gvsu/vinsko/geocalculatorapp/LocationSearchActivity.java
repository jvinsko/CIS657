package gvsu.vinsko.geocalculatorapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.borax12.materialdaterangepicker.date.DatePickerDialog;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;

import org.joda.time.DateTime;
import org.parceler.Parcels;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LocationSearchActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    int PLACE_START_AUTOCOMPLETE_REQUEST_CODE = 1;
    int PLACE_END_AUTOCOMPLETE_REQUEST_CODE = 2;

    @BindView(R.id.startLoc) TextView startLoc;
    @BindView(R.id.endLoc) TextView endLoc;
    @BindView(R.id.calcDate) TextView calcDate;

    private DateTime calcDateTime;
    private DatePickerDialog dpDialog;
    private Place plStart, plEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_search);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DateTime today = DateTime.now();
        dpDialog = DatePickerDialog.newInstance(this,
                today.getYear(), today.getMonthOfYear() - 1, today.getDayOfMonth());

        calcDate.setText(formatted(today));
    }

    @OnClick(R.id.startLoc)
    public void startLocPressed() {
        try {
            Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                            .build(this);
            startActivityForResult(intent, PLACE_START_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.endLoc)
    public void endLocPressed() {
        try {
            Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                            .build(this);
            startActivityForResult(intent, PLACE_END_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.calcDate)
    public void datePressed() {
        dpDialog.show(getFragmentManager(), "daterangedialog");
    }

    @OnClick(R.id.lsaFab)
    public void FABPressed() {
        Intent result = new Intent();
        LocationLookup ll = new LocationLookup();
        ll.origLat = plStart.getLatLng().latitude;
        ll.origLong = plStart.getLatLng().longitude;
        ll.endLat = plEnd.getLatLng().latitude;
        ll.endLong = plEnd.getLatLng().longitude;
        Parcelable parcel = Parcels.wrap(ll);
        result.putExtra("LL", parcel);
        setResult(MainActivity.SEARCH_RESULT,result);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PLACE_START_AUTOCOMPLETE_REQUEST_CODE) {
            if(resultCode == RESULT_OK) {
                plStart = PlaceAutocomplete.getPlace(this, data);
                startLoc.setText(plStart.getAddress());
            }
        } else if(requestCode == PLACE_END_AUTOCOMPLETE_REQUEST_CODE) {
            if(resultCode == RESULT_OK) {
                plEnd = PlaceAutocomplete.getPlace(this, data);
                endLoc.setText(plEnd.getAddress());
            }
        } else
            super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth, int yearEnd, int monthOfYearEnd, int dayOfMonthEnd) {
        calcDateTime = new DateTime(year, monthOfYear + 1, dayOfMonth, 0, 0);
        calcDate.setText(formatted(calcDateTime));
    }

    private String formatted(DateTime d) {
        return d.monthOfYear().getAsShortText(Locale.getDefault()) + " " + d.getDayOfMonth() + ", " + d.getYear();
    }
}
