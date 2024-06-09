package com.ohgiraffers.comprehensive.product.service;

import com.ohgiraffers.comprehensive.common.exception.ConflictException;
import com.ohgiraffers.comprehensive.common.exception.NotFoundException;
import com.ohgiraffers.comprehensive.common.exception.type.ExceptionCode;
import com.ohgiraffers.comprehensive.common.util.FileUploadUtils;
import com.ohgiraffers.comprehensive.product.domain.entity.Category;
import com.ohgiraffers.comprehensive.product.domain.entity.Product;
import com.ohgiraffers.comprehensive.product.domain.repository.CategoryRepository;
import com.ohgiraffers.comprehensive.product.domain.repository.ProductRepository;
import com.ohgiraffers.comprehensive.product.domain.type.ProductStatusType;
import com.ohgiraffers.comprehensive.product.dto.request.ProductCreateRequest;
import com.ohgiraffers.comprehensive.product.dto.request.ProductUpdateRequest;
import com.ohgiraffers.comprehensive.product.dto.response.AdminProductResponse;
import com.ohgiraffers.comprehensive.product.dto.response.AdminProductsResponse;
import com.ohgiraffers.comprehensive.product.dto.response.CustomerProductResponse;
import com.ohgiraffers.comprehensive.product.dto.response.CustomerProductsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

import static com.ohgiraffers.comprehensive.common.exception.type.ExceptionCode.NOT_ENOUGH_STOCK;
import static com.ohgiraffers.comprehensive.product.domain.type.ProductStatusType.DELETED;
import static com.ohgiraffers.comprehensive.product.domain.type.ProductStatusType.USABLE;


@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Value("${image.image-url}")
    private String IMAGE_URL;
    @Value("${image.image-dir}")
    private String IMAGE_DIR;

    private Pageable getPageable(final Integer page) {
        return PageRequest.of(page - 1, 10, Sort.by("productCode").descending());
    }

    /* 상품 목록 조회 (고객) */
    @Transactional(readOnly = true)
    public Page<CustomerProductsResponse> getCustomerProducts(final Integer page, final Long categoryCode, final String productName) {

        Page<Product> products = null;
        if(categoryCode != null && categoryCode > 0) {
            products = productRepository.findByCategoryCategoryCodeAndStatus(getPageable(page), categoryCode, ProductStatusType.USABLE);
        } else if(productName != null && !productName.isEmpty()) {
            products = productRepository.findByProductNameContainsAndStatus(getPageable(page), productName, ProductStatusType.USABLE);
        } else {
            products = productRepository.findByStatus(getPageable(page), ProductStatusType.USABLE);
        }

        return products.map(CustomerProductsResponse::from);
    }

    /* 상품 목록 조회 (관리자) */
    @Transactional(readOnly = true)
    public Page<AdminProductsResponse> getAdminProducts(final Integer page) {

        Page<Product> products = productRepository.findByStatusNot(getPageable(page), DELETED);

        return products.map(AdminProductsResponse::from);

    }

    /* 상품 상세 조회 (고객) */
    @Transactional(readOnly = true)
    public CustomerProductResponse getCustomerProduct(final Long productCode) {

        Product product = productRepository.findByProductCodeAndStatus(productCode, ProductStatusType.USABLE)
                .orElseThrow(() -> new NotFoundException(ExceptionCode.NOT_FOUND_PRODUCT_CODE));

        return CustomerProductResponse.from(product);
    }

    /* 상품 상세 조회 (관리자)
    * AdminProductResponse로 응답 */
    @Transactional(readOnly = true)
    public AdminProductResponse getAdminProduct(final Long productCode) {

        Product product = productRepository.findByProductCodeAndStatusNot(productCode, DELETED)
                .orElseThrow(() -> new NotFoundException(ExceptionCode.NOT_FOUND_PRODUCT_CODE));

        return AdminProductResponse.from(product);
    }

    private String getRandomName() { return UUID.randomUUID().toString().replace("-", ""); }

    /* 상품 등록(관리자) */
    public Long save(final ProductCreateRequest productRequest, final MultipartFile productImg) {

        /* 전달 된 파일을 서버의 지정 경로에 저장 */
        String replaceFileName = FileUploadUtils.saveFile(IMAGE_DIR, getRandomName(), productImg);

        /* 전달 받은 categoryCode를 통해 Category Entity 조회 */

        /* findById : tbl_category에 대한 select 구문이 반드시 실행 된다. */
        Category category = categoryRepository.findById(productRequest.getCategoryCode())
                .orElseThrow(() -> new NotFoundException(ExceptionCode.NOT_FOUND_CATEGORY_CODE));
        /* getReferenceById : category가 사용 되지 않는다면 tbl_category에 대한 select 구문이 실행 되지 않는다. */
        //Category category = categoryRepository.getReferenceById(productRequest.getCategoryCode());

        final Product newProduct = Product.of(
                productRequest.getProductName(),
                productRequest.getProductPrice(),
                productRequest.getProductDescription(),
                category, // 카테고리 엔티티
                IMAGE_URL + replaceFileName,   // 저장 된 파일의 url
                productRequest.getProductStock()
        );

        final Product product = productRepository.save(newProduct);

        return product.getProductCode();
    }


    public void modify(Long productCode, ProductUpdateRequest productRequest, MultipartFile productImg) {

        Product product = productRepository.findByProductCodeAndStatusNot(productCode, DELETED)
                .orElseThrow(() -> new NotFoundException(ExceptionCode.NOT_FOUND_PRODUCT_CODE));

        Category category = categoryRepository.findById(productRequest.getCategoryCode())
                .orElseThrow(() -> new NotFoundException(ExceptionCode.NOT_FOUND_CATEGORY_CODE));

        /* 이미지 수정이 필요할 경우 새로운 이미지 저장 후 기존 이미지 삭제 */
        if(productImg != null) {
            String replaceFileName = FileUploadUtils.saveFile(IMAGE_DIR, getRandomName(), productImg);
            FileUploadUtils.deleteFile(IMAGE_DIR, product.getProductImageUrl().replace(IMAGE_URL, ""));
            product.modifyProductImageUrl(IMAGE_URL + replaceFileName);
        }

        /* 수정을 위해 엔터티 정보 변경 */
        product.modify(
                productRequest.getProductName(),
                productRequest.getProductPrice(),
                productRequest.getProductDescription(),
                category,
                productRequest.getProductStock(),
                productRequest.getStatus()
        );

    }

    public void remove(Long productCode) {

        productRepository.deleteById(productCode);

    }

    public void updateStock(Long productCode, Long orderAmount) {

        Product product = productRepository.findByProductCodeAndStatus(productCode, USABLE)
                .orElseThrow(() -> new NotFoundException(ExceptionCode.NOT_FOUND_PRODUCT_CODE));

        verifyEnoughStock(product.getProductStock(), orderAmount);

        product.changeStock(orderAmount);

    }

    private void verifyEnoughStock(Long productStock, Long orderAmount) {
        if(productStock < orderAmount) throw new ConflictException(NOT_ENOUGH_STOCK);
    }


}
