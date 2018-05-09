package fyodor.validation;

import fyodor.service.IUserAttributeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class UserAttributeValidator {
    @Autowired
    private IUserAttributeService userAttributeService;

    public Set<Integer> validate(String attributeName) {
        Set<Integer> errorCodesSet = new HashSet<>();
        if (!validNameLength(attributeName)) {
            errorCodesSet.add(1);
            return errorCodesSet;
        }
        if (attributeExists(attributeName)) {
            errorCodesSet.add(2);
        }
        return  errorCodesSet;
    }

    private boolean validNameLength(String name) {
        int length = name.length();
        return (length >= 3 && length <= 32);
    }

    private boolean attributeExists(String name) {
        return userAttributeService.attributeExists(name);
    }
}
