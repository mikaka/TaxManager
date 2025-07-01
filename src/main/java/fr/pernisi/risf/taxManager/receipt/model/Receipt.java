package fr.pernisi.risf.taxmanager.receipt.model;

import lombok.Data;

import java.util.List;

@Data
public class Receipt {

    private List<ReceiptLine> lines;
    private Double totalPrice;
    private Double totalTax;
}
