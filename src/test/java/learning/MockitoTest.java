package learning;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.stereotype.Service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MockitoTest {

    @Mock
    BookService bookService;
    @Spy
    StoreService storeService = new StoreService();
    @InjectMocks
    OrderService orderService;

    @DisplayName("스터빙 안 했을 때, 확인하기")
    @Test
    void test1() {
        // given, when, then
        Book book = bookService.getBook();
        assertNull(book);
    }

    @DisplayName("Spy 스터빙 안 했을 때, 확인하기")
    @Test
    void test2() {
        // given, when
        Store store = storeService.getStore();

        // then
        assertEquals("book store", store.getType());
    }

    @DisplayName("Spy 스터빙 했을 때, 확인하기")
    @Test
    void test3() {
        // given
        Store storeDummy = new Store("drug store");
        Store store = storeService.getStore();

        // when
        when(storeService.getStore()).thenReturn(storeDummy);

        // then
        assertNotEquals(store.getType(), storeDummy.getType());
    }

    @DisplayName("주입 확인 : 새로운 객체가 주입되어서 null...?")
    @Test
    void test4() {
        // given, when, then
        assertNull(orderService.getBook());
    }

    @DisplayName("주입 확인 @Spy")
    @Test
    void test5() {
        // given
        Store store = orderService.getStore();

        // when, then
        assertEquals("book store", store.getType());
    }

    private static class OrderService {
        private final BookService bookService;
        private final StoreService storeService;

        public OrderService(BookService bookService, StoreService storeService) {
            this.bookService = bookService;
            this.storeService = storeService;
        }

        public Book getBook() {
            return bookService.getBook();
        }

        public Store getStore() {
            return storeService.getStore();
        }
    }

    private class BookService {
        public Book getBook() {
            return new Book("justice", "sandel");
        }
    }

    private class Book {
        private String title;
        private String author;

        Book(String title, String author) {
            this.title = title;
            this.author = author;
        }

        public String getTitle() {
            return title;
        }

        public String getAuthor() {
            return author;
        }
    }

    private class StoreService {
        public Store getStore() {
            return new Store("book store");
        }
    }

    private class Store {
        private String type;

        Store(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }
    }
}
