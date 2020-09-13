package tickhubs.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import tickhubs.model.User;
import tickhubs.repository.UserRepository;
import tickhubs.security.CustomUserDetail;
import tickhubs.security.UserPrincipal;

import java.util.Objects;

public class CoreUtil {

    @Autowired
    UserRepository userRepository;

    public static Long getCurrentUserId() {
        if (Objects.nonNull(SecurityContextHolder.getContext().getAuthentication())) {
            UserPrincipal principal = (UserPrincipal)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return principal.getId();
        }
        return null;
    }
}
