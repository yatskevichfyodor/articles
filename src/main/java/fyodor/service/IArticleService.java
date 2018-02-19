package fyodor.service;

import fyodor.model.Article;

import java.security.Principal;

public interface IArticleService {
    void save(Article article, Principal author);

    Article findByTitle(String title);
}
