package fyodor.controller;


import fyodor.dto.UpdateUserParamDto;
import fyodor.model.Category;
import fyodor.model.User;
import fyodor.model.UserAttribute;
import fyodor.model.UserParam;
import fyodor.service.*;
import fyodor.validation.UserAttributeValidator;
import fyodor.validation.UserParamValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.LocaleResolver;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Set;

@Controller
public class WebController {

	@Autowired
	private IUserService userService;

	@Autowired
    private IArticleService articleService;

	@Autowired
	private ICategoryService categoryService;

	@Autowired
	private IUserAttributeService userAttributeService;

	@Autowired
	private IUserParamService userParamService;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private LocaleResolver localeResolver;

	@Autowired
	private UserAttributeValidator userAttributeValidator;

	@Autowired
	private UserParamValidator userParamValidator;

	@GetMapping("/profile")
	public String userProfile(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
		User currentUser = userDetails.getUser();
		List<Category> listOfCategories = categoryService.findByArticlesIn(articleService.
				findByAuthor(currentUser));
		model.addAttribute("user", currentUser);
		model.addAttribute("timestamp", new SimpleDateFormat("yyyy-MM-dd HH:mm").format(currentUser.getTimestamp()));
		model.addAttribute("listOfCategories", categoryService.getHierarchicalListOfUsedCategories());
		model.addAttribute("paramsMap", userService.getUserParams(userDetails.getId()));

		return "profile";
	}

	@PostMapping("/checkIfAttributeNotExists")
	@ResponseBody
	public Boolean checkIfAttributeNotExists(@RequestBody String attribute) {
		return !userAttributeService.attributeExists(attribute);
	}

	@PostMapping("/add-attribute")
	@ResponseBody
	public ResponseEntity<?> addAttribute(@RequestBody String attributeName) {
		Set<Integer> errorsSet = userAttributeValidator.validate(attributeName);
		if (errorsSet.size() == 0) {
			UserAttribute attribute = userAttributeService.save(attributeName);
			return new ResponseEntity<>(attribute, HttpStatus.OK);
		}
		return new ResponseEntity<>(errorsSet, HttpStatus.NOT_ACCEPTABLE);
	}

	@PostMapping("/updateUserParam")
	@ResponseBody
	public ResponseEntity<?> updateParam(@RequestBody UpdateUserParamDto updateUserParamDto, @AuthenticationPrincipal CustomUserDetails userDetails) {
		Set<Integer> errorsSet = userParamValidator.validate(updateUserParamDto.getParamValue());
		if (errorsSet.size() == 0) {
			UserParam userParam = userParamService.save(updateUserParamDto, userDetails.getUser());
			return new ResponseEntity<>(updateUserParamDto, HttpStatus.OK);
		}
		return new ResponseEntity<>(errorsSet, HttpStatus.NOT_ACCEPTABLE);
	}
}