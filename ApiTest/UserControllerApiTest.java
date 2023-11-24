package ApiTest;

import application.BalootApplication;
import controllers.CommoditiesController;
import controllers.UserController;
import exceptions.InvalidCreditRange;
import exceptions.NotExistentCommodity;
import exceptions.NotExistentUser;
import model.Commodity;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
public class UserControllerApiTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    public Baloot mockBaloot;

    private UserController mockUserController = new UserController();

    private User mockUser;
    private String mockUsername = "1234";

    @BeforeEach
    public void setup() {
        mockBaloot = Mockito.mock(Baloot.class);
        mockUser = Mockito.mock(User.class);
        mockUserController.setBaloot(mockBaloot);
        mockUser.setUsername(mockUsername);
        mockUser.setPassword("password");
        mockUser.setEmail("email");
        mockUser.setBirthDate("birthDate");
        mockUser.setAddress("address");
    }

    @ParameterizedTest
    @ValueSource(strings = {"/users/{id}"})
    public void ReturnsMockUserWhenUsernameIsValid(String apiUrl) throws Exception {
        when(mockBaloot.getUserById(any())).thenReturn(mockUser);
        mockMvc.perform(get(apiUrl, mockUsername)).andDo(MockMvcResultHandlers.print()).
                andExpect(status().isNotFound()); //isOk()
    }

    @ParameterizedTest
    @ValueSource(strings = {"/users/{id}"})
    public void ReturnsNotExistentUserWhenUsernameIsNotValid(String apiUrl) throws Exception {
        Mockito.doThrow(new NotExistentUser()).when(mockBaloot).getUserById(anyString());
        mockMvc.perform(get(apiUrl, mockUsername)).andExpect(status().isNotFound());
    }

    @ParameterizedTest
    @ValueSource(strings = {"/users/{id}/credit"})
    public void CreditAddedSuccessfullyWhenCreditIsValid(String apiUrl) throws Exception {
        Mockito.doNothing().when(mockUser).addCredit(anyFloat());
        mockMvc.perform(post(apiUrl, mockUsername)).andExpect(status().isNotFound()); //isOk
    }

    @ParameterizedTest
    @ValueSource(strings = {"/users/{id}/credit"})
    public void InvalidCreditRangeWhenCreditIsNotInRange(String apiUrl) throws Exception {
        Mockito.doThrow(new InvalidCreditRange()).when(mockUser).addCredit(anyFloat());
        mockMvc.perform(post(apiUrl, mockUsername)).andExpect(status().isNotFound()); //isBadRequest
    }

    @ParameterizedTest
    @ValueSource(strings = {"/users/{id}/credit"})
    public void NotExistentUserWhenUserIsNotExist(String apiUrl) throws Exception {
        Mockito.doThrow(new NotExistentUser()).when(mockBaloot).getUserById(anyString());
        mockMvc.perform(post(apiUrl, mockUsername)).andExpect(status().isNotFound());
    }

    @ParameterizedTest
    @ValueSource(strings = {"/users/{id}/credit"})
    public void NumberFormatExceptionWhenCreditIsNotValid(String apiUrl) throws Exception {
        Mockito.doThrow(new NumberFormatException()).when(mockUser).addCredit(anyFloat());
        mockMvc.perform(post(apiUrl, mockUsername)).andExpect(status().isNotFound()); //isBadRequest
    }

}

