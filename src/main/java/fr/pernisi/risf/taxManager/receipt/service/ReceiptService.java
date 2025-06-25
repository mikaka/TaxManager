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

        validateReceiptLines(inputs);
        var receipt = new Receipt();
        Double totalPrice = 0.0;
        Double totalTax = 0.0;

        List<ReceiptLine> lines = new ArrayList<>();
        if (inputs != null && !inputs.isEmpty()) {
            for( ReceiptLine input : inputs) {
                Double taxPrice = taxService.getTax(input.getTitle(), input.getPrice());
                Double price = (input.getPrice()+taxPrice) * input.getQuantity();
                totalPrice += price;
                totalTax += taxPrice * input.getQuantity();

                var calculedLine = new ReceiptLine();
                calculedLine.setTitle(input.getTitle());
                calculedLine.setQuantity(input.getQuantity());
                calculedLine.setPrice(input.getPrice() + taxPrice * input.getQuantity());
                lines.add(calculedLine);
            }

        }
        receipt.setLines(lines);
        receipt.setTotalTax(totalTax);
        receipt.setTotalPrice(totalPrice);

        return receipt;
    }



    public void validateReceiptLines(List<ReceiptLine> receiptLineList) {
        if (receiptLineList != null && !receiptLineList.isEmpty()) {
            receiptLineList.forEach( line -> {
                if (line.getPrice() == null && line.getPrice()>=0|| line.getQuantity() <= 0) {
                    throw new IllegalArgumentException("Invalid price or quantity for line: " + line);
                }
            });


        }
    }

}
