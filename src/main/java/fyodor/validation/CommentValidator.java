package fyodor.validation;

import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class CommentValidator {

    public Set<Integer> validate(String commentText) {
        Set<Integer> errorCodesSet = new HashSet<>();
        if (!validCommentLength(commentText)) {
            errorCodesSet.add(1);
            return errorCodesSet;
        }

        return  errorCodesSet;
    }

    private boolean validCommentLength(String commentText) {
        int length = commentText.length();
        return (length >= 1 && length <= 1000);
    }
}
