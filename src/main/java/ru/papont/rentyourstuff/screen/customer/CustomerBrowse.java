package ru.papont.rentyourstuff.screen.customer;

import io.jmix.ui.screen.*;
import ru.papont.rentyourstuff.customer.Customer;

@UiController("Customer.browse")
@UiDescriptor("customer-browse.xml")
@LookupComponent("customersTable")
public class CustomerBrowse extends StandardLookup<Customer> {
}