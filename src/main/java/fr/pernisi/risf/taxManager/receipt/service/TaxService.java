package fr.pernisi.risf.taxmanager.receipt.service;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * easy approach: with some key words,
 * we try to reconize the type of product and we calculate the associate rate
 */
@Component
public class TaxService {

    /**
     * Get taxe relative to the Article ( try to guess the type of product)
     *
     * @param title
     * @return
     */
    public Double getTax(String title) {

        if (!StringUtils.hasLength(title)) {
            throw new IllegalArgumentException("Invalid title");
        }
        double taxRate = 0.0;
        // Simulate tax retrieval logic, e.g., from a database or configuration
        if (isDefaultProduct(title)) {
            taxRate += 0.10; // Default tax rate of 10% for non-exempt items
        }
        if (isImported(title)) {
            taxRate += 0.05; // Additional 5% tax for imported items
        }
        return taxRate;
    }

    /**
     * if the article is imported
     *
     * @param title
     * @return
     */
    private boolean isImported(String title) {
        return title.toLowerCase().contains("imported");
    }

    /**
     * If the product is a default good ( not exempted)
     *
     * @param title
     * @return
     */
    private boolean isDefaultProduct(String title) {
        return !title.toLowerCase().contains("book") && !title.toLowerCase().contains("chocolate") && !title.toLowerCase().contains("pill");
    }


}
