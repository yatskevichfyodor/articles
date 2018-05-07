package fyodor.service;

import fyodor.model.UserAttribute;

public interface IUserAttributeService {

    UserAttribute save(String name);

    UserAttribute save(UserAttribute attribute);

    boolean attributeExists(String attribute);
}
