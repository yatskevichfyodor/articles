package fyodor.service;

import fyodor.model.*;
import fyodor.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class ArticleService implements IArticleService {
    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    IUserService userService;

    @Autowired
    ICategoryService categoryService;

    @Autowired
    IImageService imageService;

    @Override
    public void save(String json, Principal author) {
        JsonParser springParser = JsonParserFactory.getJsonParser();
        Map<String, Object> data = springParser.parseMap(json);

        Article article = new Article();
        User user = userService.findByUsernameIgnoreCase(author.getName());
        article.setTitle((String)data.get("title"));
        article.setContent((String)data.get("content"));
        article.setCategory(categoryService.findById(Long.valueOf((String)data.get("category"))));
        article.setAuthor(user);
        article.setDate(new Date());

        String imageCode = (String)data.get("picture");
//        String base64Image = data.split(",")[1];
//        byte[] imageBytes = DatatypeConverter.parseBase64Binary(base64Image);
//        Image image = imageService.save(imageBytes);
        Image image = imageService.save(imageCode);
        article.setImage(image);

        articleRepository.save(article);
    }

    @Override
    public Article findByTitle(String title) {
        return articleRepository.findByTitle(title);
    }

    @Override
    public List<Article> findByCategoryAndAuthor(Category category, User author) {
        return articleRepository.findByCategoryAndAuthor(category, author);
    }

    @Override
    public List<Article> findByAuthor(User author) {
        return articleRepository.findByAuthor(author);
    }

    @Override
    public List<Article> findAll() {
        return articleRepository.findAll();
    }
}
