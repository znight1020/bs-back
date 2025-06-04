package com.bob.domain.category.repository;

import com.bob.domain.category.entity.Category;
import org.springframework.data.repository.CrudRepository;

public interface CategoryRepository extends CrudRepository<Category, Integer> {

}
