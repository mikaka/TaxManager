package fr.pernisi.risf.taxmanager.receipt.service.impl;


import fr.pernisi.risf.taxmanager.receipt.dto.ReceiptLineDto;
import fr.pernisi.risf.taxmanager.receipt.model.Receipt;
import fr.pernisi.risf.taxmanager.receipt.model.ReceiptLine;
import fr.pernisi.risf.taxmanager.receipt.service.TextsService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Log4j2
public class TextsServiceImpl implements TextsService {

    /**
     * Parse text to get all datas
     *
     * @param input Text to be parsed
     * @return List of ReceiptLineDto formated Object from parsing
     * @throws IllegalArgumentException in case of bad format
     */
    public List<ReceiptLineDto> parseInput(String input) {
        log.debug("parse input :" + input);
        List<ReceiptLineDto> lines = new ArrayList<>();
        if (StringUtils.hasLength(input)) {
            Pattern pattern = Pattern.compile("^.*$", Pattern.MULTILINE);
            Matcher matcher = pattern.matcher(input);

            while (matcher.find()) {
                lines.add(parseLine(matcher.group()));
            }
        }
        log.debug("Number of generated lines :" + lines.size());
        return lines;
    }

    /**
     * parse a line to get all data by line
     *
     * @param input line from the text
     * @return product line object
     */
    private ReceiptLineDto parseLine(String input) {
        String regex = "(\\d+)\\s+(.+?)\\s+at\\s+(\\d+\\.\\d{2})";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            int quantity = Integer.parseInt(matcher.group(1));
            String name = matcher.group(2);
            double price = Double.parseDouble(matcher.group(3));
            return new ReceiptLineDto(name, price, quantity);
        } else {
            throw new IllegalArgumentException("Format invalide : " + input);
        }
    }

    /**
     * format answer into an understable text
     *
     * @param receipt final model representing the receipt
     * @return The text format of the receipt
     */
    public String formatReceipt(Receipt receipt) {
        //
        DecimalFormatSymbols decimalFormatSymbols = DecimalFormatSymbols.getInstance();
        decimalFormatSymbols.setDecimalSeparator('.');
        DecimalFormat df = new DecimalFormat("0.00", decimalFormatSymbols);


        StringBuilder sb = new StringBuilder();
        for (ReceiptLine line : receipt.getLines()) {
            sb.append(line.getQuantity());
            sb.append(" ");
            sb.append(line.getTitle());
            sb.append(" : ");
            sb.append(df.format(line.getPrice()));
            sb.append("\n");
        }
        sb.append("Sales Taxes : ");
        sb.append(df.format(receipt.getTotalTax()));
        sb.append(" Total : ");
        sb.append(df.format(receipt.getTotalPrice()));
        return sb.toString();
    }
}
