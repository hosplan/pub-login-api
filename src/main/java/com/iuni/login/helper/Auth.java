package com.iuni.login.helper;


import com.iuni.login.domain.Authority;
import com.iuni.login.domain.Member;

public class Auth {
    private enum Role {ADMIN, MANAGER, USER, TEMPUSER};
    public Authority makeData(String type, Member member){
        Authority authority = new Authority();
        authority.setName(type);

        Role r = Role.valueOf(type);
        authority.setTier(r.ordinal());

        authority.setMember(member);
        return authority;
    }
}
