package za.co.capitec.creditcards.entity;

import jakarta.persistence.*;
import lombok.*;
import za.co.capitec.creditcards.enums.Categories;
import za.co.capitec.creditcards.enums.CreditCardType;

import java.time.LocalDate;
import java.time.LocalTime;

@Table(name = "credit_card_transactions")
@Entity(name = "credit_card_transactions")
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class CreditCardTransactions {

    @Id
    @SequenceGenerator(name = "credit_card_transactions_sequence", sequenceName = "credit_card_transactions_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "credit_card_transactions_sequence")
    private Long id;

    @Enumerated(EnumType.STRING)
    private CreditCardType cardType;

    @Enumerated(EnumType.STRING)
    private Categories category;

    private String description;
    private Double amount;
    private Boolean isImmediate = Boolean.FALSE;
    private String reference;
    private LocalTime time;
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "card_number")
    private CreditCards creditCard;
}
