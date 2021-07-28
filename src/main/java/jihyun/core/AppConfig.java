package jihyun.core;

import jihyun.core.discount.DiscountPolicy;
import jihyun.core.discount.FixedDiscountPolicy;
import jihyun.core.member.MemberRepository;
import jihyun.core.member.MemberService;
import jihyun.core.member.MemberServiceImpl;
import jihyun.core.member.MemoryMemberRepository;
import jihyun.core.order.OrderService;
import jihyun.core.order.OrderServiceImpl;

public class AppConfig {

    public MemberService memberService() {
        return new MemberServiceImpl(memberRepository());
    }

    private MemberRepository memberRepository() {
        return new MemoryMemberRepository();
    }

    public OrderService orderService() {
        return new OrderServiceImpl(memberRepository(), discountPolicy());
    }

    private DiscountPolicy discountPolicy() {
        return new FixedDiscountPolicy();
    }
}
