package jihyun.core.order;

import jihyun.core.discount.DiscountPolicy;
import jihyun.core.discount.FixedDiscountPolicy;
import jihyun.core.member.Member;
import jihyun.core.member.MemberRepository;
import jihyun.core.member.MemoryMemberRepository;

public class OrderServiceImpl implements OrderService {

    private final MemberRepository memberRepository = new MemoryMemberRepository();
    private final DiscountPolicy discountPolicy = new FixedDiscountPolicy();

    @Override
    public Order createOrder(Long memberId, String itemName, int itemPrice) {
        Member member = memberRepository.findById(memberId);
        int discountPrice = discountPolicy.discount(member, itemPrice);

        return new Order(memberId, itemName, itemPrice, discountPrice);
    }
}
