package com.Blogs.Backend.BlogsBackend.Security.service;


import com.Blogs.Backend.BlogsBackend.Security.classes.Address;
import com.Blogs.Backend.BlogsBackend.Security.classes.CartProduct;
import com.Blogs.Backend.BlogsBackend.Security.dto.AddProductToCartRequestDto;
import com.Blogs.Backend.BlogsBackend.Security.dto.AddressDto;
import com.Blogs.Backend.BlogsBackend.Security.dto.CartInfoDto;
import com.Blogs.Backend.BlogsBackend.Security.enums.ProductCategory;
import com.Blogs.Backend.BlogsBackend.Security.email.OrdersMailSender;
import com.Blogs.Backend.BlogsBackend.Security.entity.Cart;
import com.Blogs.Backend.BlogsBackend.Security.entity.Customer;
import com.Blogs.Backend.BlogsBackend.Security.entity.Orders;
import com.Blogs.Backend.BlogsBackend.Security.entity.Product;
import com.Blogs.Backend.BlogsBackend.Security.exceptions.NoContentException;
import com.Blogs.Backend.BlogsBackend.Security.repository.CartRepository;
import com.Blogs.Backend.BlogsBackend.Security.repository.CustomerRepository;
import com.Blogs.Backend.BlogsBackend.Security.repository.OrdersRepository;
import com.Blogs.Backend.BlogsBackend.Security.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private OrdersRepository ordersRepository;
    @Autowired
    private OrdersMailSender ordersMailSender;

    public void addNewAddress(AddressDto addressDto, String name) {
        try {
            // Retrieve the customer by username. If the customer does not exist, an exception will be thrown.
            Customer customer = customerRepository.findByUserName(name);

            // Check if an address with the same name already exists in the customer's address list.
            // If found, assign it to the 'address' variable.
            Address address = customer.getAddresses().stream()
                    .filter(a -> a.getAddressName().equals(addressDto.getAddressName()))
                    .findFirst()
                    .orElse(null);

            // If an address with the same name already exists, throw an exception indicating a duplicate.
            if (address != null) {
                throw new IllegalArgumentException("duplicate address");
            }

            // If no duplicate address is found, add the new address to the customer's address list.
            // 'setAddress' is used to populate the new Address object with data from the addressDto.
            customer.getAddresses().add(new Address().setAddress(addressDto));

            // Save the updated customer with the new address to the database.
            customerRepository.save(customer);
        } catch (Exception e) {
            // If any exception occurs (e.g., customer not found), throw a custom exception.
            throw new NoContentException("customer not found");
        }
    }


    public void updateAddress(AddressDto addressDto, String name) {

        // Retrieve the customer by username. If the customer does not exist, a null or exception will be returned.
        Customer customer = customerRepository.findByUserName(name);

        // Remove any existing address in the customer's address list that has the same address name as in the addressDto.
        // This ensures that the old address with the same name is deleted before adding the updated address.
        customer.getAddresses().removeIf(address -> address.getAddressName().equals(addressDto.getAddressName()));

        // Add the updated address (created using the data from addressDto) to the customer's address list.
        customer.getAddresses().add(new Address().setAddress(addressDto));

        // Save the updated customer information (with the new address list) to the database.
        customerRepository.save(customer);
    }


    public void deleteAddress(String addressDto, String name) {

        // Retrieve the customer by username. If the customer does not exist, a null or exception may be returned.
        Customer customer = customerRepository.findByUserName(name);

        // Remove the address from the customer's address list where the address name matches the given addressDto.
        // This filters out the address that needs to be deleted from the list.
        customer.getAddresses().removeIf(address -> address.getAddressName().equals(addressDto));

        // Save the updated customer information (with the address removed) to the database.
        customerRepository.save(customer);
    }


    public Address getAddress(String addressName, String name) {

        // Retrieve the customer by username. If the customer does not exist, it may return null or throw an exception.
        Customer customer = customerRepository.findByUserName(name);

        // Attempt to find the address within the customer's list of addresses by matching the address name.
        // If no match is found, the result will be null.
        Address address = customer.getAddresses().stream()
                .filter(a -> a.getAddressName().equals(addressName))
                .findFirst()
                .orElse(null);

        // Check if the address was found. If not, throw an exception indicating the address was not found.
        if (address == null) {
            throw new NoContentException("address not found");
        }

        // Return the found address.
        return address;
    }


    public List<Address> getAddresses(String name) {

        // Retrieve the customer by username. The getAddresses() method returns the list of addresses associated with the customer.
        List<Address> result = customerRepository.findByUserName(name).getAddresses();

        // Check if the list of addresses is empty. If no addresses are found, throw an exception indicating no content.
        if (result.isEmpty()) {
            throw new NoContentException();
        }

        // Return the list of addresses if they are found.
        return result;
    }


    public List<Product> getAllProducts() {

        List<Product> result = productRepository.findAll();
        if (result.isEmpty()) {
            throw new NoContentException();
        }
        return result;
    }

    public void addProductToCart(AddProductToCartRequestDto dto, String name) {

        // Validate if the product quantity is greater than 0
        if (dto.getQuantity() <= 0) {
            throw new IllegalArgumentException("invalid product quantity");
        }

        // Retrieve the customer's cart by username. If not found, throw an exception.
        Cart cart = cartRepository.findByUserName(name).orElse(null);
        if (cart == null) {
            throw new IllegalArgumentException("invalid user name");
        }

        // Retrieve the product from the product repository using the product ID from the request DTO.
        Product product = productRepository.findById(dto.getProductId()).orElse(null);
        if (product == null) {
            throw new IllegalArgumentException("invalid user name");
        }

        // Check if the requested quantity is available in stock. If not, throw an exception.
        if (product.getQuantity() < dto.getQuantity()) {
            throw new IllegalArgumentException("insufficient product quantity, try to reduce it.");
        }

        // Check if the product is already in the cart.
        CartProduct cartProduct = cart.getProducts().stream()
                .filter(i -> i.getProductId().equals(dto.getProductId()))
                .findFirst().orElse(null);

        // If the product is not in the cart, add it.
        if (cartProduct == null) {
            if (product.getQuantity() < dto.getQuantity()) {
                throw new IllegalArgumentException("insufficient product quantity, try to reduce it.");
            }
            // Add a new CartProduct object to the cart with the requested quantity.
            cart.getProducts().add(new CartProduct(dto, product));
            cartRepository.save(cart);
        } else {
            // If the product is already in the cart, remove the existing CartProduct, update the quantity, and re-add it.
            cart.getProducts().remove(cartProduct);
            // Calculate the updated quantity in the cart.
            int updatedQuantity = (Integer.parseInt(cartProduct.getQuantity()) + dto.getQuantity());

            // Check if the updated quantity exceeds the available product stock.
            if (product.getQuantity() < updatedQuantity) {
                throw new IllegalArgumentException("insufficient product quantity, try to reduce it.");
            }

            // Update the quantity of the existing CartProduct and re-add it to the cart.
            cartProduct.setQuantity(String.valueOf(updatedQuantity));
            cart.getProducts().add(cartProduct);
            cartRepository.save(cart);
        }
    }


    public void deleteProductFromCart(String productId, String name) {

        // Retrieve the cart for the given username. If the cart is not found, throw an exception.
        Cart cart = cartRepository.findByUserName(name).orElse(null);
        if (cart == null) {
            throw new IllegalArgumentException("Could not find cart, Invalid user name.");
        }

        // Attempt to remove the product from the cart based on the provided product ID.
        // If no matching product is found to remove, throw an exception.
        if (!cart.getProducts().removeIf(product -> product.getProductId().equals(productId))) {
            throw new NoContentException("Could not find product in the cart, Invalid product id.");
        }

        // Save the updated cart after the product has been removed.
        cartRepository.save(cart);
    }


    public void updateCartProduct(AddProductToCartRequestDto dto, String name) {

        // If the quantity provided in the request is less than or equal to 0,
        // remove the product from the cart by calling deleteProductFromCart method.
        if (dto.getQuantity() <= 0) {
            deleteProductFromCart(dto.getProductId(), name);
            return; // Return early as no further operations are needed
        }

        // Retrieve the user's cart. If the cart is not found, throw an exception.
        Cart cart = cartRepository.findByUserName(name).orElse(null);
        if (cart == null) {
            throw new IllegalArgumentException("Could not find cart, Invalid user name.");
        }

        // Retrieve the product based on the product ID provided. If not found, throw an exception.
        Product product = productRepository.findById(dto.getProductId()).orElse(null);
        if (product == null) {
            throw new IllegalArgumentException("Invalid product ID.");
        }

        // Search for the product in the user's cart. If the product is not found, throw an exception.
        CartProduct cartProduct = cart.getProducts().stream()
                .filter(products -> products.getProductId().equals(dto.getProductId()))
                .findFirst().orElse(null);
        if (cartProduct == null) {
            throw new IllegalArgumentException("Could not find the product in the cart.");
        }

        // Check if the requested quantity is greater than the available product quantity.
        // If yes, throw an exception.
        if (product.getQuantity() < dto.getQuantity()) {
            throw new IllegalArgumentException("Insufficient quantity available, try reducing the quantity.");
        }

        // Remove the old product entry from the cart, update the quantity, and add it back to the cart.
        cart.getProducts().remove(cartProduct);
        cartProduct.setQuantity(String.valueOf(dto.getQuantity()));
        cart.getProducts().add(cartProduct);

        // Save the updated cart back to the repository.
        cartRepository.save(cart);
    }


    public Orders placeOrderFromCart(String addressName, String name) {

        Orders order = new Orders();

        // Fetch the customer's cart and check for null to avoid potential NullPointerException.
        Cart cart = cartRepository.findByUserName(name).orElse(null);
        if (cart == null || cart.getProducts().isEmpty()) {
            throw new RuntimeException("Cart is empty or not found for user: " + name);
        }

        // Fetch customer and address details.
        Customer customer = customerRepository.findByUserName(name);
        Address address = customer.getAddresses().stream()
                .filter(a -> a.getAddressName().equals(addressName))
                .findFirst()
                .orElseThrow(() -> new NoContentException("No address found with the address name provided."));

        // Collect product IDs from cart and calculate total amount in one go.
        List<String> productIds = cart.getProducts().stream()
                .map(CartProduct::getProductId)
                .collect(Collectors.toList());

        // Retrieve all products from the repository in one go.
        List<Product> products = productRepository.findAllById(productIds);
        Map<String, Product> productMap = products.stream()
                .collect(Collectors.toMap(Product::getId, product -> product));

        double totalAmount = 0;
        List<CartProduct> outOfStockProducts = new ArrayList<>();

        // Calculate total amount and check stock.
        for (CartProduct cartProduct : cart.getProducts()) {
            Product product = productMap.get(cartProduct.getProductId());

            if (product == null || product.getQuantity() < Integer.parseInt(cartProduct.getQuantity())) {
                outOfStockProducts.add(cartProduct);
                continue; // Skip out-of-stock items without interrupting the process.
            }

            // Update stock and calculate amount.
            int quantity = Integer.parseInt(cartProduct.getQuantity());
            product.setQuantity(product.getQuantity() - quantity);
            totalAmount += Double.parseDouble(cartProduct.getPrice()) * quantity;
        }

        // If there are out-of-stock products, throw an exception with details.
        if (!outOfStockProducts.isEmpty()) {
            String message = outOfStockProducts.stream()
                    .map(CartProduct::getProductName)
                    .collect(Collectors.joining(", ", "Reduce quantity or remove out-of-stock products: ", ""));
            throw new RuntimeException(message);
        }

        // Save updated products to repository.
        productRepository.saveAll(products);

        // Prepare and save the order.
        order.setUserName(name);
        order.setAddress(address);
        order.setCartProducts(new ArrayList<>(cart.getProducts())); // Clone to avoid cart clearing affecting the order.
        order.setTotalAmount(String.valueOf(totalAmount));
        ordersRepository.save(order);

        // Clear the cart.
        cart.getProducts().clear();
        cartRepository.save(cart);

        // Send order details via email.
        ordersMailSender.sendOrderDetailsToCustomer(order);
        ordersMailSender.sendOrderDetailsToSellers(order);

        return order;
    }



    public CartInfoDto getCart(String name) {

        // Create a new CartInfoDto object to store the cart details.
        CartInfoDto dto = new CartInfoDto();

        // Fetch the cart based on the provided username.
        Cart cart = cartRepository.findByUserName(name).orElse(null);

        // If the cart is not found for the user, throw an exception.
        if (cart == null) {
            throw new IllegalArgumentException("Cart not found");
        }

        // Set the list of products in the cart to the DTO.
        dto.setProducts(cart.getProducts());

        // Calculate the total price for the products in the cart by multiplying the price and quantity of each product.
        double totalPrice = cart.getProducts().stream()
                .mapToDouble(cartProduct -> Double.parseDouble(cartProduct.getPrice())
                        * Integer.parseInt(cartProduct.getQuantity()))
                .sum();

        // Set the calculated total price to the DTO as a string.
        dto.setTotalPrice(String.valueOf(totalPrice));

        // Return the CartInfoDto containing cart products and total price.
        return dto;
    }


    public Page<Product> searchProductByName(String word, int pageNumber, int pageSize) {

        // Fetch products whose name contains the given word, with pagination.
        Page<Product> result = (Page<Product>) productRepository.findByProductNameContaining(word, PageRequest.of(pageNumber, pageSize));

        // If no products are found, throw a NoContentException.
        if (result.isEmpty()) {
            throw new NoContentException(); // Ensure NoContentException is defined
        }

        // Return the paginated result containing matching products.
        return result;
    }

    @Async
    public CompletableFuture<List<Product>> getProductChunk(int offset, int limit) {
        Pageable pageable = PageRequest.of(offset, limit);
        List<Product> products = productRepository.findAll(pageable).getContent();
        return CompletableFuture.completedFuture(products);
    }

    public int getTotalProductCount() {
        return (int) productRepository.count();
    }

    public Page<Product> findProductsWithPagination(int offset, int pageSize) {
        Page<Product> products = productRepository.findAll(PageRequest.of(offset, pageSize));
        if (products.isEmpty()) {
            throw new NoContentException();
        }
        return products;
    }

    public Page<Product> findProductsWithPagination(int offset, int pageSize, ProductCategory category) {

        // Fetch products from the specified category with pagination applied.
        Page<Product> products = productRepository.findByCategory(category.toString(), PageRequest.of(offset, pageSize));

        // If no products are found, throw a NoContentException.
        if (products.isEmpty()) {
            throw new NoContentException();
        }

        // Return the paginated list of products belonging to the specified category.
        return products;
    }

    public List<Orders> getMyOrders(String userName) {

        // Fetch all orders placed by the user.
        List<Orders> result = ordersRepository.findByUserName(userName);

        // If no orders are found, throw a NoContentException with a custom message.
        if (result.isEmpty()) {
            throw new NoContentException("no orders have been placed yet");
        }

        // Return the list of orders placed by the user.
        return result;
    }

    public CartInfoDto incrementProductInCart(String data, String name) {

        // Increment the quantity of a specific product in the cart by 1.
        addProductToCart(new AddProductToCartRequestDto(data, 1), name);

        // After adding the product, fetch and return the updated cart information.
        return getCart(name);
    }


    public CartInfoDto decrementProductInCart(String data, String name) {
        // Fetch the cart of the user by username
        Cart cart = cartRepository.findByUserName(name).orElse(null);

        // Find the product in the cart by product ID
        CartProduct cartProduct = cart.getProducts().stream()
                .filter(p -> p.getProductId().equals(data))
                .findFirst().orElse(null);

        // Remove the product from the cart
        cart.getProducts().remove(cartProduct);

        // Decrement the quantity if it's greater than 1
        if (!cartProduct.getQuantity().equals("1")) {
            String oldQuantity = cartProduct.getQuantity();
            String newQuantity = String.valueOf(Integer.parseInt(oldQuantity) - 1); // Decrease by 1
            cartProduct.setQuantity(newQuantity);

            // Add the updated product back to the cart
            cart.getProducts().add(cartProduct);
        }

        // Save the updated cart
        cartRepository.save(cart);

        // Return the updated cart details
        return getCart(name);
    }


    public List<String> getMyAddressNames(String name) {
        // Fetch the customer by username
        Customer customer = customerRepository.findByUserName(name);

        // Extract the list of address names
        List<String> addressNames = customer.getAddresses().stream()
                .map(Address::getAddressName)
                .toList();

        // Check if no addresses are found and throw an exception if so
        if (addressNames.isEmpty()) {
            throw new NoContentException("no addresses found");
        }

        // Return the list of address names
        return addressNames;
    }
}
