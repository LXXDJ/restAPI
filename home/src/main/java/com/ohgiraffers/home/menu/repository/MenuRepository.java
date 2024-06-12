package com.ohgiraffers.home.menu.repository;

import com.ohgiraffers.home.menu.entity.Menu;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Integer> {
    List<Menu> findByMenuPriceGreaterThan(Integer menuPrice, Sort sort);

    List<Menu> findByMenuPriceGreaterThanOrderByMenuCode(Integer menuPrice, Sort menuCode);
}
