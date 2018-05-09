package fyodor.controller;

import fyodor.dto.CategoryDto;
import fyodor.service.CategoryService;
import fyodor.util.UsedCategoriesHierarchyBuilder;
import fyodor.validation.CategoryValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Set;

@Controller
public class CategoryController {

    @Autowired
    private UsedCategoriesHierarchyBuilder usedCategoriesHierarchyBuilder;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryValidator categoryValidator;

//    @GetMapping(value = { "/getCategoryHierarchy" })
//    public List<CategoryDto> getCategoryHierarchy() {
//        return categoryService.getDtoHierarchy();
//    }

    @GetMapping("/categoryManagement")
    public String categoryManagement(Model model) {
        model.addAttribute("allCategories", categoryService.getFullHierarchicalList());
        model.addAttribute("limitedListOfCategories", categoryService.getFullParentsHierarchicalList());

        return "category-management";
    }

    @PostMapping("/saveCategory")
    @ResponseBody
    public ResponseEntity<?> saveCategory(@RequestBody CategoryDto categoryDto) {
        Set<Integer> errorsSet = categoryValidator.validateAdd(categoryDto.getName());
        if (errorsSet.size() != 0) {
            return new ResponseEntity<>(new ArrayList<>(errorsSet), HttpStatus.NOT_ACCEPTABLE);
        }

        categoryService.save(categoryDto);

        return new ResponseEntity<>(categoryDto, HttpStatus.OK);
    }

    @DeleteMapping("/category")
    @ResponseBody
    public ResponseEntity<?> deleteCategory(@RequestBody Long categoryId) {
        Set<Integer> errorsSet = categoryValidator.validateDelete(categoryId);
        if (errorsSet.size() != 0) {
            return new ResponseEntity<>(errorsSet, HttpStatus.NOT_ACCEPTABLE);
        }
        categoryService.delete(categoryId);

        return new ResponseEntity<>(categoryId, HttpStatus.OK);
    }
}
