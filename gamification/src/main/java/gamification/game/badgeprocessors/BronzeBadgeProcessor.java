package gamification.game.badgeprocessors;

import gamification.challenge.ChallengeSolvedDTO;
import gamification.game.domain.BadgeType;
import gamification.game.domain.ScoreCard;

import java.util.List;
import java.util.Optional;

public class BronzeBadgeProcessor implements BadgeProcessor {

    @Override
    public Optional<BadgeType> processForOptionalBadge(int currentScore, List<ScoreCard> scoreCardList,
                                                       ChallengeSolvedDTO solved) {
        return currentScore > 50 ? Optional.of(BadgeType.BRONZE) : Optional.empty();
    }

    @Override
    public BadgeType badgeType() {
        return BadgeType.BRONZE;
    }
}
