package com.dataspace.controller;

import com.dataspace.model.EcommerceOrder;
import com.dataspace.service.DataIngestionService;
import com.dataspace.service.EcommerceOrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class DataIngestionController  {

    @Autowired
    private DataIngestionService dataIngestionService;
    @Autowired
    private EcommerceOrderService ecommerceOrderService;

    @PostMapping("/upload/file")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        dataIngestionService.processFile(file);
        return ResponseEntity.ok("File uploaded and processed successfully.");
    }

    @PostMapping("/upload/data")
    public ResponseEntity<EcommerceOrder> createOrder(@RequestBody @Valid EcommerceOrder order) {
        EcommerceOrder savedOrder = ecommerceOrderService.saveOrder(order);
        return new ResponseEntity<>(savedOrder, HttpStatus.CREATED);
    }
}
