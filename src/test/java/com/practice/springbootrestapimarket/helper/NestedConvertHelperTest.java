package com.practice.springbootrestapimarket.helper;

import com.practice.springbootrestapimarket.exception.CannotConvertNestedStructureException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class NestedConvertHelperTest {

    @Test
    void convertTest() {
        // given
        MyEntity m1 = new MyEntity(1L, "myEntity1", null);
        MyEntity m2 = new MyEntity(2L, "myEntity2", m1);
        MyEntity m3 = new MyEntity(3L, "myEntity3", m1);
        MyEntity m4 = new MyEntity(4L, "myEntity4", m2);
        MyEntity m5 = new MyEntity(5L, "myEntity5", m2);
        MyEntity m6 = new MyEntity(6L, "myEntity6", m4);
        MyEntity m7 = new MyEntity(7L, "myEntity7", m3);
        MyEntity m8 = new MyEntity(8L, "myEntity8", null);

        List<MyEntity> myEntities = Arrays.asList(m1, m2, m3, m4, m5, m6, m7, m8);

        NestedConvertHelper helper = NestedConvertHelper.newInstance(
                myEntities,
                e -> new MyDto(e.getId(), e.getName(), new ArrayList<>()),
                e -> e.getParent(),
                e -> e.getId(),
                d -> d.getChildren()
        );

        // when
        List<MyDto> result = helper.convert();

        // then
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0).getId()).isEqualTo(1);
        assertThat(result.get(0).getChildren().size()).isEqualTo(2);
        assertThat(result.get(0).getChildren().get(0).getId()).isEqualTo(2);
        assertThat(result.get(0).getChildren().get(0).getChildren().size()).isEqualTo(2);
        assertThat(result.get(0).getChildren().get(0).getChildren().get(0).getId()).isEqualTo(4);
        assertThat(result.get(0).getChildren().get(0).getChildren().get(0).getChildren().size()).isEqualTo(1);
        assertThat(result.get(0).getChildren().get(0).getChildren().get(0).getChildren().get(0).getId()).isEqualTo(6);
        assertThat(result.get(0).getChildren().get(0).getChildren().get(1).getId()).isEqualTo(5);
        assertThat(result.get(0).getChildren().get(1).getId()).isEqualTo(3);
        assertThat(result.get(0).getChildren().get(1).getChildren().size()).isEqualTo(1);
        assertThat(result.get(0).getChildren().get(1).getChildren().get(0).getId()).isEqualTo(7);
        assertThat(result.get(1).getId()).isEqualTo(8);
        assertThat(result.get(1).getChildren().size()).isEqualTo(0);
    }

    @DisplayName("어떤 자식의 부모는 반드시 자식보다 앞에 나와야한다.")
    @Test
    void test2() {
        // given
        MyEntity m1 = new MyEntity(1L, "myEntity1", null);
        MyEntity m2 = new MyEntity(2L, "myEntity2", m1);
        MyEntity m3 = new MyEntity(3L, "myEntity3", m1);
        MyEntity m4 = new MyEntity(4L, "myEntity4", m2);
        MyEntity m5 = new MyEntity(5L, "myEntity5", m2);
        MyEntity m6 = new MyEntity(6L, "myEntity6", m4);
        MyEntity m7 = new MyEntity(7L, "myEntity7", m3);
        MyEntity m8 = new MyEntity(8L, "myEntity8", null);

        List<MyEntity> myEntities = Arrays.asList(m2, m3, m4, m5, m6, m7, m1, m8);

        NestedConvertHelper helper = NestedConvertHelper.newInstance(
                myEntities,
                e -> new MyDto(e.getId(), e.getName(), new ArrayList<>()),
                e -> e.getParent(),
                e -> e.getId(),
                d -> d.getChildren()
        );

        // when,  then
        assertThatThrownBy(() -> helper.convert()).isInstanceOf(CannotConvertNestedStructureException.class);
    }

    @DisplayName("부모가 없는 루트는, 항상 제일 앞에 있어야한다.")
    @Test
    void test3() {
        // given
        MyEntity m1 = new MyEntity(1L, "myEntity1", null);
        MyEntity m2 = new MyEntity(2L, "myEntity2", m1);
        MyEntity m3 = new MyEntity(3L, "myEntity3", m1);
        MyEntity m4 = new MyEntity(4L, "myEntity4", m2);
        MyEntity m5 = new MyEntity(5L, "myEntity5", m2);
        MyEntity m6 = new MyEntity(6L, "myEntity6", m4);
        MyEntity m7 = new MyEntity(7L, "myEntity7", m3);
        MyEntity m8 = new MyEntity(8L, "myEntity8", null);

        List<MyEntity> myEntities = Arrays.asList(m2, m3, m4, m5, m6, m7, m1, m8);

        NestedConvertHelper helper = NestedConvertHelper.newInstance(
                myEntities,
                e -> new MyDto(e.getId(), e.getName(), new ArrayList<>()),
                e -> e.getParent(),
                e -> e.getId(),
                d -> d.getChildren()
        );

        // when,  then
        assertThatThrownBy(() -> helper.convert()).isInstanceOf(CannotConvertNestedStructureException.class);
    }

    private static class MyEntity {
        private Long id;
        private String name;
        private MyEntity parent;

        public MyEntity(Long id, String name, MyEntity parent) {
            this.id = id;
            this.name = name;
            this.parent = parent;
        }

        public MyEntity getParent() {
            return parent;
        }

        public Long getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }

    private static class MyDto {
        private Long id;
        private String name;
        private List<MyDto> children;

        public MyDto(Long id, String name, List<MyDto> children) {
            this.id = id;
            this.name = name;
            this.children = children;
        }

        public Long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public List<MyDto> getChildren() {
            return children;
        }
    }

}