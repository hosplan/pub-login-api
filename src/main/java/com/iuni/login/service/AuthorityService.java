package com.iuni.login.service;

import com.iuni.login.domain.Authority;
import com.iuni.login.repository.AuthorityRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("authorityService")
public class AuthorityService {
    private final AuthorityRepository authorityRepository;

    public AuthorityService(AuthorityRepository authorityRepository){
        this.authorityRepository = authorityRepository;
    }

    public int getTier(String name){
        Optional<Authority> opt_auth = this.authorityRepository.findByName(name);
        return opt_auth.map(Authority::getTier).orElse(-1);
    }

    public Authority getByName(String name){
        Optional<Authority> optionalAuthority = authorityRepository.findByName(name);
        return optionalAuthority.orElseGet(Authority::new);
    }
}
