package com.practice.springbootrestapimarket.factory.entity;

import com.practice.springbootrestapimarket.entity.member.Role;
import com.practice.springbootrestapimarket.entity.member.RoleType;

public class RoleFactory {

    public static Role createRole() {
        return new Role(RoleType.ROLE_NORMAL);
    }

}
