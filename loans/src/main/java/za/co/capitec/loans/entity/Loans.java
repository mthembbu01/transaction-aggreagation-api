package za.co.capitec.loans.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;
import za.co.capitec.loans.enums.LoanStatus;
import za.co.capitec.loans.enums.LoanType;

import java.time.LocalDate;
import java.util.Objects;

@Table(name = "loans")
@Entity(name = "loans")
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class Loans extends BaseEntity {

    @Id
    @SequenceGenerator(name = "loans_sequence", sequenceName = "loans_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "loans_sequence")
    private Long id;

    private Long loanNumber;

    @Enumerated(EnumType.STRING)
    private LoanType loanType;     // PERSONAL, HOME, VEHICLE, BUSINESS, STUDENT

    private String mobileNumber;
    private String idNumber;
    private Double loanAmount;
    private Double outstandingBalance;
    private Double monthlyInstalment;
    private LocalDate startDate;
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    private LoanStatus status;     // ACTIVE, CLOSED, DEFAULTED

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
        Loans loans = (Loans) o;
        return getId() != null && Objects.equals(getId(), loans.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy
                ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode()
                : getClass().hashCode();
    }
}
