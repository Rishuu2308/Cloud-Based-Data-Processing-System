package com.dataspace.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import com.opencsv.bean.CsvBindByName;

import java.time.LocalDate;

@Entity
@Table(name = "ecommerce_orders")
public class EcommerceOrder {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CsvBindByName(column = "Order ID") // Maps "Order ID" column to this field
    @NotNull(message = "Order ID cannot be null")
    private String orderId;

    @CsvBindByName(column = "Order Date")
    private LocalDate orderDate;

    @CsvBindByName(column = "Customer ID")
    @NotNull(message = "Customer ID cannot be null")
    @Positive(message = "Customer ID must be positive")
    private Long customerId;

    @CsvBindByName(column = "Customer Name")
    @NotBlank(message = "Customer name cannot be blank")
    private String customerName;

    @CsvBindByName(column = "Product")
    @NotBlank(message = "Product cannot be blank")
    private String product;

    @CsvBindByName(column = "Category")
    @NotBlank(message = "Category cannot be blank")
    private String category;

    @CsvBindByName(column = "Quantity")
    @NotNull(message = "Quantity cannot be null")
    @Positive(message = "Quantity must be greater than zero")
    private Integer quantity;

    @CsvBindByName(column = "Unit Price")
    @NotNull(message = "Unit price cannot be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Unit price must be greater than zero")
    private Double unitPrice;

    private Double totalPrice;
    private Double discount;
    private String reformatOrderDate;
    private Double totalPriceAfterDiscount;
    public String getReformatOrderDate() {
        return reformatOrderDate;
    }
    public Double getTotalPriceAfterDiscount() {
        return totalPriceAfterDiscount;
    }

    public void setTotalPriceAfterDiscount(Double totalPriceAfterDiscount) {
        this.totalPriceAfterDiscount = totalPriceAfterDiscount;
    }
    public void setReformatOrderDate(String reformatOrderDate) {
        this.reformatOrderDate = reformatOrderDate;
    }
    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Long getId() {
        return id;
    }

    public String getOrderId() {
        return orderId;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getProduct() {
        return product;
    }

    public String getCategory() {
        return category;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }



    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }


}

