package antifraud.repositoreis;

import antifraud.models.SuspiciousIP;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SuspiciousIPRepository extends JpaRepository<SuspiciousIP,Long> {
    Optional<SuspiciousIP> findSuspiciousIPByIp(String ip );
}
