package ptit.htpt.bookstore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ptit.htpt.bookstore.constant.MessageConstants;
import ptit.htpt.bookstore.dto.ResponseDto;
import ptit.htpt.bookstore.entity.Category;
import ptit.htpt.bookstore.repository.CategoryRepository;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public ResponseDto getAll() {
        return new ResponseDto("0", "Success", categoryRepository.findAll());
    }

    public ResponseDto create(Category category) {
        Category check = categoryRepository.findByName(category.getName());
        if (check != null) return new ResponseDto("1", MessageConstants.CATEGORY_EXIST, null);
        return new ResponseDto("0", MessageConstants.CATEGORY_CREATED, categoryRepository.save(category));
    }

    public ResponseDto update(Category category) {
        Category check = categoryRepository.findByName(category.getName());
        if (check != null && check.getId() != category.getId())
            return new ResponseDto("1", MessageConstants.CATEGORY_EXIST, null);
        return new ResponseDto("0", MessageConstants.CATEGORY_UPDATED, categoryRepository.save(category));
    }

    public ResponseDto delete(Category category) {
        try {
            categoryRepository.delete(category);
        } catch (Exception e) {
            return new ResponseDto("1", MessageConstants.CATEGORY_DELETE_FAIL, e);
        }
        return new ResponseDto("0", MessageConstants.CATEGORY_DELETED, null);
    }
}
