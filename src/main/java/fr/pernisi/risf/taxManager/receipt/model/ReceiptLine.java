package fr.pernisi.risf.taxmanager.receipt.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReceiptLine {

    private String title;
    private Double price;
    private int quantity;


}
