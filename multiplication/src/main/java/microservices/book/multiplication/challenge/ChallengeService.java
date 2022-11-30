package microservices.book.multiplication.challenge;

import java.util.List;

public interface ChallengeService {

    ChallengeAttempt verifyAttempt(ChallengeAttemptDTO attemptDTO);

    List<ChallengeAttempt> getStatsForUser(String userAlias);
}
