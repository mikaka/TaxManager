package fr.pernisi.risf.taxmanager.receipt.controller;


import fr.pernisi.risf.taxmanager.receipt.dto.ReceiptLineDto;
import fr.pernisi.risf.taxmanager.receipt.model.Receipt;
import fr.pernisi.risf.taxmanager.receipt.service.TextsService;
import fr.pernisi.risf.taxmanager.receipt.service.ReceiptService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/receipts")
@Log4j2
public class ReceiptController {

    private final TextsService textsService;
    private final ReceiptService receiptService;

    /**
     * Request for creating a new receipt from an text input
     *
     * @param input
     * @return
     */
    @PostMapping
    public String createReceipt(@RequestBody String input) {
        log.info("create a unit for " + input);
        List<ReceiptLineDto> receiptLineList = textsService.parseInput(input);
        Receipt receipt = receiptService.createReceipt(receiptLineList);

        return textsService.formatReceipt(receipt);
    }

}