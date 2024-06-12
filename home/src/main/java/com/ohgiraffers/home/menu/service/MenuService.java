package com.ohgiraffers.home.menu.service;

import com.ohgiraffers.home.menu.dto.CategoryDTO;
import com.ohgiraffers.home.menu.dto.MenuDTO;
import com.ohgiraffers.home.menu.entity.Category;
import com.ohgiraffers.home.menu.entity.Menu;
import com.ohgiraffers.home.menu.repository.CategoryRepository;
import com.ohgiraffers.home.menu.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuService {
    private final MenuRepository menuRepository;
    private final ModelMapper modelMapper;
    private final CategoryRepository categoryRepository;

    public MenuDTO findMenuByCode(int menuCode){
        Menu menu = menuRepository.findById(menuCode).orElseThrow(IllegalArgumentException::new);

        return modelMapper.map(menu, MenuDTO.class);
    }

    public List<MenuDTO> findMenuList(){
        List<Menu> menuList = menuRepository.findAll(Sort.by("menuCode").descending());
        return menuList.stream().map(menu -> modelMapper.map(menu, MenuDTO.class)).toList();
    }

    public Page<MenuDTO> findMenuList(Pageable pageable){
        pageable = PageRequest.of(
                pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber() - 1,
                pageable.getPageSize(),
                Sort.by("menuCode").descending()
        );
        Page<Menu> menuList = menuRepository.findAll(pageable);
        return menuList.map(menu -> modelMapper.map(menu, MenuDTO.class));
    }

    public List<MenuDTO> findByMenuPrice(Integer menuPrice) {
//        List<Menu> menuList = menuRepository.findByMenuPriceGreaterThan(menuPrice, Sort.by("menuPrice").descending());
        List<Menu> menuList = menuRepository.findByMenuPriceGreaterThanOrderByMenuCode(menuPrice, Sort.by("menuCode").descending());
        return menuList.stream().map(menu -> modelMapper.map(menu, MenuDTO.class)).toList();
    }

    public List<CategoryDTO> findCategoryList(){
//        List<Category> categoryList = categoryRepository.findAll(Sort.by("categoryCode"));
        List<Category> categoryList = categoryRepository.findAllCategory();
        return categoryList.stream().map(category -> modelMapper.map(category, CategoryDTO.class)).toList();
    }

    @Transactional
    public void registMenu(MenuDTO menu) {
        menuRepository.save(modelMapper.map(menu, Menu.class));
    }

    @Transactional
    public void modifyMenu(MenuDTO menu) {
        Menu foundMenu = menuRepository.findById(menu.getMenuCode()).orElseThrow(IllegalArgumentException::new);
        foundMenu.modifyMenuName(menu.getMenuName());
    }

    @Transactional
    public void deleteMenu(Integer menuCode) {
        menuRepository.deleteById(menuCode);
    }
}
