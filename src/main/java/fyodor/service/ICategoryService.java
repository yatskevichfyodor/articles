package fyodor.service;

import fyodor.model.Category;

import java.util.List;

public interface ICategoryService {
    List<Category> findAll();
}
