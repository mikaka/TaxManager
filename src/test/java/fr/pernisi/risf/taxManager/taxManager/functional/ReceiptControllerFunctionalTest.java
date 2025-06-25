package fr.pernisi.risf.taxManager.taxmanager.functional;



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
    }
}