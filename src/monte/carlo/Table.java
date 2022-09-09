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

    public void outputPointsCSV(){
        //print labels
        String header = "";
        for (String label : labels) {
           header+= label + ",";
        }
        header = header.substring(0, header.length()-1);
        System.out.println(header);
        //output data
        for (int i = 0; i <length; i++) {
            String out = "";
            for (ArrayList<Data> array : data) {
                out+= array.get(i).toStringValue() + ",";
            }
            out = out.substring(0, out.length()-1);
            System.out.println(out);
        }
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

    //resolution scale make the graph bigger, connect will draw lines between points
    public void outputPlot(String independent_label, String dependent_label,Vector2 resolution_scale, boolean connect){

        //create filename
        String filename = independent_label + "_" + dependent_label + ".PNG";
        //get data
        ArrayList<Data> x_data = data[getIDFromLabel(independent_label)];
        ArrayList<Data> y_data = data[getIDFromLabel(dependent_label)];
        //find max x and y values for domain and range
         Vector2 max = new Vector2(0);
         Vector2 min =new Vector2( Integer.MAX_VALUE);
        for (int i = 0; i < length; i++) {
            Vector2 point = new Vector2(x_data.get(i), y_data.get(i) );
            max = point.getMax(max);
            min = point.getMin(min);
        }

        //plot points
        Graph graph = new Graph(min, max,resolution_scale);
        for (int i = 0; i < length; i++) {
            graph.plot(x_data.get(i).toNumericalValue(),y_data.get(i).toNumericalValue() );
         }


        //connect dots if specified
        if(connect){
            graph.setColor(0,255,0);
            for (int i = 1; i < length; i++) {
                Vector2 point_a = graph.transformVector(new Vector2(x_data.get(i),y_data.get(i)));
                Vector2 point_b = graph.transformVector(new Vector2(x_data.get(i-1),y_data.get(i-1)));
                double slope = (point_b.y-point_a.y)/(point_b.x-point_a.x);
                for(double j = point_b.x+1; j < point_a.x; j++){
                    double new_y = slope * (j - point_a.x)  + point_a.y;
                    graph.pixel(j,new_y);
                }
            }
        }
        //plot axis lines
        graph.setColor(0,0,255);
        graph.drawAxis();
        //save
        graph.savePNG(filename);
    }
}
