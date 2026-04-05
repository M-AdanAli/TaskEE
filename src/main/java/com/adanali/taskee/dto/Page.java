package com.adanali.taskee.dto;

import java.io.Serializable;
import java.util.List;

public record Page<T> (List<T> items, long currentPage, long totalPages, long totalItems) implements Serializable {

    public boolean hasPrevious() {
        return currentPage > 1;
    }

    public boolean hasNext() {
        return currentPage < totalPages;
    }
}
