package gamification.game;

import gamification.challenge.ChallengeSolvedDTO;
import gamification.game.BadgeRepository;
import gamification.game.GameService;
import gamification.game.GameService.GameResult;
import gamification.game.GameServiceImpl;
import gamification.game.ScoreRepository;
import gamification.game.badgeprocessors.BadgeProcessor;
import gamification.game.domain.BadgeCard;
import gamification.game.domain.BadgeType;
import gamification.game.domain.ScoreCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
public class GameServiceTest {

    private GameService gameService;

    @Mock
    private ScoreRepository scoreRepository;

    @Mock
    private BadgeRepository badgeRepository;

    @Mock
    private BadgeProcessor badgeProcessor;

    @BeforeEach
    public void setUp() {
        gameService = new GameServiceImpl(scoreRepository, badgeRepository, List.of(badgeProcessor));
    }

    @Test
    public void processCorrectAttemptTest() {
        // GIVEN
        long userId = 1L, attemptId = 10L;
        ChallengeSolvedDTO challengeSolvedDTO = new ChallengeSolvedDTO(attemptId, true, 30, 40, userId, "john_doe");
        ScoreCard scoreCard = new ScoreCard(userId, attemptId);
        given(scoreRepository.getTotalScoreForUser(userId)).willReturn(Optional.of(10));
        given(scoreRepository.findByUserIdOrderByScoreTimestampDesc(userId)).willReturn(List.of(scoreCard));
        given(badgeRepository.findByUserIdOrderByBadgeTimestampDesc(userId)).willReturn(List.of(new BadgeCard(userId,
                BadgeType.FIRST_WON)));
        given(badgeProcessor.badgeType()).willReturn(BadgeType.LUCKY_NUMBER);
        given(badgeProcessor.processForOptionalBadge(10, List.of(scoreCard), challengeSolvedDTO)).willReturn(Optional.of(BadgeType.LUCKY_NUMBER));


        // WHEN
        final GameResult gameResult = gameService.newAttemptForUser(challengeSolvedDTO);

        // THEN
        then(gameResult).isEqualTo(new GameResult(10, List.of(BadgeType.LUCKY_NUMBER)));
        verify(scoreRepository).save(scoreCard);
        verify(badgeRepository).saveAll(List.of(new BadgeCard(userId, BadgeType.LUCKY_NUMBER)));
    }

    @Test
    public void processWrongAttemptTest() {
        // GIVEN
        ChallengeSolvedDTO challengeSolvedDTO = new ChallengeSolvedDTO(10L, false, 30, 40, 1L, "john_doe");

        // WHEN
        GameResult resultGameResult = gameService.newAttemptForUser(challengeSolvedDTO);
        GameResult expectedGameResult = new GameResult(0, List.of());

        // THEN
        then(resultGameResult).isEqualTo(expectedGameResult);
    }
}
