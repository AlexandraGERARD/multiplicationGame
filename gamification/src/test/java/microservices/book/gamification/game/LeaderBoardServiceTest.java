package microservices.book.gamification.game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class LeaderBoardServiceTest {

    private LeaderBoardService leaderBoardService;

    @Mock
    private ScordCardRepository scordCardRepository;

    @Mock
    private BadgeCardRepository badgeCardRepository;

    @BeforeEach
    public void setUp() {
        leaderBoardService = new LeaderBoardServiceImpl(scordCardRepository, badgeCardRepository);
    }

    @Test
    public void should

}
