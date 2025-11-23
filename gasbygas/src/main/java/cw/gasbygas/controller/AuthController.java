package cw.gasbygas.controller;

import cw.gasbygas.model.User;
import cw.gasbygas.request.BusinessRegistrationRequest;
import cw.gasbygas.request.CustomerRegistrationRequest;
import cw.gasbygas.request.LoginRequest;
import cw.gasbygas.request.OutletRegistrationRequest;
import cw.gasbygas.response.LoginResponse;
import cw.gasbygas.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register/customer")
    public ResponseEntity<User> registerCustomer(@Valid @RequestBody CustomerRegistrationRequest request) {
        return ResponseEntity.ok(userService.registerCustomer(request));
    }

    @PostMapping("/register/business")
    public ResponseEntity<User> registerBusiness(@Valid @RequestBody BusinessRegistrationRequest request) {
        return ResponseEntity.ok(userService.registerBusinessCustomer(request));
    }

    @PostMapping("/register/outlet")
    public ResponseEntity<User> registerOutlet(@Valid @RequestBody OutletRegistrationRequest request) {
        return ResponseEntity.ok(userService.registerOutlet(request));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) throws Exception {
        return ResponseEntity.ok(userService.login(request));
    }
}
