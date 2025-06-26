package fr.pernisi.risf.taxmanager.receipt.service;


import fr.pernisi.risf.taxmanager.receipt.dto.ReceiptLineDto;
import fr.pernisi.risf.taxmanager.receipt.model.Receipt;
import fr.pernisi.risf.taxmanager.receipt.model.ReceiptLine;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Log4j2
public class ReceiptService {

    private final TaxService taxService;

    /**
     * Create a receipt object ( could be transformed into an entity, in the next evolution)
     *
     * @param inputs datas from parser
     * @return Receipt object
     */
    public Receipt createReceipt(List<ReceiptLineDto> inputs) {
        log.info("Creation of the Receipt object");
        if (inputs == null || inputs.isEmpty()) {
            throw new IllegalArgumentException("No line in input");
        }

        var receipt = new Receipt();
        double totalPrice = 0.0;
        double totalTax = 0.0;

        List<ReceiptLine> lines = new ArrayList<>();
        for (ReceiptLineDto lineDto : inputs) {
            validateReceiptLine(lineDto);
            double taxPriceUnit = calculateTaxePriceLine(lineDto);
            double priceUnit = lineDto.price() + taxPriceUnit;

            totalPrice += priceUnit * lineDto.quantity();
            totalTax += taxPriceUnit * lineDto.quantity();

            lines.add(buildLineReceipt(lineDto, priceUnit * lineDto.quantity()));
        }

        receipt.setLines(lines);
        receipt.setTotalTax(totalTax); // Round to 2 decimal places
        receipt.setTotalPrice(totalPrice);

        return receipt;
    }

    /**
     * round 0,05 => ( rate * price )
     * @param lineProduct
     * @return
     */
    private double calculateTaxePriceLine(ReceiptLineDto lineProduct) {
        return customRound(taxService.getTax(lineProduct.title()) * lineProduct.price());
    }

    private ReceiptLine buildLineReceipt(ReceiptLineDto line, double price) {
        var calculedLine = new ReceiptLine();
        calculedLine.setTitle(line.title());
        calculedLine.setQuantity(line.quantity());
        calculedLine.setPrice(price);
        return calculedLine;
    }

    /**
     * A line is valid if the price is >0 and the quantity also
     *
     * @param input line of receipt
     * @throws IllegalArgumentException Bad elements
     */
    private void validateReceiptLine(ReceiptLineDto input) {
        if (input.price() == null || input.price() <= 0 || input.quantity() <= 0) {
            throw new IllegalArgumentException("Invalid price or quantity for line: " + input);
        }
        if (!StringUtils.hasLength(input.title())) {
            throw new IllegalArgumentException("No title for : " + input);
        }
    }

    /**
     * round to the next 0,05
     *
     * @param value to be rounded
     * @return rounded value
     */
    private double customRound(Double value) {
        return Math.ceil(value * 20.0) / 20.0;
    }


}
