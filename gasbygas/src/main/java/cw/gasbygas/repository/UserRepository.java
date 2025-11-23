package cw.gasbygas.repository;

import cw.gasbygas.domain.UserStatus;
import cw.gasbygas.domain.UserType;
import cw.gasbygas.model.Customer;
import cw.gasbygas.model.Outlet;
import cw.gasbygas.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
    Optional<User> findByNic(String nic);
    List<User> findByType(UserType type);
    List<User> findByStatus(UserStatus status);

    @Query("SELECT u FROM User u WHERE u.status = :status AND u.type = :userType")
    List<User> findByStatusAndType(UserStatus status, String userType);

    @Query("SELECT c FROM Customer c JOIN c.user u WHERE c.type = 'DOMESTIC' AND (:status IS NULL OR u.status = :status)")
    List<Customer> findDomesticCustomersByUserStatus(@Param("status") UserStatus status);

    @Query("SELECT c FROM Customer c JOIN c.user u WHERE c.type = 'BUSINESS' AND (:status IS NULL OR u.status = :status)")
    List<Customer> findBusinessCustomersByUserStatus(@Param("status") UserStatus status);

    @Query("SELECT c FROM Outlet c JOIN c.user u WHERE u.type = 'OUTLET_MANAGER' AND (:status IS NULL OR u.status = :status)")
    List<Outlet> findOutletCustomersByUserStatus(@Param("status") UserStatus status);
}
