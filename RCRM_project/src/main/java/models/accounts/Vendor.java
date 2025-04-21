package models.accounts;

import java.math.BigDecimal;

public class Vendor extends Account{
    protected int profit;
    public Vendor(int id, String username, String password, int profit) {
        super(id, username, password);
        this.profit = profit;
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
