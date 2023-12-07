package ch.ipt.see.playground;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MathControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void calculateRectangleArea_twoDimensional() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/math/area_computation/hyperrectangle")
                        .content("<calculation><side length=\"4\"/><side length=\"8\"/></calculation>")
                        .contentType(MediaType.APPLICATION_XML)
                )
                .andExpect(status().isOk())
                .andExpect(content().xml("<calculation><area>32</area></calculation>"));
    }

    @Test
    void calculateRectangleArea_threeDimensional() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/math/area_computation/hyperrectangle")
                        .content("<calculation><side length=\"4\"/><side length=\"8\"/><side length=\"2\"/></calculation>")
                        .contentType(MediaType.APPLICATION_XML)
                )
                .andExpect(status().isOk())
                .andExpect(content().xml("<calculation><area>64</area></calculation>"));
    }
}
