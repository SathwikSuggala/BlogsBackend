package com.Blogs.Backend.BlogsBackend.Security.controller;


import com.Blogs.Backend.BlogsBackend.Security.dto.AddProductToCartRequestDto;
import com.Blogs.Backend.BlogsBackend.Security.dto.AddressDto;
import com.Blogs.Backend.BlogsBackend.Security.enums.ProductCategory;
import com.Blogs.Backend.BlogsBackend.Security.dto.RequestStringDto;
import com.Blogs.Backend.BlogsBackend.Security.entity.Product;
import com.Blogs.Backend.BlogsBackend.Security.exceptions.NoContentException;
import com.Blogs.Backend.BlogsBackend.Security.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class CustomerController {

    @Autowired
    CustomerService customerService;

    //address apis
    @PostMapping("/addNewAddress")
    public ResponseEntity<?> addNewAddress(@RequestBody AddressDto addressDto, Principal principal){
        try{
            customerService.addNewAddress(addressDto, principal.getName());
            return new ResponseEntity<>(true, HttpStatus.OK);
        }catch (Exception ne){
            return new ResponseEntity<>(false, HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/getAddress/{addressName}")
    public ResponseEntity<?> getAddress(@PathVariable String addressName, Principal principal){
        try{
            return new ResponseEntity<>(customerService.getAddress(addressName, principal.getName()), HttpStatus.OK);
        }catch (NoContentException ne){
            return new ResponseEntity<>(false, HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping("/getAddresses")
    public ResponseEntity<?> getAddresses(Principal principal){
        try{
            return new ResponseEntity<>(customerService.getAddresses(principal.getName()), HttpStatus.OK);
        }catch (NoContentException ne){
            return new ResponseEntity<>(false, HttpStatus.NO_CONTENT);
        }
    }

    @PutMapping("/updateAddress")
    public ResponseEntity<?> updateAddress(@RequestBody AddressDto addressDto, Principal principal){
        try{
            customerService.updateAddress(addressDto, principal.getName());
            return new ResponseEntity<>(true, HttpStatus.OK);
        }catch (Exception ne){
            return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/deleteAddress/{name}")
    public ResponseEntity<?> deleteAddress(@PathVariable String name, Principal principal){
        try{
            customerService.deleteAddress(name, principal.getName());
            return new ResponseEntity<>(true, HttpStatus.OK);
        }catch (Exception ne){
            return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
        }
    }

    //cart apis
    @GetMapping("/getCart")
    public ResponseEntity<?> getCart(Principal principal){
        try{
            return new ResponseEntity<>(customerService.getCart(principal.getName()), HttpStatus.OK);
        }catch (IllegalArgumentException ne){
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/addToCart")
    public ResponseEntity<?> addProductToCart(@RequestBody AddProductToCartRequestDto dto, Principal principal){
        try{
            customerService.addProductToCart(dto, principal.getName());
            return new ResponseEntity<>(true, HttpStatus.OK);
        }catch (IllegalArgumentException ne){
            return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/deleteFromCart")
    public ResponseEntity<?> deleteProductFromCart(@RequestBody RequestStringDto dto, Principal principal){
        try{
            customerService.deleteProductFromCart(dto.getData(), principal.getName());
            return new ResponseEntity<>(true, HttpStatus.OK);
        }catch (NoContentException ne){
            return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
        }

    }

    @PutMapping("/updateCart")
    public ResponseEntity<?> updateCartProduct(@RequestBody AddProductToCartRequestDto dto, Principal principal){
        try{
            customerService.updateCartProduct(dto, principal.getName());
            return new ResponseEntity<>(true, HttpStatus.OK);
        }catch (NoContentException ne){
            return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/checkOut")
    public ResponseEntity<?> orderProducts(@RequestBody RequestStringDto dto, Principal principal){
        try{
            return new ResponseEntity<>(customerService.placeOrderFromCart(dto.getData(), principal.getName()), HttpStatus.OK);
        }catch (NoContentException ne){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (RuntimeException re){
            return new ResponseEntity<>(re.getMessage(),HttpStatus.OK);
        }
    }

    //product apis
    @GetMapping("/getAllProducts")
    public ResponseEntity<?> getAllProducts() {
        try {
            int total = customerService.getTotalProductCount(); // will be fetched only one time.
            int chunkSize = 10; // creating the size for each batch of products
            int totalChunks = (int) Math.ceil((double) total / chunkSize);

            List<CompletableFuture<List<Product>>> futures = new ArrayList<>();

            // get each chunk asynchronously
            for (int i = 0; i < totalChunks; i++) {
                futures.add(customerService.getProductChunk(i, chunkSize));
            }

            // combine all chunks into a single list
            List<Product> allProducts = new ArrayList<>();
            for (CompletableFuture<List<Product>> future : futures) {
                allProducts.addAll(future.join());  //creating threads here.
            }

            // Return the combined list of products
            return new ResponseEntity<>(allProducts, HttpStatus.OK);

        } catch (NoContentException ne) {
            return new ResponseEntity<>("No products available", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/searchProduct/{pageNumber}/{pageSize}")
    public ResponseEntity<?> searchProducts(@RequestBody RequestStringDto dto, @PathVariable int pageNumber, @PathVariable int pageSize){
        try{
            return new ResponseEntity<>(customerService.searchProductByName(dto.getData(), pageNumber, pageSize), HttpStatus.OK);
        }catch (NoContentException ne){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping("/getAllCategories")
    public ResponseEntity<?> getAllCategories(){
        return new ResponseEntity<>(Arrays.asList(ProductCategory.values()), HttpStatus.OK);
    }

    @GetMapping("/getAllProducts/{offset}/{pageSize}")
    public ResponseEntity<?> getAllProducts(@PathVariable int offset, @PathVariable int pageSize){
        try{
            return new ResponseEntity<>(customerService.findProductsWithPagination(offset, pageSize), HttpStatus.OK);
        }catch (NoContentException ne) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }}

//    @GetMapping("/getAllProducts/{offset}/{pageSize}")
//    public ResponseEntity<?> getAllProducts(@PathVariable int offset, @PathVariable int pageSize) {
//        try {
//            int totalProducts = customerService.getTotalProductCount();
//            int totalPages = (int) Math.ceil((double) totalProducts / pageSize);
//
//            // Calculate starting page based on offset and pageSize
//            int startingPage = offset / pageSize;
//
//            List<CompletableFuture<Page<Product>>> futures = new ArrayList<>();
//
//            // Fetch each page asynchronously starting from the offset
//            for (int page = startingPage; page < totalPages; page++) {
//                futures.add(customerService.findProductsWithPaginationAsync(page, pageSize));
//            }
//
//            // Wait for all asynchronous tasks to complete
//            List<Product> allProducts = new ArrayList<>();
//            int totalElements = 0;
//            for (CompletableFuture<Page<Product>> future : futures) {
//                Page<Product> productPage = future.join();
//                allProducts.addAll(productPage.getContent());
//                totalElements += productPage.getNumberOfElements();
//            }
//
//            // Create a Page object containing the results and pagination info
//            Page<Product> resultPage = new PageImpl<>(allProducts, PageRequest.of(offset, pageSize), totalElements);
//
//            return new ResponseEntity<>(resultPage, HttpStatus.OK);
//
//        } catch (NoContentException ne) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//    }

    @GetMapping("/getAllProducts/{offset}/{pageSize}/{category}")
    public ResponseEntity<?> getAllProducts(@PathVariable int offset, @PathVariable int pageSize, @PathVariable ProductCategory category){
        try{
            return new ResponseEntity<>(customerService.findProductsWithPagination(offset, pageSize, category), HttpStatus.OK);
        }catch (NoContentException ne){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping("/getMyOrders")
    public ResponseEntity<?> getMyOrders(Principal principal){
        try{
            return new ResponseEntity<>(customerService.getMyOrders(principal.getName()), HttpStatus.OK);
        }catch (NoContentException ne){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @PostMapping("/incrementProduct")
    public ResponseEntity<?> incrementProductInCart(@RequestBody RequestStringDto dto, Principal principal){
        try {
            return new ResponseEntity<>(customerService.incrementProductInCart(dto.getData(), principal.getName()), HttpStatus.OK);
        }catch (IllegalArgumentException ie){
            return new ResponseEntity<>("stock not available, try again later",HttpStatus.OK);
        }
    }

    @PostMapping("/decrementProduct")
    public ResponseEntity<?> decrementProductInCart(@RequestBody RequestStringDto dto, Principal principal){
        return new ResponseEntity<>(customerService.decrementProductInCart(dto.getData(), principal.getName()),HttpStatus.OK);
    }

    @GetMapping("/addressNames")
    public ResponseEntity<?> getMyAddressNames(Principal principal){
        return new ResponseEntity<>(customerService.getMyAddressNames(principal.getName()), HttpStatus.OK);
    }
}
