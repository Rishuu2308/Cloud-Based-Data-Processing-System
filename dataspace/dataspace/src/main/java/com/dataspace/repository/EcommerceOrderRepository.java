package com.dataspace.repository;


import com.dataspace.model.EcommerceOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EcommerceOrderRepository extends JpaRepository<EcommerceOrder, Long> {
    List<EcommerceOrder> findByOrderDateBetween(LocalDate startDate, LocalDate endDate);
    List<EcommerceOrder> findByCustomerId(Long customerId);
    List<EcommerceOrder> findByCategory(String category);
}
