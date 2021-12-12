package jpabook.jpashop.domain;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;


@Entity
@Table(name = "orders")
@Getter
@Setter
public class Order {

    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    // 하나의 주문과 여러개의  주문 상품 관계가 연관 관계를 가질수 있음
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = LAZY, cascade = CascadeType.ALL)
    private Delivery delivery;

    // 주문 상태
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    // 주문 날짜
    private LocalDateTime orderDate;

    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    // 주문 데이터 생성
    // 주문 정보 데이터 생성이란 주문 상품, 주문 상태, 주문 날짜  데이터를 초기화 하는것을 말한다.
    // 양방향 연관 관계 편의 함수 addOrderItem 을 이용해 주문에 속하는 여러개의 주문 상품 데이터에 대해 연관 관계 데이터를 설정
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems) {
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        
        // 주문 상품 정보 여러개를 인자로 받아와서 주문 엔티티의 가상 필드 orderItems에 추가 하고 주문 상품 데이터의 주문 fk도 초기화
        // 양방향 연관관게 편의 함수 orderItems 를 추가
        for(OrderItem orderItem: orderItems) {
            order.addOrderItem(orderItem);
        }

        order.setStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());

        return order;
    }

    // 필수 비즈니스 로직 추가
    // 주문 상품들의 가격  총합
    public int getTotalPrice() {
        int totalPrice = 0;
        for (OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }
    // 주문 취소 함수 추가
    public void cancel() {
        if (delivery.getStatus() == DeliveryStatus.COMP) {
            throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능합니다.");
        }

        this.setStatus(OrderStatus.CANCEL);
        for (OrderItem orderItem : orderItems) {
            orderItem.cancel();
        }
    }

}