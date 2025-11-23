package cw.gasbygas.controller;

import cw.gasbygas.model.Stock;
import cw.gasbygas.response.OutletResponse;
import cw.gasbygas.response.OutletStatusResponse;
import cw.gasbygas.service.OutletService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/outlet")
public class OutletController {
    private final OutletService outletService;

    public OutletController(OutletService outletService) {
        this.outletService = outletService;
    }

    @GetMapping
    public ResponseEntity<List<OutletResponse>> getOutlets(
            @RequestParam(required = false) String district) {
        return ResponseEntity.ok(outletService.getOutletsByDistrict(district));
    }

    /*@GetMapping("/{outletId}/status")
    public ResponseEntity<OutletStatusResponse> getOutletStatus(
            @PathVariable int outletId) {
        return ResponseEntity.ok(outletService.getOutletStatus(outletId));
    }*/

    @GetMapping("outlet/{outletId}/stock")
    public ResponseEntity<List<Stock>> getAllStocks(@PathVariable int outletId)
    {
        return ResponseEntity.ok(outletService.getAllStockForOutlet(outletId));
    }

    @PatchMapping("/{outletId}/stock")
    public ResponseEntity<Void> updateStock(
            @PathVariable int outletId,
            @RequestParam int gasId,
            @RequestParam int quantity) {
        outletService.updateStock(outletId, gasId, quantity);
        return ResponseEntity.ok().build();
    }
}
