package jpabook.jpashop.service;

import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.SearchRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;


// 왜 주문 함수의 인자로 memberId 와 itemId와 count 가 넘어 오나?
//

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {
    private final EntityManager em;

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    @Transactional
    public Long order(Long memberId, Long itemId, int count) {
        // 멤버 객체 조회 해오기 (인자로 넘어온 memberId로  멤버 조회 , 배송 정보 설정과 주문 정보 생성에 필요)
        Member member = memberRepository.findOne(memberId);

        // 상품 객체  조회 해오기 ( 인자로 넘어온 itemId로 상품 정보 조회, 상품은 하나씩만 선택 )
        Item item = itemRepository.findOne(itemId);

        // 주문 데이터 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        // 배송 관련 데이터 생성
        Delivery delivery = new Delivery();
        delivery.setAddress((member.getAddress()));
        delivery.setStatus(DeliveryStatus.READY);

        // 주문 객체 생성
        Order order = Order.createOrder(member, delivery, orderItem);

        // 주문 데이터 저장
        orderRepository.save(order);

        // 주문 아이디 반환
        return order.getId();
    }


    // 주문 취소 함수 추가
    public void cancelOrder(Long orderId) {

        // orderId로 주문 정보 조회
        Order order = orderRepository.findOne(orderId);
        System.out.println("취소할 주문 : " + order);

        // 주문 취소
        order.cancel();
    }

    public List<Order> searchOrderBySearchRequirement(SearchRequirement sr) {
        return orderRepository.findAllByString(sr);
    }

    public List<Order> searchOrder2(SearchRequirement sr) {
          return orderRepository.findAllByString(sr);
    }

    // 주문 조회
    public List<Order> findOrders(SearchRequirement sr) {
        return orderRepository.findAllByString(sr);
    }

    // 주문 취소 하기
    @Transactional
    public void cancleOrder(Long orderId) {
        Order order = orderRepository.findOne(orderId);
        System.out.println("cancel order ::::::::::::::::" + order);
        // 주문 취소
        order.cancel();
    }


}
