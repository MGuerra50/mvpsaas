package com.saas.b2b.customer.adapter.in.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.saas.b2b.customer.application.port.in.CreateCustomerUseCase;
import com.saas.b2b.customer.application.port.in.DeleteCustomerUseCase;
import com.saas.b2b.customer.application.port.in.GetCustomerUseCase;
import com.saas.b2b.customer.application.port.in.ListCustomersUseCase;
import com.saas.b2b.customer.application.port.in.UpdateCustomerUseCase;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

	private final ListCustomersUseCase listCustomersUseCase;
	private final GetCustomerUseCase getCustomerUseCase;
	private final CreateCustomerUseCase createCustomerUseCase;
	private final UpdateCustomerUseCase updateCustomerUseCase;
	private final DeleteCustomerUseCase deleteCustomerUseCase;

	public CustomerController(
			ListCustomersUseCase listCustomersUseCase,
			GetCustomerUseCase getCustomerUseCase,
			CreateCustomerUseCase createCustomerUseCase,
			UpdateCustomerUseCase updateCustomerUseCase,
			DeleteCustomerUseCase deleteCustomerUseCase) {
		this.listCustomersUseCase = listCustomersUseCase;
		this.getCustomerUseCase = getCustomerUseCase;
		this.createCustomerUseCase = createCustomerUseCase;
		this.updateCustomerUseCase = updateCustomerUseCase;
		this.deleteCustomerUseCase = deleteCustomerUseCase;
	}

	@GetMapping
	CustomerPageResponse list(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size,
			@RequestParam(required = false) String search) {
		return CustomerPageResponse.from(listCustomersUseCase.list(search, page, size));
	}

	@GetMapping("/{id}")
	CustomerResponse getById(@PathVariable Long id) {
		return CustomerResponse.from(getCustomerUseCase.getById(id));
	}

	@PostMapping
	ResponseEntity<CustomerResponse> create(@Valid @RequestBody CreateCustomerRequest request) {
		var customer = createCustomerUseCase.create(new CreateCustomerUseCase.CreateCustomerCommand(
				request.name(), request.phone(), request.documentId()));
		return ResponseEntity.status(HttpStatus.CREATED).body(CustomerResponse.from(customer));
	}

	@PutMapping("/{id}")
	CustomerResponse update(@PathVariable Long id, @Valid @RequestBody UpdateCustomerRequest request) {
		var customer = updateCustomerUseCase.update(id, new UpdateCustomerUseCase.UpdateCustomerCommand(
				request.name(), request.phone(), request.documentId()));
		return CustomerResponse.from(customer);
	}

	@DeleteMapping("/{id}")
	ResponseEntity<Void> delete(@PathVariable Long id) {
		deleteCustomerUseCase.delete(id);
		return ResponseEntity.noContent().build();
	}
}
