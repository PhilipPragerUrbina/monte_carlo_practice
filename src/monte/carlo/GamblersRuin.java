package monte.carlo;

import java.util.Random;

//amount is # of sims to average
//step is how much to move each step
//rows or steps in number of steps to move
public class GamblersRuin {

    //settings
    private int num_simulations;
    private int start_money;
    private int goal_amount;
    private int reward;
    private int punishment;

    //data table
    private Table<Integer, Double> table;

    //initial settings
    public GamblersRuin(int start_money, int reward, int punishment, int goal_amount, int num_simulations){
        this.num_simulations = num_simulations;
        this.start_money = start_money;
        this.goal_amount = goal_amount;
        this.punishment = punishment;
        this.reward = reward;
        table = new Table<>();
    }
    public GamblersRuin(int num_simulations){
        this(50,1,-1,250,num_simulations);
    }

    //get average number of bets
    public double runBetSimulation(){
        //average sims
        double total = 0;
        for (int i = 0; i < num_simulations; i++) {
            total += betSimulation();
        }
        return total/num_simulations;
    }
    //return probability of success
    public double runSuccessSimulation(){
            double total = 0;
            for (int i = 0; i < num_simulations; i++) {
                total += successSimulation();
            }
            return total/num_simulations;
    }



    private  double successSimulation() {
        int money = start_money;
        while(money>0 && money < goal_amount){
            money += moneyModifier();
        }
        if(money > 0){return 1;}
        return 0;
    }


    private  double betSimulation() {
        int money = start_money;
        int round = 0;
        while(money>0 && money < goal_amount){
           money += moneyModifier();
            round++;
        }
        return round;
    }

    //how much does money change after a bet
    private  int moneyModifier(){
        if(Math.random() < 0.5){
            return  reward;
        }else{
            return  punishment;
        }
    }


    //fill table with increasing number of steps
    public void fillTable(int rows){


    }
    //get the table
    public Table<Integer,Double> getTable(){
        return table;
    }
}



