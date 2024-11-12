package com.Blogs.Backend.BlogsBackend.Security.email;


import com.Blogs.Backend.BlogsBackend.Security.classes.Address;
import com.Blogs.Backend.BlogsBackend.Security.classes.CartProduct;
import com.Blogs.Backend.BlogsBackend.Security.entity.Orders;
import com.Blogs.Backend.BlogsBackend.Security.entity.Product;
import com.Blogs.Backend.BlogsBackend.Security.entity.Seller;
import com.Blogs.Backend.BlogsBackend.Security.entity.User;
import com.Blogs.Backend.BlogsBackend.Security.repository.ProductRepository;
import com.Blogs.Backend.BlogsBackend.Security.repository.SellerRepository;
import com.Blogs.Backend.BlogsBackend.Security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrdersMailSender {

    @Autowired
    private EmailSender emailSender;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private SellerRepository sellerRepository;

    @Async("taskExecutor")
    public void sendOrderDetailsToCustomer(Orders order) {
        User user = userRepository.findByUsername(order.getUserName()).orElse(null);
        if (user == null) {
            return; // Handle the case if user is not found
        }

        String userEmail = user.getEmail();
        String subject = "Order Confirmation - Order ID: " + order.getId();

        // Format the order details into a readable format
        StringBuilder body = new StringBuilder();
        body.append("Dear ").append(user.getFirstName()).append(",\n\n");
        body.append("Thank you for your order! Here are the details:\n\n");
        body.append("Order ID: ").append(order.getId()).append("\n");
        body.append("Total Amount: $").append(order.getTotalAmount()).append("\n\n");
        body.append("Shipping Address:\n");
        body.append(order.getAddress().getFullName()).append("\n");
        body.append(order.getAddress().getHouseNumber()).append(", ")
                .append(order.getAddress().getStreet()).append("\n");
        body.append(order.getAddress().getCity()).append(", ")
                .append(order.getAddress().getState()).append(" ")
                .append(order.getAddress().getPinCode()).append("\n");
        body.append(order.getAddress().getCountry()).append("\n\n");

        body.append("Order Items:\n");
        for (CartProduct product : order.getCartProducts()) {
            body.append("- ").append(product.getProductName())
                    .append(" (ID: ").append(product.getProductId()).append(")\n")
                    .append("  Quantity: ").append(product.getQuantity())
                    .append(" | Price: $").append(product.getPrice()).append("\n\n");
        }

        body.append("If you have any questions about your order, feel free to contact us.\n\n");
        body.append("Best regards,\nYour Company Name");

        // Send the email
        emailSender.sendMail(userEmail, subject, body.toString());
    }


    @Async("taskExecutor")
    public void sendOrderDetailsToSellers(Orders order) {
        // Get product IDs from the order
        List<String> productIds = order.getCartProducts().stream()
                .map(CartProduct::getProductId)
                .toList();

        // Fetch products based on product IDs
        List<Product> products = productRepository.findAllById(productIds);

        // Extract seller usernames associated with each product
        List<String> sellerUserNames = products.stream()
                .map(Product::getCreatedBy)
                .distinct()
                .toList();

        // Fetch sellers' details using their usernames
        List<User> sellerList = userRepository.findAllByUserNameIn(sellerUserNames);
//        List<Seller> sellers = sellerRepository.findAllByUserNameIn(sellerUserNames);
        // Map each seller's username to their email for easy lookup
        Map<String, String> sellerEmailMap = sellerList.stream()
                .collect(Collectors.toMap(User::getUserName, User::getEmail));

        // Group products by their sellers
        Map<String, List<Product>> productsBySeller = products.stream()
                .collect(Collectors.groupingBy(Product::getCreatedBy));

        // Format the user's address
        Address address = order.getAddress();
        String formattedAddress = String.format(
                "%s\n%s, %s\n%s, %s, %s, %s\n%s",
                address.getFullName(),
                address.getHouseNumber(), address.getStreet(),
                address.getCity(), address.getState(), address.getPinCode(),
                address.getCountry(),
                address.getMobileNumber()
        );

        // Send order details to each seller
        for (Map.Entry<String, List<Product>> entry : productsBySeller.entrySet()) {
            String sellerUserName = entry.getKey();
            String sellerEmail = sellerEmailMap.get(sellerUserName);
            List<Product> sellerProducts = entry.getValue();
            // Build email body
            StringBuilder body = new StringBuilder();
            body.append("Dear ").append(sellerUserName).append(",\n\n");
            body.append("You have new orders for the following products:\n\n");

            for (Product product : sellerProducts) {
                int quantity = order.getCartProducts().stream()
                        .filter(cartProduct -> cartProduct.getProductId().equals(product.getId()))
                        .mapToInt(cartProduct -> Integer.parseInt(cartProduct.getQuantity()))
                        .sum();

                body.append("Product Name: ").append(product.getProductName()).append("\n")
                        .append("Product ID: ").append(product.getId()).append("\n")
                        .append("Quantity Ordered: ").append(quantity).append("\n\n");
            }

            body.append("Shipping Address:\n").append(formattedAddress).append("\n\n");
            body.append("Please ensure timely processing of these orders.\n\n");
            body.append("Best regards,\nYour Company Name");

            addOrderToSeller(sellerUserName, body);
            // Send email to the seller
            emailSender.sendMail(sellerEmail, "New Order Details", body.toString());
        }
    }

    private void addOrderToSeller(String sellerUserName, StringBuilder body) {
        Seller seller = sellerRepository.findByUserName(sellerUserName);
        seller.getOrders().add(String.valueOf(body));
        sellerRepository.save(seller);
    }

}

