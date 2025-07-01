package fr.pernisi.risf.taxmanager.taxmanager.unit;


import fr.pernisi.risf.taxmanager.receipt.dto.ReceiptLineDto;
import fr.pernisi.risf.taxmanager.receipt.model.Receipt;
import fr.pernisi.risf.taxmanager.receipt.service.impl.ReceiptServiceImpl;
import fr.pernisi.risf.taxmanager.receipt.service.TaxService;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class ReceiptServiceTest {

    @Mock
    TaxService taxService;

    @InjectMocks
    private ReceiptServiceImpl receiptService;

    List<ReceiptLineDto> receiptLineList;


    @BeforeEach
    void setUp() {

        receiptLineList = new ArrayList<>();

        receiptLineList.add(new ReceiptLineDto("book", 12.49, 1));
        receiptLineList.add(new ReceiptLineDto("music CD", 14.99, 1));
        receiptLineList.add(new ReceiptLineDto("chocolate bar", 0.85, 1));


    }


    @DisplayName("Should parse a unit empty")
    @Test
    void itSouldThowIfNoLineReceipt() {
        assertThrows(IllegalArgumentException.class, () -> receiptService.createReceipt(Collections.emptyList()));
    }

    @DisplayName("Should parse a unit full with no imported products")
    @Test
    void itSouldCreate3LineReceipt() {

        when(taxService.getTax("book")).thenReturn(0.00);
        when(taxService.getTax("music CD")).thenReturn(0.10);
        when(taxService.getTax("chocolate bar")).thenReturn(0.00);

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
        assertEquals("1,50", nf.format(receipt.getTotalTax()));

    }

    @DisplayName("Should parse a unit full with  imported products")
    @Test
    void itSouldCreateimportedLineReceipt() {

        List<ReceiptLineDto> importedReceiptLineList = new ArrayList<>();
        importedReceiptLineList.add(new ReceiptLineDto("imported box of chocolates", 10.00, 1));
        importedReceiptLineList.add(new ReceiptLineDto("imported bottle of perfume", 47.50, 1));


        when(taxService.getTax("imported box of chocolates")).thenReturn(0.05);
        when(taxService.getTax("imported bottle of perfume")).thenReturn(0.15);


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
        assertEquals("7,65", nf.format(receipt.getTotalTax()));

    }

    @DisplayName("Should round the total price and tax to the nearest 0.05")
    @Test
    void itSouldCreateRoundResult() {
        List<ReceiptLineDto> roundReceiptLineList = new ArrayList<>();
        roundReceiptLineList.add(new ReceiptLineDto("imported bottle of perfume", 27.99, 1));
        roundReceiptLineList.add(new ReceiptLineDto("bottle of perfume", 18.99, 1));
        roundReceiptLineList.add(new ReceiptLineDto("packet of headache pills", 9.75, 1));
        roundReceiptLineList.add(new ReceiptLineDto("imported box of chocolates", 11.25, 1));


        when(taxService.getTax("imported bottle of perfume")).thenReturn(0.15);
        when(taxService.getTax("bottle of perfume")).thenReturn(0.10);
        when(taxService.getTax("packet of headache pills")).thenReturn(0.0);
        when(taxService.getTax("imported box of chocolates")).thenReturn(0.05);

        Receipt receipt = receiptService.createReceipt(roundReceiptLineList);
        NumberFormat nf = new DecimalFormat("0.00");

        assertEquals(4, receipt.getLines().size());

        assertEquals("imported bottle of perfume", receipt.getLines().get(0).getTitle());
        assertEquals(1, receipt.getLines().get(0).getQuantity());
        assertEquals("32,19", nf.format(receipt.getLines().get(0).getPrice()));

        assertEquals("bottle of perfume", receipt.getLines().get(1).getTitle());
        assertEquals(1, receipt.getLines().get(1).getQuantity());
        assertEquals("20,89", nf.format(receipt.getLines().get(1).getPrice()));

        assertEquals("packet of headache pills", receipt.getLines().get(2).getTitle());
        assertEquals(1, receipt.getLines().get(2).getQuantity());
        assertEquals("9,75", nf.format(receipt.getLines().get(2).getPrice()));

        assertEquals("imported box of chocolates", receipt.getLines().get(3).getTitle());
        assertEquals(1, receipt.getLines().get(3).getQuantity());
        assertEquals("11,85", nf.format(receipt.getLines().get(3).getPrice()));


        assertEquals("6,70", nf.format(receipt.getTotalTax()));
        assertEquals("74,68", nf.format(receipt.getTotalPrice()));

    }

    @DisplayName("Should throw if bad information")
    @Test
    void itSouldThrowIfBadEinformation() {
        List<ReceiptLineDto> importedReceiptLineList = new ArrayList<>();
        importedReceiptLineList.add(new ReceiptLineDto("imported box of chocolates", 10.00, -3));
        assertThrows(IllegalArgumentException.class, () -> receiptService.createReceipt(importedReceiptLineList));
    }

    @DisplayName("Should throw if bad information price ")
    @Test
    void itSouldThrowIfBadInformationPrice() {

        assertThrows(IllegalArgumentException.class, () -> receiptService.createReceipt(
                List.of(new ReceiptLineDto("imported box of chocolates", -10.00, 1))));
    }

    @DisplayName("Should throw if bad information title")
    @Test
    void itSouldThrowIfBadinformationTitle() {

        assertThrows(IllegalArgumentException.class, () -> receiptService.createReceipt(
                List.of(new ReceiptLineDto("", 10.00, 1))));
    }
}