package fr.carla.shop.bdd;

import fr.carla.shop.domain.CustomerOrder;
import fr.carla.shop.domain.Product;
import fr.carla.shop.domain.User;
import fr.carla.shop.repository.OrderRepository;
import fr.carla.shop.repository.ProductRepository;
import fr.carla.shop.repository.UserRepository;
import fr.carla.shop.service.AuthenticationService;
import fr.carla.shop.service.OrderService;
import fr.carla.shop.service.ProductCatalogService;
import fr.carla.shop.service.RegistrationService;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class OnlineShopStepDefinitions {

    private UserRepository userRepository;
    private ProductRepository productRepository;
    private OrderRepository orderRepository;

    private RegistrationService registrationService;
    private AuthenticationService authenticationService;
    private ProductCatalogService productCatalogService;
    private OrderService orderService;

    private final Map<String, Product> products = new HashMap<>();
    private final Map<String, CustomerOrder> orders = new HashMap<>();

    private String currentMessage;
    private RuntimeException caughtException;
    private List<Product> currentResults;
    private String currentPage;

    @Before
    public void setUp() {
        userRepository = mock(UserRepository.class);
        productRepository = mock(ProductRepository.class);
        orderRepository = mock(OrderRepository.class);

        registrationService = new RegistrationService(userRepository);
        authenticationService = new AuthenticationService(userRepository);
        productCatalogService = new ProductCatalogService(productRepository);
        orderService = new OrderService(orderRepository);

        products.clear();
        orders.clear();

        currentMessage = null;
        caughtException = null;
        currentResults = null;
        currentPage = null;
    }

    @When("the user opens the registration form")
    public void theUserOpensTheRegistrationForm() {
        currentPage = "registration";
    }

    @Then("the registration form is displayed")
    public void theRegistrationFormIsDisplayed() {
        assertEquals("registration", currentPage);
    }

    @Given("no account exists with username {string}")
    public void noAccountExistsWithUsername(String username) {
        when(userRepository.existsByUsername(username))
                .thenReturn(false);
    }

    @Given("an account already exists with username {string}")
    public void anAccountAlreadyExistsWithUsername(String username) {
        when(userRepository.existsByUsername(username))
                .thenReturn(true);
    }

    @When("the user creates an account with email {string}, username {string} and password {string}")
    public void theUserCreatesAnAccount(
            String email,
            String username,
            String password
    ) {
        try {
            currentMessage = registrationService.register(
                    email,
                    username,
                    password
            );
        } catch (RuntimeException exception) {
            caughtException = exception;
        }
    }

    @Then("account creation is confirmed")
    public void accountCreationIsConfirmed() {
        assertNull(caughtException);
        assertNotNull(currentMessage);
        assertTrue(currentMessage.contains("Account created"));
    }

    @Then("the user account is saved")
    public void theUserAccountIsSaved() {
        verify(userRepository).save(any(User.class));
    }

    @Then("the user account is not saved")
    public void theUserAccountIsNotSaved() {
        verify(userRepository, never()).save(any(User.class));
    }

    @When("the user opens the login form")
    public void theUserOpensTheLoginForm() {
        currentPage = "login";
    }

    @Then("the login form is displayed")
    public void theLoginFormIsDisplayed() {
        assertEquals("login", currentPage);
    }

    @Given("a user exists with username {string} and password {string}")
    public void aUserExistsWithUsernameAndPassword(
            String username,
            String password
    ) {
        User user = new User(
                username + "@mail.com",
                username,
                password
        );

        when(userRepository.findByUsername(username))
                .thenReturn(Optional.of(user));
    }

    @When("the user logs in with username {string} and password {string}")
    public void theUserLogsInWithUsernameAndPassword(
            String username,
            String password
    ) {
        try {
            currentMessage = authenticationService.login(
                    username,
                    password
            );
        } catch (RuntimeException exception) {
            caughtException = exception;
        }
    }

    @Then("login is successful")
    public void loginIsSuccessful() {
        assertNull(caughtException);
        assertEquals("Redirected to home page", currentMessage);
    }

    @Then("the user is redirected to the home page")
    public void theUserIsRedirectedToTheHomePage() {
        assertEquals("Redirected to home page", currentMessage);
    }

    @When("the user opens the search bar")
    public void theUserOpensTheSearchBar() {
        currentPage = "search";
    }

    @Then("the search bar is displayed")
    public void theSearchBarIsDisplayed() {
        assertEquals("search", currentPage);
    }

    @Given("the following products exist")
    public void theFollowingProductsExist(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps();

        for (Map<String, String> row : rows) {
            Product product = new Product(
                    row.get("reference"),
                    row.get("name"),
                    row.get("category"),
                    Double.parseDouble(row.get("price"))
            );

            products.put(product.getReference(), product);
        }

        configureProductRepository();
    }

    @Given("product {string} named {string} exists in category {string} with price {double}")
    public void productExists(
            String reference,
            String name,
            String category,
            double price
    ) {
        Product product = new Product(
                reference,
                name,
                category,
                price
        );

        products.put(reference, product);

        configureProductRepository();
    }

    private void configureProductRepository() {
        when(productRepository.searchByKeyword(anyString()))
                .thenAnswer(invocation -> {
                    String keyword = invocation
                            .getArgument(0, String.class)
                            .toLowerCase();

                    return products.values()
                            .stream()
                            .filter(product ->
                                    product.getName()
                                            .toLowerCase()
                                            .contains(keyword)
                                            || product.getReference()
                                            .toLowerCase()
                                            .contains(keyword)
                            )
                            .toList();
                });

        when(productRepository.searchByMaxPrice(anyDouble()))
                .thenAnswer(invocation -> {
                    double maxPrice = invocation.getArgument(0, Double.class);

                    return products.values()
                            .stream()
                            .filter(product ->
                                    product.getPrice() <= maxPrice
                            )
                            .toList();
                });

        when(productRepository.findByCategory(anyString()))
                .thenAnswer(invocation -> {
                    String category = invocation
                            .getArgument(0, String.class);

                    return products.values()
                            .stream()
                            .filter(product ->
                                    product.getCategory()
                                            .equalsIgnoreCase(category)
                            )
                            .toList();
                });

        when(productRepository.findByReference(anyString()))
                .thenAnswer(invocation -> {
                    String reference = invocation.getArgument(
                            0,
                            String.class
                    );

                    return Optional.ofNullable(products.get(reference));
                });
    }

    @When("the user searches products with keyword {string}")
    public void theUserSearchesProductsWithKeyword(String keyword) {
        currentResults = productCatalogService.searchByKeyword(keyword);
    }

    @When("the user searches products with maximum price {double}")
    public void theUserSearchesProductsWithMaximumPrice(double maxPrice) {
        currentResults = productCatalogService.searchByMaxPrice(maxPrice);
    }

    @Then("{int} products are returned")
    public void productsAreReturned(int expectedSize) {
        assertNotNull(currentResults);
        assertEquals(expectedSize, currentResults.size());
    }

    @Then("the results contain product {string}")
    public void theResultsContainProduct(String reference) {
        assertNotNull(currentResults);

        assertTrue(
                currentResults
                        .stream()
                        .anyMatch(product ->
                                product.getReference().equals(reference)
                        )
        );
    }

    @When("the user opens the categories page")
    public void theUserOpensTheCategoriesPage() {
        currentPage = "categories";
    }

    @Then("the categories page is displayed")
    public void theCategoriesPageIsDisplayed() {
        assertEquals("categories", currentPage);
    }

    @When("the user selects category {string}")
    public void theUserSelectsCategory(String category) {
        currentResults = productCatalogService.findByCategory(category);
    }

    @Given("order {string} exists")
    public void orderExists(String orderId) {
        CustomerOrder order = new CustomerOrder(orderId);
        orders.put(orderId, order);

        when(orderRepository.findById(orderId))
                .thenReturn(Optional.of(order));
    }

    @Given("order {string} does not exist")
    public void orderDoesNotExist(String orderId) {
        when(orderRepository.findById(orderId))
                .thenReturn(Optional.empty());
    }

    @Given("order {string} already contains product {string} with quantity {int}")
    public void orderAlreadyContainsProductWithQuantity(
            String orderId,
            String productReference,
            int quantity
    ) {
        CustomerOrder order = orders.get(orderId);
        Product product = products.get(productReference);

        for (int i = 0; i < quantity; i++) {
            order.addProduct(product);
        }
    }

    @When("the user adds product {string} to order {string}")
    public void theUserAddsProductToOrder(
            String productReference,
            String orderId
    ) {
        try {
            Product product = productCatalogService
                    .findByReference(productReference);

            currentMessage = orderService.addProductToOrder(
                    orderId,
                    product
            );
        } catch (RuntimeException exception) {
            caughtException = exception;
        }
    }

    @Then("product addition is confirmed")
    public void productAdditionIsConfirmed() {
        assertNull(caughtException);
        assertEquals("Product added to order", currentMessage);
    }

    @Then("order {string} contains product {string} with quantity {int}")
    public void orderContainsProductWithQuantity(
            String orderId,
            String productReference,
            int expectedQuantity
    ) {
        CustomerOrder order = orders.get(orderId);

        assertNotNull(order);
        assertTrue(order.containsProduct(productReference));
        assertEquals(
                expectedQuantity,
                order.getProductQuantity(productReference)
        );
    }

    @When("the user removes product {string} from order {string}")
    public void theUserRemovesProductFromOrder(
            String productReference,
            String orderId
    ) {
        try {
            currentMessage = orderService.removeProductFromOrder(
                    orderId,
                    productReference
            );
        } catch (RuntimeException exception) {
            caughtException = exception;
        }
    }

    @Then("product removal is confirmed")
    public void productRemovalIsConfirmed() {
        assertNull(caughtException);
        assertEquals("Product removed from order", currentMessage);
    }

    @Then("order {string} does not contain product {string}")
    public void orderDoesNotContainProduct(
            String orderId,
            String productReference
    ) {
        CustomerOrder order = orders.get(orderId);

        assertNotNull(order);
        assertFalse(order.containsProduct(productReference));
    }

    @When("the user opens the checkout form")
    public void theUserOpensTheCheckoutForm() {
        currentPage = "checkout";
    }

    @Then("the checkout form is displayed")
    public void theCheckoutFormIsDisplayed() {
        assertEquals("checkout", currentPage);
    }

    @When("the user validates order {string}")
    public void theUserValidatesOrder(String orderId) {
        try {
            currentMessage = orderService.validateOrder(orderId);
        } catch (RuntimeException exception) {
            caughtException = exception;
        }
    }

    @Then("order validation is confirmed")
    public void orderValidationIsConfirmed() {
        assertNull(caughtException);
        assertEquals("Order confirmed", currentMessage);
    }

    @Then("the confirmation message contains {string}")
    public void theConfirmationMessageContains(String expectedText) {
        assertNotNull(currentMessage);
        assertTrue(currentMessage.contains(expectedText));
    }

    @Then("an error is returned")
    public void anErrorIsReturned() {
        assertNotNull(caughtException);
    }

    @Then("the error message contains {string}")
    public void theErrorMessageContains(String expectedText) {
        assertNotNull(caughtException);
        assertTrue(caughtException.getMessage().contains(expectedText));
    }
}