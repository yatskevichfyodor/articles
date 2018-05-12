package fyodor.service;

import fyodor.dto.EditUserParamDto;
import fyodor.model.User;
import fyodor.model.UserParam;

public interface IUserParamService {
    UserParam edit(EditUserParamDto editUserParamDto, User user);
}
