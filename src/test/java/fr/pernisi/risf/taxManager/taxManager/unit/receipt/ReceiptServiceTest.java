package fr.pernisi.risf.taxManager.taxManager.unit.receipt;


import fr.pernisi.risf.taxManager.taxManager.receipt.model.Receipt;
import fr.pernisi.risf.taxManager.taxManager.receipt.model.ReceiptLine;
import fr.pernisi.risf.taxManager.taxManager.receipt.service.ReceiptService;
import fr.pernisi.risf.taxManager.taxManager.receipt.service.TaxService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class ReceiptServiceTest {

    @Mock
    TaxService taxService;

    @InjectMocks
    private ReceiptService receiptService;

    List<ReceiptLine> receiptLineList;
    List<ReceiptLine> importedReceiptLineList;

    @BeforeEach
    void setUp() {

        receiptLineList = new ArrayList<>();

        receiptLineList.add(new ReceiptLine("book", 12.49, 1));
        receiptLineList.add(new ReceiptLine("music CD", 14.99, 1));
        receiptLineList.add(new ReceiptLine("chocolate bar", 0.85, 1));

        importedReceiptLineList = new ArrayList<>();

        importedReceiptLineList.add(new ReceiptLine("imported box of chocolates", 10.00, 1));
        importedReceiptLineList.add(new ReceiptLine("imported bottle of perfume", 47.50, 1));

    }


    @DisplayName("Should parse a receipt empty")
    @Test
    void itSouldCreateNoLineReceipt() {

        Receipt receipt = receiptService.createReceipt(Collections.emptyList());
        assertEquals(0, receipt.getLines().size());
        assertEquals(0.0, receipt.getTotalPrice());
        assertEquals(0.0, receipt.getTotalTax());

    }

    @DisplayName("Should parse a receipt full with no imported products")
    @Test
    void itSouldCreate3LineReceipt() {

        when(taxService.getTax("book", 12.49)).thenReturn(0.00);
        when(taxService.getTax("music CD", 14.99)).thenReturn(1.50);
        when(taxService.getTax("chocolate bar", 0.85)).thenReturn(0.00);

        Receipt receipt = receiptService.createReceipt(receiptLineList);
        NumberFormat nf = new DecimalFormat("0.00");

        assertEquals(3, receipt.getLines().size());
        assertEquals("book", receipt.getLines().get(0).getTitle());
        assertEquals(1, receipt.getLines().get(0).getQuantity());
        assertEquals("12,49", nf.format(receipt.getLines().get(0).getPrice()));

        assertEquals("music CD", receipt.getLines().get(1).getTitle());
        assertEquals(1, receipt.getLines().get(1).getQuantity());
        assertEquals("16,49", nf.format(receipt.getLines().get(1).getPrice()));

        assertEquals("chocolate bar", receipt.getLines().get(2).getTitle());
        assertEquals(1, receipt.getLines().get(2).getQuantity());
        assertEquals("0,85", nf.format(receipt.getLines().get(2).getPrice()));

        assertEquals("29,83", nf.format(receipt.getTotalPrice()));
        assertEquals("1,50" , nf.format(receipt.getTotalTax()));

    }

    @DisplayName("Should parse a receipt full with  imported products")
    @Test
    void itSouldCreateimportedLineReceipt() {

        when(taxService.getTax("imported box of chocolates", 10.00)).thenReturn(0.50);
        when(taxService.getTax("imported bottle of perfume", 47.50)).thenReturn(7.15);


        Receipt receipt = receiptService.createReceipt(importedReceiptLineList);
        NumberFormat nf = new DecimalFormat("0.00");

        assertEquals(2, receipt.getLines().size());

        assertEquals("imported box of chocolates", receipt.getLines().get(0).getTitle());
        assertEquals(1, receipt.getLines().get(0).getQuantity());
        assertEquals("10,50", nf.format(receipt.getLines().get(0).getPrice()));

        assertEquals("imported bottle of perfume", receipt.getLines().get(1).getTitle());
        assertEquals(1, receipt.getLines().get(1).getQuantity());
        assertEquals("54,65", nf.format(receipt.getLines().get(1).getPrice()));


        assertEquals("65,15", nf.format(receipt.getTotalPrice()));
        assertEquals("7,65" , nf.format(receipt.getTotalTax()));

    }


}