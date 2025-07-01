package fr.pernisi.risf.taxmanager.receipt.service;

import fr.pernisi.risf.taxmanager.receipt.dto.ReceiptLineDto;
import fr.pernisi.risf.taxmanager.receipt.model.Receipt;

import java.util.List;

public interface ReceiptService {
    Receipt createReceipt(List<ReceiptLineDto> inputs);
}
