package pl.smoleck.hajsownik.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class SpendingGoal {
    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private double balance;

}
