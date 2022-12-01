package gamification.game.badgeprocessors;

import gamification.challenge.ChallengeSolvedDTO;
import gamification.game.domain.BadgeType;
import gamification.game.domain.ScoreCard;

import java.util.List;
import java.util.Optional;

public interface BadgeProcessor {

    Optional<BadgeType> processForOptionalBadge(int currentScore, List<ScoreCard> scoreCardList,
                                                ChallengeSolvedDTO solved);

    BadgeType badgeType();
}
