package api.dtos.responses;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
public class PaginatedResponseDTO<C> {
    private final List<C> content;
    private final Integer pageNumber;
    private final Integer pageSize;
    private final Integer pageTotal;

    public PaginatedResponseDTO(Page<C> page) {
        this.content = page.getContent();
        this.pageNumber = page.getPageable().getPageNumber();
        this.pageSize = page.getSize();
        this.pageTotal = page.getTotalPages();
    }
}
