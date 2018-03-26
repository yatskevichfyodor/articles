package fyodor.service;

import fyodor.model.Rating;

public interface IRatingService {
    void changeState(Long articleId, String username, String ratingState);

    Rating findRatingByUsernameAndArticleId(String username, Long articleId);

    Long getValuesNumberByArticleId(Long id, String value);
}
