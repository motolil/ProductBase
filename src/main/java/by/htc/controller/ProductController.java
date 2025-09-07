package by.htc.controller;

import by.htc.model.Price;
import by.htc.model.Product;
import by.htc.util.HibernateUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import org.hibernate.Session;
import org.hibernate.query.Query;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * Контроллер для отображения и изменения товаров в визуальной таблице.
 */
public class ProductController {

    //Поля таблицы
    @FXML private TableView<Product> productTable;
    @FXML private TableColumn<Product, Integer> codeColumn;
    @FXML private TableColumn<Product, String> nameColumn;
    @FXML private TableColumn<Product, String> barCodeColumn;
    @FXML private TableColumn<Product, String> priceColumn;
    @FXML private TableColumn<Product, BigDecimal> quantityColumn;
    @FXML private TableColumn<Product, String> modelColumn;
    @FXML private TableColumn<Product, String> sortColumn;
    @FXML private TableColumn<Product, String> colorColumn;
    @FXML private TableColumn<Product, String> sizeColumn;
    @FXML private TableColumn<Product, String> weightColumn;
    @FXML private TableColumn<Product, String> dateChangesColumn;
    @FXML private TextField searchField;

    // Поля формы
    @FXML private GridPane formPane;
    @FXML private TextField codeField, nameField, barCodeField, priceField, quantityField;
    @FXML private TextField modelField, sortField, colorField, sizeField, weightField;

    private ObservableList<Product> productList = FXCollections.observableArrayList();
    private boolean isEditMode = false;
    private Product editingProduct = null;

    //Инициализация(загрузка таблицы)
    @FXML
    public void initialize() {
        codeColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getCode()).asObject());
        nameColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getName()));
        barCodeColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getBarCode()));
        priceColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getPrice().getPrice().toString()));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        modelColumn.setCellValueFactory(new PropertyValueFactory<>("model"));
        sortColumn.setCellValueFactory(new PropertyValueFactory<>("sort"));
        colorColumn.setCellValueFactory(new PropertyValueFactory<>("color"));
        sizeColumn.setCellValueFactory(new PropertyValueFactory<>("size"));
        weightColumn.setCellValueFactory(new PropertyValueFactory<>("weight"));
        dateChangesColumn.setCellValueFactory(cellData
                -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getFormattedDate()));

        loadProducts();
    }

    private void loadProducts() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Product> products = session.createNativeQuery("SELECT * FROM products", Product.class).list();
            productList.setAll(products);
            productTable.setItems(productList);
        }
    }

    @FXML
    private void onSearch() {
        String keyword = searchField.getText().trim().toLowerCase(Locale.forLanguageTag("ru"));
        String keyword_name = searchField.getText();

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
             Query<Product> query = session.createNativeQuery(
                    "SELECT * FROM products p JOIN prices pr ON p.id_price = pr.id WHERE " +
                            "CAST(p.code AS TEXT) LIKE :kw OR " +
                            "LOWER(p.name) LIKE :kw_name OR " +
                            "LOWER(p.barCode) LIKE :kw OR " +
                            "CAST(pr.price AS TEXT) LIKE :kw", Product.class);
            query.setParameter("kw", "%" + keyword + "%");
            query.setParameter("kw_name", "%" + keyword_name + "%");

            productList.setAll(query.list());
            productTable.setItems(productList);
        }
    }

    @FXML
    private void onAddProduct() {
        isEditMode = false;
        editingProduct = null;
        clearForm();
        formPane.setVisible(true);
        formPane.setManaged(true);
    }

    @FXML
    private void onEditProduct() {
        Product selected = productTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            isEditMode = true;
            editingProduct = selected;
            fillForm(selected);
            formPane.setVisible(true);
            formPane.setManaged(true);
        }
    }

    @FXML
    private void onDeleteProduct() {
        Product selected = productTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                session.beginTransaction();
                session.delete(selected);
                session.getTransaction().commit();
                productList.remove(selected);
            }
        }
    }

    @FXML
    private void onSaveProduct() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();

            Product product = isEditMode ? editingProduct : new Product();
            if (!isEditMode) {
                product.setId(UUID.randomUUID().toString());
            }

            product.setCode(Integer.parseInt(codeField.getText()));
            product.setName(nameField.getText());
            product.setBarCode(barCodeField.getText());
            product.setQuantity(new BigDecimal(quantityField.getText()));
            product.setModel(modelField.getText());
            product.setSort(sortField.getText());
            product.setColor(colorField.getText());
            product.setSize(sizeField.getText());
            product.setWeight(weightField.getText());
            product.setDateChanges(LocalDateTime.now());

            if (isEditMode) {
                Price existingPrice = product.getPrice();
                existingPrice.setPrice(new BigDecimal(priceField.getText()));
                session.merge(existingPrice);
            } else {
                Price newPrice = new Price();
                newPrice.setId(UUID.randomUUID().toString());
                newPrice.setPrice(new BigDecimal(priceField.getText()));
                newPrice.getProducts().add(product);
                product.setPrice(newPrice);
                session.persist(newPrice);
            }

            if (isEditMode) {
                session.merge(product);
            } else {
                session.persist(product);
            }

            session.getTransaction().commit();
            loadProducts();
            formPane.setVisible(false);
            formPane.setManaged(false);
        }
    }

    @FXML
    private void onCancelForm() {
        formPane.setVisible(false);
        formPane.setManaged(false);
        clearForm();
    }

    private void clearForm() {
        codeField.clear();
        nameField.clear();
        barCodeField.clear();
        priceField.clear();
        quantityField.clear();
        modelField.clear();
        sortField.clear();
        colorField.clear();
        sizeField.clear();
        weightField.clear();
    }

    private void fillForm(Product product) {
        codeField.setText(String.valueOf(product.getCode()));
        nameField.setText(product.getName());
        barCodeField.setText(product.getBarCode());
        priceField.setText(product.getPrice().getPrice().toString());
        quantityField.setText(product.getQuantity().toString());
        modelField.setText(product.getModel());
        sortField.setText(product.getSort());
        colorField.setText(product.getColor());
        sizeField.setText(product.getSize());
        weightField.setText(product.getWeight());
    }
}

