package multiplication.challenge;

import multiplication.serviceclients.GamificationServiceClient;
import multiplication.user.User;
import multiplication.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ChallengeServiceTest {

    private ChallengeService challengeService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ChallengeAttemptRepository attemptRepository;

    @Mock
    private GamificationServiceClient gameClient;

    @BeforeEach
    public void setUp() {
        challengeService = new ChallengeServiceImpl(userRepository, attemptRepository, gameClient);

        given(attemptRepository.save(any())).will(returnsFirstArg());
    }

    @Test
    public void checkCorrectAttemptTest() {
        // GIVEN
        ChallengeAttemptDTO attemptDTO = new ChallengeAttemptDTO(50, 60, "john_doe", 3000);

        // WHEN
        ChallengeAttempt resultAttempt = challengeService.verifyAttempt(attemptDTO);

        // THEN
        then(resultAttempt.isCorrect()).isTrue();
        verify(userRepository).save(new User("john_doe"));
        verify(attemptRepository).save(resultAttempt);
        verify(gameClient).sendAttempt(resultAttempt);
    }

    @Test
    public void checkWrongAttemptTest() {
        // GIVEN
        ChallengeAttemptDTO attemptDTO = new ChallengeAttemptDTO(50, 60, "john_doe", 5000);

        // WHEN
        ChallengeAttempt resultAttempt = challengeService.verifyAttempt(attemptDTO);

        // THEN
        then(resultAttempt.isCorrect()).isFalse();
        verify(gameClient).sendAttempt(resultAttempt);
    }

    @Test
    public void checkExistingUserTest() {
        // GIVEN
        User existingUser = new User(1L, "john_doe");
        given(userRepository.findByAlias("john_doe")).willReturn(Optional.of(existingUser));
        ChallengeAttemptDTO attemptDTO = new ChallengeAttemptDTO(50, 60, "john_doe", 5000);

        // WHEN
        ChallengeAttempt resultAttempt = challengeService.verifyAttempt(attemptDTO);

        // THEN
        then(resultAttempt.isCorrect()).isFalse();
        then(resultAttempt.getUser()).isEqualTo(existingUser);
        verify(userRepository, never()).save(any());
        verify(attemptRepository).save(resultAttempt);
        verify(gameClient).sendAttempt(resultAttempt);
    }
}