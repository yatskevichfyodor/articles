package fyodor.service;

import fyodor.dto.UpdateUserParamDto;
import fyodor.model.User;
import fyodor.model.UserParam;
import fyodor.repository.UserAttributeRepository;
import fyodor.repository.UserParamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserParamService implements IUserParamService {

    @Autowired
    private UserAttributeRepository userAttributeRepository;

    @Autowired
    private UserParamRepository userParamRepository;

    @Override
    public UserParam save(UpdateUserParamDto updateUserParamDto, User user) {
        UserParam userParam = new UserParam();
        UserParam.UserParamId userParamId = new UserParam.UserParamId();
        userParamId.setAttribute(userAttributeRepository.findById(updateUserParamDto.getAttributeId()));
        userParamId.setUser(user);
        userParam.setId(userParamId);
        userParam.setValue(updateUserParamDto.getParamValue());

        return userParamRepository.save(userParam);
    }
}
