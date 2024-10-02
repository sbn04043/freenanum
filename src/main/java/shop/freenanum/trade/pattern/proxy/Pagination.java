package shop.freenanum.trade.pattern.proxy;

import lombok.Data;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component("page")
@Data
@Lazy
public class Pagination {

    private Long totalCount, startRow, endRow; // row
    private Long pageCount, pageSize, startPage, endPage; // page
    private Long blockCount, prevBlock, nextBlock; // block
    private boolean existPrev, existNext;

    //받아올 값
    private Long rowNum, pageNum, blockNum;


    //search 조건 추가해야하지만 일단 X
    private final int BLOCK_SIZE = 5;
    private final int PAGE_SIZE = 20;

    public Pagination() {
    }

    public Pagination(Long pageNum, Long totalCount) {
        this.pageNum = pageNum;
        this.totalCount = totalCount;

        this.pageCount = this.totalCount % PAGE_SIZE == 0 ? this.totalCount / PAGE_SIZE : this.totalCount / PAGE_SIZE + 1;
        this.startRow = (this.pageNum - 1) * PAGE_SIZE;
        this.endRow = pageNum * PAGE_SIZE;

        if (pageCount < 5L) {
            this.startPage = 1L;
            this.endPage = pageCount;
        } else if (pageNum < 3L) {
            this.startPage = 1L;
            this.endPage = 5L;
        } else if (pageNum > pageCount - 2L) {
            this.startPage = pageCount - 4L;
            this.endPage = pageCount;
        } else {
            this.startPage = pageNum - 2L;
            this.endPage = pageNum + 2L;
        }

        this.existPrev = pageNum != 1L;
        this.existNext = !pageNum.equals(pageCount);
        this.nextBlock = Math.min(pageNum + BLOCK_SIZE, endPage);
        this.prevBlock = Math.max(pageNum - BLOCK_SIZE, startPage);
    }
}
