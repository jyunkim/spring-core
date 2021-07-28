package jihyun.core;

import jihyun.core.discount.FixedDiscountPolicy;
import jihyun.core.member.MemberService;
import jihyun.core.member.MemberServiceImpl;
import jihyun.core.member.MemoryMemberRepository;
import jihyun.core.order.OrderService;
import jihyun.core.order.OrderServiceImpl;

public class AppConfig {

    public MemberService memberService() {
        return new MemberServiceImpl(new MemoryMemberRepository());
    }

    public OrderService orderService() {
        return new OrderServiceImpl(new MemoryMemberRepository(), new FixedDiscountPolicy());
    }
}
