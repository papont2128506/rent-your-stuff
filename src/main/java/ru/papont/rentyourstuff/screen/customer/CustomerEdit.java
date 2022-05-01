package ru.papont.rentyourstuff.screen.customer;

import io.jmix.ui.screen.*;
import ru.papont.rentyourstuff.customer.Customer;

@UiController("Customer.edit")
@UiDescriptor("customer-edit.xml")
@EditedEntityContainer("customerDc")
public class CustomerEdit extends StandardEditor<Customer> {
}