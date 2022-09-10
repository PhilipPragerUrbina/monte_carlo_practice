package monte.carlo;

public class Main {

    public static void main(String[] args) {



        //create graph
        TrafficModel m = new TrafficModel(5,300,1000,0.3333, true);
        m.graph(1000);
        m.graph(4000);

        //create display
        TrafficModel mm = new TrafficModel(3,20,200,0.3333, false);
        for (int i = 0; i < 2000; i++) {
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
