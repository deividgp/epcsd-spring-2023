package edu.uoc.epcsd.productcatalog.controllers;

import edu.uoc.epcsd.productcatalog.controllers.dtos.CreateCategoryRequest;
import edu.uoc.epcsd.productcatalog.entities.Category;
import edu.uoc.epcsd.productcatalog.services.CategoryService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;

@Log4j2
@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public List<Category> getAllCategories() {
        log.trace("getAllCategories");

        return categoryService.findAll();
    }

    @PostMapping
    public ResponseEntity<Long> createCategory(@RequestBody CreateCategoryRequest createCategoryRequest) {
        log.trace("createCategory");

        log.trace("Creating category " + createCategoryRequest);
        Long categoryId = categoryService.createCategory(
                createCategoryRequest.getParentId(),
                createCategoryRequest.getName(),
                createCategoryRequest.getDescription()).getId();
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(categoryId)
                .toUri();

        return ResponseEntity.created(uri).body(categoryId);
    }

    // 1. query categories by name
    @GetMapping("/byName/{name}")
    @ResponseStatus(HttpStatus.OK)
    public List<Category> getCategoriesByName(@PathVariable @NotNull String name) {
        log.trace("getCategoriesByName");

        return categoryService.findAllByName(name);
    }

    // 2. query categories by description
    @GetMapping("/byDescription/{description}")
    @ResponseStatus(HttpStatus.OK)
    public List<Category> getCategoriesByDescription(@PathVariable @NotNull String description) {
        log.trace("getCategoriesByDescription");

        return categoryService.findAllByDescription(description);
    }

    // 3. query categories by parent category (must return all categories under the category specified by the id parameter)
    @GetMapping("/byParent/{parentId}")
    @ResponseStatus(HttpStatus.OK)
    public List<Category> getCategoriesByParent(@PathVariable @NotNull Long parentId) {
        log.trace("getCategoriesByParent");

        return categoryService.findByParent(parentId);
    }
}
