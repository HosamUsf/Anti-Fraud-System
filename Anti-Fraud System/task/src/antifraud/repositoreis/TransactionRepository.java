package antifraud.repositoreis;

import antifraud.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction,Long> {

    @Query("SELECT distinct t.region FROM Transaction t where t.number = :givenCardNumber")
    List<String> findAllRegions(@Param("givenCardNumber")String givenCardNumber);

    @Query("SELECT distinct t.ip FROM Transaction t where t.number = :givenCardNumber")
    List<String> findAllIp(@Param("givenCardNumber")String givenCardNumber);
    List<Transaction> findAllByDateBetweenAndNumber(@Param("startDate") LocalDateTime dateStart, @Param("endDate") LocalDateTime dateEnd, @Param("number") String number);


}
