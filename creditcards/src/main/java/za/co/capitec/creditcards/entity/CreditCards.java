package za.co.capitec.creditcards.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;
import za.co.capitec.creditcards.enums.CreditCardType;

import java.time.LocalDate;
import java.util.Objects;

@Table(name = "creditcards")
@Entity(name = "creditcards")
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class CreditCards extends BaseEntity {

    @Id
    @SequenceGenerator(name = "creditcards_sequence", sequenceName = "creditcards_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "creditcards_sequence")
    private Long id;

    private Long cardNumber;
    private Long accountNumber;

    @Enumerated(EnumType.STRING)
    private CreditCardType cardType; // VISA, MASTERCARD, AMEX
    private String mobileNumber;
    private String idNumber;
    private Double creditLimit;
    private Double availableCredit;
    private Double outstandingBalance;
    private Double minimumPayment;
    private LocalDate issueDate;
    private LocalDate expiryDate;
    private boolean activeSw;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy
                ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass()
                : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy
                ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass()
                : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        CreditCards that = (CreditCards) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy
                ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode()
                : getClass().hashCode();
    }
}
