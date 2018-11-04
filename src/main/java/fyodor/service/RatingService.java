package fyodor.service;


import fyodor.model.Article;
import fyodor.model.Rating;
import fyodor.model.User;
import fyodor.repository.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class RatingService {

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private UserService userService;

    @Transactional
    public void changeState(Long articleId, String username, String ratingState) {
        Article article = articleService.findById(articleId);
        User user = userService.findByUsernameIgnoreCase(username);


        Rating rating = new Rating();
        Rating.RatingId ratingId = new Rating.RatingId();
        ratingId.setArticle(article);
        ratingId.setUser(user);
        rating.setId(ratingId);

        if (ratingState.equals("NONE")) {
//            ratingRepository.deleteByUserIdAndArticleId(user.getId(), article.getId());
            ratingRepository.deleteById(ratingId);
            return;
        }

        Rating.RatingEnum value;
        if (ratingState.equals("LIKE")) value = Rating.RatingEnum.LIKE;
        else if (ratingState.equals("DISLIKE")) value = Rating.RatingEnum.DISLIKE;
        else throw new RuntimeException("Unsupported state exception");
        rating.setValue(value);

        ratingRepository.save(rating);
    }

    public Rating findRatingByUserAndArticleId(User user, Long articleId) {
        return ratingRepository.findByUserIdAndArticleId(user.getId(), articleId);
    }

    public void deleteRatings(List<Rating> ratings) {
        Set<Rating.RatingId> ratingIds = new HashSet<>();
        for (Rating rating: ratings) {
            ratingIds.add(rating.getId());
        }
        ratingRepository.deleteRatings(ratingIds);
    }
}
