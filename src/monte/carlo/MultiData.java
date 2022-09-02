package monte.carlo;

import java.util.Arrays;
//todo switch to templates
//todo add object labels
//todo add table labels

//represent data points with multiple values
public class MultiData {
    private Object[] data;
    public MultiData(Object... data_list){
        data = new Object[data_list.length];
        for (int i = 0; i < data_list.length; i++) {
            data[i] = data_list[i];
        }
    }
    public Object getData(int index){
        return data[index];
    }

    @Override
    public String toString() {
        String a = "";
        for (Object obj :
                data) {
            a += obj.toString() + " , ";
        }
        return a;
    }
}
