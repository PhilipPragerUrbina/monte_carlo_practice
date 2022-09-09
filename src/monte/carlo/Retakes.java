package monte.carlo;
//class for the "Do retakes make our tests fairer?" problem



public class Retakes {

    //settings
    private int num_simulations;
    private int test_questions;
    private int A_score ;
    private double student_p ;

    //data table
    private Table table;

    //initial settings
    public Retakes(int num_simulations, int test_questions, int A_score, double student_p){
        this.num_simulations = num_simulations;
        this.test_questions = test_questions;
        this.student_p = student_p;
        this.A_score = A_score;

    }

    //test different p values in student_test and put them in table
    public void fillTableStudentTest(double start, double end, double inc){
        table = new Table("P","A_probability");
        for (double i = start; i < end; i+=inc) {
            student_p = i;
            table.addDataPoint(new FloatingPointData(i), new FloatingPointData(singleTestExperiment()));
        }
    }

    public void fillTable3D(double start_p, double end_p, double inc_p, int start_retakes, int end_retakes, int num_tests){
        table = new Table("P","Retakes", "A_prob");
        for (double i = start_p; i < end_p; i+=inc_p) {
            student_p = i;
            for (int j = start_retakes; j < end_retakes; j++) {
                table.addDataPoint(new FloatingPointData(i), new IntData(j), new FloatingPointData(retakeExperiment(num_tests,j)));
            }
        }
    }

    public void fillTableRetakes(int start, int end, int num_tests){
        table = new Table("Retakes","fail_prob");
        for (int i = start; i < end; i++) {
            table.addDataPoint(new IntData(i), new FloatingPointData(retakeExperiment(num_tests,i)));
        }
    }


    //return probability of not ending up with an A after multiple retake tests
    public double retakeExperiment(int num_tests, int num_retakes){
        double sum = 0; //sum # of good scores
        for (int i = 0; i < num_simulations; i++) {
            double average_grade = 0;
            for (int j = 0; j < num_tests; j++) {
                average_grade+= takeTest(num_retakes);//take test with retakes
            }
            average_grade /= (double)num_tests;
            if(average_grade >= A_score){
                sum += 1;
            }
        }
        return 1.0 - sum/num_simulations;//convert from A probability to non A probability
    }


    //return probability of not ending up with an A after multiple tests
    public double multipleTestExperiment(int num_tests){
        double sum = 0; //sum A scores
        for (int i = 0; i < num_simulations; i++) {
            //take the tests
            double average_grade = 0;
            for (int j = 0; j < num_tests; j++) {
                average_grade+= takeTest();
            }
            average_grade /= (double)num_tests; //overall grade
            //check if A
            if(average_grade >= A_score){
                sum += 1;
            }
        }
        return 1.0 - sum/num_simulations;//convert from A probability to non A probability
    }

    //simulate student taking test with probability of correct answer p
    //return probability of an A
    public double singleTestExperiment(){
        double sum = 0; //sum A scores
        for (int i = 0; i < num_simulations; i++) {
            if(takeTest() > A_score){
                sum += 1;
            }
        }
        return sum/num_simulations;
    }


    //return percent grade
    public double takeTest(){
        double questions_right = 0;
        for (int i = 0; i < test_questions; i++) {
            if(Math.random() < student_p){
                questions_right++;
            }
        }
        return (questions_right / (double)test_questions) * 100.0;
    }

    //return best percent grade. 0 retakes means 1 test.
    public double takeTest(int num_retakes){
        double best_score = 0;
        for (int i = 0; i < num_retakes+1; i++) {
            best_score = Math.max(best_score, takeTest());
        }
        return best_score;
    }



    //get the table
    public Table getTable(){
        return table;
    }
}



