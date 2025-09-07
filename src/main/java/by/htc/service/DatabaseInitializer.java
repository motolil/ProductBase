package by.htc.service;

import by.htc.model.Price;
import by.htc.model.Product;
import by.htc.util.HibernateUtil;
import org.hibernate.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * Класс для проверки наличия данных в таблице и автоматическое заполнение.
 */
public class DatabaseInitializer {
    public static void initialize() {
        Random random = new Random();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();

            // Проверка наличия товара в таблицах, если нет - заполняем 1000
            Integer count = (Integer) session.createNativeQuery("SELECT COUNT(*) FROM products").uniqueResult();

            if (count == 0) {

                // Создание 100 разных цен для привязки к товарам
                List<Price> priceList = new ArrayList<>();
                for (int i = 0; i < 100; i++) {
                    Price price = new Price();
                    price.setId(UUID.randomUUID().toString());
                    price.setPrice(BigDecimal.valueOf(99.99 + i + random.nextInt(100)));
                    session.save(price);
                    priceList.add(price);
                }

                // Разные характеристики
                String[] models = {"A", "B", "C", "D", "E"};
                String[] sorts = {"Премиум", "Стандарт", "Эконом"};
                String[] colors = {"Черный", "Белый", "Синий", "Красный", "Зеленый", "Серый", "Бежевый", "Желтый", "Фиолетовый", "Оранжевый"};
                String[] sizes = {"Обычный", "Большой", "Огромный", "Маленький", "Крошечный"};

                //  Создание 1000 товаров
                for (int i = 1; i <= 1000; i++) {
                    Product product = new Product();
                    product.setId(UUID.randomUUID().toString());
                    product.setCode(100000 + i);
                    product.setName("Товар №" + i);
                    product.setBarCode(String.format("%13d", 460000000000L + i));
                    product.setQuantity(BigDecimal.valueOf(random.nextInt(100)));

                    // Каждым 10 товарам - одна цена
                    Price price = priceList.get((i - 1) / 10);
                    product.setPrice(price);
                    price.getProducts().add(product);

                    product.setModel(models[i % models.length]);
                    product.setSort(sorts[i % sorts.length]);
                    product.setColor(colors[i % colors.length]);
                    product.setSize(sizes[i % sizes.length]);
                    product.setWeight(String.valueOf(random.nextInt(1000)));

                    // Разная дата изменения
                    product.setDateChanges(LocalDateTime.now().minusDays(i % 60).minusHours(i % 24).minusMinutes(i % 60));

                    session.save(product);
                }

                session.getTransaction().commit();
            }

        }
        catch(Exception e){
            System.err.println("Ошибка при создании записей: " + e.getMessage());
        }
    }
}


