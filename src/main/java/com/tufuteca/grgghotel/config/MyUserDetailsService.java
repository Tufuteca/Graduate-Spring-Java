package com.tufuteca.grgghotel.config;

import com.tufuteca.grgghotel.model.users.Users;
import com.tufuteca.grgghotel.repository.users.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UsersRepository usersRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Users> user = usersRepository.findByEmail(username);
        if (user.isEmpty()) {
            user = usersRepository.findByUsername(username);
        }
        if (user.isEmpty()) {
            user = usersRepository.findByPhoneNumber(username);
        }
        if (user.isEmpty()) {
            user = usersRepository.findByEmail(username);
        }

        if(user.get().getActive() || user.get().getDeleted() || user.get().getBlocked()){
            throw new UsernameNotFoundException(username);
        }else {
            return user.map(MyUserDetails::new)
                    .orElseThrow(() -> new UsernameNotFoundException(username + " Такого пользователя нет"));
        }
    }


}
