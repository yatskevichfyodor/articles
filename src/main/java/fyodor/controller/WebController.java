package fyodor.controller;

import fyodor.model.*;
import fyodor.service.*;
import fyodor.validation.UserAttributeValidator;
import fyodor.validation.UserParamValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;

@Controller
public class WebController {

	@Autowired
	private IUserService userService;

	@Autowired
	private ICategoryService categoryService;

	@ModelAttribute("currentUser")
	public User getPrincipal(@AuthenticationPrincipal CustomUserDetails userDetails) {
		if (userDetails == null) return null;
		return userDetails.getUser();
	}

	@GetMapping("/profile")
	public String userProfile(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
		User currentUser = userDetails.getUser();
		model.addAttribute("user", currentUser);
		model.addAttribute("timestamp", new SimpleDateFormat("yyyy-MM-dd HH:mm").format(currentUser.getTimestamp()));
		model.addAttribute("listOfCategories", categoryService.getHierarchicalListOfUsedCategories());
		model.addAttribute("paramsMap", userService.getUserParams(userDetails.getId()));

		return "profile";
	}
}