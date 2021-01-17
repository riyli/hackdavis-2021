package com.example.trashtracker;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    HashMap<String, Integer> itemMap = new HashMap<String, Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InputStream is = getResources().openRawResource(R.raw.carbon);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));

        String line = "";
        try {
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(",");

                System.out.println(tokens[0]);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.items_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    public void enterNode(View view) {
        Log.d("DEBUGGING: ", "Finish button has been clicked");

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);

        String item = (String) spinner.getSelectedItem();

        if (item.compareTo(getResources().getStringArray(R.array.items_array)[0]) != 0) {
            TableLayout table = (TableLayout) findViewById(R.id.table);

            // increment in map
            if (itemMap.get(item) == null) {
                itemMap.put(item, 1);
            }
            else {
                itemMap.put(item, itemMap.get(item) + 1);
            }

            // show on table
            TableRow row = new TableRow(this);
            TextView itemTable = new TextView(this);
            itemTable.setText(item);
            row.addView(itemTable);
            TextView countTable = new TextView(this);
            countTable.setText(itemMap.get(item).toString());
            row.addView(countTable);
            TextView carbonItemTable = new TextView(this);
            carbonItemTable.setText("test");
            row.addView(carbonItemTable);
            TextView totalTable = new TextView(this);
            totalTable.setText("test");
            row.addView(totalTable);
            table.addView(row);
        }

        spinner.setSelection(0);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}