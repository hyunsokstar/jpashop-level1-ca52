package jpabook.jpashop.domain.item;
import jpabook.jpashop.domain.Category;
import jpabook.jpashop.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Setter
public  class Item {

    @Id
    @GeneratedValue
    @Column(name="item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    // 여러개의 아이템이 여러개의 카테고리와 연관 관계를 가질 수 있다. (양방향 참조 허용)
    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();


    //  Item 엔티티 관련 필수 비즈니스 로직 추가

    // 1. 재고 증가
    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }

    // 2. 재고 감소
    public void removeStock(int quantity) {
        int restStock = this.stockQuantity - quantity;
        if(restStock < 0 ) {
            throw new NotEnoughStockException("need more stock");
        }
        this.stockQuantity = restStock;
    }

}
