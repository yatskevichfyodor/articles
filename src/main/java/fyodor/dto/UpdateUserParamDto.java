package fyodor.dto;

import lombok.Data;

@Data
public class UpdateUserParamDto {
    private Long attributeId;
    private String paramValue;
}
