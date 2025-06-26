package fr.pernisi.risf.taxmanager.receipt.service;


import fr.pernisi.risf.taxmanager.receipt.dto.ReceiptLineDto;
import fr.pernisi.risf.taxmanager.receipt.model.Receipt;
import fr.pernisi.risf.taxmanager.receipt.model.ReceiptLine;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Log4j2
public class TextsService {

    /**
     * Parse input
     *
     * @param input
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
                parseLine(matcher.group(), lines);
            }
        }
        log.debug("Number of generated lines :" + lines.size());
        return lines;
    }

    /**
     * parse a line
     *
     * @param input
     * @param lines
     */
    private void parseLine(String input, List<ReceiptLineDto> lines) {
        String regex = "(\\d+)\\s+(.+?)\\s+at\\s+(\\d+\\.\\d{2})";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            int quantity = Integer.parseInt(matcher.group(1));
            String name = matcher.group(2);
            double price = Double.parseDouble(matcher.group(3));
            lines.add(new ReceiptLineDto(name, price, quantity));
        } else {
            throw new IllegalArgumentException("Format invalide : " + input);
        }
    }

    /**
     * format answer into an understable text
     *
     * @param receipt
     * @return
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
