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
    private int street_length;
    private int num_simulations;
    private double p;

    public TrafficModel(int max_v, int num_cars, int street_length, int num_simulations, double p) {
        this.max_v = max_v;
        this.num_cars = num_cars;
        this.street_length = street_length;
        this.num_simulations = num_simulations;
        this.p = p;
        if(num_cars >= street_length){
            System.err.println("k < M!");
            return;
        }
        //initialize cars
        cars_now = new Car[num_cars];
        cars_after = new Car[num_cars];
        for (int i = 0; i < num_cars; i++) {
            cars_now[i] = new Car(0, i, i); //make a line of cars
        }

    }



    public void step(){


        for (int i = 0; i < num_cars; i++) {
            Car this_car = cars_now[i];
            Car next_car = getCarinFront(this_car);
/*
            if(this_car.v == 0){
                System.err.println(this_car);
            }*/

            Car new_car = new Car(this_car.v,this_car.cell,this_car.id); //create copy of car to apply changes to

            //step 1
            if(new_car.v < max_v){
                new_car.v++;
            }
            //step 2
            if(new_car.v >= Math.abs(next_car.cell - this_car.cell) ){
                new_car.v = Math.abs(next_car.cell - this_car.cell)-1;
            }
            //step 3
            if(Math.random() < p && new_car.v > 0){
                new_car.v--;
            }


           //step 4
            moveCar(new_car);
            cars_after[i] = new_car;

        }
        cars_now = cars_after;

    }

    //get id of car closest to start of road for displaying the street
    private int getCarClosestToStart(){
        int id = 0;
        int earliest_cell = street_length;
        for (Car car :
                cars_now) {
            if(car.cell < earliest_cell){
                earliest_cell =car.cell;
                id = car.id;
            }
        }
        return id;
    }

    public void graph(int num){
        Graph g = new Graph(new Vector2(0), new Vector2(street_length, num), new Vector2(1));
        g.setColor(250,250,250);
        for (int i = 0; i < num; i++) {
            for (Car a : cars_now) {
                g.pixel( a.cell, i);
            }
            step();
        }
        g.savePNG("test.PNG");
    }

    public void print(){
        //start pos
        Car last = new Car(0,0,0);
        int start = getCarClosestToStart();
        for (int i = start; i < num_cars+start; i++) {
            //loop id
            int id = i;
            if(id >= num_cars){
                id = id - num_cars;
            }
            Car current = cars_now[id];
            //output road
            for (int j = last.cell +1 ; j < current.cell; j++) {
                System.out.print("-");
            }
            //output current car
            System.out.print("C");

            last =current;

        }
        //end
        for (int j = last.cell+1 ; j < street_length; j++) {
            System.out.print("-");
        }

        System.out.println();
    }


    private Car getCarinFront(Car car){
        int new_id = (car.id + 1)%num_cars;
        return cars_now[new_id];
    }

    private void moveCar(Car car){
        int next_cell = car.cell + car.v; //get next pos
        next_cell = next_cell % street_length;
        car.cell = next_cell;
    }

}
