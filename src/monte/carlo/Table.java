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
    public void outputPlot(String independent_label, String dependent_label, int multiplier){
        //create filename
        String filename = independent_label + "_" + dependent_label + ".PNG";
        //get data
        ArrayList<Data> x_data = data[getIDFromLabel(independent_label)];
        ArrayList<Data> y_data = data[getIDFromLabel(dependent_label)];

        //find max x and y values for domain and range
        //todo allow negative values
        int width = 0;
        int height = 0;
        for (int i = 0; i < length; i++) {
            //round up to ints
            int x = (int)x_data.get(i).toNumericalValue()+1;
            int y =  (int)y_data.get(i).toNumericalValue()+1;
            if(x > width) {width = x;}
            if(y > height){height = y;}
        }
        Graph graph = new Graph(width * multiplier,height * multiplier);
        for (int i = 0; i < length; i++) {
            graph.plot(x_data.get(i).toNumericalValue()*multiplier,y_data.get(i).toNumericalValue()*multiplier );

        }
        graph.savePNG(filename);
    }
}
