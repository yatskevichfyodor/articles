package fyodor.service;

import fyodor.dto.UpdateUserParamDto;
import fyodor.model.User;
import fyodor.model.UserParam;

public interface IUserParamService {
    UserParam save(UpdateUserParamDto updateUserParamDto, User user);
}
