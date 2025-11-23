package cw.gasbygas.repository;

import cw.gasbygas.domain.CustomerType;
import cw.gasbygas.domain.UserStatus;
import cw.gasbygas.model.Customer;
import cw.gasbygas.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    Optional<Customer> findByUserId(int userId);
    List<Customer> findByType(CustomerType type);
    Customer findByUser(User user);
    List<Customer> findByUserStatusAndType(UserStatus status, CustomerType type);
}
