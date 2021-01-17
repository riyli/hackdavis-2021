import java.io.FileReader;
import java.io.BufferedReader;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.lang.Math;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.awt.Color;

public class Consumption {

    final double avg = 156.23;
    // this is derived from the average american emitting 16.2 tons of co2 annually, and since we 
    // want the daily consumption we do 16.2 tons / 365 days * 16 oz * 2000 lbs
    // so we get 1420.3 oz per day, but since only 11% of carbon emissions are from trash
    // this is 1420.3 * .11 = 156.23
    
    double consumption;
    List<String[]> data;

    public Consumption() {
        this.consumption = 0.0;
        this.data = csvToArray("carbon.csv");
    }

    public double get() {
        return this.consumption;
    }

    public double add(String item) {

        for (int i = 0; i < this.data.size(); i++) {
            if (this.data.get(i)[0] == item) {
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

    public Color color() {

        if (this.consumption == 0) {
            return new Color(255, 255, 255);
        }

        int shade;

        // int shade = 255 - (int)(this.progress() * 255);

        // if (this.progress() > 0) { return new Color(255, shade, shade); }
        // else if (this.progress() < 0) { return new Color(shade, 255, shade); }

        if (this.progress() > 0.5) { 
            shade = 255 - (int)((this.progress() - 0.5) * 255 * 2);
            return new Color(255, shade, shade);
        }

        else if (this.progress() < 0.5) {
            shade = 255 - (int)((0.5 - this.progress()) * 255 * 2);
            return new Color(shade, 255, shade);
        }

        return new Color(255, 255, 255);
    }

    public static List<String[]> csvToArray(String filename) {

        List<String[]> data = new ArrayList<String[]>();
        String testRow;

        try {

            // Open and read the file
            BufferedReader br = new BufferedReader(new FileReader(filename));

            // Read data as long as it's not empty
            // Parse the data by comma using .split() method
            // Place into a temporary array, then add to List 
            while ((testRow = br.readLine()) != null) {
                String[] line = testRow.split(",");
                data.add(line);
            }

            br.close();

        } catch (FileNotFoundException e) {
            System.out.println("ERROR: File not found " + filename);

        } catch (IOException e) {
            System.out.println("ERROR: Could not read " + filename);
        }

        return data;
    }

}