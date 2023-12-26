package es.uca.iw.simcard;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SimCardRepository extends JpaRepository<SimCard, Integer> {
    Optional<SimCard> findByNumber(Integer number);

    List<SimCard> findAll();
}
