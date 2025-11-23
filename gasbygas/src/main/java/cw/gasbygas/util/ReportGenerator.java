/*package cw.gasbygas.util;

import cw.gasbygas.exceptionHandling.ReportGenerationException;
import cw.gasbygas.model.GasRequest;
import cw.gasbygas.repository.GasRequestRepository;
import cw.gasbygas.repository.GasStockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.swing.text.Document;
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ReportGenerator {
    private final GasRequestRepository gasRequestRepository;
    private final GasStockRepository gasStockRepository;

    public byte[] generateSalesReport(LocalDateTime startDate, LocalDateTime endDate) {
        try {
            Document document = new Document(PageSize.A4);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, out);

            document.open();

            // Add title
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Paragraph title = new Paragraph("Sales Report", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            // Add date range
            document.add(new Paragraph("\nPeriod: " +
                    startDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) + " to " +
                    endDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))));

            // Add sales table
            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            table.setSpacingBefore(20f);

            // Add headers
            Stream.of("Date", "Item", "Quantity", "Unit Price", "Total")
                    .forEach(header -> {
                        PdfPCell cell = new PdfPCell();
                        cell.setPhrase(new Phrase(header));
                        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        table.addCell(cell);
                    });

            // Add data
            List<GasRequest> requests = gasRequestRepository.findByCreatedAtBetween(startDate, endDate);
            requests.forEach(request -> {
                table.addCell(request.getCreatedAt().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
                table.addCell(request.getGas().getItem());
                table.addCell(String.valueOf(request.getQuantity()));
                table.addCell(String.format("%.2f", request.getUnitPrice()));
                table.addCell(String.format("%.2f", request.getTotalPrice()));
            });

            document.add(table);

            // Add summary
            double totalSales = requests.stream()
                    .mapToDouble(GasRequest::getTotalPrice)
                    .sum();
            document.add(new Paragraph("\nTotal Sales: Rs. " + String.format("%.2f", totalSales)));

            document.close();
            return out.toByteArray();
        } catch (Exception e) {
            throw new ReportGenerationException("Failed to generate sales report", e);
        }
    }
}
*/