package jihyun.core;

import jihyun.core.member.Grade;
import jihyun.core.member.Member;
import jihyun.core.member.MemberService;
import jihyun.core.order.Order;
import jihyun.core.order.OrderService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class OrderApp {

    public static void main(String[] args) {
        // 스프링 컨테이너, AppConfig를 설정 정보로 사용
        ApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);
        // param1 = 스프링 빈 이름(default = @Bean 메서드 이름), param2 = 스프링 빈이 속한 클래스
        MemberService memberService = ac.getBean("memberService", MemberService.class);
        OrderService orderService = ac.getBean("orderService", OrderService.class);

        long memberId = 1L;
        Member member = new Member(memberId, "memberA", Grade.VIP);
        memberService.join(member);
        Order order = orderService.createOrder(memberId, "itemA", 10000);

        System.out.println("order = " + order);
    }
}
