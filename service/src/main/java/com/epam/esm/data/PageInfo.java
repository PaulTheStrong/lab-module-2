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

    private PageInfo next() {
        return new PageInfo(pageSize, currentPage + 1, entityCount);
    }

    public PageInfo nextOrLast() {
        if (hasNext()) {
            return next();
        }
        return new PageInfo(pageSize, lastPage, entityCount);
    }

    public boolean hasPrevious() {
        return currentPage != 1;
    }

    private PageInfo prev() {
        return new PageInfo(pageSize, currentPage - 1, entityCount);
    }

    public PageInfo prevOrFirst() {
        if (hasPrevious()) {
            return prev();
        }
        return this;
    }

    public PageInfo first() {
        return new PageInfo(pageSize, 1, entityCount);
    }

    public PageInfo last() {
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
