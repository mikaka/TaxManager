package fr.pernisi.risf.taxmanager.receipt.service;



import fr.pernisi.risf.taxmanager.receipt.dto.ReceiptLineDto;
import fr.pernisi.risf.taxmanager.receipt.model.Receipt;
import fr.pernisi.risf.taxmanager.receipt.model.ReceiptLine;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class ReceiptService {

    private final TaxService taxService;

    /**
     * Create a receipt object ( could be transformed into an entity, in the next evolution)
     * @param inputs
     * @return Receipt object
     */
    public Receipt createReceipt(List<ReceiptLineDto> inputs) {
        log.info("Creation of the Receipt object");
        var receipt = new Receipt();
        double totalPrice = 0.0;
        double totalTax = 0.0;

        List<ReceiptLine> lines = new ArrayList<>();
        if (inputs != null && !inputs.isEmpty()) {
            for( ReceiptLineDto lineProduct : inputs) {
                validateReceiptLine(lineProduct);
                double taxPrice = calculateTaxePriceLine(lineProduct);
                double price = calculatePriceLine(lineProduct, taxPrice);
                totalPrice += price;
                totalTax += taxPrice * lineProduct.quantity();

                lines.add(buildLineReceipt(lineProduct,price, taxPrice));
            }

        }
        receipt.setLines(lines);
        receipt.setTotalTax(totalTax); // Round to 2 decimal places
        receipt.setTotalPrice(totalPrice);

        return receipt;
    }

    private double calculatePriceLine(ReceiptLineDto lineProduct, double taxPrice) {
        return (lineProduct.price() + taxPrice) * lineProduct.quantity();
    }

    private double calculateTaxePriceLine(ReceiptLineDto lineProduct) {
        return customRound(taxService.getTax(lineProduct.title(), lineProduct.price()));
    }

    private ReceiptLine buildLineReceipt(ReceiptLineDto line, double price, double taxPrice) {
        var calculedLine = new ReceiptLine();
        calculedLine.setTitle(line.title());
        calculedLine.setQuantity(line.quantity());
        calculedLine.setPrice(price);
        return calculedLine;
    }

    /**
     * A line is valid if the price is >0 and the quantity also
     * @param input line of receipt
     * @throws IllegalArgumentException Bad elements
     */
    private void validateReceiptLine(ReceiptLineDto input) {
        if (input.price() == null || input.price()<=0|| input.quantity() <= 0) {
            throw new IllegalArgumentException("Invalid price or quantity for line: " + input);
        }
    }

    /**
     * round to the next 0,05
     * @param value
     * @return rounded value
     */
    private double customRound(Double value) {
        return Math.ceil( value * 20.0) / 20.0;
    }



}
