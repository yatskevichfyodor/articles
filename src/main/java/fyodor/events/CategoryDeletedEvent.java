package fyodor.events;

import fyodor.model.Category;
import lombok.Data;

@Data
public class CategoryDeletedEvent {
    private Category category;

    public CategoryDeletedEvent(Category category) {
        this.category = category;
    }
}
