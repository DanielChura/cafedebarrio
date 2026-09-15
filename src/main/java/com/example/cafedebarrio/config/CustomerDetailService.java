package com.example.cafedebarrio.config;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.cafedebarrio.entity.User;
import com.example.cafedebarrio.repository.UserRepository;

@Service
public class CustomerDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomerDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return user;
    }

}
