package svend.taikon.Model;

import svend.taikon.LargeNumber;

import java.util.Objects;
import java.util.UUID;


public class User {
    private UUID id;
    private String name;
    private LargeNumber income;
    private LargeNumber balance;
    private int incomeMultiplier;

    public User() {
    }

    public User(UUID id, String name, LargeNumber income, LargeNumber balance,int incomeMultiplier) {
        this.id = id;
        this.name = name;
        this.income = income;
        this.balance = balance;
        this.incomeMultiplier = incomeMultiplier;
    }

    public LargeNumber getActiveIncome(){//Доход с бустами
        return new LargeNumber(income.toString()).multiply(new LargeNumber(String.valueOf(incomeMultiplier)));
    }

    public int getIncomeMultiplier() {
        return incomeMultiplier;
    }

    public void setIncomeMultiplier(int incomeMultiplier) {
        this.incomeMultiplier = incomeMultiplier;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LargeNumber getIncome() {
        return income;
    }

    public void setIncome(LargeNumber income) {
        this.income = income;
    }

    public LargeNumber getBalance() {
        return balance;
    }

    public void setBalance(LargeNumber balance) {
        this.balance = balance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(name, user.name) && Objects.equals(income, user.income) && Objects.equals(balance, user.balance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, income, balance);
    }
}
