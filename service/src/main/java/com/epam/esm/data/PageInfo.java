package com.epam.esm.data;

public class PageInfo {

    private static final int FIRST_PAGE = 1;
    private static final int ZERO_COUNT = 0;
    private final int pageSize;
    private final int currentPage;
    private final int entityCount;
    private final int lastPage;

    public PageInfo(int pageSize, int currentPage, int entityCount) {
        this.pageSize = Math.max(pageSize, FIRST_PAGE);
        this.entityCount = Math.max(entityCount, ZERO_COUNT);
        this.lastPage = (entityCount + pageSize - 1) / pageSize;
        int lastOrGivenPage = Math.min(currentPage, lastPage);
        this.currentPage = Math.max(FIRST_PAGE, lastOrGivenPage);
    }

    public boolean hasNext() {
        return lastPage > currentPage;
    }

    private PageInfo getNext() {
        return new PageInfo(pageSize, currentPage + 1, entityCount);
    }

    public PageInfo getNextOrLast() {
        if (hasNext()) {
            return getNext();
        }
        return new PageInfo(pageSize, lastPage, entityCount);
    }

    public boolean hasPrevious() {
        return currentPage != 1;
    }

    private PageInfo getPrevious() {
        return new PageInfo(pageSize, currentPage - 1, entityCount);
    }

    public PageInfo getPreviousOrFirst() {
        if (hasPrevious()) {
            return getPrevious();
        }
        return this;
    }

    public PageInfo getFirst() {
        return new PageInfo(pageSize, 1, entityCount);
    }

    public PageInfo getLast() {
        return new PageInfo(pageSize, lastPage, entityCount);
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getEntityCount() {
        return entityCount;
    }
}
