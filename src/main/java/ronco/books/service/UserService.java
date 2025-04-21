package ronco.books.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ronco.books.model.User;

public interface UserService extends UserDetailsService {
    User save(User user);
    boolean existsByUsername(String username);
}