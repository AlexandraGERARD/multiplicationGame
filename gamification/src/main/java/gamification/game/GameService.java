package gamification.game;

import gamification.challenge.ChallengeSolvedDTO;
import lombok.Value;
import gamification.game.domain.BadgeType;

import java.util.List;

public interface GameService {

    GameResult newAttemptForUser(ChallengeSolvedDTO challenge);

    @Value
    class GameResult {
        int score;
        List<BadgeType> badges;
    }
}
