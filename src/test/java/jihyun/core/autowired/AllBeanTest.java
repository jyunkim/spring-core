package jihyun.core.autowired;

import jihyun.core.AutoAppConfig;
import jihyun.core.discount.DiscountPolicy;
import jihyun.core.member.Grade;
import jihyun.core.member.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

public class AllBeanTest {

    @Test
    void findAllBean() {
        // DiscoutService도 스프링 빈으로 등록
        ApplicationContext ac = new AnnotationConfigApplicationContext(AutoAppConfig.class, DiscountService.class);
        DiscountService discountService = ac.getBean(DiscountService.class);
        Member member = new Member(1L, "userA", Grade.VIP);
        int discountPrice = discountService.discount(member, 10000, "fixedDiscountPolicy");

        assertThat(discountPrice).isEqualTo(1000);
    }

    static class DiscountService {

        private final Map<String, DiscountPolicy> policyMap;
        private final List<DiscountPolicy> policies;

        // @Autowired 생략
        public DiscountService(Map<String, DiscountPolicy> policyMap, List<DiscountPolicy> policies) {
            this.policyMap = policyMap;
            this.policies = policies;
            // map의 키에 스프링 빈의 이름을 넣어주고, 값으로 DiscountPolicy 타입으로 조회한 모든 스프링 빈을 담는다
            System.out.println("policyMap = " + policyMap);
            // DiscountPolicy 타입으로 조회한 모든 스프링 빈을 담는다
            System.out.println("policies = " + policies);
        }

        // 인자로 넘어온 정책에 따라 할인 방식 결정
        public int discount(Member member, int price, String discountCode) {
            DiscountPolicy discountPolicy = policyMap.get(discountCode);
            return discountPolicy.discount(member, price);
        }
    }
}
