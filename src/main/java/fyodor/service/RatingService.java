package fyodor.service;


import fyodor.model.Article;
import fyodor.model.Rating;
import fyodor.model.RatingId;
import fyodor.model.User;
import fyodor.repository.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class RatingService implements IRatingService {

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private IArticleService articleService;

    @Autowired
    private IUserService userService;

    @Override
    @Transactional
    public void changeState(Long articleId, String username, String ratingState) {
        Article article = articleService.findById(articleId);
        User user = userService.findByUsernameIgnoreCase(username);


        Rating rating = new Rating();
        RatingId ratingId = new RatingId();
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

    @Override
    public Rating findRatingByUsernameAndArticleId(String username, Long articleId) {
        return ratingRepository.findByUserIdAndArticleId(userService.findByUsernameIgnoreCase(username).getId(), articleId);
    }

    @Override
    public Long getValuesNumberByArticleId(Long id, String value) {
        return ratingRepository.getValuesNumberByArticleId(String.valueOf(id), value);
    }
}
