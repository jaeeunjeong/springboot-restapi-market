package com.practice.springbootrestapimarket.factory.entity;

import com.practice.springbootrestapimarket.dto.sign.SignUpRequest;
import com.practice.springbootrestapimarket.entity.member.Member;
import com.practice.springbootrestapimarket.entity.member.Role;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MemberFactory {

    public static Member createMember() {
        return new Member("jejeong@email", "1!2@3#4$", "eugene", "nicky", Arrays.asList());
    }

    public static Member createMember(String email, String password, String username, String nickname) {
        return new Member(email, password, username, nickname, Collections.emptyList());
    }

    public static Member createMemberWithRoles(List<Role> roles) {
        return new Member("jejeong@email", "1!2@3#4$", "eugene", "nicky", roles);
    }
}
