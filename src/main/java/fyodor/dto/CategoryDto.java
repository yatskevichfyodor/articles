package fyodor.dto;

import lombok.Data;

@Data
public class CategoryDto {
    private Long id;
    private String name;
    private Long parentId;
}