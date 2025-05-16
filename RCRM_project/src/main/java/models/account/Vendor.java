package models.account;

public class Vendor extends Account{
    protected int profit = 0;


    public Vendor(int id, String username, String password, int profit) {
        super(id, username, password, EAccType.VENDOR);
        this.profit = profit;
    }

    public Vendor(Account account) {
        super(account);

    }

    public int getProfit() {
        return profit;
    }

    public void setProfit(int profit) {
        this.profit = profit;
    }

    @Override
    public String toString() {
        return "Vendor{" +
                super.toString() +
                "profit=" + profit +
                '}';
    }
}
