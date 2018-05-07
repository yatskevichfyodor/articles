package fyodor.service;

import fyodor.model.Rating;
import fyodor.model.User;

public interface IRatingService {
    void changeState(Long articleId, String username, String ratingState);

    Rating findRatingByUsernameAndArticleId(String username, Long articleId);

    Rating findRatingByUserAndArticleId(User user, Long articleId);

    Long getValuesNumberByArticleId(Long id, String value);
}
