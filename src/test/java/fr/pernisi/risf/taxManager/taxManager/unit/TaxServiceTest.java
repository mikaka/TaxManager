package fr.pernisi.risf.taxmanager.taxmanager.unit;

import fr.pernisi.risf.taxmanager.receipt.service.TaxService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TaxServiceTest {

    @InjectMocks
    private TaxService taxService;

    @Test
    @DisplayName("Should return zero tax for tax-exempt product")
    void shouldReturnZeroTaxForExemptProduct() {
        double tax = taxService.getTax("book");
        assertEquals(0.0, tax, 0.001);

        tax = taxService.getTax("chocolate");
        assertEquals(0.0, tax, 0.001);

        tax = taxService.getTax("pills");
        assertEquals(0.0, tax, 0.001);
    }

    @Test
    @DisplayName("Should return correct tax for non-exempt product")
    void shouldReturnCorrectTaxForNonExemptProduct() {
        double tax = taxService.getTax("music CD");
        assertEquals(0.10, tax, 0.001);

        tax = taxService.getTax("perfumes");
        assertEquals(0.10, tax, 0.001);
    }

    @Test
    @DisplayName("Should return correct tax for imported product")
    void shouldReturnCorrectTaxForImportedProduct() {
        double tax = taxService.getTax("imported bottle of perfume");
        assertEquals(0.15, tax, 0.001);

        tax = taxService.getTax("imported box of chocolates");
        assertEquals(0.05, tax, 0.001);
    }

    @Test
    @DisplayName("Should throw  if no title")
    void shouldThrowIfNoTitle() {
        assertThrows(IllegalArgumentException.class, () -> taxService.getTax(""));

    }
}