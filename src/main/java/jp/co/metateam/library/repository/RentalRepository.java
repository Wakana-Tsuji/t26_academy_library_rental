package jp.co.metateam.library.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jp.co.metateam.library.model.Rental;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Long> {
    
    List<Rental> findByStockIdAndStatusIn(
        String stockId,
        List<Integer> statusList
    );

}

