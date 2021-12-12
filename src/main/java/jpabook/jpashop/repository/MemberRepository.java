package jpabook.jpashop.repository;
import jpabook.jpashop.domain.Member;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;


@Repository
public class MemberRepository {

    @PersistenceContext
    private EntityManager em;

    // 멤버 객체를 인자로 받아 db에 저장
    public void save(Member member){
        em.persist(member);
    }

    // 숫자 id로  멤버 엔티티를 조회 한 결과를 리턴
    public Member findOne(Long id) {
        return em.find(Member.class, id);
    }

    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class).getResultList();
    }

    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name= :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }

}
