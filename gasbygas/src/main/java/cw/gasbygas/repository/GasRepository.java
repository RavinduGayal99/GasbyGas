package cw.gasbygas.repository;

import cw.gasbygas.model.Gas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GasRepository extends JpaRepository<Gas, Integer> {
    List<Gas> findByItemIn(List<String> items);

    @Query("SELECT g FROM Gas g WHERE g.item IN ('12.5kg', '5kg', '2.3kg')")
    List<Gas> findStandardCylinderTypes();
}