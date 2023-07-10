package com.sparta.myselectshop.dto;

import com.sparta.myselectshop.entity.Product;
import com.sparta.myselectshop.entity.ProductFolder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class ProductResponseDto {
    private Long id;
    private String title;
    private String link;
    private String image;
    private int lprice;
    private int myprice;

    private List<FolderResponseDto> productFolderList = new ArrayList<>();
    // Product에 해시태크처럼 걸린 상품들의 정보를 보내주는데 여러 개 등록할 수 있기 때문에 List
    // FolderResponseDto라고 폴더에 정보가 담긴 데이터를 보내줄건데 이름은 productFolderList


    public ProductResponseDto(Product product) {
        this.id = product.getId();
        this.title = product.getTitle();
        this.link = product.getLink();
        this.image = product.getImage();
        this.lprice = product.getLprice();
        this.myprice = product.getMyprice();

        for (ProductFolder productFolder : product.getProductFolderList()) { // 지연 로딩
            productFolderList.add(new FolderResponseDto(productFolder.getFolder()));
        // product에 연결한 중간 테이블 ProductFolderList를 가지고 옴
        // folder -> FolderResponseDto로 변환
        // productFolderList에 저장 ==> List에는 폴더의 정보들이 담기게 됨
        // Client로 넘어감 -> 반환됨
        }
    }
}