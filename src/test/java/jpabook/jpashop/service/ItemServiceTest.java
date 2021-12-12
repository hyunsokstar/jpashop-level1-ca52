package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;



@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class ItemServiceTest {

    @Autowired
    ItemService itemService;

    @Autowired
    ItemRepository itemRepository;

    @Test
    public void 상품데이터저장테스트() {

        // 객체 생성
        Item item = new Item();
        item.setName("bmw");
        item.setPrice(10000);
        // 저장
        itemService.saveItem(item);
        System.out.println("상품데이터저장테스트 실행 확인 !!!!!!!!!!!!!!!!!!!");

        // 생성된 아이디 얻어 오기 + 확인 하기
        Long savedItemId = item.getId();
        System.out.println("savedItemId : " + savedItemId);

        // 생성된 아이디로 조회
        Item findItem = itemService.findOne(savedItemId);
        // Category category1 = new Category();
        // item.setCategories([categor1]); 카테고리는 어떻게?

        // 예상, 실제(조회한것) 객체 비교
        Assertions.assertEquals(item, findItem);
    }

    @Test
    public void 전체상품리스튼조회테스트() {

        // 상품 데이터 저장 1
        Item item1 = new Item();
        item1.setName("bmw");
        item1.setPrice(10000);

        // 상품 데이터 저장 2
        Item item2 = new Item();
        item2.setName("bmw2");
        item2.setPrice(20000);

        // 예상 리스트 미리 만들기
        List<Item> expectedResult = Arrays.asList(item1, item2);

        // 상품 데이터 2개 저장
        itemService.saveItem(item1);
        itemService.saveItem(item2);

        // 리스트 조회
        List<Item> itemList = itemService.findItems();

        // 예상, 실제 비교
        Assertions.assertEquals(expectedResult, itemList);
    }

    @Test
    public void 단일상품조회테스트() {
        // 상품 데이터 생성
        Item item1 = new Item();
        item1.setName("bmw");
        item1.setPrice(10000);

        // 상품 데이터 생성
        Item item2 = new Item();
        item2.setName("bmw2");
        item2.setPrice(20000);
        
        // 상품 데이터 2개 저장
        itemService.saveItem(item1);
        itemService.saveItem(item2);

        // 첫번째 item id 조회
        Long savedFirstItemId = item1.getId();
        // 첫번재 item id로 상품 엔티티 조회
        Long resultItemId = itemService.findOne(savedFirstItemId).getId();

        // 예상 실제 비교
        Assertions.assertEquals(savedFirstItemId, resultItemId);
    }

}