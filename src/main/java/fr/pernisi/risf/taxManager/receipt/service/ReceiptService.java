package fr.pernisi.risf.taxmanager.receipt.service;



import fr.pernisi.risf.taxmanager.receipt.model.Receipt;
import fr.pernisi.risf.taxmanager.receipt.model.ReceiptLine;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReceiptService {

    private final TaxService taxService;

    public Receipt createReceipt(List<ReceiptLine> inputs) {


        var receipt = new Receipt();
        double totalPrice = 0.0;
        double totalTax = 0.0;

        List<ReceiptLine> lines = new ArrayList<>();
        if (inputs != null && !inputs.isEmpty()) {
            for( ReceiptLine input : inputs) {

                validateReceiptLine(input);

                Double taxPrice = customRound(taxService.getTax(input.getTitle(), input.getPrice()));
                Double price = (input.getPrice()+taxPrice) * input.getQuantity();
                totalPrice += price;
                totalTax += taxPrice * input.getQuantity();

                var calculedLine = new ReceiptLine();
                calculedLine.setTitle(input.getTitle());
                calculedLine.setQuantity(input.getQuantity());
                calculedLine.setPrice((input.getPrice() + taxPrice) * input.getQuantity()); // Round to 2 decimal places
                lines.add(calculedLine);
            }

        }
        receipt.setLines(lines);
        receipt.setTotalTax(totalTax); // Round to 2 decimal places
        receipt.setTotalPrice(totalPrice);

        return receipt;
    }

    /**
     * A line is valid if the price is >0 and the quantity also
     * @param input line of receipt
     * @throws IllegalArgumentException Bad elements
     */
    private void validateReceiptLine(ReceiptLine input) {
        if (input.getPrice() == null || input.getPrice() != null && input.getPrice()<=0|| input.getQuantity() <= 0) {
            throw new IllegalArgumentException("Invalid price or quantity for line: " + input);
        }
    }

    private double customRound(Double value) {
        return Math.ceil( value * 20.0) / 20.0;
    }



}
