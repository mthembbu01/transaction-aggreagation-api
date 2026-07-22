package za.co.capitec.accounts.entity;


import lombok.*;
import za.co.capitec.accounts.enums.AccountType;
import za.co.capitec.accounts.enums.Categories;

import java.time.LocalDate;
import java.time.LocalTime;

//@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Transactions {
    private Long id;
    private Long accountNumber;
    private AccountType accountType;
    private String description;
    private Double amount;
    private Boolean isImmediate = Boolean.FALSE;
    private LocalTime time;
    private LocalDate date;
    private Categories category;
    private String reference;
}
