package test.com.epam.esm.service;

import com.epam.esm.data.PageInfo;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PageInfoTest {

    @Test
    public void testPageInfoConstructorWithExceedingCurrentPageValueShouldSetLastPage() {
        PageInfo pageInfo = new PageInfo(15, 10, 30);
        assertEquals(2, pageInfo.getCurrentPage());
    }

    @Test
    public void testPageInfoConstructorWithNegativeCurrentPageThenShouldBeSettedOne() {
        PageInfo pageInfo = new PageInfo(15, -15, 32);
        assertEquals(1, pageInfo.getCurrentPage());
    }

    @Test
    public void testHasNextShouldReturnTrueWhenNextPageExists() {
        PageInfo pageInfo = new PageInfo(15, 2, 31);
        assertTrue(pageInfo.hasNext());
    }

    @Test
    public void testNextShouldReturnNextWhenHasNext() {
        PageInfo pageInfo = new PageInfo(15, 2, 31);
        PageInfo next = pageInfo.getNextOrLast();
        assertEquals(3, next.getCurrentPage());
    }

    @Test
    public void testNextShouldReturnPageInfoWithLastPageOnLastPage() {
        PageInfo pageInfo = new PageInfo(15, 2, 30);
        PageInfo next = pageInfo.getNextOrLast();
        assertEquals(2, next.getCurrentPage());
    }

    @Test
    public void testHasPrevShouldReturnTrueWhenPrevPageExists() {
        PageInfo pageInfo = new PageInfo(15, 2, 31);
        assertTrue(pageInfo.hasPrevious());
    }

    @Test
    public void testPrevShouldReturnPrevWhenHasPrev() {
        PageInfo pageInfo = new PageInfo(15, 2, 31);
        PageInfo prev = pageInfo.getPreviousOrFirst();
        assertEquals(1, prev.getCurrentPage());
    }

    @Test
    public void testPrevShouldReturnPageInfoWithFirstOnFirstPage() {
        PageInfo pageInfo = new PageInfo(15, 1, 30);
        PageInfo prev = pageInfo.getPreviousOrFirst();
        assertEquals(1, prev.getCurrentPage());
    }

    @Test
    public void testFirstShouldReturnFirstPageOnMiddlePage() {
        PageInfo pageInfo = new PageInfo(15, 2, 31);
        PageInfo first = pageInfo.getFirst();
        assertEquals(1, first.getCurrentPage());
    }

    @Test
    public void testLastShouldReturnLastPageOnFirstPage() {
        PageInfo pageInfo = new PageInfo(15, 1, 44);
        PageInfo next = pageInfo.getLast();
        assertEquals(3, next.getCurrentPage());
    }

}
