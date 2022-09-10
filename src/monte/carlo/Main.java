package monte.carlo;

public class Main {

    public static void main(String[] args) {


        Table data = new Table("k","d");

        for (int k = 55; k <= 500; k+=5) {
            //get average total distance of cars
            final int num_simulations = 100; //how many times to run experiment
            double total = 0;
            for (int i = 0; i < num_simulations; i++) {
                //create experiment
                TrafficModel experiment =  new TrafficModel(5,k,1000,1.0/3.0, false);
                for (int j = 0; j < 1000; j++) {experiment.step();} //burn in
                //get total distance of single simulation
                int experiment_steps = 0;
                for (int j = 0; j < 1000; j++) {
                    experiment_steps+=experiment.step();
                }
                total += experiment_steps;
            }
            double data_result = total/num_simulations;
            data.addDataPoint(new IntData(k),new FloatingPointData(data_result));
        }
        //data.outputFormatted(20);
        data.outputPointsCSV();


        //create graph
        TrafficModel m = new TrafficModel(5,300,1000,0.3333, true);
        m.graph(1000);
        m.graph(1000);

        //create display
        TrafficModel mm = new TrafficModel(3,20,200,0.3333, false);
        for (int i = 0; i < 0; i++) {
            mm.print();
            mm.step();

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


        /*
        Retakes r = new Retakes(10000,7,85,0.75 );
        r.fillTable3D(0.7,0.9, 0.005, 0, 5,3);
        r.getTable().outputPointsCSV();
*/

        /*
        GamblersRuin ruin =  new GamblersRuin(1000);
        ruin.fillTableGoal(50,250,5);
        ruin.getTable().outputFormatted(11);
        ruin.getTable().outputPlot("Target","Bets", new Vector2(1, 0.01),true);
        ParticleDiffusion a =  new ParticleDiffusion(1.0, 1000);
        a.fillTable(100);
        a.getTable().outputFormatted(12);
        a.getTable().outputPlot("Num steps", "Distance", new Vector2(2,2) ,true);
        */


    }
}
