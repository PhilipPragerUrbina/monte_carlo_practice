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
            if(new_car.v > Math.abs(next_car.cell - this_car.cell) ){
                new_car.v = Math.abs(next_car.cell - this_car.cell)-1;
            }
            //step 3
            if(Math.random() < p && new_car.v > 0){
                new_car.v = new_car.v - 1;
            }


           //step 4
            moveCar(new_car);
            cars_after[i] = new_car;

        }
        cars_now = cars_after;

    }

    public void print(){
        //start pos
        //todo make this the first car based on position on steet not on order
        Car last = new Car(0,0,0);

        for (int i = 0; i < num_cars; i++) {
            Car current = cars_now[i];

            //output road
            for (int j = last.cell+1; j < current.cell; j++) {
                System.out.print("-");
            }

            //output current car
            System.out.print("C");

            last =current;

        }
        System.out.println();
    }


    private Car getCarinFront(Car car){
        int new_id = car.id + 1;
        if(new_id == num_cars){
            new_id = 0;
        }
        return cars_now[new_id];
    }

    private void moveCar(Car car){
        int current_cell = car.cell; //get current position
        int next_cell = car.cell + car.v; //get next pos
        if(next_cell >= street_length){ //loop around
            next_cell = next_cell - street_length;
        }
        car.cell = next_cell;
    }

}
