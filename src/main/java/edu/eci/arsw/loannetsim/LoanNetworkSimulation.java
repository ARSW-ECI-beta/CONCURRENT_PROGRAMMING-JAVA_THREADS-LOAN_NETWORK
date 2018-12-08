/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.loannetsim;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author hcadavid
 */
public class LoanNetworkSimulation {

    private static final int NUMOF_LENDERS=20;
    
    public static void main(String args[]) throws InterruptedException, IOException{

        int balancesSum=0;
        
        List<Lender> lenders = setupLoanNetwork(NUMOF_LENDERS);

        if (lenders != null) {
            for (Lender im : lenders) {
                new Thread(im).start();
            }
        }

        while (true){
            Thread.sleep(10000);

            Lender.pause();

            System.out.println("*** PRESS ENTER TO VIEW STATISTICS ***");

            System.in.read();

            balancesSum=0;
            for (Lender ln : lenders) {
                balancesSum += ln.getBalance();
            }

            System.out.println("Sum of balances:" + balancesSum);

            System.out.println("Press enter to continue simulation or Ctrl+C to abort...");

            System.in.read();

            Lender.resume();
            synchronized (lenders) {
                lenders.notifyAll();
            }

        }
        
        

        
    }


    public static List<Lender> setupLoanNetwork(int ni) {

        List<Lender> il = new LinkedList<>();

        for (int i = 0; i < ni; i++) {

            Lender i1 = new Lender();
            i1.setLoanNetworkPopulation(il);
            i1.setLenderName("Lender #" + i);
            il.add(i1);
        }
        return il;

    }
    
    
}
