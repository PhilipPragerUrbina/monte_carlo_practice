package monte.carlo;

public class Main {

    public static void main(String[] args) {

        GamblersRuin ruin =  new GamblersRuin(100);
        ruin.fillTableGoal(50,250,5);
        System.out.println("filled");
        ruin.getTable().output();

        /*
	ParticleDiffusion a =  new ParticleDiffusion(1.0, 1000);
    a.fillTable(100);
    a.getTable().outputCoords();
    a.getTable().outputPlot((Integer x)-> x,(Double y) -> y, 2 );
*/
    }
}
