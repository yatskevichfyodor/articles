package fyodor.controller;


import fyodor.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
public class AdminController {

    @Autowired
    private IUserService userService;

    @GetMapping("/admin")
    public String admin(HttpServletRequest request, Model model) {
        model.addAttribute("listOfUsers", userService.findAll());

        return "admin";
    }

    @GetMapping("/admin/action/delete")
    public @ResponseBody
    String delete(@RequestParam(value="idArray[]") String[] idArray) {
        userService.delete(idArray);
        return "done";
    }

    @GetMapping("/admin/action/block")
    public @ResponseBody String block(@RequestParam(value="idArray[]") String[] idArray) {
        userService.block(idArray);
        return "done";
    }

    @GetMapping("/admin/action/unblock")
    public @ResponseBody String unblock(@RequestParam(value="idArray[]") String[] idArray) {
        userService.unblock(idArray);
        return "done";
    }

    @GetMapping("/admin/action/admin")
    public @ResponseBody String makeAdmin(@RequestParam(value="idArray[]") String[] idArray) {
        userService.addRole(idArray, "ROLE_ADMIN");
        return "done";
    }

    @GetMapping("/admin/action/disrank")
    public @ResponseBody String disrank(@RequestParam(value="idArray[]") String[] idArray) {
        userService.deleteRole(idArray, "ROLE_ADMIN");
        return "done";
    }
}
