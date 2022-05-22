package com.practice.springbootrestapimarket.dto;

import com.practice.springbootrestapimarket.entity.member.Member;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {
    private Long id;
    private String email;
    private String username;
    private String nickname;

    public static MemberDto toDto(Member member){
        return new MemberDto(member.getId(), member.getEmail(), member.getUsername(), member.getNickname());
    }
}
