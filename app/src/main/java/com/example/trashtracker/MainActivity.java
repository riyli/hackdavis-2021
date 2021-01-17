package com.example.trashtracker;

import androidx.appcompat.app.AppCompatActivity;


import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
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
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    ArrayList<String[]> data;
    double consumption;
    double avg = 156.23;

    public MainActivity() {
        this.consumption = 0.0;
        this.data = data;
        this.avg = avg;
    }

    HashMap<String, Integer> itemMap = new HashMap<String, Integer>();
    HashMap<String, Double> carbonMap = new HashMap<String, Double>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InputStream is = getResources().openRawResource(R.raw.carbon);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));

        data = new ArrayList<String[]>();

        String line = "";
        try {
            // skip first line
            line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(",");
                data.add(tokens);
                this.carbonMap.put(tokens[0], Double.valueOf(tokens[1]));
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

            add(item);

            ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar4);
            progressBar.setProgress((int) (progress() * 100));

            String hex = String.format("#%02x%02x%02x", color()[0], color()[1], color()[2]);

            progressBar.getProgressDrawable().setColorFilter(null);
            progressBar.getProgressDrawable().setColorFilter(Color.parseColor(hex), android.graphics.PorterDuff.Mode.SRC_IN);

            TextView val = (TextView) findViewById(R.id.textView3);
            String textToSet = "your daily carbon footprint is " + Integer.toString((int) percent()) + "% of the average";
            val.setText(textToSet);

            // show on table

            TableRow row = (TableRow) table.findViewWithTag(item);
            // if row not in table
            if (row == null) {
                row = new TableRow(this);
                row.setTag(item);

                // fill table

                TextView itemTable = new TextView(this);
                itemTable.setTag("itemTable");
                itemTable.setText(item);
                row.addView(itemTable);
                TextView countTable = new TextView(this);
                countTable.setTag("countTable");
                countTable.setText(itemMap.get(item).toString());
                row.addView(countTable);
                TextView carbonItemTable = new TextView(this);
                carbonItemTable.setTag("carbonItemTable");
                DecimalFormat df = new DecimalFormat("###.00");
                carbonItemTable.setText(df.format(carbonMap.get(item)));
                row.addView(carbonItemTable);
                TextView totalTable = new TextView(this);
                totalTable.setTag("totalTable");
                totalTable.setText(df.format(itemMap.get(item) * carbonMap.get(item)).toString());
                row.addView(totalTable);
                table.addView(row);


            }
            else {
                // update rows

                TextView countTable = row.findViewWithTag("countTable");
                countTable.setText(itemMap.get(item).toString());
                TextView totalTable = row.findViewWithTag("totalTable");
                DecimalFormat df = new DecimalFormat("###.00");
                totalTable.setText(df.format(itemMap.get(item) * carbonMap.get(item)).toString());
            }
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

    public double get() {
        return this.consumption;
    }

    public double add(String item) {

        for (int i = 0; i < this.data.size(); i++) {
            if (this.data.get(i)[0].equals(item)) {
                this.consumption += Double.valueOf(this.data.get(i)[1]);
            }
        }

        return this.consumption;
    }

    public double percent() {
        return Math.floor(this.consumption / this.avg * 100);
    }

    public double progress() {

        double progress = this.percent() / 200;

        if (progress <= 1.0) { return progress; }
        else { return 1.0; }
    }

    public int[] color() {

        if (this.consumption == 0) {
            return new int[]{255, 255, 255};
        }

        int shade;

        // int shade = 255 - (int)(this.progress() * 255);

        // if (this.progress() > 0) { return new Color(255, shade, shade); }
        // else if (this.progress() < 0) { return new Color(shade, 255, shade); }

        if (this.progress() > 0.5) {
            shade = 255 - (int) ((this.progress() - 0.5) * 255 * 2);
            return new int[]{255, shade, shade};
        } else if (this.progress() < 0.5) {
            shade = 255 - (int) ((0.5 - this.progress()) * 255 * 2);
            return new int[]{shade, 255, shade};
        }

        return new int[]{255, 255, 255};
    }
}