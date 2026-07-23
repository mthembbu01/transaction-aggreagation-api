package za.co.capitec.loans.audit;

import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("auditAwareImpl")
public class AuditAwareImpl {

    public Optional<String> getCurrentAuditor() {
        return Optional.of("LOANS_MS");
    }
}

