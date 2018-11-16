package fyodor.controller;

import fyodor.model.*;
import fyodor.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;

@Controller
public class WebController {

	@Autowired private UserService userService;
	@Autowired private CategoryService categoryService;

	@ModelAttribute("currentUser")
	public User getPrincipal(@AuthenticationPrincipal CustomUserDetails userDetails) {
		if (userDetails == null) return null;
		return userDetails.getUser();
	}

	@GetMapping("/profile")
	public String profile(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
		User currentUser = userDetails.getUser();
		model.addAttribute("user", currentUser);
		model.addAttribute("timestamp", new SimpleDateFormat("yyyy-MM-dd HH:mm").format(currentUser.getTimestamp()));
		model.addAttribute("listOfCategories", categoryService.getHierarchicalListOfUserCategories(currentUser));
		model.addAttribute("paramsMap", userService.getUserParams(userDetails.getId()));
		model.addAttribute("isProfilePage", "true");

		return "profile";
	}

	@GetMapping("/user/{username}")
	public String userProfile(Model model, @PathVariable("username") String username,
							  @AuthenticationPrincipal CustomUserDetails userDetails) {
		User user = userService.findByUsernameIgnoreCase(username);
		//if (userDetails == null) return "redirect:/home";
		if (userDetails != null && userDetails.getUser().equals(user)) return "redirect:/profile";

		model.addAttribute("isProfilePage", "false");

		model.addAttribute("user", user);
		model.addAttribute("timestamp", new SimpleDateFormat("yyyy-MM-dd HH:mm").format(user.getTimestamp()));
		model.addAttribute("listOfCategories", categoryService.getHierarchicalListOfUserCategories(user));
		model.addAttribute("paramsMap", userService.getUserParams(user.getId()));

		return "profile";
	}

}