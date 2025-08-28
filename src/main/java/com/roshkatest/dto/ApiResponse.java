package com.roshkatest.dto;

import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class ApiResponse<T> {
    private T data;
    private MetaData meta;
    
    @Data
    @AllArgsConstructor
    public static class MetaData {
        private long total;
        private int page;
        private int totalPages;
    }
}
