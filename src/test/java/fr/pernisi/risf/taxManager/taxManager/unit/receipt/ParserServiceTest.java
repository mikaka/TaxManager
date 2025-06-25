package fr.pernisi.risf.taxManager.taxManager.unit.receipt;



import fr.pernisi.risf.taxManager.taxManager.receipt.model.ReceiptLine;
import fr.pernisi.risf.taxManager.taxManager.receipt.service.ParserService;
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

    @DisplayName("Should parse a receipt with one line")
    @Test
    void itSouldParseReceipt1Line() {
        String input ="1 book at 12.49";
        List<ReceiptLine> receiptLineList = parserService.parseInput(input);
        assertEquals(1, receiptLineList.size());
        assertEquals("book", receiptLineList.get(0).getTitle());
        assertEquals(12.49, receiptLineList.get(0).getPrice());
        assertEquals(1, receiptLineList.get(0).getQuantity());
    }

    @DisplayName("Should parse a receipt with several line")
    @Test
    void itSouldParseReceipt3Line() {
        String input = """
                1 book at 12.49
                1 music CD at 14.99
                1 chocolate bar at 0.85
                """;
        List<ReceiptLine> receiptLineList = parserService.parseInput(input);
        assertEquals(3, receiptLineList.size());
        assertEquals("book", receiptLineList.get(0).getTitle());
        assertEquals(12.49, receiptLineList.get(0).getPrice());
        assertEquals(1, receiptLineList.get(0).getQuantity());

        assertEquals("music CD", receiptLineList.get(1).getTitle());
        assertEquals(14.99, receiptLineList.get(1).getPrice());
        assertEquals(1, receiptLineList.get(1).getQuantity());

        assertEquals("chocolate bar", receiptLineList.get(2).getTitle());
        assertEquals(0.85, receiptLineList.get(2).getPrice());
        assertEquals(1, receiptLineList.get(2).getQuantity());
    }

    @DisplayName("Should parse a receipt with no line")
    @Test
    void itSouldParseReceiptNoLine() {

        String input = "";
        itshouldreturnNoline(input);

        input = null;
        itshouldreturnNoline(input);
    }

    private void itshouldreturnNoline(String input) {
        List<ReceiptLine> receiptLineList = parserService.parseInput(input);
        assertEquals(0, receiptLineList.size());
    }

}