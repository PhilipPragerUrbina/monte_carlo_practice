package monte.carlo;
//Nagel-Schrekenberg traffic model with mutliple connected roads

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class TrafficModelComplex {
    //ticker for assigning ids to cars
    private int last_id = 0;
    //struct to contain car data
    class Car{
        int v;
        int cell;
        int id;
        //create new car
        public Car(int cell){
            this.v = 0;
            this.cell = cell;
            this.id = last_id;
            last_id++;  //unique id
        }
        //make copy of car
        public Car(Car other){
            this.v = other.v;
            this.cell = other.cell;
            this.id = other.id;
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

    //struct to contain street data
    class Lane{
        int end_junction; //junction it ends in
        int start_junction;
        public Lane(int end_junction, int start_junction) {
            this.end_junction = end_junction;
            this.start_junction = start_junction;
        }
        int getCells(){
            return (int)junctions[start_junction].position.distance(junctions[end_junction].position);
        }
        ArrayList<Car> cars = new ArrayList<>(); //cars in road
    }
    //junctions class
    class Junction{
        ArrayList<Integer> outgoing_lanes = new ArrayList<>(); //lanes that go out from the junction
        Vector2 position;
        Car car_in_junction;
        Junction(Vector2 position){
            this.position = position;
        }
        void addLane(int id){
            outgoing_lanes.add(id);
        }
    }
    private ArrayList<Lane> lanes;
    private Junction[] junctions;

    //settings
    private int max_v;
    private double p;
    private  int num_cars;
    private int num_junctions;
    private int max_connections;
    private Vector2 area_dimensions;
    double min_road_length;

    Graph g;

    public TrafficModelComplex(double p, int max_v, int num_cars, int num_junctions, int max_connections, Vector2 area_dimensions, int min_road_length) {
        this.max_v = max_v;
        this.num_cars = num_cars;
        this.num_junctions = num_junctions; //two lanes per street
        this.max_connections = max_connections;
        this.p = p;
        this.area_dimensions = area_dimensions;
        this.min_road_length = min_road_length;
        //initialize arrays
        lanes = new ArrayList<>();
        junctions = new Junction[num_junctions];
        //debug graph
        g = new Graph(new Vector2(0), area_dimensions, new Vector2(1));
        if(num_cars >= genStreets()){ //generate streets
            System.err.println("K must be less than M!");
            return;
        };
        genCars(); //add cars



        g.savePNG("test21.png"); //save for debugw

    }

    //generate a bunch fo streets of varying lengths and return number of cells
    private int genStreets(){

//java.awt.Graphics gg = new Graphics2D();




        //generate random junctions
        //generate random points within an area, at minimum a certain distance apart
        for (int i = 0; i < num_junctions; i++) {
            int min_distance = 0;
            int num_tries = 0;
            while (min_distance < min_road_length){
                junctions[i] = new Junction(new Vector2(RandomRange(0, (int)area_dimensions.x),RandomRange(0, (int)area_dimensions.y)) );
                min_distance = Integer.MAX_VALUE;
                for (int j = 0; j < i; j++) {
                    if(junctions[i].position.distance(junctions[j].position) < min_distance){
                        min_distance = (int)junctions[i].position.distance(junctions[j].position);
                    }
                }
                num_tries++;
                if(num_tries > 1000){ //make sure the loop does not run forever
                    System.err.println("Can not find possible road configuration for these settings.");
                    return 0;
                }
            }

        }

        //generate connections
        for (int i = 0; i < num_junctions; i++) {
            //for each junction
            Junction junction = junctions[i];
            //get desired connection number
            int desired_connections = RandomRange(3, max_connections);
            //get n nearest junctions
            int[] nearest_ids = new int[max_connections]; //nearest junctions
            Double[] nearest_distances = new Double[max_connections]; // a bit of buffer in case nearest intersect
            for (int j = 0; j < num_junctions; j++) {
                if(j==i){continue;} //for each other junction
                double distance = junction.position.distance(junctions[j].position);//get distance
                for (int k = 0; k < max_connections; k++) {
                    if(nearest_distances[k] == null || distance <  nearest_distances[k]){
                            nearest_distances[k] = distance;
                            nearest_ids[k] = j;
                            break; //first that has more distance is replaced
                    }
                }
            }
            int num_connections = 0;
            //create lanes
            g.setColor(0,255,0);
            for (int id : nearest_ids) {
                if(num_connections > desired_connections){break;};
                Lane lane = new Lane(id, i);
                        if(doesNotIntersect(lane)) {
                            lanes.add(lane); //create lane to other junctions
                            junction.addLane(lanes.size() - 1); //add this lane to this junction(Outgoing)
                            g.drawLine(junction.position, new Vector2(junctions[id].position.x + 2, junctions[id].position.y + 2));//debug
                            num_connections++;
                        }
            }

            //add overlapping lanes if neccecary(overpasses)
            g.setColor(100,100,0);
            if(junction.outgoing_lanes.size() < 2){
                Lane lane = new Lane(nearest_ids[0], i);
                    lanes.add(lane); //create lane to other junctions
                    junction.addLane(lanes.size() - 1); //add this lane to this junction(Outgoing)
                    g.drawLine(junction.position, new Vector2(junctions[nearest_ids[0]].position.x + 2, junctions[nearest_ids[0]].position.y + 2));//debug

                 lane = new Lane(nearest_ids[1], i);
                lanes.add(lane); //create lane to other junctions
                junction.addLane(lanes.size() - 1); //add this lane to this junction(Outgoing)
                g.drawLine(junction.position, new Vector2(junctions[nearest_ids[1]].position.x + 2, junctions[nearest_ids[1]].position.y + 2));//debug
               // gg.drawLine(junction.position.x, junction.position.y, junctions[nearest_ids[1]].position.x + 2,junctions[nearest_ids[1]].position.y + 2);
            }

        }

        g.setColor(255,0,0);
        for (Junction a : junctions) {
            if(a.outgoing_lanes.size() == 0){
                System.err.println("aaaa");
            }
            g.pixel(a.position); //print for debug
        }


        //count street lengths
        int num_cells = 0;
        for (Lane lane : lanes) {
            num_cells+=lane.getCells();
        }

        return num_cells;
    }


    private void genCars(){
        //randomly distribute cars to all lanes
        int cars_left = num_cars;
        int[] cars_to_add = new int[lanes.size()];
        while (cars_left > 0){
            int random_id = RandomRange(0, lanes.size());
            if(cars_to_add[random_id] < lanes.get(random_id).getCells()){ //check if full
                cars_to_add[random_id]++;
                cars_left--;
            }
        }

        for (int i = 0; i < cars_to_add.length; i++) {
            int num_cars_to_add = cars_to_add[i];
            Lane lane_to_add_to = lanes.get(i);
            for (int j = 0; j < num_cars_to_add; j++) {
                lane_to_add_to.cars.add(new Car(j));
            }
        }

        g.setColor(0,0,255);
        for (Lane l :
                lanes) {
            for (Car c :
                    l.cars) {
                Vector2 start = junctions[l.start_junction].position;
                Vector2 dir = junctions[l.end_junction].position.subtract(junctions[l.start_junction].position).normalized();
                Vector2 car_pos = start.move(dir,c.cell);
                g.pixel(car_pos);
            }
        }

    }
  /*
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
*/

    //utils
    private static int RandomRange(int min,int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    //check if line does not intersect
    private boolean doesNotIntersect(Lane a) {
        boolean intersects = false;
        for (Lane lane : lanes) { //add offset to some positions, to avoid ends being counted as intersections
            if(lane.start_junction == a.end_junction && a.start_junction == lane.end_junction) { //check if same street
                continue;
            }
            if (doesIntersect(junctions[a.end_junction].position, junctions[a.start_junction].position, junctions[lane.end_junction].position.add(new Vector2(2)), junctions[lane.start_junction].position.add(new Vector2(2)))) {
                return false;
            }
        }
        return true;
    }
    static boolean doesIntersect(Vector2 p1, Vector2 q1, Vector2 p2, Vector2 q2)
    {
        int o1 = orientation(p1, q1, p2);
        int o2 = orientation(p1, q1, q2);
        int o3 = orientation(p2, q2, p1);
        int o4 = orientation(p2, q2, q1);
        if (o1 != o2 && o3 != o4) {   return true;}
        if (o1 == 0 && onSegment(p1, p2, q1)) return true;
        if (o2 == 0 && onSegment(p1, q2, q1)) return true;
        if (o3 == 0 && onSegment(p2, p1, q2)) return true;
        if (o4 == 0 && onSegment(p2, q1, q2)) return true;
        return false;
    }
    private static int orientation(Vector2 p, Vector2 q, Vector2 r)
    {
        int val = (int)((q.y - p.y) * (r.x - q.x) - (q.x - p.x) * (r.y - q.y));
        if (val == 0) return 0;
        return (val > 0)? 1: 2;
    }
    private static boolean onSegment(Vector2 p, Vector2 q, Vector2 r) {return q.x < Math.max(p.x, r.x) && q.x > Math.min(p.x, r.x) && q.y < Math.max(p.y, r.y) && q.y > Math.min(p.y, r.y);}

}
