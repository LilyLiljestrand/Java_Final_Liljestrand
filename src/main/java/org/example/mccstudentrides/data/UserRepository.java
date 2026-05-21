package org.example.mccstudentrides.data;

import org.example.mccstudentrides.domain.User;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface UserRepository extends CrudRepository<String, Integer>{
    User findByUsername(String username);
    User getUsersById(int id);
    List<User> getUserContactByContact(String contact);
}
