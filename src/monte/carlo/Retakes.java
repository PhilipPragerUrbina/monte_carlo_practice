package monte.carlo;
//class for the "Do retakes make our tests fairer?" problem
public class Retakes {

    //settings
    private int num_simulations;
    private int test_questions;
    private int A_score = 85;

    //data table
    private Table table;

    //initial settings
    public Retakes(int num_simulations, int test_questions){
        this.num_simulations = num_simulations;
        this.test_questions = test_questions;

    }

    //test different p values in student_test and put them in table
    public void fillTableStudentTest(double start, double end, double inc){
        table = new Table("P","A_probability");
        for (double i = start; i < end; i+=0.005) {
            table.addDataPoint(new FloatingPointData(i), new FloatingPointData(studentTest(i)));
        }
    }

    //simulate student taking test with probability of correct answer p
    //return probability of an A
    public double studentTest(double p){
        double sum = 0; //sum A scores
        for (int i = 0; i < num_simulations; i++) {
            if(simulateStudent(p) >= 85){
                sum += 1;
            }
        }
        return sum/num_simulations;
    }
    //return percent grade
    public double simulateStudent(double p){
        double questions_right = 0;
        for (int i = 0; i < test_questions; i++) {
            if(Math.random() < p){
                questions_right++;
            }
        }
        return questions_right / test_questions * 100.0;
    }



    //get the table
    public Table getTable(){
        return table;
    }
}



