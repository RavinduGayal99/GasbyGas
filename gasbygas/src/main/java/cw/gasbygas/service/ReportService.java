package cw.gasbygas.service;

import cw.gasbygas.domain.CustomerType;

import java.time.LocalDateTime;

public interface ReportService {
    byte[] generateSalesReport(LocalDateTime startDate, LocalDateTime endDate);
    byte[] generateStockReport(int outletId);
    byte[] generateCustomerReport(CustomerType type);
}

