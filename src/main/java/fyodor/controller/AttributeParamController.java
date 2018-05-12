package fyodor.controller;

import fyodor.dto.EditUserParamDto;
import fyodor.model.User;
import fyodor.model.UserAttribute;
import fyodor.service.*;
import fyodor.validation.UserAttributeValidator;
import fyodor.validation.UserParamValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@Controller
public class AttributeParamController {

    @Autowired
    private IUserAttributeService userAttributeService;

    @Autowired
    private IUserParamService userParamService;

    @Autowired
    private UserAttributeValidator userAttributeValidator;

    @Autowired
    private UserParamValidator userParamValidator;

    @ModelAttribute("currentUser")
    public User getPrincipal(@AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails == null) return null;
        return userDetails.getUser();
    }

    @PostMapping("/userAttribute/add")
    @ResponseBody
    public ResponseEntity<?> addAttribute(@RequestBody String attributeName) {
        Set<Integer> errorsSet = userAttributeValidator.validate(attributeName);
        if (errorsSet.size() == 0) {
            UserAttribute attribute = userAttributeService.save(attributeName);
            return new ResponseEntity<>(attribute, HttpStatus.OK);
        }
        return new ResponseEntity<>(errorsSet, HttpStatus.NOT_ACCEPTABLE);
    }

    @DeleteMapping("/userAttribute/delete")
    @ResponseStatus(HttpStatus.OK)
    public void deleteAttribute(@RequestBody Long attributeId) {
        userAttributeService.disable(attributeId);
    }

    @PostMapping("/userParam/edit")
    @ResponseBody
    public ResponseEntity<?> editParam(@RequestBody EditUserParamDto editUserParamDto, @AuthenticationPrincipal CustomUserDetails userDetails) {
        Set<Integer> errorsSet = userParamValidator.validate(editUserParamDto.getParamValue());
        if (errorsSet.size() == 0) {
            userParamService.edit(editUserParamDto, userDetails.getUser());
            return new ResponseEntity<>(editUserParamDto, HttpStatus.OK);
        }
        return new ResponseEntity<>(errorsSet, HttpStatus.NOT_ACCEPTABLE);
    }
    
}
