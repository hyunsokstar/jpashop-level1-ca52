package jpabook.jpashop.domain;

import jpabook.jpashop.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


@Entity
@Getter
@Setter
public class OrderItem {

    @Id
    @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    private int orderPrice;
    private int count;

    // 주문 상품 데이터는 상품, 가격, 수량에 대한 정보를 나타내는 데이터를 말한다.
    // so 주문 데이터를 생성 하기 이전에 주문 상품 데이터를 먼저 생성 한뒤 주문 데이터에서 이를 참조 하도록 해야 하며
    // 상품의 재고 수량은 감소 시켜야 한다.
    // 필수 비즈니스 로직으로 주문 상품 데이터 추가 함수를 미리 만들어 놓을 필요가  있다.
    public static OrderItem createOrderItem(Item item, int orderPrice, int count) {
        // 주문 상품 객체 생성
        OrderItem orderItem = new OrderItem();
        // 필드 초기화
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);
        // 재고 수량 줄이기
        item.removeStock(count);
        // 주문 상품 객체 리턴
        return orderItem;
    }

    // 필수 비즈니스 로직 추가
    // 주문 상품 가격 계산 <=> 주문 가격 * 수량
    public int getTotalPrice() {
        return getOrderPrice() * getCount();
    }

    // src\main\java\jpabook\jpashop\domain\OrderItem.java
    // 주문 취소, 재고 수량 원복
    public void cancel() {
        getItem().addStock(count);
    }

}
