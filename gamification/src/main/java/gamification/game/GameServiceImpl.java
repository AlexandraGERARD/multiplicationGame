package gamification.game;

import gamification.challenge.ChallengeSolvedDTO;
import gamification.game.badgeprocessors.BadgeProcessor;
import gamification.game.domain.BadgeCard;
import gamification.game.domain.BadgeType;
import gamification.game.domain.ScoreCard;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {

    private final ScoreRepository scoreRepository;
    private final BadgeRepository badgeRepository;
    private final List<BadgeProcessor> badgeProcessors;

    @Override
    public GameResult newAttemptForUser(final ChallengeSolvedDTO challenge) {
        if (challenge.isCorrect()) {
            ScoreCard scoreCard = new ScoreCard(challenge.getUserId(), challenge.getAttemptId());
            scoreRepository.save(scoreCard);
            log.info("User {} scored {} points for attempt id {}", challenge.getUserAlias(), scoreCard.getScore(),
                    challenge.getUserId());

            List<BadgeCard> badgeCards = processForBadges(challenge);
            return new GameResult(scoreCard.getScore(),
                    badgeCards.stream().map(BadgeCard::getBadgeType).collect(Collectors.toList()));
        } else {
            log.info("Attempt id {} is not correct. User {} does not get score.", challenge.getAttemptId(),
                    challenge.getUserAlias());
            return new GameResult(0, List.of());
        }
    }

    private List<BadgeCard> processForBadges(final ChallengeSolvedDTO solvedChallenge) {
        Optional<Integer> optTotalScore = scoreRepository.getTotalScoreForUser(solvedChallenge.getUserId());

        if (optTotalScore.isEmpty()){
            return Collections.emptyList();
        }

        int totalScore = optTotalScore.get();

        List<ScoreCard> scoreCardList =
                scoreRepository.findByUserIdOrderByScoreTimestampDesc(solvedChallenge.getUserId());
        Set<BadgeType> alreadyGotBadges =
                badgeRepository.findByUserIdOrderByBadgeTimestampDesc(solvedChallenge.getUserId()).stream().map(BadgeCard::getBadgeType).collect(Collectors.toSet());

        List<BadgeCard> newBadgeCards =
                badgeProcessors.stream().filter(bp -> !alreadyGotBadges.contains(bp.badgeType())).map(bp -> bp.processForOptionalBadge(totalScore, scoreCardList, solvedChallenge)).flatMap(Optional::stream) // returns an empty stream if empty
                .map(badgeType -> new BadgeCard(solvedChallenge.getUserId(), badgeType)).collect(Collectors.toList());

        badgeRepository.saveAll(newBadgeCards);

        return newBadgeCards;
    }
}
