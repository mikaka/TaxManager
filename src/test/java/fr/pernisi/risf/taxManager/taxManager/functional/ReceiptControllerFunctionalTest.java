package fr.pernisi.risf.taxmanager.taxmanager.functional;



import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ReceiptControllerFunctionalTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnExpectedReceipt() throws Exception {
        String input = "1 book at 12.49\n1 music CD at 14.99\n1 chocolate bar at 0.85";
        String expectedOutput = "1 book : 12.49\n1 music CD : 16.49\n1 chocolate bar : 0.85\nSales Taxes : 1.50 Total : 29.83";

        mockMvc.perform(post("/receipts")
                .contentType(MediaType.TEXT_PLAIN)
                .content(input))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedOutput));


        String input2 = """
                1 imported bottle of perfume at 27.99
                1 bottle of perfume at 18.99
                1 packet of headache pills at 9.75
                1 imported box of chocolates at 11.25
                """;
        String expectedOutput2 = """
                1 imported bottle of perfume : 32.19
                1 bottle of perfume : 20.89
                1 packet of headache pills : 9.75
                1 imported box of chocolates : 11.85
                Sales Taxes : 6.70 Total : 74.68
                """;

        mockMvc.perform(post("/receipts")
                        .contentType(MediaType.TEXT_PLAIN)
                        .content(input2))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedOutput2.trim()));
    }


}