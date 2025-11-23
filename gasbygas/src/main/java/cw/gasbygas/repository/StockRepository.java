package cw.gasbygas.repository;

import cw.gasbygas.model.Stock;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Integer> {
    List<Stock> findByOutletId(int outletId);
    Optional<Stock> findByOutletIdAndGasId(int outletId, int gasId);

    @Query("SELECT gs FROM Stock gs WHERE gs.outlet.id = :outletId AND gs.quantity > 0")
    List<Stock> findAvailableStockByOutletId(int outletId);

    @Modifying
    @Transactional
    @Query("UPDATE Stock s SET s.quantity = :quantity WHERE s.outlet.id = :outletId AND s.gas.id = :gasId")
    int updateStock(@Param("outletId") int outletId, @Param("gasId") int gasId, @Param("quantity") int quantity);
}
