package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    // 회원 저장, 멤버 객체 받아서 저장 + id 리턴(저장한뒤 id  자동 설정 됨)
    @Transactional
    public Long join(Member member) {
        validateDuplicateMember(member);
        memberRepository.save(member); // save 할때  setId 된뒤 저장됨
        return member.getId();
    }

    // 멤버 엔티티를 이름으로 조회해서 없으면  IllegalStateException 가 발생하는 유효성 검사 함수 만들기
    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getName());

        if (!findMembers.isEmpty()) {
            throw new IllegalStateException(("이미 존재하는 회원 입니다"));
        }
    }

    // 회원 엔티티 전체 조회 , 리턴 리스트
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    // 회원 한명 조회, 멤버 객체 리턴
    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }
    
}
