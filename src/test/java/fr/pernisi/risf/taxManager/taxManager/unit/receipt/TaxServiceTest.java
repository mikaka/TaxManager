package fr.pernisi.risf.taxManager.taxManager.unit.receipt;

import fr.pernisi.risf.taxManager.taxManager.receipt.service.TaxService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TaxServiceTest {

    @InjectMocks
    private TaxService taxService;

    @Test
    @DisplayName("Should return zero tax for tax-exempt product")
    void shouldReturnZeroTaxForExemptProduct() {
        double tax = taxService.getTax("book", 12.49);
        assertEquals(0.0, tax, 0.001);

        tax = taxService.getTax("chocolate", 0.1);
        assertEquals(0.0, tax, 0.001);

        tax = taxService.getTax("pills", 12.49);
        assertEquals(0.0, tax, 0.001);
    }

    @Test
    @DisplayName("Should return correct tax for non-exempt product")
    void shouldReturnCorrectTaxForNonExemptProduct() {
        NumberFormat nf = new DecimalFormat("0.00");
        double tax = taxService.getTax("music CD", 14.99);
        assertEquals(1.50, tax, 0.001);

        tax = taxService.getTax("perfumes", 30.1);
        assertEquals(3.01, tax, 0.001);
    }

    @Test
    @DisplayName("Should return correct tax for imported product")
    void shouldReturnCorrectTaxForImportedProduct() {
        double tax = taxService.getTax("imported bottle of perfume", 40.00);
        assertEquals(6.00, tax, 0.001);
    }
}