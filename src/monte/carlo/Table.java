package monte.carlo;

import java.sql.SQLOutput;
import java.util.ArrayList;

//represent a data table
public class Table {
    //data
    private String[] labels;
    private ArrayList<Data>[] data;
    private int length = 0;
    public Table(String... labels){
        //get labels
        this.labels = labels;
        //create data columns
        data = new ArrayList[labels.length];
        for (int i = 0; i < labels.length; i++) {
            data[i] = new ArrayList<>();
        }
    }

    public int getTableWidth(){
        return labels.length;
    }

    //add data, if too little data, fill in the rest of the values with empty data
    public void addDataPoint(Data... values){
        if(values.length > getTableWidth()){
            System.err.println("Too many values for data table");
            return;
        }
        //add data
        for (int i = 0; i < values.length; i++) {
            data[i].add(values[i]);
        }
        //fill in empty data
        for (int i = values.length; i < getTableWidth(); i++) {
            data[i].add(new EmptyData());
        }
        length++;
    }


    //output table
    public void output(){
        //output labels
        for (String label : labels) {
            System.out.print(label + " , ");
        }
        System.out.print('\n');
        //output data
        for (int i = 0; i <length; i++) {
            for (ArrayList<Data> array : data) {
                System.out.print(array.get(i).toStringValue() + " , ");
            }
            System.out.print('\n');
        }
    }

    //output table nicely
    public void outputFormatted(int target_cell_width){
        //output labels
        for (String label : labels) {
            System.out.print(" | " + label); //use nice dividers
            for (int i = 0; i < target_cell_width-label.length(); i++) {
                System.out.print(' '); //fill in space until target width for even table
            }
        }
        System.out.println(" | ");
        //output data
        for (int i = 0; i <length; i++) {
            for (ArrayList<Data> array : data) {
                System.out.print(" | ");
                System.out.print(array.get(i).toStringValue());
                for (int g = 0; g < target_cell_width-array.get(i).toStringValue().length(); g++) {
                    System.out.print(' ');
                }
            }
            System.out.println(" | ");
        }
    }

    //get id from label
    int getIDFromLabel(String label){
        for (int i = 0; i < labels.length; i++) {
            if(labels[i].equals(label)){
                return i;
            }
        }
        return 0;
    }

    //multiplier make the graph bigger
    public void outputPlot(String independent_label, String dependent_label, double multiplier){

        //create filename
        String filename = independent_label + "_" + dependent_label + ".PNG";
        //get data
        ArrayList<Data> x_data = data[getIDFromLabel(independent_label)];
        ArrayList<Data> y_data = data[getIDFromLabel(dependent_label)];

        //find max x and y values for domain and range
        //todo allow negative values
        double max_x = 0;
        double max_y = 0;
        double min_x = Integer.MAX_VALUE;
        double min_y = Integer.MAX_VALUE;

        for (int i = 0; i < length; i++) {
            //round up to ints
            double x = x_data.get(i).toNumericalValue();
            double y =  y_data.get(i).toNumericalValue();
            if(x > max_x) {max_x = x;}
            max_x = Math.max(x, max_x);
            max_y = Math.max(y, max_y);

            min_x = Math.min(x, min_x);
            min_y = Math.min(y, min_y);
        }

        int width = (int)((max_x - min_x) * multiplier)+2;
        int height = (int)((max_y - min_y) * multiplier)+2;
        Graph graph = new Graph(width ,height);
        for (int i = 0; i < length; i++) {

            graph.plot((x_data.get(i).toNumericalValue() - min_x) * multiplier,(y_data.get(i).toNumericalValue() - min_y)*multiplier );

        }
        graph.savePNG(filename);
    }
}
