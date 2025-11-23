package cw.gasbygas.service.impl;

import cw.gasbygas.domain.CustomerType;
import cw.gasbygas.domain.OutletType;
import cw.gasbygas.domain.UserStatus;
import cw.gasbygas.domain.UserType;
import cw.gasbygas.model.*;
import cw.gasbygas.repository.*;
import cw.gasbygas.request.BusinessRegistrationRequest;
import cw.gasbygas.request.CustomerRegistrationRequest;
import cw.gasbygas.request.LoginRequest;
import cw.gasbygas.request.OutletRegistrationRequest;
import cw.gasbygas.response.*;
import cw.gasbygas.service.UserService;
import cw.gasbygas.util.EmailService;
import cw.gasbygas.util.JwtService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final OutletRepository outletRepository;
    private final GasRepository gasRepository;
    private final StockRepository stockRepository;
    private final JwtService jwtService;
    private final EmailService emailService;

    @Override
    @Transactional
    public User registerCustomer(CustomerRegistrationRequest request) {
        validateNewUser(request.getEmail());

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setName(request.getName());
        user.setContact(request.getContact());
        user.setNic(request.getNic());
        user.setType(UserType.CUSTOMER);
        user.setStatus(UserStatus.PENDING_VERIFICATION);

        user = userRepository.save(user);

        Customer customer = new Customer();
        customer.setUser(user);
        customer.setType(CustomerType.DOMESTIC);
        customerRepository.save(customer);

        emailService.sendWelcomeEmail(user.getEmail(), user.getName());

        return user;
    }

    @Override
    public User registerBusinessCustomer(BusinessRegistrationRequest request) {
        validateNewUser(request.getEmail());
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setName(request.getName());
        user.setContact(request.getContact());
        user.setType(UserType.CUSTOMER);
        user.setStatus(UserStatus.PENDING_CERTIFICATION);

        user = userRepository.save(user);

        Customer customer = new Customer();
        customer.setUser(user);
        customer.setType(CustomerType.BUSINESS);
        customer.setGasType(request.getGasType());
        customerRepository.save(customer);

        emailService.sendWelcomeEmail(user.getEmail(), user.getName());

        return user;
    }

    @Override
    public User registerOutlet(OutletRegistrationRequest request) {
        validateNewUser(request.getEmail());

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setName(request.getName());
        user.setContact(request.getContact());
        user.setType(UserType.OUTLET_MANAGER);
        user.setStatus(UserStatus.PENDING_VERIFICATION);

        user = userRepository.save(user);

        Outlet outlet = new Outlet();
        outlet.setUser(user);
        outlet.setDistrict(request.getDistrict());
        outlet.setType(request.getOutletType());
        outlet = outletRepository.save(outlet);

        List<Integer> gasIds = new ArrayList<>();

        if (outlet.getType() == OutletType.RETAIL) {
            gasIds = List.of(1, 2, 3);
        } else if (outlet.getType() == OutletType.BULK) {
            gasIds = List.of(5);
        } else if (outlet.getType() == OutletType.ALL) {
            gasIds = List.of(1, 2, 3, 4, 5);
        }

        for (int gasId : gasIds) {
            Gas gas = gasRepository.findById(gasId).orElseThrow(() -> new RuntimeException("Gas item not found"));

            Stock stock = new Stock();
            stock.setOutlet(outlet);
            stock.setGas(gas);
            stock.setQuantity(0);

            stockRepository.save(stock);
        }

        emailService.sendWelcomeEmail(user.getEmail(), user.getName());

        return user;
    }

    @Override
    public LoginResponse login(LoginRequest request) throws AuthenticationException {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AuthenticationException("Invalid credentials"));


        if (user.getType() != UserType.ADMIN && user.getType() != UserType.DISPATCH_MANAGER) {
            if (user.getStatus() != UserStatus.VERIFIED) {
                throw new AuthenticationException("Account not verified");
            }
        }

        String token = jwtService.generateToken(user);
        CustomerType customerType = null;

        if (user.getType() == UserType.CUSTOMER) {
            Customer customer = customerRepository.findByUser(user);
            if (customer != null) {
                customerType = customer.getType();
            }
        }

        return new LoginResponse(token, user.getType(), user.getName(), customerType, user.getId());
    }

    @Override
    public User updateUserStatus(int userId, UserStatus status) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.setStatus(status);

        emailService.sendAccountStatusEmail(user.getEmail(), user);

        return userRepository.save(user);
    }

    @Override
    public List<User> getUsersByType(UserType type) {
        return List.of();
    }

    @Override
    public List<UserResponse> getUsersByStatusAndType(UserStatus status, String userType) {
        List<User> users = userRepository.findByStatusAndType(status, userType);
        return users.stream().map(this::mapToUserResponse).collect(Collectors.toList());
    }

    @Override
    public List<DomesticUserResponse> getDomesticUsersByStatus(UserStatus status) {
        return userRepository.findDomesticCustomersByUserStatus(status)
                .stream()
                .map(customer -> new DomesticUserResponse(
                        customer.getUser().getId(),
                        customer.getUser().getName(),
                        customer.getUser().getEmail(),
                        customer.getUser().getContact(),
                        customer.getUser().getStatus()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<BusinessUserResponse> getBusinessUsersByStatus(UserStatus status) {
        return userRepository.findBusinessCustomersByUserStatus(status)
                .stream()
                .map(customer -> new BusinessUserResponse(
                        customer.getUser().getId(),
                        customer.getUser().getName(),
                        customer.getUser().getEmail(),
                        customer.getUser().getContact(),
                        customer.getGasType(),
                        customer.getUser().getStatus()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<OutletUserResponse> getOutletUsersByStatus(UserStatus status) {
        return userRepository.findOutletCustomersByUserStatus(status)
                .stream()
                .map(outlet -> new OutletUserResponse(
                    outlet.getUser().getId(),
                    outlet.getUser().getName(),
                    outlet.getUser().getEmail(),
                    outlet.getUser().getContact(),
                    outlet.getType(),
                    outlet.getDistrict(),
                    outlet.getUser().getStatus()
                ))
                .collect(Collectors.toList());
    }

    private void validateNewUser(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Email already registered");
        }
    }

    private UserResponse mapToUserResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getStatus()
        );
    }
}