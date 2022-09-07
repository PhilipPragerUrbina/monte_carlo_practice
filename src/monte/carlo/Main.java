package monte.carlo;

public class Main {

    public static void main(String[] args) {


        Retakes r = new Retakes(10000,100);
        Retakes rr = new Retakes(10000,18*5); //5 tests 18 questions overall grad is identical to one big test of 18*5 questions
        r.fillTableStudentTest(0.88,0.93, 0.005);
        r.getTable().outputFormatted(14);
        r.getTable().outputPlot("P","A_probability",200);


        GamblersRuin ruin =  new GamblersRuin(1000);
        ruin.fillTableGoal(50,250,5);
        ruin.getTable().outputFormatted(11);
        ParticleDiffusion a =  new ParticleDiffusion(1.0, 1000);
        a.fillTable(100);
        a.getTable().outputFormatted(12);
        a.getTable().outputPlot("Num steps", "Distance", 2 );

    }
}
