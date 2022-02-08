package com.async.executorservice;

import com.async.domain.Product;
import com.async.domain.ProductInfo;
import com.async.domain.Review;
import com.async.service.ProductInfoService;
import com.async.service.ReviewService;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.*;

import static com.async.util.CommonUtil.stopWatch;
import static com.async.util.LoggerUtil.log;

@RequiredArgsConstructor
public class ProductServiceUsingExecutorService {
    static ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    private final ProductInfoService productInfoService;
    private final ReviewService reviewService;

    public Product retrieveProductDetails(String productId) throws ExecutionException, InterruptedException {
        stopWatch.start();

        Future<ProductInfo> productInfoFuture = executorService.submit(() -> productInfoService.retrieveProductInfo(productId));
        Future<Review> reviewFuture = executorService.submit(() -> reviewService.retrieveReviews(productId));

        ProductInfo productInfo = productInfoFuture.get(); //productInfoFuture.get(1, TimeUnit.SECONDS);

        Review review = reviewFuture.get();

        stopWatch.stop();
        log("Total Time Taken : "+ stopWatch.getTime());
        return new Product(productId, productInfo, review);
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        ProductInfoService productInfoService = new ProductInfoService();
        ReviewService reviewService = new ReviewService();
        ProductServiceUsingExecutorService productService = new ProductServiceUsingExecutorService(productInfoService, reviewService);
        String productId = "ABC123";
        Product product = productService.retrieveProductDetails(productId);
        log("Product is " + product);

        executorService.shutdown();

    }
}
