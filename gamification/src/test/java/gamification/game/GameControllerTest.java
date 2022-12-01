package gamification.game;

import gamification.challenge.ChallengeSolvedDTO;
import gamification.game.GameController;
import gamification.game.GameService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.BDDAssertions.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(SpringExtension.class)
@AutoConfigureJsonTesters
@WebMvcTest(GameController.class)
public class GameControllerTest {

    @MockBean
    private GameService gameService;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<ChallengeSolvedDTO> jsonRequestAttempt;

    @Test
    void postValidResult() throws Exception {
        // GIVEN
        ChallengeSolvedDTO challengeSolvedDTO = new ChallengeSolvedDTO(10L, true, 30, 40, 1L, "john_doe");

        // WHEN
        MockHttpServletResponse response =
                mvc.perform(post("/attempts").contentType(MediaType.APPLICATION_JSON).content(jsonRequestAttempt.write(challengeSolvedDTO).getJson())).andReturn().getResponse();

        // THEN
        then(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }
}
