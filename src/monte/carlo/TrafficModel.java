package monte.carlo;
//Nagel-Schrekenberg traffic model
public class TrafficModel {
    //struct to contain car data
    class Car{
        int v;
        int cell;
        int id;
        public Car(int v, int cell, int id){
            this.v = v;
            this.cell = cell;
            this.id = id;
        }

        @Override
        public String toString() {
            return "Car{" +
                    "v=" + v +
                    ", cell=" + cell +
                    ", id=" + id +
                    '}';
        }
    }
    private Car[] cars_now;
    private Car[] cars_after;

    //settings
    private int max_v;
    private  int num_cars;
    private int num_cells;
    private double p;

    public TrafficModel(int max_v, int num_cars, int street_length, double p, boolean even_space) {
        this.max_v = max_v;
        this.num_cars = num_cars;
        this.num_cells = street_length;
        this.p = p;
        if(num_cars >= street_length){
            System.err.println("k < M!");
            return;
        }
        //initialize cars
        cars_now = new Car[num_cars];
        cars_after = new Car[num_cars];



        for (int i = 0; i < num_cars; i++) {
            if(even_space){
                cars_now[i] = new Car(0, i * Math.round(num_cells/num_cars), i); //even spaced cars
            }else{
                cars_now[i] = new Car(0, i , i); //make a line of cars
            }

        }

    }

    // step forward simulation, and return number of steps taken(total car travel distance)
    public int step(){

        int total_steps = 0;
        for (int i = 0; i < num_cars; i++) {
            Car this_car = cars_now[i];
            Car next_car = getCarinFront(this_car);


            Car new_car = new Car(this_car.v,this_car.cell,this_car.id); //create copy of car to apply changes to

            //step 1
            if(new_car.v < max_v){
                new_car.v++;
            }
            //step 2
            if( distance(this_car, next_car) < new_car.v ){
                new_car.v = distance(this_car, next_car)-1;
            }
            //step 3
            if( new_car.v > 0 && Math.random() < p){
                new_car.v--;
                //do this for more noisy result
               // new_car.v = new_car.v - (int)(Math.random() * new_car.v);
            }


           //step 4
            total_steps += moveCar(new_car);
            cars_after[i] = new_car;

        }
        cars_now = cars_after;
        return total_steps;
    }

    private int distance(Car a, Car b){
        return Math.abs((b.cell - a.cell)%num_cells);
    }

    private Car getCarinFront(Car car){
        int new_id = (car.id + 1)%num_cars;
        return cars_now[new_id];
    }
    //move car by its v
    private int moveCar(Car car){
        int next_cell = car.cell + car.v; //get next pos
        next_cell = next_cell % num_cells;
        car.cell = next_cell;

        //return the distance traveled
        return car.v;
    }



    public void graph(int num){
        Graph g = new Graph(new Vector2(0), new Vector2(num_cells, num), new Vector2(1));
        g.setColor(250,250,250);
        for (int i = num; i >= 0; i--) {
            for (Car a : cars_now) {
                g.pixel( a.cell, i);
            }
            step();
        }
        g.savePNG("test.PNG");
    }

    public void print(){


        Car[] street = new Car[num_cells];
        for (Car car : cars_now){
            street[car.cell] = car;
        }
        for (int i = 0; i < num_cells; i++) {
           if(street[i] != null){
               System.out.print("Car");
           }else{
               System.out.print("-");
           }
        }
        System.out.println();
    }




}
