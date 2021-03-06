package com.example.srirang.nira_android;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

/**
 * Created by Srirang on 3/11/2018.
 */

public class AddForm extends AppCompatActivity {

    private EditText alkanity, color, ph, sodium, chloride, potassium, calcium, manganese,
            magnesium, lead, mercury, arsenic, dissolved_oxygen, temperature, time;
    private Button submit;
    private boolean mFieldsHasChanged = false;
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            mFieldsHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addform);

        alkanity = findViewById(R.id.alkanity);
        color = findViewById(R.id.color);
        ph = findViewById(R.id.ph);
        sodium = findViewById(R.id.sodium);
        chloride = findViewById(R.id.chloride);
        potassium = findViewById(R.id.potassium);
        calcium = findViewById(R.id.calcium);
        manganese = findViewById(R.id.manganese);
        magnesium = findViewById(R.id.magnesium);
        lead = findViewById(R.id.lead);
        mercury = findViewById(R.id.mercury);
        arsenic = findViewById(R.id.arsenic);
        dissolved_oxygen = findViewById(R.id.dissolvedoxygen);
        temperature = findViewById(R.id.temperature);
        time = findViewById(R.id.time);

        //Setting up OnTouchListener
        alkanity.setOnTouchListener(mTouchListener);
        color.setOnTouchListener(mTouchListener);
        ph.setOnTouchListener(mTouchListener);
        sodium.setOnTouchListener(mTouchListener);
        chloride.setOnTouchListener(mTouchListener);
        potassium.setOnTouchListener(mTouchListener);
        calcium.setOnTouchListener(mTouchListener);
        manganese.setOnTouchListener(mTouchListener);
        magnesium.setOnTouchListener(mTouchListener);
        lead.setOnTouchListener(mTouchListener);
        mercury.setOnTouchListener(mTouchListener);
        arsenic.setOnTouchListener(mTouchListener);
        dissolved_oxygen.setOnTouchListener(mTouchListener);
        temperature.setOnTouchListener(mTouchListener);
        time.setOnTouchListener(mTouchListener);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getSupportActionBar().setElevation(0f);
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_form, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_save:
                onAddSubmitClicked();
                return true;
            case R.id.home:
                //Show warning because maybe user pressed back button accidently.
                if (!mFieldsHasChanged) {
                    NavUtils.navigateUpFromSameTask(this);
                    return true;
                }

                DialogInterface.OnClickListener discardButtonClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        NavUtils.navigateUpFromSameTask(AddForm.this);
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(this)
                        .setMessage("Discard Changes?")
                        .setPositiveButton("Yes", discardButtonClickListener)
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (dialog != null) {
                                    dialog.dismiss();
                                }
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

    public void onAddSubmitClicked() {
        DMSModal newdms = new DMSModal(Double.parseDouble(sodium.getText().toString()),
                Double.parseDouble(chloride.getText().toString()),
                Double.parseDouble(potassium.getText().toString()),
                Double.parseDouble(calcium.getText().toString()),
                Double.parseDouble(manganese.getText().toString()),
                Double.parseDouble(magnesium.getText().toString()),
                Double.parseDouble(lead.getText().toString()),
                Double.parseDouble(mercury.getText().toString()),
                Double.parseDouble(arsenic.getText().toString())
        );
        WQIModal newwqi = new WQIModal(alkanity.getText().toString(),
                color.getText().toString(),
                Double.parseDouble(ph.getText().toString()),
                newdms, Integer.parseInt(dissolved_oxygen.getText().toString()));


        Date date = new Date();
        //Not using Location API AS OF NOW HARDCODED
        AddModal newpost = new AddModal("35.09 67.98", "kolhapur", "up", newwqi, Double.parseDouble(temperature.getText().toString()),
                date, "img/src/images/public/3332434gfw2", 5);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference db = database.getReference().child("sample");
        db.push().setValue(newpost);
        Toast.makeText(this, "Entry Completed", Toast.LENGTH_LONG).show();
    }
}
