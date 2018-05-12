package fyodor.service;

import fyodor.dto.EditUserParamDto;
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
    public UserParam edit(EditUserParamDto editUserParamDto, User user) {
        if (editUserParamDto.getParamValue().equals("")) {
            UserParam.UserParamId userParamId = new UserParam.UserParamId();
            userParamId.setAttribute(userAttributeRepository.findById(editUserParamDto.getAttributeId()).get());
            userParamId.setUser(user);
            UserParam userParam = userParamRepository.findById(userParamId);
            userParamRepository.delete(userParam);

            return null;
        }

        UserParam userParam = new UserParam();
        UserParam.UserParamId userParamId = new UserParam.UserParamId();
        userParamId.setAttribute(userAttributeRepository.findById(editUserParamDto.getAttributeId()).get());
        userParamId.setUser(user);
        userParam.setId(userParamId);
        userParam.setValue(editUserParamDto.getParamValue());

        return userParamRepository.save(userParam);
    }
}
