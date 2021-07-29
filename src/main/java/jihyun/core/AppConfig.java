package jihyun.core;

import jihyun.core.discount.DiscountPolicy;
import jihyun.core.discount.FixedDiscountPolicy;
import jihyun.core.member.MemberRepository;
import jihyun.core.member.MemberService;
import jihyun.core.member.MemberServiceImpl;
import jihyun.core.member.MemoryMemberRepository;
import jihyun.core.order.OrderService;
import jihyun.core.order.OrderServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    // 반환된 객체가 스프링 빈으로 스프링 컨테이너에 등록됨
    @Bean
    public MemberService memberService() {
        return new MemberServiceImpl(memberRepository());
    }

    @Bean
    public MemberRepository memberRepository() {
        return new MemoryMemberRepository();
    }

    @Bean
    public OrderService orderService() {
        return new OrderServiceImpl(memberRepository(), discountPolicy());
    }

    @Bean
    public DiscountPolicy discountPolicy() {
        return new FixedDiscountPolicy();
    }
}
