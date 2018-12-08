package edu.eci.arsw.loannetsim;

import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Lender implements Runnable {

    private int balance;

    private static final int DEFAULT_BALANCE_AMOUNT = 500;
    private static final int DEFAULT_LOAN_AMOUNT = 10;

    private List<Lender> loanNetworkPopulation = null;

    private String name;

    public void setLenderName(String name) {
        this.name = name;
    }

    private final Random r = new Random(System.currentTimeMillis());

    private static boolean pause = false;

    public static void pause() {
        pause = true;
    }

    public static void resume() {
        pause = false;
    }

    public Lender() {
        this.balance = DEFAULT_BALANCE_AMOUNT;
        this.name = "NN";
    }

    @Override
    public void run() {

        while (true) {
            
            if (pause){
                synchronized(loanNetworkPopulation){
                    try {
                        loanNetworkPopulation.wait();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Lender.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }                
            }

            Lender im;

            int myIndex = loanNetworkPopulation.indexOf(this);

            int nextFighterIndex = r.nextInt(loanNetworkPopulation.size());

            //avoid self-loan
            if (nextFighterIndex == myIndex) {
                nextFighterIndex = ((nextFighterIndex + 1) % loanNetworkPopulation.size());
            }

            im = loanNetworkPopulation.get(nextFighterIndex);

            im.lend(this);
            

            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }

    public void lend(Lender i2) {

        if (i2.getBalance() > 0) {
            i2.changeBalance(i2.getBalance() - DEFAULT_LOAN_AMOUNT);
            this.balance += DEFAULT_LOAN_AMOUNT;
            System.out.println("$$$ Transaction: " + this + " lends "+DEFAULT_LOAN_AMOUNT +" to "+ i2);
        } else {
            System.out.println(this + " says:" + i2 + " is already in bankrupt!");
        }

    }

    public void setLoanNetworkPopulation(List<Lender> population) {
        this.loanNetworkPopulation = population;
    }

    public void changeBalance(int v) {
        balance = v;
    }

    public int getBalance() {
        return balance;
    }

    @Override
    public String toString() {

        return name + "[" + balance + "]";
    }

}
