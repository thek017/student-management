package com.keshav.student_management.util;

import com.keshav.student_management.dto.response.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Global utility class for pagination and sorting helper operations.
 */
public class PageUtils {

    private PageUtils() {
        // Private constructor to prevent instantiation of utility class
    }

    /**
     * Constructs a {@link Pageable} instance given pagination and sorting parameters.
     *
     * @param pageNo   zero-based page index
     * @param pageSize size of the page to be returned
     * @param sortBy   property name to sort by
     * @param sortDir  sort direction ("asc" or "desc")
     * @return configured Pageable instance
     */
    public static Pageable createPageable(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir != null && sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        return PageRequest.of(pageNo, pageSize, sort);
    }

    /**
     * Converts a Spring Data {@link Page} of domain entities into a custom {@link PageResponse} DTO.
     *
     * @param page   Spring Data Page object
     * @param mapper function to convert entity element T into response DTO element R
     * @param <T>    Source type (Entity)
     * @param <R>    Target type (Response DTO)
     * @return PageResponse containing mapped content and pagination metadata
     */
    public static <T, R> PageResponse<R> toPageResponse(Page<T> page, Function<T, R> mapper) {
        List<R> content = page.getContent()
                .stream()
                .map(mapper)
                .collect(Collectors.toList());

        return PageResponse.<R>builder()
                .content(content)
                .pageNo(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .build();
    }
}
