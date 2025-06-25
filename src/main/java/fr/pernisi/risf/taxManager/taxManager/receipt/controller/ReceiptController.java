package fr.pernisi.risf.taxManager.taxManager.receipt.controller;


import fr.pernisi.risf.taxManager.taxManager.receipt.model.Receipt;
import fr.pernisi.risf.taxManager.taxManager.receipt.model.ReceiptLine;
import fr.pernisi.risf.taxManager.taxManager.receipt.service.ParserService;
import fr.pernisi.risf.taxManager.taxManager.receipt.service.ReceiptService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/receipts")
public class ReceiptController {

    private final ParserService parserService;
    private final ReceiptService receiptService;

    @PostMapping
    public String createReceipt(@RequestBody String input) {

        List<ReceiptLine> receiptLineList = parserService.parseInput(input);
        Receipt receipt = receiptService.createReceipt(receiptLineList);

        return parserService.formatReceipt(receipt);
    }
}