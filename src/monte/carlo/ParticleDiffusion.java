package monte.carlo;

//amount is # of sims to average
//step is how much to move each step
//rows or steps in number of steps to move
public class ParticleDiffusion {

    //settings
    private double step;
    private int num_simulations;

    //data table
    private Table table;

    //initial settings
    public ParticleDiffusion(double step , int num_simulations){
        this.num_simulations = num_simulations;
        this.step = step;
        table = new Table("Num steps", "Distance");
    }

    //run a single simulation
    public double run(int steps){
        return calculateAverage(step, steps, num_simulations);
    }

    //fill table with increasing number of steps
    public void fillTable(int rows){

        for (int i = 0; i < rows; i++) {
            table.addDataPoint(new IntData(i), new FloatingPointData(calculateAverage(step,i,num_simulations)));
        }
    }
    //get the table
    public Table getTable(){
      return table;
    }

    //calculate the average
    private static double calculateAverage(double step, int steps, int amount){
        //average sims
        double total = 0;
        for (int i = 0; i < amount; i++) {
            total += runSim(steps, step);
        }
        return total/amount;
    }

    //run the simulation
    private static double runSim(int steps, double step) {
        //start pos is origin
        Vector3 pos = new Vector3(0);
        Vector3 start_pos = pos;
        //simulate
        for (int i = 0; i < steps; i++) {
            pos = pos.move(Vector3.randomDirection(), step); //move in random dir
        }
        return pos.distance(start_pos);
    }
}



