import java.util.*;

class Product {
    private int id;
    private String name;
    private double price;
    private int stockQuantity;

    public Product(int id, String name, double price, int stockQuantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }
}

class User {
    private String username;
    private String password;
    private ShoppingCart cart = new ShoppingCart();

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public ShoppingCart getCart() {
        return cart;
    }
}

class ShoppingCart {
    private Map<Product, Integer> items = new HashMap<>();

    public void addItem(Product product, int quantity) {
        items.put(product, items.getOrDefault(product, 0) + quantity);
    }

    public void removeItem(Product product, int quantity) {
        int currentQuantity = items.getOrDefault(product, 0);
        if (currentQuantity >= quantity) {
            items.put(product, currentQuantity - quantity);
        }
    }

    public Map<Product, Integer> getItems() {
        return items;
    }
}

class Order {
    private int orderId;
    private User user;
    private Map<Product, Integer> orderedItems = new HashMap<>();
    private double totalAmount;

    public Order(int orderId, User user, Map<Product, Integer> orderedItems) {
        this.orderId = orderId;
        this.user = user;
        this.orderedItems.putAll(orderedItems);
        this.totalAmount = calculateTotalAmount();
    }

    private double calculateTotalAmount() {
        return orderedItems.entrySet().stream()
            .mapToDouble(entry -> entry.getKey().getPrice() * entry.getValue())
            .sum();
    }

    public int getOrderId() {
        return orderId;
    }

    public User getUser() {
        return user;
    }

    public Map<Product, Integer> getOrderedItems() {
        return orderedItems;
    }

    public double getTotalAmount() {
        return totalAmount;
    }
}

class ECommercePlatform {
    private List<User> users = new ArrayList<>();
    private List<Product> products = new ArrayList<>();
    private List<Order> orders = new ArrayList<>();
    private int orderIdCounter = 1;

    public void addUser(User user) {
        users.add(user);
    }

    public void addProduct(Product product) {
        products.add(product);
    }

    public void processOrder(User user) {
        ShoppingCart cart = user.getCart();
        Map<Product, Integer> cartItems = cart.getItems();

        if (!cartItems.isEmpty()) {
            for (Map.Entry<Product, Integer> entry : cartItems.entrySet()) {
                Product product = entry.getKey();
                int quantity = entry.getValue();
                int availableStock = product.getStockQuantity();

                if (quantity <= availableStock) {
                    product.setStockQuantity(availableStock - quantity);
                } else {
                    System.out.println("Not enough stock for: " + product.getName());
                    continue;
                }
            }

            Order order = new Order(orderIdCounter++, user, cartItems);
            orders.add(order);
            cartItems.clear();
            System.out.println("Order placed successfully.");
        } else {
            System.out.println("Cart is empty.");
        }
    }
}

public class ECommerceSystem {
    public static void main(String[] args) {
        ECommercePlatform platform = new ECommercePlatform();

        // Adding users
        User user1 = new User("alice123", "password1");
        User user2 = new User("bob456", "password2");
        platform.addUser(user1);
        platform.addUser(user2);

        // Adding products
        Product product1 = new Product(1, "Laptop", 899.99, 10);
        Product product2 = new Product(2, "Smartphone", 499.99, 20);
        platform.addProduct(product1);
        platform.addProduct(product2);

        // User 1 adds items to the cart
        user1.getCart().addItem(product1, 2);
        user1.getCart().addItem(product2, 1);

        // User 2 adds items to the cart
        user2.getCart().addItem(product2, 3);

        // User 1 places an order
        platform.processOrder(user1);

        // User 2 places an order
        platform.processOrder(user2);

        // Displaying orders
        System.out.println("Orders:");
        for (Order order : platform.orders) {
            System.out.println("Order ID: " + order.getOrderId());
            System.out.println("User: " + order.getUser().getUsername());
            System.out.println("Total Amount: $" + order.getTotalAmount());
            System.out.println("Ordered Items:");
            for (Map.Entry<Product, Integer> entry : order.getOrderedItems().entrySet()) {
                System.out.println("- " + entry.getKey().getName() + " x" + entry.getValue());
            }
            System.out.println();
        }
    }
}
