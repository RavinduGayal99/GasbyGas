package cw.gasbygas.controller;

import cw.gasbygas.domain.RequestStatus;
import cw.gasbygas.model.Customer;
import cw.gasbygas.model.Gas;
import cw.gasbygas.model.Outlet;
import cw.gasbygas.model.Stock;
import cw.gasbygas.request.BulkGasRequest;
import cw.gasbygas.request.GasRequestDTO;
import cw.gasbygas.request.GasRequestOutletRequest;
import cw.gasbygas.response.GasRequestOutletResponse;
import cw.gasbygas.response.GasRequestResponse;
import cw.gasbygas.service.GasRequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gas")
@RequiredArgsConstructor
public class GasRequestController {
    private final GasRequestService gasRequestService;

    @PostMapping("/request/domestic")
    public ResponseEntity<GasRequestResponse> createDomesticRequest(
            @Valid @RequestBody GasRequestDTO request) {
        return ResponseEntity.ok(gasRequestService.createDomesticRequest(request));
    }

    @PostMapping("/request/business")
    public ResponseEntity<GasRequestResponse> createBusinessRequest(
            @Valid @RequestBody GasRequestDTO request) {
        return ResponseEntity.ok(gasRequestService.createBusinessRequest(request));
    }

    @PostMapping("/request/outlet/bulk")
    public ResponseEntity<List<GasRequestResponse>> createBulkOutletRequest(
            @Valid @RequestBody BulkGasRequest request) {
        return ResponseEntity.ok(gasRequestService.createBulkOutletRequest(request));
    }

    @PostMapping("/request/outlet")
    public ResponseEntity<List<GasRequestOutletResponse>> createOutletRequest(@Valid @RequestBody List<GasRequestOutletRequest> request)
    {
        return ResponseEntity.ok(gasRequestService.createOutletRequest(request));
    }

    @GetMapping("/requests")
    public ResponseEntity<List<GasRequestResponse>> getRequests(
            @RequestParam int userId,
            @RequestParam(required = false) RequestStatus status) {
        return ResponseEntity.ok(gasRequestService.getRequestsByUserAndStatus(userId, status));
    }

    @GetMapping("/requests/domestic")
    public ResponseEntity<List<GasRequestResponse>> getDomesticRequests(
            @RequestParam String statusFilter) {
        return ResponseEntity.ok(gasRequestService.getDomesticRequests(statusFilter));
    }

    @GetMapping("/requests/outlet")
    public ResponseEntity<List<GasRequestResponse>> getOutletRequestsByStatus(
            @RequestParam int outletId,
            @RequestParam RequestStatus status) {
        List<GasRequestResponse> responses = gasRequestService.getOutletRequestsByStatus(outletId, status);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/requests/outlet/{outletId}/filter")
    public ResponseEntity<List<GasRequestResponse>> getRequestsByStatusForOutlet(
            @PathVariable int outletId,
            @RequestParam String statusFilter) {
        return ResponseEntity.ok(gasRequestService.getRequestsByStatusForOutlet(outletId, statusFilter));
    }

    @PatchMapping("/requests/{requestId}/status")
    public ResponseEntity<Void> updateRequestStatus(
            @PathVariable int requestId,
            @RequestParam RequestStatus status) {
        gasRequestService.updateRequestStatus(requestId, status);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/requests/{requestId}/cancel")
    public ResponseEntity<Void> cancelDomesticRequest(
            @PathVariable int requestId,
            @RequestParam int userId) {
        gasRequestService.cancelDomesticRequest(requestId, userId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/requests/outlet/{requestId}/status")
    public ResponseEntity<Void> updateOutletRequestStatus(
            @PathVariable int requestId,
            @RequestParam RequestStatus status) {
        gasRequestService.updateOutletRequestStatus(requestId, status);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/domestic")
    public ResponseEntity<List<Gas>> getDomesticGas() {
        List<Gas> domesticGas = gasRequestService.getGasByIds(List.of(1, 2, 3));
        return ResponseEntity.ok(domesticGas);
    }

    @GetMapping("/business/cylinder")
    public ResponseEntity<Gas> getBusinessCylinderGas() {
        return gasRequestService.getGasById(4)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/business/bulk")
    public ResponseEntity<Gas> getBusinessBulkGas() {
        return gasRequestService.getGasById(5)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/outlet")
    public ResponseEntity<List<Gas>> getOutletGas() {
        return ResponseEntity.ok(gasRequestService.getAllGasItems());
    }

    @GetMapping("/outlet/verified")
    public ResponseEntity<List<Outlet>> getVerifiedOutlets() {
        return ResponseEntity.ok(gasRequestService.getVerifiedOutlets());
    }

    @GetMapping("/verified/retail-and-all")
    public ResponseEntity<List<Outlet>> getVerifiedRetailAndAllOutlets() {
        return ResponseEntity.ok(gasRequestService.getVerifiedRetailAndAllOutlets());
    }

    @GetMapping("/verified/bulk")
    public ResponseEntity<List<Outlet>> getVerifiedBulkOutlets() {
        return ResponseEntity.ok(gasRequestService.getVerifiedBulkOutlets());
    }

    @GetMapping("/verified/all")
    public ResponseEntity<List<Outlet>> getAllVerifiedOutlets() {
        return ResponseEntity.ok(gasRequestService.getVerifiedAllOutlets());
    }

    @GetMapping("/business/verified")
    public ResponseEntity<List<Customer>> getVerifiedBusiness() {
        return ResponseEntity.ok(gasRequestService.getVerifiedBusiness());
    }

    @PatchMapping("/business/requests/{requestId}/cancel")
    public ResponseEntity<Void> cancelBusinessRequest(
            @PathVariable int requestId) {
        gasRequestService.cancelBusinessRequest(requestId);
        return ResponseEntity.ok().build();
    }
}
