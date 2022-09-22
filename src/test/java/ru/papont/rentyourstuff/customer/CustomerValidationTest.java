package ru.papont.rentyourstuff.customer;


import io.jmix.core.DataManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class CustomerValidationTest {

    @Autowired
    private DataManager dataManager;

    @Autowired
    private ValidationVerification validationVerification;

    private Customer customer;

    @BeforeEach
    void setUp() {
        //given
        customer = dataManager.create(Customer.class);
    }

    @Test
    void given_validCustomer_when_saveCustomer_then_oneViolationOccurs() {
        customer.setEmail("InvalidEmailAddress");

        // when
        List<ValidationVerification.ValidationResult> violations = validationVerification.validate(customer);

        //then
        assertThat(violations)
                .hasSize(1);

    }

    @Test
    void given_validCustomer_when_saveCustomer_then_customerIsInvalidBecauseOfEmail() {
        customer.setEmail("InvalidEmailAddress" );

        // when
        ValidationVerification.ValidationResult streetViolations = validationVerification.validationFirst(customer);

        //and
        assertThat(streetViolations.getAttribute())
                .isEqualTo("email");

        assertThat(streetViolations.getErrorType())
                .isEqualTo(validationVerification.validationMessage("Email"));

    }
}
