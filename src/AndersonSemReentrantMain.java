import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AndersonSemReentrantMain {
    
    private static final int WAIT_SEC = 10;        

    public static void main(String[] args) {
                    
        final Account a = new Account(1000);
        final Account b = new Account(4000);
        
        new Thread (() -> {
            try {
                transfer(a, b, 300);
            } catch (InsuffientFundsException ex) {
                Logger.getLogger(AndersonSemReentrantMain.class.getName()).log(Level.SEVERE, null, ex);
            }
        }).start();  
        
        try {
            transfer(b, a, 500);
        } catch (InsuffientFundsException ex) {
            Logger.getLogger(AndersonSemReentrantMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //move amount from source to destination
    static void transfer(Account acc_src, Account acc_dst, int amount) 
            throws InsuffientFundsException{
        if (acc_src.getBalance() < amount)
            throw new InsuffientFundsException();
        
        Lock lock_src = new ReentrantLock();
        Lock lock_dst = new ReentrantLock();
        
        try {
            if (lock_src.tryLock(WAIT_SEC, TimeUnit.SECONDS)){
                try{
                    Thread.sleep(1000);
                    if (lock_dst.tryLock(WAIT_SEC, TimeUnit.SECONDS)){
                        try{
                            System.out.format("Balance before source = %d, balance destination = %d, amount = %d %n", acc_src.getBalance(), acc_dst.getBalance(), amount);
                        
                            acc_src.decBalance(amount);
                            acc_dst.incBalance(amount);
                                
                            System.out.format("Balance after source = %d, balance destination = %d %n", acc_src.getBalance(), acc_dst.getBalance());
                        }
                        finally{
                            lock_dst.unlock();
                        }
                    }
                    else{
                        System.out.println("Error waiting lock dst.");
                    }
                }
                finally{
                    lock_src.unlock();
                }
            }
            else{
                System.out.println("Error waiting lock src.");
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(AndersonSemReentrantMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

private static class InsuffientFundsException extends Exception {

    public InsuffientFundsException() {
        }
    }
    
}
