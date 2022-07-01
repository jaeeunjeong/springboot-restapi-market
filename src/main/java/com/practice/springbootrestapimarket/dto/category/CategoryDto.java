package com.practice.springbootrestapimarket.dto.category;

import com.practice.springbootrestapimarket.entity.category.Category;
import com.practice.springbootrestapimarket.helper.NestedConvertHelper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {
    private Long id;
    private String name;
    private List<CategoryDto> children;

    public static List<CategoryDto> toDtoList(List<Category> categories){
        NestedConvertHelper helper = NestedConvertHelper.newInstance(
                categories,
                c->new CategoryDto(c.getId(), c.getName(), new ArrayList<>()),
                c-> c.getParent(),
                c -> c.getId(),
                d -> d.getChildren());
        return helper.convert();
    }
}