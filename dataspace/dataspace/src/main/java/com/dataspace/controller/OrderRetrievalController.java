package com.dataspace.controller;
import com.dataspace.model.EcommerceOrder;
import com.dataspace.service.EcommerceOrderService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderRetrievalController {

    private final EcommerceOrderService orderService;

    public OrderRetrievalController(EcommerceOrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public List<EcommerceOrder> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/by-date")
    public List<EcommerceOrder> getOrdersByDate(
            @RequestParam(required = false) String filter, // e.g., "last_n_days", "last_n_weeks", "last_n_months", "date_range"
            @RequestParam(required = false) Integer value, // Number of days/weeks/months
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        LocalDate now = LocalDate.now();

        if ("last_n_days".equalsIgnoreCase(filter) && value != null) {
            LocalDate fromDate = now.minusDays(value);
            return orderService.getOrdersByDateRange(fromDate, now);
        } else if ("last_n_weeks".equalsIgnoreCase(filter) && value != null) {
            LocalDate fromDate = now.minusWeeks(value);
            return orderService.getOrdersByDateRange(fromDate, now);
        } else if ("last_n_months".equalsIgnoreCase(filter) && value != null) {
            LocalDate fromDate = now.minusMonths(value);
            return orderService.getOrdersByDateRange(fromDate, now);
        } else if ("date_range".equalsIgnoreCase(filter) && startDate != null && endDate != null) {
            if (startDate.isAfter(endDate)) {
                throw new IllegalArgumentException("startDate must be earlier than endDate");
            }
            return orderService.getOrdersByDateRange(startDate, endDate);
        } else {
            throw new IllegalArgumentException("Invalid filter or missing parameters");
        }
    }
    @GetMapping("/by-customer")
    public List<EcommerceOrder> getOrdersByCustomer(@RequestParam Long customerId) {
        return orderService.getOrdersByCustomer(customerId);
    }

    @GetMapping("/by-category")
    public List<EcommerceOrder> getOrdersByCategory(@RequestParam String category) {
        return orderService.getOrdersByCategory(category);
    }
}
