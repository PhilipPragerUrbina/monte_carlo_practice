package monte.carlo;

public class Main {

    public static void main(String[] args) {




        Retakes r = new Retakes(10000,7,85,0.75 );
        r.fillTable3D(0.7,0.9, 0.005, 0, 5,3);
        r.getTable().outputPointsCSV();


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
