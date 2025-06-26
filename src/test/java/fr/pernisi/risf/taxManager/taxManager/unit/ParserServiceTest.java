package fr.pernisi.risf.taxmanager.taxmanager.unit;


import fr.pernisi.risf.taxmanager.receipt.dto.ReceiptLineDto;
import fr.pernisi.risf.taxmanager.receipt.service.ParserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith(MockitoExtension.class)
class ParserServiceTest {


    @InjectMocks
    private ParserService parserService;

    @DisplayName("Should parse a unit with one line")
    @Test
    void itSouldParseReceipt1Line() {
        String input ="1 book at 12.49";
        List<ReceiptLineDto> receiptLineList = parserService.parseInput(input);
        assertEquals(1, receiptLineList.size());
        assertEquals("book", receiptLineList.get(0).title());
        assertEquals(12.49, receiptLineList.get(0).price());
        assertEquals(1, receiptLineList.get(0).quantity());
    }

    @DisplayName("Should parse a unit with several line")
    @Test
    void itSouldParseReceipt3Line() {
        String input = """
                1 book at 12.49
                1 music CD at 14.99
                1 chocolate bar at 0.85
                """;
        List<ReceiptLineDto> receiptLineList = parserService.parseInput(input);
        assertEquals(3, receiptLineList.size());
        assertEquals("book", receiptLineList.get(0).title());
        assertEquals(12.49, receiptLineList.get(0).price());
        assertEquals(1, receiptLineList.get(0).quantity());

        assertEquals("music CD", receiptLineList.get(1).title());
        assertEquals(14.99, receiptLineList.get(1).price());
        assertEquals(1, receiptLineList.get(1).quantity());

        assertEquals("chocolate bar", receiptLineList.get(2).title());
        assertEquals(0.85, receiptLineList.get(2).price());
        assertEquals(1, receiptLineList.get(2).quantity());
    }

    @DisplayName("Should parse a unit with no line")
    @Test
    void itSouldParseReceiptNoLine() {

        String input = "";
        itshouldreturnNoline(input);

        input = null;
        itshouldreturnNoline(input);
    }

    private void itshouldreturnNoline(String input) {
        List<ReceiptLineDto> receiptLineList = parserService.parseInput(input);
        assertEquals(0, receiptLineList.size());
    }

}