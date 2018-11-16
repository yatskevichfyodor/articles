package fyodor.dto;

import fyodor.validation.CategoryNotExists;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class CategoryDto {
    private Long id;

    @NotNull
    @Size(min=3, max=20, message="Name should have atleast 3 characters")
    @CategoryNotExists
    private String name;

    private Long parentId;
}
