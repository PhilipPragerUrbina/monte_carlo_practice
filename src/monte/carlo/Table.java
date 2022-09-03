package monte.carlo;

import java.util.ArrayList;

//represent a data table
public class Table<X,Y> {
    //x and y data
    private ArrayList<X> data_x;
    private ArrayList<Y> data_y;
    public Table(){
        data_x = new ArrayList<>();
        data_y = new ArrayList<>();
    }

    //add data
    public void addDataPoint(X x, Y y){
        data_y.add(y);
        data_x.add(x);
    }

    //output table
    public void output(){
        for (int i = 0; i < data_x.size(); i++) {
            System.out.println(data_x.get(i) + " , " + data_y.get(i) );
        }
    }

    public void outputCoords(){
        for (int i = 0; i < data_x.size(); i++) {
            System.out.println("(" + data_x.get(i) + ',' + data_y.get(i) + ')');
        }
    }

    @Override
    public String toString() {
        String s = "";
        for (int i = 0; i < data_x.size(); i++) {
            s += (data_x.get(i) + " , " + data_y.get(i) + "\n");
        }
        return s;
    }

    //create a plot. Specify to lambda functions with specified template input types, that return a single double value used for plotting
    //example usage with int and double: a.getTable().outputPlot((Integer x)-> x,(Double y) -> y ,1);
    //multiplier make the graph bigger
    public void outputPlot(getNumericalValue<X> x_numerical_value, getNumericalValue<Y> y_numerical_value, int multiplier){
        String filename = data_x.get(0).getClass().getName() + "_" + data_y.get(0).getClass().getName() + ".PNG";
        //find max x and y values for domain and range
        int width = 0;
        int height = 0;
        for (int i = 0; i < data_x.size(); i++) {
            //round up to ints
            int x = (int)x_numerical_value.getValue(data_x.get(i))+1;
            int y = (int)y_numerical_value.getValue(data_y.get(i))+1;
            if(x > width) {width = x;}
            if(y > height){height = y;}
        }


        Graph graph = new Graph(width * multiplier,height * multiplier);
        for (int i = 0; i < data_x.size(); i++) {
            graph.plot(x_numerical_value.getValue(data_x.get(i))*multiplier,y_numerical_value.getValue(data_y.get(i))*multiplier );

        }
        graph.savePNG(filename);

    }
}
