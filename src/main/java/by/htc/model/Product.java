package by.htc.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Класс(сущность) товара, хранящаяся в таблице products.
 */
@Entity
@Table(name = "products")
public class Product {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @ManyToOne
    @JoinColumn(name = "id_price", nullable = false)
    private Price price;
    private int code;
    private String name;
    private String barCode;
    private BigDecimal quantity;
    private String model;
    private String sort;
    private String color;
    private String size;
    private String weight;

    @Column(name = "date_changes")
    private LocalDateTime dateChanges;

    //Форматирование даты
    public String getFormattedDate() {
        return dateChanges != null
                ? dateChanges.format(java.time.format.DateTimeFormatter.ofPattern("dd.MM.yyyy")) : "";
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public void setDateChanges(LocalDateTime dateChanges) {
        this.dateChanges = dateChanges;
    }

    public String getId() {
        return id;
    }

    public Price getPrice() {
        return price;
    }

    public int getCode() {
        return code;
    }

    public String getBarCode() {
        return barCode;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public String getModel() {
        return model;
    }

    public String getSort() {
        return sort;
    }

    public String getColor() {
        return color;
    }

    public String getSize() {
        return size;
    }

    public String getWeight() {
        return weight;
    }

}


