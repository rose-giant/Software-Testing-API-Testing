package controllers;

import application.BalootApplication;
import exceptions.NotExistentCommodity;
import model.Comment;
import model.Commodity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import service.Baloot;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = BalootApplication.class)
public class CommodityControllerTest {

   @Autowired
   private MockMvc mockMvc;
   @MockBean
   public Baloot mockBaloot;
   public String EXISTING_COMMODITY_ID = "1";
   public String responseString = "[{\"id\":\"1\",\"name\":\"iPhone\",\"providerId\":\"1\",\"price\":100,\"categories\":[\"phone\",\"tech\"],\"rating\":9.8,\"inStock\":100,\"image\":\"\",\"userRate\":{},\"initRate\":0.0},{\"id\":\"2\",\"name\":\"Galaxy\",\"providerId\":\"2\",\"price\":100,\"categories\":[\"phone\",\"tech\"],\"rating\":8.8,\"inStock\":100,\"image\":\"\",\"userRate\":{},\"initRate\":0.0}]";

   public String commodityResponse1 = "[{\"id\":\"1\",\"name\":\"iPhone\",\"providerId\":\"1\",\"price\":100,\"categories\":[\"phone\",\"tech\"],\"rating\":9.8,\"inStock\":100,\"image\":\"\",\"userRate\":{},\"initRate\":0.0}]";
   public String commodityResponse2 = "[{\"id\":\"2\",\"name\":\"Galaxy\",\"providerId\":\"2\",\"price\":100,\"categories\":[\"phone\",\"tech\"],\"rating\":8.8,\"inStock\":100,\"image\":\"\",\"userRate\":{},\"initRate\":0.0}]";
   public ArrayList<String> commodityResponseArray = new ArrayList<>();


   public String commodity1CommentResponse = "[{\n" +
           "    \"userEmail\": \"amin@gmail.com\",\n" +
           "    \"username\": \"amin\",\n" +
           "    \"commodityId\": 1,\n" +
           "    \"text\": \"good\",\n" +
           "    \"date\": \"2023-01-01\"\n" +
           "  }]";
    public CommoditiesController mockCommodityController = new CommoditiesController();
    public Commodity mockCommodity = new Commodity();

    public ArrayList<Comment> mockCommentList = new ArrayList<>();
    public ArrayList<Commodity> mockCommodityList = new ArrayList<>();

    public String sampleCommodityId = "1";
    public String sampleCommodityName = "iPhone";
    public String sampleCommodityProviderId = "1";
    public int sampleCommodityPrice = 100;
    public ArrayList<String> sampleCommodityCategories = new ArrayList<>();

    public float sampleCommodityRating = 9.8F;
    public int sampleCommodityInStock = 100;
    public String sampleCommodityImage = "";
    public Map<String, Integer> sampleCommodityUserRate = new HashMap<>();
    public float sampleCommodityInitRate = 0.0F;

    public Comment mockComment = new Comment();
    public String sampleCommentUserEmail = "amin@gmail.com";
    public String sampleCommentUsername = "amin";
    public int sampleCommentCommodityId = 1;
    public String sampleCommentText = "good";
    public String sampleCommentDate = "2023-01-01";
    
    @BeforeEach
    public void setup() {
        mockBaloot = new Baloot();
        mockBaloot = Mockito.mock(Baloot.class);
        mockCommodityController.setBaloot(mockBaloot);

        commodityResponseArray.add(commodityResponse1);
        commodityResponseArray.add(commodityResponse2);

        sampleCommodityCategories.add("phone");
        sampleCommodityCategories.add("tech");

        mockComment.setId(sampleCommentCommodityId);
        mockComment.setUserEmail(sampleCommentUserEmail);
        mockComment.setUsername(sampleCommentUsername);
        mockComment.setText(sampleCommentText);
        mockComment.setDate(sampleCommentDate);

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

        mockCommodityList.add(mockCommodity);
    }

    @Test
    public void getCommoditiesReturnsStatusOKWithMockBeanService() throws Exception {
        String apiURL = "/commodities";
        mockMvc.perform(get(apiURL)).andExpect(status().isOk());
    }

    @Test
    public void getCommoditiesReturnsMockCommodityListWithMockBeanService() throws Exception {
        String apiURL = "/commodities";
        mockMvc.perform(get(apiURL)).
        andDo(MockMvcResultHandlers.print()).
                andExpect(content().json(responseString));
    }

    @ParameterizedTest
    @ValueSource(strings = {"1", "2"})
    public void getCommoditiesByIdReturnsOKStatusWhenCalledWithExistingId(String id) throws Exception {
        String apiURL = "/commodities/{id}";
        mockMvc.perform(get(apiURL, id)).
                andExpect(status().isOk());
    }

    @ParameterizedTest
    @ValueSource(strings = {"1", "2"})
    public void getCommoditiesByIdReturnsExpectedResponseWhenCalledWithExistingId(String id) throws Exception {
        String apiURL = "/commodities/{id}";
        mockMvc.perform(get(apiURL, id)).
                andExpect(content().json(commodityResponseArray.get(Integer.parseInt(id) - 1)));
    }

    @ParameterizedTest
    @ValueSource(strings = {"11", "sfs", "-21"})
    public void getCommoditiesByIdReturnsNOTFOUNDStatusWhenCalledWithNoneExistingId(String id) throws Exception {
        String apiURL = "/commodities/{id}";
        mockMvc.perform(get(apiURL, id)).
                andExpect(status().is(404));
    }

    @ParameterizedTest
    @ValueSource(strings = {"11", "sfs", "-21"})
    public void getCommoditiesByIdReturnsEmptyResponseWhenCalledWithNoneExistingId(String id) throws Exception {
        String apiURL = "/commodities/{id}";
        mockMvc.perform(get(apiURL, id)).
                andExpect(content().string(""));
    }

    //issue yaftam: when there are not any commodities, the exception is not thrown
    @ParameterizedTest
    @ValueSource(strings = {"11", "sfs", "-21"})
    public void getCommoditiesByIdReturnsNOTExistingCommodityExceptionWhenCalledWithNoneExistingId(String id) throws Exception {
        String apiURL = "/commodities/{id}";
        mockMvc.perform(get(apiURL, id)).andExpect(result -> assertTrue(result.getResolvedException() instanceof NotExistentCommodity));
    }


//    @ParameterizedTest
//    @ValueSource(strings = {"0", "232", "sd"})
//    public void rateCommodityReturnsNOTFOUNDStatusForRatingToANoneExistingCommodity(String id) throws Exception {
//        Map<String, String> userRate = new HashMap<>();
//        userRate.put("username", "rose");
//        userRate.put("rate", "9");
//        String apiUrl = "/commodities/{id}/rate";
//        mockMvc.perform(post(apiUrl, id)
//                        .param("rate", userRate.get("rate"))
//                .param("username", userRate.get("username")))
//                .andExpect(status().is(404));
//    }

//
//    @Test
//    public void addCommodityCommentAddsCommentByUsernameToAnExistingCommodity() throws Exception {
//        String apiUrl = "/commodities/{id}/comment" ;
//
//        Map<String, String> userComment = new HashMap<>();
//        userComment.put("username", "rose");
//        userComment.put("comment", "cool");
//
//        mockMvc.perform(post(apiUrl, EXISTING_COMMODITY_ID).param("username", userComment.get("username"))
//                .param("comment", userComment.get("comment"))).andDo(MockMvcResultHandlers.print());
//    }

    @Test
    public void getCommodityCommentReturnsCommentsForExistingCommodity() throws Exception {
        String apiUrl = "/commodities/{id}/comment";
        String commodityId = "1";
        mockCommentList.add(mockComment);
        mockMvc.perform(get(apiUrl, commodityId)).andExpect(content().json(commodity1CommentResponse));
    }

    @Test
    public void getCommodityCommentReturnsOKStatusForExistingCommodity() throws Exception {
        String apiUrl = "/commodities/{id}/comment";
        String commodityId = "1";
        mockCommentList.add(mockComment);
        mockMvc.perform(get(apiUrl, commodityId)).andExpect(status().isOk());
    }


    //issue yaftam: status 200 is returned when the is does not exist
//    @Test
//    public void getCommodityCommentReturnsNOTFOUNDStatusForNoneExistingCommodity() throws Exception {
//        String apiUrl = "/commodities/{id}/comment";
//        String NoneExistingCommodityId = "311";
//        mockCommentList.add(mockComment);
//        NotExistentCommodity nec = new NotExistentCommodity();
//
//        mockMvc.perform(get(apiUrl, NoneExistingCommodityId)).andExpect(result -> assertEquals(nec.getMessage(), result.getResolvedException()));
//    }

    @ParameterizedTest
    @ValueSource(strings = {"", "sdfsdfsd", "903i393"})
    public void searchCommoditiesReturnsEmptyListForInvalidSearchOption(String invalidSearchOption) throws Exception {
        Map<String, String> input = new HashMap<>();
        input.put("searchOption", invalidSearchOption);
        input.put("searchValue", "a");
        assertEquals(mockCommodityController.searchCommodities(input), new ResponseEntity<>(new ArrayList(), HttpStatus.OK));
    }

    @ParameterizedTest
    @ValueSource(strings = {"a", "sdf", "3232"})
    public void searchCommoditiesReturnsMockCommodityListForNameOptionAndDifferentSearchValues(String searchValue) {
        Map<String, String> input = new HashMap<>();
        input.put("searchOption", "name");
        input.put("searchValue", searchValue);
        when(mockBaloot.filterCommoditiesByName(input.get("searchValue"))).thenReturn(mockCommodityList);
        assertEquals(mockCommodityController.searchCommodities(input), new ResponseEntity<>(mockCommodityList, HttpStatus.OK));
    }

    @ParameterizedTest
    @ValueSource(strings = {"a", "sdf", "3232"})
    public void searchCommoditiesReturnsMockCommodityListForCategoryOptionAndDifferentSearchValues(String searchValue) {
        Map<String, String> input = new HashMap<>();
        input.put("searchOption", "category");
        input.put("searchValue", searchValue);
        when(mockBaloot.filterCommoditiesByCategory(input.get("searchValue"))).thenReturn(mockCommodityList);
        assertEquals(mockCommodityController.searchCommodities(input), new ResponseEntity<>(mockCommodityList, HttpStatus.OK));
    }

    @ParameterizedTest
    @ValueSource(strings = {"a", "sdf", "3232"})
    public void searchCommoditiesReturnsMockCommodityListForProviderOptionAndDifferentSearchValues(String searchValue) {
        Map<String, String> input = new HashMap<>();
        input.put("searchOption", "provider");
        input.put("searchValue", searchValue);
        when(mockBaloot.filterCommoditiesByProviderName(input.get("searchValue"))).thenReturn(mockCommodityList);
        assertEquals(mockCommodityController.searchCommodities(input), new ResponseEntity<>(mockCommodityList, HttpStatus.OK));
    }

    @Test
    public void getSuggestedCommoditiesReturnsOKStatusForAnExistingCommodity() throws Exception{
        String apiUrl = "/commodities/{id}/suggested";
        mockMvc.perform(get(apiUrl, EXISTING_COMMODITY_ID)).
        andDo(MockMvcResultHandlers.print())
        .andExpect(status().isOk());
    }

    @Test
    public void getSuggestedCommoditiesForId1ReturnsCommodity2OAsResponseBody() throws Exception{
        String apiUrl = "/commodities/{id}/suggested";
        mockMvc.perform(get(apiUrl, EXISTING_COMMODITY_ID)).
                andDo(MockMvcResultHandlers.print())
                .andExpect(content().string(commodityResponse2));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "sd", "78", "-211"})
    public void getSuggestedCommoditiesReturnsNOTFOUNDStatusForANoneExistingCommodity(String noneExistentId) throws Exception{
        String apiUrl = "/commodities/{id}/suggested";
        mockMvc.perform(get(apiUrl, noneExistentId)).
                andDo(MockMvcResultHandlers.print())
                .andExpect(status().is(404));
    }

    @Test
    public void getSuggestedCommoditiesReturnsEmptyStringForEmptyCommodityId() throws Exception{
        String id = "";
        String apiUrl = "/commodities/{id}/suggested";
        mockMvc.perform(get(apiUrl, id)).
                andDo(MockMvcResultHandlers.print())
                .andExpect(content().string(""))
                .andExpect(status().is(404));
    }

    @ParameterizedTest
    @ValueSource(strings = {"sd", "78", "-211"})
    public void getSuggestedCommoditiesReturnsNullForANoneExistingCommodity(String noneExistentId) throws Exception{
        String apiUrl = "/commodities/{id}/suggested";
        mockMvc.perform(get(apiUrl, noneExistentId)).
                andDo(MockMvcResultHandlers.print())
                .andExpect(content().string("[]"))
                .andExpect(status().is(404));
    }

    //issue yaftam: exception is not thrown
//    @ParameterizedTest
//    @ValueSource(strings = {"", "sd", "78", "-211"})
//    public void getSuggestedCommoditiesThrowsNotExistingCommodityExceptionForANoneExistingCommodity(String noneExistentId) throws Exception{
//        String apiUrl = "/commodities/{id}/suggested";
//        mockMvc.perform(get(apiUrl, noneExistentId)).
//                andDo(MockMvcResultHandlers.print())
//                .andExpect(result -> {
//                    assertTrue( result.getResolvedException() instanceof NotExistentCommodity);
//                });
//    }

}















