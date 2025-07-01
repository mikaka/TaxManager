package fr.pernisi.risf.taxmanager.receipt.service.impl;

import fr.pernisi.risf.taxmanager.receipt.service.TaxService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * easy approach: with some key words,
 * we try to reconize the type of product and we calculate the associate rate
 */
@Service
public class TaxServiceImpl implements TaxService {

    /**
     * Get taxe relative to the Article ( try to guess the type of product)
     *
     * @param title
     * @return tax rate
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
     * @return true if it's an imported product
     */
    private boolean isImported(String title) {
        return title.toLowerCase().contains("imported");
    }

    /**
     * If the product is a default good ( not exempted)
     *
     * @param title
     * @return true if it's a normal product ( not exempted)
     */
    private boolean isDefaultProduct(String title) {
        return !title.toLowerCase().contains("book") && !title.toLowerCase().contains("chocolate") && !title.toLowerCase().contains("pill");
    }


}
