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
}
