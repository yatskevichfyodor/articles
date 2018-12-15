package fyodor.controller;

import fyodor.dto.EditUserParamDto;
import fyodor.model.User;
import fyodor.model.UserAttribute;
import fyodor.service.*;
import fyodor.validation.AttributeNotExists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Size;

@Controller
public class AttributeParamController {

    @Autowired private UserAttributeService userAttributeService;
    @Autowired private UserParamService userParamService;

    @ModelAttribute("currentUser")
    public User getPrincipal(@AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails == null) return null;
        return userDetails.getUser();
    }

    @PostMapping("/userAttribute/add")
    @ResponseBody
    public ResponseEntity<?> addAttribute(@RequestBody @Size(min = 3, max = 32, message = "Name should have atleast 3 characters") @AttributeNotExists String attributeName, Errors errors) {
        if (errors.hasErrors()) throw new RuntimeException();

        UserAttribute attribute = userAttributeService.save(attributeName);
        return new ResponseEntity<>(attribute, HttpStatus.OK);
    }

    @DeleteMapping("/userAttribute/delete")
    @ResponseStatus(HttpStatus.OK)
    public void deleteAttribute(@RequestBody Long attributeId) {
        userAttributeService.disable(attributeId);
    }

    @PostMapping("/userParam/edit")
    @ResponseBody
    public ResponseEntity<?> editParam(@RequestBody @Valid EditUserParamDto editUserParamDto, Errors errors, @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (errors.hasErrors()) throw new RuntimeException();

        userParamService.edit(editUserParamDto, userDetails.getUser());
        return new ResponseEntity<>(editUserParamDto, HttpStatus.OK);
    }

}
