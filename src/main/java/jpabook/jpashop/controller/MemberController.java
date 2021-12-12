package jpabook.jpashop.controller;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import javax.validation.Valid;

import org.springframework.validation.BindingResult;

import java.util.List;


@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/members/new")
    public String createForm(Model model) {
        model.addAttribute("memberForm", new MemberForm());
        return "members/createMemberForm";
    }

    //    @PostMapping("/members/new")
    //    public String create(@Valid MemberForm form) {
    //        Address address = new Address(form.getCity(), form.getStreet(), form.getZipcode());
    //        Member member = new Member();
    //        member.setName(form.getName());
    //        // 엔티티 필드가 객체이므로 객체를 set 한다.
    //        member.setAddress(address);
    //        // 서비스 계층 함수를 이용해 회원 가입
    //        memberService.join(member);
    //        return "redirect:/";
    //    }

    @PostMapping("/members/new")
    public String create(@Valid MemberForm form, BindingResult result) {

        if (result.hasErrors()) {
            return "members/createMemberForm";
        }

        Address address = new Address(form.getCity(), form.getStreet(), form.getZipcode());

        Member member = new Member();
        member.setName(form.getName());
        member.setAddress(address);

        memberService.join(member);
        return "redirect:/";
    }

    @GetMapping("/members")
    public String list(Model model) {
        List<Member> members = memberService.findMembers();
        model.addAttribute("members", members);

        return "members/memberList";
    }


}
