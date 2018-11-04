package fyodor.controller;


import fyodor.repository.OverallDao;
import fyodor.service.UserService;
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
    private UserService userService;

    @Autowired
    private OverallDao overallDao;

    @GetMapping("/admin")
    public String admin(HttpServletRequest request, Model model) {
        model.addAttribute("listOfUsers", userService.findAll());

        return "admin";
    }

    @GetMapping("/admin/action/delete")
    @ResponseBody
    public String delete(@RequestParam(value="idArray[]") String[] idArray) {
        userService.delete(idArray);
        return "done";
    }

    @GetMapping("/admin/action/block")
    @ResponseBody
    public String block(@RequestParam(value="idArray[]") String[] idArray) {
        userService.block(idArray);
        return "done";
    }

    @GetMapping("/admin/action/unblock")
    @ResponseBody
    public String unblock(@RequestParam(value="idArray[]") String[] idArray) {
        userService.unblock(idArray);
        return "done";
    }

    @GetMapping("/admin/action/admin")
    @ResponseBody
    public String makeAdmin(@RequestParam(value="idArray[]") String[] idArray) {
        userService.addRole(idArray, "ROLE_ADMIN");
        return "done";
    }

    @GetMapping("/admin/action/disrank")
    @ResponseBody
    public String disrank(@RequestParam(value="idArray[]") String[] idArray) {
        userService.deleteRole(idArray, "ROLE_ADMIN");
        return "done";
    }

    @GetMapping("/sql-terminal")
    public String sqlTerminal() {
        return "sql-terminal";
    }

    @GetMapping("/sql-request")
    @ResponseBody
    public String sqlRequest(@RequestParam("request") String request) {
        return overallDao.sqlRequest(request);
    }
}
