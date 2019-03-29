package ptit.htpt.bookstore.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ptit.htpt.bookstore.dto.ResponseDto;
import ptit.htpt.bookstore.entity.Category;
import ptit.htpt.bookstore.service.CategoryService;

@RestController
@RequestMapping("/api/category/")
public class CategoryResource {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("getAll")
    public ResponseDto getAll(){
        return categoryService.getAll();
    }

    @PostMapping("create")
    public ResponseDto create(@RequestBody Category category){
        return categoryService.create(category);
    }

    @PostMapping("update")
    public ResponseDto update(@RequestBody Category category){
        return categoryService.update(category);
    }

    @PostMapping("delete")
    public ResponseDto delete(@RequestBody Category category){
        return categoryService.delete(category);
    }

}
