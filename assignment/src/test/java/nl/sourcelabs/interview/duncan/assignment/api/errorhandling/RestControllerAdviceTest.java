package nl.sourcelabs.interview.duncan.assignment.api.errorhandling;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import nl.sourcelabs.interview.duncan.assignment.util.UserInputValidationException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(controllers = TestThrowableRestController.class)
class RestControllerAdviceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestThrowableRestController testThrowableRestController;

    @Test
    @DisplayName("Given a UserInputValidationException Then we receive error with same exception message")
    void handle_userInputValidationException() throws Exception {

        final String message = "Our custom message that is safe for the user to see";
        testThrowableRestController.setThrowable(new UserInputValidationException(message));

        mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(message));

    }

    @Test
    @DisplayName("Given a generic Runtime Exception Then we receive error with generic message hiding exception message")
    void handle_runtimeException() throws Exception {

        final String message = "Our secret message that is not safe for the user to see";
        testThrowableRestController.setThrowable(new RuntimeException(message));

        final String expectedGenericErrorMessage = "Something has gone wrong in the server - consult server logs";
        mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value(expectedGenericErrorMessage));

    }

}
