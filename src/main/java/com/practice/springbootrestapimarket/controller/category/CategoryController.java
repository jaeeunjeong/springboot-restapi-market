package com.practice.springbootrestapimarket.controller.category;

import com.practice.springbootrestapimarket.dto.category.CategoryCreateRequest;
import com.practice.springbootrestapimarket.dto.response.Response;
import com.practice.springbootrestapimarket.service.category.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Api(value = "Category Controller", tags = "Category")
@RestController
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @ApiOperation(value = "모든 카테고리 조회", notes = "모든 카테고리를 조회합니다..")
    @GetMapping("/api/categories")
    @ResponseStatus(HttpStatus.OK)
    public Response readAll() {
        return Response.success(categoryService.readAll());
    }

    @ApiOperation(value = "카테고리 생성", notes = "카테고리를 만듭니다.")
    @PostMapping("/api/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public Response create(CategoryCreateRequest req) {
        categoryService.create(req);
        return Response.success();
    }

    @ApiOperation(value = "카테고리 삭제", notes = "카테고리를 삭제합니다.")
    @DeleteMapping("/api/categories/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response delete(@ApiParam(value = "카테고리 id", required = true) @PathVariable Long id) {
        categoryService.delete(id);
        return Response.success();
    }
}
