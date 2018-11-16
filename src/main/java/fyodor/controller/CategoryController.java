package fyodor.controller;

import fyodor.dto.CategoryDto;
import fyodor.service.CategoryService;
import fyodor.validation.CategoryWithoutArticles;
import fyodor.validation.CategoryWithoutSubcategories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@Controller
public class CategoryController {

    @Autowired private CategoryService categoryService;

    @GetMapping("/categoryManagement")
    public String categoryManagement(Model model) {
        model.addAttribute("allCategories", categoryService.getFullHierarchicalList());
        model.addAttribute("limitedListOfCategories", categoryService.getFullParentsHierarchicalList());

        return "category-management";
    }

    @PostMapping("/category/add")
    @ResponseBody
    public ResponseEntity<?> saveCategory(@RequestBody CategoryDto categoryDto, Errors errors) {
        if (errors.hasErrors()) throw new RuntimeException();

        categoryService.save(categoryDto);

        return new ResponseEntity<>(categoryDto, HttpStatus.OK);
    }

    @DeleteMapping("/category/delete")
    @ResponseBody
    public ResponseEntity<?> deleteCategory(@RequestBody @CategoryWithoutSubcategories @CategoryWithoutArticles Long categoryId, Errors errors) {
        if (errors.hasErrors()) throw new RuntimeException();

        categoryService.delete(categoryId);

        return new ResponseEntity<>(categoryId, HttpStatus.OK);
    }
}
