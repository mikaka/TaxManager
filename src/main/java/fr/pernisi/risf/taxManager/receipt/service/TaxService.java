package fr.pernisi.risf.taxmanager.receipt.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * easy approach: with some key words,
 * we try to reconize the type of product and we calculate the associate rate
 */
@Component
@Log4j2
public class TaxService {
    public Double getTax(String title) {
        log.info("get Tax for : "+title);
        if ( !StringUtils.hasLength(title)) {
            throw new IllegalArgumentException("Invalid title");
        }
        double taxRate = 0.0;
        // Simulate tax retrieval logic, e.g., from a database or configuration
        if (isDefaultProduct(title)){
            log.debug("it s a default product");
            taxRate += 0.10; // Default tax rate of 10% for non-exempt items
        }else{
            log.debug("it s a exempted product");
        }
        if(isImported(title)) {
            log.debug("it s a imported product");
            taxRate += 0.05; // Additional 5% tax for imported items
        }
        return taxRate;
    }

    private boolean isImported(String title) {
        return title.toLowerCase().contains("imported");
    }

    private boolean isDefaultProduct(String title) {
        return !title.toLowerCase().contains("book") && !title.toLowerCase().contains("chocolate") && !title.toLowerCase().contains("pill");
    }


}
