package com.dataspace.service;
import com.dataspace.exception.CustomValidationException;
import com.dataspace.model.EcommerceOrder;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import jakarta.validation.ConstraintViolation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.Validator;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class DataIngestionService {

    @Autowired
    private EcommerceOrderService ecommerceOrderService;

    @Autowired
    private Validator validator;

    public void processFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new RuntimeException("Uploaded file is empty.");
        }

        InputStream inputStream;
        try {
            inputStream = file.getInputStream();
        } catch (IOException e) {
            throw new RuntimeException("Error reading the uploaded file.", e);
        }

        String fileType = file.getContentType();
        String originalFilename = file.getOriginalFilename();

        List<EcommerceOrder> orders;

        if ("text/csv".equals(fileType) ||
                (originalFilename != null && originalFilename.endsWith(".csv"))) {
            orders = parseCsv(inputStream);
        } else if ("application/json".equals(fileType) ||
                (originalFilename != null && originalFilename.endsWith(".json"))) {
            orders = parseJson(inputStream);
        } else {
            // Unsupported format → runtime exception
            throw new RuntimeException("Unsupported file format. Only CSV and JSON are allowed.");
        }

        transformOrders(orders);
        validateAndSave(orders);
    }

    private void transformOrders(List<EcommerceOrder> orders) {
        for (EcommerceOrder order : orders) {
            LocalDate originalDate = order.getOrderDate();
            if(originalDate == null){
                originalDate = LocalDate.now();
                order.setOrderDate(originalDate);
            }
            String newDateFormat = reformatDate(String.valueOf(originalDate));
            order.setReformatOrderDate(newDateFormat);

            if (order.getQuantity() != null && order.getUnitPrice() != null) {
                double total = order.getQuantity() * order.getUnitPrice();
                order.setTotalPrice(total);
            }
            else{
                order.setTotalPrice(0.0);

            }
            double total = (order.getQuantity() != null ? order.getQuantity() : 0)
                    * (order.getUnitPrice() != null ? order.getUnitPrice() : 0.0);

            double discount = 0.0;
            if (total > 500) {
                discount = total * 0.15;
            } else if (total > 100) {
                discount = total * 0.10;
            }

            double finalPrice = total - discount;
            order.setDiscount(discount);
            order.setTotalPriceAfterDiscount(finalPrice);

        }
    }
    private List<EcommerceOrder> parseCsv(InputStream inputStream) {
        try {
            CsvToBean<EcommerceOrder> csvToBean = new CsvToBeanBuilder<EcommerceOrder>(
                    new InputStreamReader(inputStream))
                    .withType(EcommerceOrder.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            return csvToBean.parse();
        } catch (Exception e) {
            throw new RuntimeException("Error parsing CSV file: " + e.getMessage(), e);
        }
    }

    private String reformatDate(String dateStr) {
        SimpleDateFormat fromSdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat toSdf = new SimpleDateFormat("MM/dd/yyyy");
        try {
            Date date = fromSdf.parse(dateStr);
            return toSdf.format(date);
        } catch (Exception e) {
            // If invalid date, you can throw or return the original
            throw new RuntimeException("Invalid date format for: " + dateStr, e);
        }
    }
    private List<EcommerceOrder> parseJson(InputStream inputStream) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(
                    inputStream, new TypeReference<>() {
                    });
        } catch (IOException e) {
            throw new RuntimeException("Error parsing JSON file: " + e.getMessage(), e);
        }
    }

    /**
     * Validates each order. If any constraint violations exist, accumulates them
     * into errorList and throws CustomValidationException.
     */
    private void validateAndSave(List<EcommerceOrder> orders) {
        List<Map<String, Object>> errorList = new ArrayList<>();

        // Loop with index
        for (int i = 0; i < orders.size(); i++) {
            EcommerceOrder order = orders.get(i);
            Set<ConstraintViolation<EcommerceOrder>> violations = validator.validate(order);

            if (!violations.isEmpty()) {
                // We’ll collect row number + fields and messages
                Map<String, Object> rowErrors = new LinkedHashMap<>();
                rowErrors.put("row", i + 1); // or i if you prefer zero-based

                // Now capture each field error
                Map<String, String> fieldErrors = new LinkedHashMap<>();
                for (ConstraintViolation<EcommerceOrder> violation : violations) {
                    fieldErrors.put(violation.getPropertyPath().toString(), violation.getMessage());
                }
                rowErrors.put("fieldErrors", fieldErrors);

                errorList.add(rowErrors);
            } else {
                ecommerceOrderService.saveOrder(order);
            }
        }

        if (!errorList.isEmpty()) {
            throw new CustomValidationException("Validation errors occurred.", errorList);
        }
    }
}
