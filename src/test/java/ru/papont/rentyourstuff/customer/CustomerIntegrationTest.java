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

    @Autowired
    private Validator validator;

    private Customer customer;

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

    @Test
    void given_customerWithInvalidEmail_when_validateCustomer_then_customerIsInvalid() {

        //given
        customer.setEmail("InvalidEmailAddress");

        // when
        Set<ConstraintViolation<Customer>> validations = validator.validate(customer, Default.class);

        //then
        assertThat(validations)
                .hasSize(1);

        //and
        ConstraintViolation<Customer> emailValidation = firstValidation(validations);

        assertThat(emailValidation.getPropertyPath().toString())
                .isEqualTo("email");

        assertThat(emailValidation.getMessageTemplate())
                .isEqualTo("{javax.validation.constraints.Email.message}");

    }

    private ConstraintViolation<Customer> firstValidation(Set<ConstraintViolation<Customer>> validations) {
        return validations.stream().findFirst().orElseThrow();
    }
}