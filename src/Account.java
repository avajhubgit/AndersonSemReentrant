
public class Account {
    
    //size of account
    private volatile int  balance;

    public int getBalance() {
        return balance;
    }
    
    public Account(int balance) {
        this.balance = balance;
    }
    
    //deposite
    public void incBalance(int amount){
        balance += amount;
    }
    
    //with draw
    public void decBalance(int amount){
        balance -= amount;
    }
    
}
