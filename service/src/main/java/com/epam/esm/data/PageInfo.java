package com.epam.esm.data;

public class PageInfo {

    private final int pageSize;
    private final int currentPage;
    private final int entityCount;
    private final int lastPage;

    public PageInfo(int pageSize, int currentPage, int entityCount) {
        this.pageSize = Math.max(pageSize, 1);
        this.entityCount = Math.max(entityCount, 0);
        this.lastPage = (entityCount + pageSize - 1) / pageSize;
        this.currentPage = Math.max(1, Math.min(currentPage, lastPage));
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
