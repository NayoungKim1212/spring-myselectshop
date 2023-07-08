package com.sparta.myselectshop.scheduler;

import com.sparta.myselectshop.entity.Product;
import com.sparta.myselectshop.naver.dto.ItemDto;
import com.sparta.myselectshop.naver.service.NaverApiService;
import com.sparta.myselectshop.repository.ProductRepository;
import com.sparta.myselectshop.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j(topic = "Scheduler") // 로그 찍기 위해 사용
@Component
@RequiredArgsConstructor
public class Scheduler {

    private final NaverApiService naverApiService; // 해당하는 목록을 다시 재검색
    private final ProductService productService; // 검색해야할 Product의 목록
    private final ProductRepository productRepository;

    // 초, 분, 시, 일, 월, 주 순서

    // @Scheduled 지정한 특정 시간마다 메서드가 동작
    @Scheduled(cron = "0 0 1 * * *") // 매일 새벽 1시
    // cron : 어떤 특정 시간마다 어떤 작업을 자동 수행하게 하고 싶을 때 사용
    public void updatePrice() throws InterruptedException {
        log.info("가격 업데이트 실행");
        List<Product> productList = productRepository.findAll(); // 재검색 해야할 Product 목록
        for (Product product : productList) {
            // 1초에 한 상품 씩 조회합니다 (NAVER 제한)
            TimeUnit.SECONDS.sleep(1);

            // i 번째 관심 상품의 제목으로 검색을 실행합니다.
            String title = product.getTitle();
            List<ItemDto> itemDtoList = naverApiService.searchItems(title);

            if (itemDtoList.size() > 0) {
                ItemDto itemDto = itemDtoList.get(0);
                // i 번째 관심 상품 정보를 업데이트합니다.
                Long id = product.getId(); // for문을 돌았던 product의 id
                try {
                    productService.updateBySearch(id, itemDto);
                } catch (Exception e) {
                    log.error(id + " : " + e.getMessage());
                }
            }
        }
    }

}