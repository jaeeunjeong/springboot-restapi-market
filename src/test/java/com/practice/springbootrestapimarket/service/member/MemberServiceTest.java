package com.practice.springbootrestapimarket.service.member;

import com.practice.springbootrestapimarket.dto.MemberDto;
import com.practice.springbootrestapimarket.entity.member.Member;
import com.practice.springbootrestapimarket.exception.MemberNotFoundException;
import com.practice.springbootrestapimarket.repository.member.MemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static com.practice.springbootrestapimarket.factory.entity.MemberFactory.createMember;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @InjectMocks
    MemberService memberService;
    @Mock
    MemberRepository memberRepository;

    @Test
    void readTest() {
        // given
        Member member = createMember();
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));

        // when
        MemberDto result = memberService.read(1L);

        // then
        assertThat(result.getId()).isEqualTo(member.getId());
        assertThat(result.getEmail()).isEqualTo(member.getEmail());
    }

    @Test
    void readMemberNotFoundTest() {
        // given
        given(memberRepository.findById(any())).willReturn(Optional.ofNullable(null));

        // when, then
        assertThatThrownBy(() -> memberService.read(1L)).isInstanceOf(MemberNotFoundException.class);
    }

    @Test
    void deleteTest() {
        // given
        given(memberRepository.existsById(anyLong())).willReturn(true);

        // when
        memberService.delete(0L);

        // then
        verify(memberRepository).deleteById(anyLong());
    }

    @Test
    void deleteMemberNotFound() {
        // given
        given(memberRepository.existsById(any())).willReturn(false);

        // when, then
        assertThatThrownBy(()-> memberService.delete(1L)).isInstanceOf(MemberNotFoundException.class);
    }

}