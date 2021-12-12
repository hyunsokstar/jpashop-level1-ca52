package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;


@RunWith(SpringRunner.class) // 이거 뭐지?
@SpringBootTest
@Transactional // 이거 의미는?
public class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;


    @Test
    public void 서비스함수_회원저장_테스트() {
        // step1: 회원 가입후 id 반환 받기
        Member member = new Member();
        member.setName("hyunsok");
        Long savedMemberId = memberService.join(member);
        System.out.println("savedMemberId : "+ savedMemberId);

        // step2 반환 받은 id로 회원 한명 조회 해서 멤버 객체 리턴 받기
        Member findMember = memberRepository.findOne(savedMemberId);

        //  a 와 b 가 같은 객체임을 확인
        Assertions.assertEquals(member, findMember);

    }

}