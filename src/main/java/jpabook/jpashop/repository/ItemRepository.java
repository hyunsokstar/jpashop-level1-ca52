package jpabook.jpashop.repository;
import jpabook.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import java.util.List;


@Repository // 이거 뭐지?
@RequiredArgsConstructor // 이거 뭐지?
public class ItemRepository {

    private final EntityManager em;

    // 상품 데이터 저장을 위한 함수
    // item 객체로 저장하려고 했는데 이전에 item.getId()를 통해 실제 디비에 값이 존재하는지 조사한뒤
    // 존재할 경우 수정
    // 존재 하지 않을 경우 저장
    public void save(Item item) {
        if (item.getId() == null) { 
            em.persist(item);
        } else {
            em.merge(item);
        }
    }

    // 개별 아이템 id로 조회 하기
    public Item findOne(Long id) {
        return em.find(Item.class, id);
    }

    // 상품 목록 일괄 조회
    // createQuery 함수와 jpql 문법 사용
    public List<Item> findAll() {
        return em.createQuery("select i from Item i", Item.class)
                .getResultList();
    }

}
