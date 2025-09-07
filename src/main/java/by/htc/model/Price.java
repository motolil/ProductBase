package by.htc.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс(сущность) цен товаров, хранящаяся в таблице prices.
 */
@Entity
@Table(name = "prices")
public class Price {
    @Id
    private String id;

    @Column(nullable = false)
    private BigDecimal price;

    @OneToMany(mappedBy = "price", cascade = CascadeType.ALL)
    private List<Product> products = new ArrayList<>();

    public void setId(String id) {
        this.id = id;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public String getId() {
        return id;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public List<Product> getProducts() {
        return products;
    }

}


