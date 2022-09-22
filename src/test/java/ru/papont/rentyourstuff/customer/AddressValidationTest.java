package ru.papont.rentyourstuff.customer;


import io.jmix.core.DataManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.papont.rentyourstuff.entity.Address;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class AddressValidationTest {

    @Autowired
    private DataManager dataManager;

    @Autowired
    private ValidationVerification validationVerification;

    private Address address;

    @BeforeEach
    void setUp() {
        //given
        address = dataManager.create(Address.class);
    }

    @Test
    void given_addressWithInvalidStreet_when_validateAddress_then_oneViolationOccurs() {
        address.setStreet(null);

        // when
        List<ValidationVerification.ValidationResult> violations = validationVerification.validate(address);

        //then
        assertThat(violations)
                .hasSize(1);

    }

    @Test
    void given_addressWithInvalidStreet_when_validateAddress_then_addressIsInvalidBecauseOfStreet() {
        address.setStreet(null);

        // when
        ValidationVerification.ValidationResult streetViolations = validationVerification.validationFirst(address);

        //and
        assertThat(streetViolations.getAttribute())
                .isEqualTo("street");

        assertThat(streetViolations.getErrorType())
                .isEqualTo(validationVerification.validationMessage("NotBlank"));

    }
}
