package fyodor.service;

import fyodor.model.Rating;
import fyodor.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface IRatingService {
    void changeState(Long articleId, String username, String ratingState);

    Rating findRatingByUserAndArticleId(User user, Long articleId);

    Long getValuesNumberByArticleId(Long id, String value);

    void deleteRatings(List<Rating> ratings);
}
