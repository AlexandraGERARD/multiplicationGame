package gamification.game;

import gamification.game.domain.BadgeCard;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BadgeRepository extends CrudRepository<BadgeCard, Long> {

    List<BadgeCard> findByUserIdOrderByBadgeTimestampDesc(final Long userId);

}