package com.ohgiraffers.home.menu.controller;

import com.ohgiraffers.home.common.Pagenation;
import com.ohgiraffers.home.common.PagingButton;
import com.ohgiraffers.home.menu.dto.CategoryDTO;
import com.ohgiraffers.home.menu.dto.MenuDTO;
import com.ohgiraffers.home.menu.service.MenuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/menu")
@RequiredArgsConstructor
public class MenuController {
    private final MenuService menuService;

    @GetMapping("/{menuCode}")
    public String findMenuByCode(@PathVariable int menuCode, Model model){
        MenuDTO menu = menuService.findMenuByCode(menuCode);
        model.addAttribute("menu", menu);

        return "menu/detail";
    }

    @GetMapping("/list")
    public String findMenuList(Model model, @PageableDefault Pageable pageable){
        Page<MenuDTO> menuList = menuService.findMenuList(pageable);

        PagingButton paging = Pagenation.getPagingButtonInfo(menuList);
        model.addAttribute("menuList", menuList);
        model.addAttribute("paging", paging);

        return "/menu/list";
    }

    @GetMapping("/querymethod")
    public void queryMethodPage(){}

    @GetMapping("/search")
    public String findByMenuPrice(@RequestParam Integer menuPrice, Model model){
        List<MenuDTO> menuList = menuService.findByMenuPrice(menuPrice);
        model.addAttribute("menuList", menuList);
        model.addAttribute("menuPrice", menuPrice);

        return "menu/searchResult";
    }

    @GetMapping("/regist")
    public void registPage(){}

    @GetMapping("/category")
    @ResponseBody
    public List<CategoryDTO> findCategoryList(){
        return menuService.findCategoryList();
    }

    @PostMapping("/regist")
    public String registMenu(MenuDTO menu){
        menuService.registMenu(menu);

        return "redirect:/menu/list";
    }

    @GetMapping("/modify")
    public void modifyPage(){}

    @PostMapping("/modify")
    public String modifyMenu(MenuDTO menu){
        menuService.modifyMenu(menu);
        return "redirect:/menu/" + menu.getMenuCode();
    }

    @GetMapping("/delete")
    public void deletePage(){}

    @PostMapping("/delete")
    public String deleteMenu(@RequestParam Integer menuCode){
        menuService.deleteMenu(menuCode);
        return "redirect:/menu/list";
    }
}
