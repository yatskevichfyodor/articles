package fyodor.repository;

import fyodor.model.Article;
import fyodor.model.Category;
import fyodor.model.Image;
import fyodor.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ArticleDao {

    public static final String tableName = "articles";

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    private Connection connect;

    public ArticleDao() {
        try {
            connect = ConnectorDB.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Article> convertResultSetToArticles(ResultSet rs) throws SQLException {
        List<Article> result = new ArrayList<>();
        while (rs.next()) {
            Article article = new Article();
            article.setId(rs.getInt(1));
            article.setContent(rs.getString(2));
//            article.setTimestamp(rs.getDate(3));
            article.setTitle(rs.getString(4));
            User author = userRepository.findById(rs.getLong(5)).get();
            article.setAuthor(author);
            Category category = categoryRepository.findById(rs.getLong(6)).get();
            article.setCategory(category);
            Image image = imageRepository.findById(rs.getLong(7)).get();
            article.setImage(image);
            article.setPopularity(rs.getLong(8));

            result.add(article);
        }

        return result;
    }

    private String convertListToString(List<Long> list) {
        StringBuilder output = new StringBuilder();
        for (Long id: list) {
            output.append(id);
            output.append(",");
        }
        output.delete(output.length() - 1, output.length());

        return output.toString();
    }

    private List<Article> getArticlesByQuery(CharSequence query) {
        List<Article> articles;
        try {
            PreparedStatement ps = connect.prepareStatement(query.toString());
            ResultSet rs = ps.executeQuery();

            articles = convertResultSetToArticles(rs);

            if (ps != null)
                ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return articles;
    }


    //FIND OPERATIONS
    public Article findByTitle(String title) {
        StringBuilder query = new StringBuilder("" +
                "SELECT *\n" +
                "FROM " + tableName + " a\n" +
                "WHERE\n" +
                "  a.title='" + title + "'"
        );

        return getArticlesByQuery(query).get(0);
    }

    public Article findByTitleIgnoreCase(String title) {
        StringBuilder query = new StringBuilder("" +
                "SELECT *\n" +
                "FROM " + tableName + " a\n" +
                "WHERE\n" +
                "  lower(a.title)=lower('" + title + "')"
        );

        return getArticlesByQuery(query).get(0);
    }

    public List<Article> findAllByCategoryAndAuthor(Category category, User author) {
        StringBuilder query = new StringBuilder("" +
                "SELECT *\n" +
                "FROM " + tableName + " a\n" +
                "WHERE\n" +
                "  a.category_id='" + category.getId() + "'" +
                "  AND a.author_id='" + author.getId() + "'"
        );

        return getArticlesByQuery(query);
    }

    public List<Article> findAllByCategories(List<Category> categories) {
        if (categories.size() == 0) return null;

        StringBuilder query = new StringBuilder("" +
                "SELECT *\n" +
                "FROM " + tableName + " a\n" +
                "WHERE\n" +
                "  a.category_id in (" + convertListToString(categories.stream().map(it -> it.getId()).collect(Collectors.toList())));
        query.append(")\n"
        );

        return getArticlesByQuery(query);
    }

    public List<Article> findAllByCategoriesSortedByPopularity(List<Category> categories) {
        if (categories.size() == 0) return null;

        StringBuilder query = new StringBuilder("" +
                "SELECT *\n" +
                "FROM " + tableName + " a\n" +
                "WHERE\n" +
                "  a.category_id in (" + convertListToString(categories.stream().map(it -> it.getId()).collect(Collectors.toList())));
        query.append(")\n" +
                "ORDER BY popularity DESC;");

        return getArticlesByQuery(query);
    }

    public List<Article> findAllByCategoriesSortedByDateDesc(List<Category> categories) {
        if (categories.size() == 0) return null;

        StringBuilder query = new StringBuilder("" +
                "SELECT *\n" +
                "FROM " + tableName + " a\n" +
                "WHERE\n" +
                "  a.category_id in (" + convertListToString(categories.stream().map(it -> it.getId()).collect(Collectors.toList())));
        query.append(")\n" +
                "ORDER BY timestamp DESC;");

        return getArticlesByQuery(query);
    }

    public List<Article> findAllByCategoriesSortedByDateAsc(List<Category> categories) {
        if (categories.size() == 0) return null;

        StringBuilder query = new StringBuilder("" +
                "SELECT *\n" +
                "FROM " + tableName + " a\n" +
                "WHERE\n" +
                "  a.category_id in (" + convertListToString(categories.stream().map(it -> it.getId()).collect(Collectors.toList())));
        query.append(")\n" +
                "ORDER BY timestamp ASC;");

        return getArticlesByQuery(query);
    }

    public Date getLastTimeUserAddedArticle(User user) throws SQLException {
        PreparedStatement ps = connect.prepareStatement("\n" +
                "SELECT timestamp\n" +
                "FROM " + tableName + "\n" +
                "WHERE author_id = ?\n" +
                "ORDER BY timestamp DESC\n" +
                "LIMIT 1");
        ps.setLong(1, user.getId());
        ResultSet rs = ps.executeQuery();

        while (rs.next())
            return rs.getTimestamp(1);
        return null;
    }


    // INSERT OPERATIONS
//    public Article save(Article article) {
//        PreparedStatement ps = connect.prepareStatement("" +
//         "INSERT INTO article(id, content, timestamp, title, author_id, category_id, image_id, popularity)\n" +
//         "VALUES ('" + article.getId() + "', '" + article.getContent() + "', '" + article.getTimestamp() + "', '" + article.getTitle() + "', '" + article.getAuthor().getId() + "', '" + article.getCategory().getId() + "', '" + article.getImage().getId() + "', '" + article.getPopularity()) + "')";
//
//        return getArticlesByQuery(query);
//    }
}
