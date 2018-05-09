package fyodor.service;

import fyodor.model.UserAttribute;
import fyodor.repository.UserAttributeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

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
        UserAttribute attribute = userAttributeRepository.findByNameIgnoreCase(name);
        if (attribute == null) {
            attribute = new UserAttribute();
            attribute.setName(name);
        }
        attribute.setEnabled(true);

        return userAttributeRepository.save(attribute);
    }

    @Override
    public UserAttribute save(UserAttribute attribute) {
        return userAttributeRepository.save(attribute);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        userAttributeRepository.delete(id);
    }

    @Override
    public void disable(Long id) {
        UserAttribute userAttribute = userAttributeRepository.findById(id);
        userAttribute.setEnabled(false);

        userAttributeRepository.save(userAttribute);
    }
}
