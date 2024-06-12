package com.ohgiraffers.home.menu.repository;

import com.ohgiraffers.home.menu.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

    @Query(value="SELECT * FROM tbl_category ORDER BY category_code DESC", nativeQuery = true)
    List<Category> findAllCategory();
}
