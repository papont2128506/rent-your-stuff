package ru.papont.rentyourstuff.screen.customer;

import io.jmix.core.DataManager;
import io.jmix.ui.Screens;
import io.jmix.ui.component.Button;
import io.jmix.ui.component.Table;
import io.jmix.ui.screen.Screen;
import io.jmix.ui.testassist.UiTestAssistConfiguration;
import io.jmix.ui.testassist.junit.UiTest;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import ru.papont.rentyourstuff.RentYourStuffApplication;
import ru.papont.rentyourstuff.customer.Customer;
import ru.papont.rentyourstuff.entity.Address;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@UiTest(authenticatedUser = "admin",
        mainScreenId = "MainScreen",
        screenBasePackages = "ru.papont.rentyourstuff.screen.main")
@ContextConfiguration(classes = {RentYourStuffApplication.class, UiTestAssistConfiguration.class})
@AutoConfigureTestDatabase
class CustomerBrowseTest {

    @Autowired
    DataManager dataManager;
    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = dataManager.create(Customer.class);
        customer.setFirstName("Foo");
        customer.setLastName("Bar");

        Address address = dataManager.create(Address.class);
        address.setStreet("Foo Street 1");
        address.setCity("Foo City") ;
        customer.setAddress(address);

         dataManager.save(customer);
    }

//    @Test
//    void given_oneCustomerExists_when_openCustomerBrowse_then_tableContainsTheCustomer(Screens screens) {
//
//        //given:
//        CustomerBrowse customerBrowse = openCustomerBrowse(screens);
//
//        //expect:
//        Customer firstCustomer = firstLoadedCustomer(customerBrowse);
//
//        assertThat(firstCustomer)
//                .isEqualTo(customer);
//
//        assertThat(firstCustomer.getFirstName() )
//                .isEqualTo(customer.getFirstName());
//
//
//    }

    @Test
    void given_oneCustomerExists_when_openCustomerBrowse_then_tableContainsTheCustomer(Screens screens) {

        //given:
        CustomerBrowse customerBrowse = openCustomerBrowse(screens);

        assertThat(firstLoadedCustomer(customerBrowse))
                .isEqualTo(customer);

    }


    @Test
    void given_oneCustomerExists_when_editCustomer_then_CustomerEditorIsShown (Screens screens) {

        //given:
        CustomerBrowse customerBrowse = openCustomerBrowse(screens);

        //and:
        Customer firstCustomer  = firstLoadedCustomer(customerBrowse);

        //and:
        selectCustomerInTable(customerBrowse, firstCustomer);

        //when:
        button(customerBrowse, "editBtn").click();

        //then:
        CustomerEdit customerEdit = getScreenOfType(screens, CustomerEdit.class);

         assertThat(customerEdit.getEditedEntity())
                 .isEqualTo(firstCustomer);
    }

    private void selectCustomerInTable(CustomerBrowse customerBrowse, Customer firstCustomer) {
        Table<Customer> customerTable = customersTable(customerBrowse);
        customerTable.setSelected(firstCustomer);
    }

    @NotNull
    private <T> T getScreenOfType(Screens screens, Class<T> tClass) {
        Screen screen = screens.getOpenedScreens().getActiveScreens().stream().findFirst().orElseThrow();
        assertThat(screen).isInstanceOf(tClass);
        return (T) screen;
    }


    @Nullable
    private Button button(CustomerBrowse customerBrowse, String buttonId) {
        return (Button) customerBrowse.getWindow().getComponent(buttonId);
    }


    @NotNull
    private Customer firstLoadedCustomer(CustomerBrowse customerBrowse) {
        Collection<Customer> customers = loadedCustomers(customerBrowse);
        return customers.stream().findFirst().orElseThrow();
    }

    @NotNull
    private CustomerBrowse openCustomerBrowse(Screens screens) {
        CustomerBrowse customerBrowse = screens.create(CustomerBrowse.class);
        customerBrowse.show();

        return customerBrowse;
    }

    @NotNull
    private Collection<Customer> loadedCustomers(CustomerBrowse customerBrowse) {
        Table<Customer> customersTable = customersTable(customerBrowse);
        return customersTable.getItems().getItems();
    }

    @Nullable
    private Table<Customer> customersTable(CustomerBrowse customerBrowse) {
        return (Table<Customer>) customerBrowse.getWindow().getComponent("customersTable");
    }


    @AfterEach
    void tearDown() {
        dataManager.remove(customer );
    }
}