package jpabook.jpashop.service;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.SearchRequirement;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import java.util.List;

import static org.assertj.core.api.Fail.fail;


@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class OrderServiceTest {

    @Autowired
    EntityManager em;

    @Autowired
    OrderService orderService;

    @Autowired
    OrderRepository orderRepository;

    // 서비스 계층 주문 함수 테스트
    // public Long order(Long memberId, Long itemId, int count) {..}
    @Test
    public void 서비스_주문함수테스트() {

        // 회원 데이터 생성
        Member member = createMember(); // 회원 데이터 생성 함수 별개로 작성
        // 상품 데이터 생성
        Item item = createBook("사이버트럭", 20000, 10); // 상품 데이터 생성
        // 주문 수량 설정
        int orderCount = 2;

        // 주문 데이터 생성
        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);

        // 생성한 주문 데이터 조회해 보기
        Order resultOrder = orderRepository.findOne(orderId);

        // 여기서부터 데이터 검증

        // 조회한  주문 데이터의 OrderStatus = OrderStatus.ORDER
        Assertions.assertEquals(OrderStatus.ORDER, resultOrder.getStatus());
        // 주문 상품 데이터의 개수는 1개이다.
        Assertions.assertEquals(1, resultOrder.getOrderItems().size());
        // 주문 상품들의 총 가격 <=> 20000 * 2 = 40000
        Assertions.assertEquals(40000, resultOrder.getTotalPrice());
        // 주문 수량만큼 재고수 감소 check <=> 재고: 10 - 주문 2 = 8 (남은 재고)
        Assertions.assertEquals(8, item.getStockQuantity());

    }

    @Test
    public void 서비스_주문취소_함수_테스트() throws Exception {
        Member member = createMember();
        Item item = createBook("카누", 1000, 10);
        int orderCount = 2;

        // 주문 데이터 생성 + id 반환
        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);

        // 생성한 주문 데이터에 대해 주문 취소 하기
        orderService.cancelOrder(orderId);

        // 생성한 주문 데이터 조회 by 반환 받은 id
        Order resultOrder = orderRepository.findOne(orderId);

        // 생성한 주문 데이터 주문 상태는 취소 이어야 한다.
        Assertions.assertEquals(OrderStatus.CANCEL, resultOrder.getStatus());
    }

    @Test(expected = NotEnoughStockException.class)
    public void 주문수량이재고수량보다많을경우에러발생확인() {
        Member member = createMember();
        Item item = createBook("카누", 10000, 10);
        int orderCount = 11;

        orderService.order(member.getId(), item.getId(), orderCount);

        // 에러 안발생할 경우 fail() 실행
        fail("재고 10개 주문 수량이 11개인데 에러 안발생 ");
    }

    @Test
    public void 주문데이터_이름_검색조건_으로조회후리스트반환() {

        Member member = createMember();
        Item item = createBook("카누", 10000, 10);
        int orderCount = 2;

        orderService.order(member.getId(), item.getId(), orderCount);

        SearchRequirement sr = new SearchRequirement();
        sr.setOrderStatus(OrderStatus.ORDER);
        sr.setMemberName("hyun1");

        List<Order> result = orderService.searchOrderBySearchRequirement(sr);
        System.out.println("result : "+ result);

        // 검색 결과 1개
        Assertions.assertEquals(1,  result.size());

    }


    // 회원 데이터 생성
    private Member createMember() {
        Member member = new Member();
        member.setName("hyun1");
        member.setAddress(new Address("서울", "강변", "128-220"));
        em.persist(member);
        return member;
    }

    // 상품 데이터 생성
    private Book createBook(String name, int price, int stockQuantity) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }

}