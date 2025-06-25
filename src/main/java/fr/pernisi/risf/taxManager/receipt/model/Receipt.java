package fr.pernisi.risf.taxmanager.receipt.model;

import lombok.Data;

import java.util.List;

@Data
public class Receipt {

    List<ReceiptLine> lines;
    Double totalPrice;
    Double totalTax;
}
