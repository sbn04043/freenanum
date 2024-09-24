package shop.freenanum.trade.pattern.proxy;

import lombok.Data;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component("page")
@Data
@Lazy
public class Pagination {

    private int totalCount, startRow, endRow; // row
    private int pageCount, pageSize, startPage, endPage; // page
    private int blockCount, prevBlock, nextBlock; // block
    private boolean existPrev, existNext;

    //받아올 값
    private int rowNum, pageNum, blockNum;


    //search 조건 추가해야하지만 일단 X
    private final int BLOCK_SIZE = 5;
    private final int PAGE_SIZE = 20;

    public Pagination() {
    }

    public Pagination(int pageNum, int totalCount) {
        this.pageNum = pageNum;
        this.totalCount = totalCount;

        this.pageCount = this.totalCount % PAGE_SIZE == 0 ? this.totalCount / PAGE_SIZE : this.totalCount / PAGE_SIZE + 1;
        this.startRow = (pageNum - 1) * PAGE_SIZE;
        this.endRow = pageNum * PAGE_SIZE;

        if (pageCount < 5) {
            this.startPage = 1;
            this.endPage = pageCount;
        } else if (pageNum < 3) {
            this.startPage = 1;
            this.endPage = 5;
        } else if (pageNum > pageCount - 2) {
            this.startPage = pageCount - 4;
            this.endPage = pageCount;
        } else {
            this.startPage = pageNum - 2;
            this.endPage = pageNum + 2;
        }

        this.existPrev = pageNum == 1;
        this.existNext = pageNum == pageCount;
        this.nextBlock = startPage + BLOCK_SIZE;
        this.prevBlock = startPage - BLOCK_SIZE;
    }
}
