package com.codepath.gridimagesearch.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import com.codepath.gridimagesearch.R;

/**
 * Created by shehba.shahab on 9/26/15.
 */
public class AdvancedSearchActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced_search);
    }

    public void backToGrid(View view) {
        Spinner sizeSpinner = (Spinner) findViewById(R.id.sizeSpinner);
        Spinner colorSpinner = (Spinner) findViewById(R.id.colorSpinner);
        Spinner imageTypeSpinner = (Spinner) findViewById(R.id.imageTypeSpinner);
        EditText etSiteFilter = (EditText) findViewById(R.id.etSiteFilter);

        String valueSizeSpinner = sizeSpinner.getSelectedItem().toString();
        String valueColorSpinner = colorSpinner.getSelectedItem().toString();
        String valueImageTypeSpinner = imageTypeSpinner.getSelectedItem().toString();
        String valueSiteText = etSiteFilter.getText().toString();

        Intent intent = new Intent();
        intent.putExtra("sizeValue", valueSizeSpinner);
        intent.putExtra("colorValue", valueColorSpinner);
        intent.putExtra("imageTypeValue", valueImageTypeSpinner);
        intent.putExtra("siteValue", valueSiteText);

        setResult(RESULT_OK, intent);
        finish();
    }
}
