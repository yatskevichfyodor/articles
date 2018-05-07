package fyodor.service;

import fyodor.model.UserAttribute;
import fyodor.repository.UserAttributeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserAttributeService implements IUserAttributeService {
    @Autowired
    private UserAttributeRepository userAttributeRepository;

    @Override
    public boolean attributeExists(String attribute) {
        if (userAttributeRepository.findByNameIgnoreCase(attribute) != null)
            return true;
        return false;
    }

    @Override
    public UserAttribute save(String name) {
        UserAttribute attribute = new UserAttribute();
        attribute.setName(name);

        return userAttributeRepository.save(attribute);
    }

    @Override
    public UserAttribute save(UserAttribute attribute) {
        return userAttributeRepository.save(attribute);
    }
}
