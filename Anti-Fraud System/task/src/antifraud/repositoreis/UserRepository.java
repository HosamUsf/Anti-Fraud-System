package antifraud.repositoreis;

import antifraud.models.Userr;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Userr,Long> {


   Optional<Userr> findByUsernameIgnoreCase(String email);

    void deleteByUsername(String username);
}
