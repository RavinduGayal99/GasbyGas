package cw.gasbygas.repository;

import cw.gasbygas.domain.OutletType;
import cw.gasbygas.domain.UserStatus;
import cw.gasbygas.model.Outlet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OutletRepository extends JpaRepository<Outlet, Integer> {
    List<Outlet> findByDistrict(String district);
    List<Outlet> findByType(OutletType type);

    @Query("SELECT DISTINCT o.district FROM Outlet o")
    List<String> findAllDistricts();

    List<Outlet> findByUserStatus(UserStatus status);

    List<Outlet> findByTypeInAndUser_Status(List<OutletType> types, UserStatus status);

    List<Outlet> findByTypeAndUser_Status(OutletType type, UserStatus status);
}
