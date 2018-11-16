package fyodor.service;

import fyodor.model.UserAttribute;
import fyodor.repository.UserAttributeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class UserAttributeService {

    @Autowired private UserAttributeRepository userAttributeRepository;

    public boolean attributeExists(String attribute) {
        if (userAttributeRepository.findByNameIgnoreCase(attribute) != null)
            return true;
        return false;
    }

    public UserAttribute save(String name) {
        UserAttribute attribute = userAttributeRepository.findByNameIgnoreCase(name);
        if (attribute == null) {
            attribute = new UserAttribute();
            attribute.setName(name);
        }
        attribute.setEnabled(true);

        return userAttributeRepository.save(attribute);
    }

    public UserAttribute save(UserAttribute attribute) {
        return userAttributeRepository.save(attribute);
    }

    @Transactional
    public void delete(Long id) {
        userAttributeRepository.delete(userAttributeRepository.findById(id).get());
    }

    public void disable(Long id) {
        UserAttribute userAttribute = userAttributeRepository.findById(id).get();
        userAttribute.setEnabled(false);

        userAttributeRepository.save(userAttribute);
    }
}
