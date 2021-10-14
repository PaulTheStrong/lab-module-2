package com.epam.esm.data;

public class PageInfo {

    private final int pageSize;
    private final int currentPage;
    private final int entityCount;

    public PageInfo(int pageSize, int currentPage, int entityCount) {
        this.pageSize = Math.max(pageSize, 1);
        this.currentPage = Math.max(currentPage, 1);
        this.entityCount = Math.max(entityCount, 0);
    }

    public boolean hasNext() {
        return (entityCount + pageSize - 1) / pageSize != currentPage;
    }

    public PageInfo next() {
        return new PageInfo(pageSize, currentPage + 1, entityCount);
    }

    public PageInfo nextOrLast() {
        if (hasNext()) {
            return next();
        }
        return this;
    }

    public boolean hasPrevious() {
        return currentPage != 1;
    }

    public PageInfo prev() {
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
        return new PageInfo(pageSize, (entityCount + pageSize - 1) / pageSize, entityCount);
    }

    public PageInfo withPage(int page) {
        return new PageInfo(pageSize, page, entityCount);
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
