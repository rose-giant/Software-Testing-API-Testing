package ApiTest;

import application.BalootApplication;
import controllers.CommoditiesController;
import exceptions.NotExistentCommodity;
import model.Commodity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import service.Baloot;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = BalootApplication.class)
public class CommodityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    public Baloot mockBaloot;

    private CommoditiesController mockCommodityController = new CommoditiesController();

    private Commodity mockCommodity = new Commodity();

    public ArrayList<Commodity> mockCommodityList = new ArrayList<>();
    public String sampleCommodityId = "id";
    public String sampleCommodityName = "razi";
    public String sampleCommodityProviderId = "pId";
    public int sampleCommodityPrice = 1;
    public ArrayList<String> sampleCommodityCategories = new ArrayList<>();
    public float sampleCommodityRating = 2;
    public int sampleCommodityInStock = 3;
    public String sampleCommodityImage = "image";
    public Map<String, Integer> sampleCommodityUserRate = new HashMap<>();
    public float sampleCommodityInitRate = 4;

    @BeforeEach
    public void setup() {
        mockBaloot = Mockito.mock(Baloot.class);
        mockCommodityController.setBaloot(mockBaloot);

        mockCommodity.setId(sampleCommodityId);
        mockCommodity.setName(sampleCommodityName);
        mockCommodity.setProviderId(sampleCommodityProviderId);
        mockCommodity.setPrice(sampleCommodityPrice);
        mockCommodity.setCategories(sampleCommodityCategories);
        mockCommodity.setRating(sampleCommodityRating);
        mockCommodity.setInStock(sampleCommodityInStock);
        mockCommodity.setImage(sampleCommodityImage);
        mockCommodity.setUserRate(sampleCommodityUserRate);
        mockCommodity.setInitRate(sampleCommodityInitRate);
    }

    @ParameterizedTest
    @ValueSource(strings = {"/commodities"})
    public void getCommoditiesReturnsMockCommodityList(String apiUrl) throws Exception {

        mockCommodityList.add(mockCommodity);
        when(mockBaloot.getCommodities()).thenReturn(mockCommodityList);
        mockMvc.perform(get(apiUrl)).andDo(MockMvcResultHandlers.print()).
                andExpect(status().isOk());
    }

}














