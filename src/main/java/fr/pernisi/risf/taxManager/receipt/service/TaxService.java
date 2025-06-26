package fr.pernisi.risf.taxmanager.receipt.service;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * easy approach: with some key words,
 * we try to reconize the type of product and we calculate the associate rate
 */
@Service
public class  TaxService {
    public Double getTax(String title, double price) {

        if ( !StringUtils.hasLength(title) || price < 0) {
            throw new IllegalArgumentException("Invalid title or price");
        }
        double taxRate = 0.0;
        // Simulate tax retrieval logic, e.g., from a database or configuration
        if (isDefaultProduct(title)){
            taxRate += 0.10; // Default tax rate of 10% for non-exempt items
        }
        if(isImported(title)) {
            taxRate += 0.05; // Additional 5% tax for imported items
        }
        return taxRate * price;
    }

    private boolean isImported(String title) {
        return title.toLowerCase().contains("imported");
    }

    private boolean isDefaultProduct(String title) {
        return !title.toLowerCase().contains("book") && !title.toLowerCase().contains("chocolate") && !title.toLowerCase().contains("pill");
    }


}
