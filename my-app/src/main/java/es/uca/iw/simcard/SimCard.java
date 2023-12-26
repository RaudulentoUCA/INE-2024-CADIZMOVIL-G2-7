package es.uca.iw.simcard;

import es.uca.iw.tarifa.Tarifa;
import jakarta.persistence.*;

@Entity
public class SimCard {
    @Id
    @Column(name="number", unique = true, nullable = false)
    private Integer number;
    @OneToOne
    private Tarifa tarifa;

    @Column(name="usedMinutes")
    private Integer usedMinutes;

    @Column(name = "usedMb")
    private Integer usedMb;

    @Column(name = "usedSms")
    private Integer usedSms;

    @Column(name = "balance")
    private float balance;

    @Column(name = "isActive", nullable = false)
    private boolean isActive;

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Tarifa getTarifa() {
        return tarifa;
    }

    public void setTarifa(Tarifa tarifa) {
        this.tarifa = tarifa;
    }

    public Integer getUsedMinutes() {
        return usedMinutes;
    }

    public void setUsedMinutes(Integer usedMinutes) {
        this.usedMinutes = usedMinutes;
    }

    public Integer getUsedMb() {
        return usedMb;
    }

    public void setUsedMb(Integer usedMb) {
        this.usedMb = usedMb;
    }

    public Integer getUsedSms() {
        return usedSms;
    }

    public void setUsedSms(Integer usedSms) {
        this.usedSms = usedSms;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }


    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public void addMoney(float amount) {
        if (isActive) {
            balance += amount;
        } else {
            System.out.println("Cannot add money to an inactive SIM card.");
        }
    }
}