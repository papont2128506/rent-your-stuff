package ru.papont.rentyourstuff.customer;

import io.jmix.core.DataManager;
import io.jmix.core.security.SystemAuthenticator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ru.papont.rentyourstuff.entity.Address;


import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.groups.Default;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
class CustomerIntegrationTest {

    @Autowired
    private DataManager dataManager;

    @Autowired
    private SystemAuthenticator systemAuthenticator;

    private Customer customer;

    @Autowired
    private ValidationVerification validationVerification;

    @BeforeEach
    void setUp() {
        customer = dataManager.create(Customer.class);
    }

    @Test
    void given_validCustomer_when_saveCustomer_then_customerIsSaved() {

        //given
        customer.setFirstName("Foo");
        customer.setLastName("Bar");
        customer.setEmail("foo@bar.com");

        Address address = dataManager.create(Address.class);
        address.setCity("City");
        address.setStreet("Street");
        address.setPostCode("111111");
        customer.setAddress(address);

        // when
        Customer savedCustomer = systemAuthenticator.withSystem(() -> dataManager.save(customer));

        //then
        assertThat(savedCustomer.getId())
                .isNotNull();

    }
 }