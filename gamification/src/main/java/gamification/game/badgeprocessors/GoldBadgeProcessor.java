package gamification.game.badgeprocessors;

import gamification.challenge.ChallengeSolvedDTO;
import gamification.game.domain.BadgeType;
import gamification.game.domain.ScoreCard;

import java.util.List;
import java.util.Optional;

public class GoldBadgeProcessor implements BadgeProcessor {
    @Override
    public Optional<BadgeType> processForOptionalBadge(int currentScore, List<ScoreCard> scoreCardList,
                                                       ChallengeSolvedDTO solved) {
        return currentScore > 400 ? Optional.of(BadgeType.GOLD) : Optional.empty();
    }

    @Override
    public BadgeType badgeType() {
        return BadgeType.GOLD;
    }
}
