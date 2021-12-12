package jpabook.jpashop.repository;
import jpabook.jpashop.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;


@Repository
@RequiredArgsConstructor
public class OrderRepository {
    private final EntityManager em;

    //  주문 정보 저장 함수
    public void save(Order order) {
        em.persist(order);
    }

    // 주문 정보 하나 찾기
    public Order findOne(Long id) {
        return em.find(Order.class, id);
    }

    public List<Order> findAllByString(SearchRequirement sr) {
        String jpql = "select o from Order o join o.member m";
        boolean isFirstCondition = true;

        //  검색 조건
        if (sr.getOrderStatus() != null) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " o.status = :status";
        }

        //회원 이름 검색
        if (StringUtils.hasText(sr.getMemberName())) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " m.name like :name";
        }

        TypedQuery<Order> query = em.createQuery(jpql, Order.class)
                .setMaxResults(1000);

        if (sr.getOrderStatus() != null) {
            query = query.setParameter("status", sr.getOrderStatus());
        }
        if (StringUtils.hasText(sr.getMemberName())) {
            query = query.setParameter("name", sr.getMemberName());
        }

        return query.getResultList();
    }
}

