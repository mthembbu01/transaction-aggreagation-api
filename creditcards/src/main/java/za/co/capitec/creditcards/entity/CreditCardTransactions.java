package za.co.capitec.creditcards.entity;

import jakarta.persistence.*;
import lombok.*;
import za.co.capitec.creditcards.enums.Categories;
import za.co.capitec.creditcards.enums.CreditCardType;
import za.co.capitec.creditcards.utilities.dates.DateUtils;

import java.math.BigDecimal;
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
    private BigDecimal amount;
    private LocalTime time;
    private LocalDate date;
    /**
     * The constructor to create a transaction
     * @param creditCard
     * @param description
     * @param amount
     * @param category
     * @return
     */
    public static CreditCardTransactions create(CreditCards creditCard, String description, BigDecimal amount, Categories category){
        return CreditCardTransactions
                .builder()
                .creditCard(creditCard)
                .cardType(creditCard.getCardType())
                .description(description)
                .amount(amount)
                .category(category)
                .date(DateUtils.getCurrentDate())
                .time(DateUtils.getCurrentTime())
                .build();
    }

    @ManyToOne
    @JoinColumn(name = "card_id")
    private CreditCards creditCard;
}
