package cw.gasbygas.controller;

import cw.gasbygas.domain.UserStatus;
import cw.gasbygas.model.User;
import cw.gasbygas.response.BusinessUserResponse;
import cw.gasbygas.response.DomesticUserResponse;
import cw.gasbygas.response.OutletUserResponse;
import cw.gasbygas.response.UserResponse;
import cw.gasbygas.service.ReportService;
import cw.gasbygas.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final UserService userService;
    //private final ReportService reportService;

    public AdminController(UserService userService/*, ReportService reportService*/) {
        this.userService = userService;
        //this.reportService = reportService;
    }

    @GetMapping("/users/domestic")
    public List<DomesticUserResponse> getDomesticUsersByStatus(@RequestParam(required = false) UserStatus status) {
        return userService.getDomesticUsersByStatus(status);
    }

    @GetMapping("/users/business")
    public List<BusinessUserResponse> getBusinessUsersByStatus(@RequestParam(required = false) UserStatus status) {
        return userService.getBusinessUsersByStatus(status);
    }

    @GetMapping("/users/outlet")
    public List<OutletUserResponse> getOutletUsersByStatus(@RequestParam(required = false) UserStatus status) {
        return userService.getOutletUsersByStatus(status);
    }

    @GetMapping("/users")
    public List<UserResponse> getUsersByStatus(
            @RequestParam(required = false) UserStatus status,
            @RequestParam(required = false) String userType) {
        return userService.getUsersByStatusAndType(status, userType);
    }

    @PatchMapping("/users/{userId}/status")
    public ResponseEntity<User> updateUserStatus(
            @PathVariable int userId,
            @RequestParam UserStatus status) {
        return ResponseEntity.ok(userService.updateUserStatus(userId, status));
    }

    /*@GetMapping("/reports/sales")
    public ResponseEntity<byte[]> getSalesReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        byte[] report = reportService.generateSalesReport(startDate, endDate);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=sales-report.pdf")
                .body(report);
    }*/
}