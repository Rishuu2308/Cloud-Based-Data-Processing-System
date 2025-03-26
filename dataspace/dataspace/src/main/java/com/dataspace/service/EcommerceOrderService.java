package com.dataspace.service;

import com.dataspace.model.EcommerceOrder;
import com.dataspace.repository.EcommerceOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EcommerceOrderService {

    @Autowired
    EcommerceOrderRepository ecommerceOrderRepository;

    public EcommerceOrder saveOrder(EcommerceOrder ecommerceOrder) {
        LocalDate originalDate = ecommerceOrder.getOrderDate();
        if(originalDate == null){
            System.out.println("date");
            originalDate = LocalDate.now();
            System.out.println(originalDate);
            ecommerceOrder.setOrderDate(originalDate);
        }
        ecommerceOrder.setReformatOrderDate(
                    reformatDate(String.valueOf(originalDate))
            );
        if (ecommerceOrder.getQuantity() != null && ecommerceOrder.getUnitPrice() != null) {
            double total = ecommerceOrder.getQuantity() * ecommerceOrder.getUnitPrice();
            ecommerceOrder.setTotalPrice(total);
        }
        else{
            ecommerceOrder.setTotalPrice(0.0);
        }
        double total = (ecommerceOrder.getQuantity() != null ? ecommerceOrder.getQuantity() : 0)
                * (ecommerceOrder.getUnitPrice() != null ? ecommerceOrder.getUnitPrice() : 0.0);

        double discount = 0.0;
        if (total > 500) {
            discount = total * 0.15;
        } else if (total > 100) {
            discount = total * 0.10;
        }

        double finalPrice = total - discount;
        ecommerceOrder.setDiscount(discount);
        ecommerceOrder.setTotalPriceAfterDiscount(finalPrice);

        return ecommerceOrderRepository.save(ecommerceOrder);
    }

    private String reformatDate(String dateStr) {
        try {
            SimpleDateFormat fromSdf = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat toSdf = new SimpleDateFormat("MM/dd/yyyy");
            Date date = fromSdf.parse(dateStr);
            return toSdf.format(date);
        } catch (Exception e) {

            throw new RuntimeException("Error reformatting date: " + dateStr, e);
        }
    }

    public List<EcommerceOrder> getAllOrders() {
        return ecommerceOrderRepository.findAll();
    }

    public List<EcommerceOrder> getOrdersByDateRange(LocalDate startDate, LocalDate endDate) {
        return ecommerceOrderRepository.findByOrderDateBetween(startDate, endDate);
    }
    public List<EcommerceOrder> getOrdersByCustomer(Long customerId) {
        return ecommerceOrderRepository.findByCustomerId(customerId);
    }

    public List<EcommerceOrder> getOrdersByCategory(String category) {
        return ecommerceOrderRepository.findByCategory(category);
    }

    public long getTotalOrders() {
        return ecommerceOrderRepository.count();
    }

    // Method to calculate total revenue
    public double getTotalRevenue() {
        return ecommerceOrderRepository.findAll().stream()
                .mapToDouble(EcommerceOrder::getTotalPriceAfterDiscount)
                .sum();
    }

    // Method to get category-wise breakdown of orders
    public Map<String, Long> getCategoryBreakdown() {
        return ecommerceOrderRepository.findAll().stream()
                .collect(Collectors.groupingBy(EcommerceOrder::getCategory, Collectors.counting()));
    }
}
