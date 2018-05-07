package fyodor.validation;

import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class UserParamValidator {
    public Set<Integer> validate(String paramValue) {
        Set<Integer> errorCodesSet = new HashSet<>();
        if (!validValueLength(paramValue)) {
            errorCodesSet.add(1);
            return errorCodesSet;
        }
        return  errorCodesSet;
    }

    private boolean validValueLength(String value) {
        int length = value.length();
        return (length >= 3 && length <=32);
    }
}
