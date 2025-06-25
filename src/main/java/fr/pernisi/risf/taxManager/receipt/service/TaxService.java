package fr.pernisi.risf.taxmanager.receipt.service;

import org.springframework.stereotype.Service;

@Service
public class  TaxService {
    public Double getTax(String title, Double price) {

        if (title == null || title.isEmpty() || price == null || price < 0) {
            throw new IllegalArgumentException("Invalid title or price");
        }
        double taxRate = 0.0;
        // Simulate tax retrieval logic, e.g., from a database or configuration
        if ((!title.toLowerCase().contains("book") && !title.toLowerCase().contains("chocolate") && !title.toLowerCase().contains("pill"))){
            taxRate += 0.10; // Default tax rate of 10% for non-exempt items
        }
        if( title.toLowerCase().contains("imported")) {
            taxRate += 0.05; // Additional 5% tax for imported items
        }
        return taxRate * price;
    }


}
