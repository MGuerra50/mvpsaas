package com.saas.b2b.customer.application.service;

import org.springframework.stereotype.Service;

import com.saas.b2b.customer.application.port.in.CreateCustomerUseCase;
import com.saas.b2b.customer.application.port.in.DeleteCustomerUseCase;
import com.saas.b2b.customer.application.port.in.GetCustomerUseCase;
import com.saas.b2b.customer.application.port.in.ListCustomersUseCase;
import com.saas.b2b.customer.application.port.in.UpdateCustomerUseCase;
import com.saas.b2b.customer.application.port.out.CustomerRepositoryPort;
import com.saas.b2b.customer.domain.exception.CustomerNotFoundException;
import com.saas.b2b.customer.domain.model.Customer;
import com.saas.b2b.shared.api.PageResult;
import com.saas.b2b.shared.tenancy.TenantContext;

@Service
public class CustomerService implements
		ListCustomersUseCase,
		GetCustomerUseCase,
		CreateCustomerUseCase,
		UpdateCustomerUseCase,
		DeleteCustomerUseCase {

	private final CustomerRepositoryPort customerRepositoryPort;

	public CustomerService(CustomerRepositoryPort customerRepositoryPort) {
		this.customerRepositoryPort = customerRepositoryPort;
	}

	@Override
	public PageResult<Customer> list(String search, int page, int size) {
		Long tenantId = TenantContext.requireCurrentTenantId();
		return customerRepositoryPort.search(tenantId, search, page, size);
	}

	@Override
	public Customer getById(Long id) {
		return customerRepositoryPort.findById(id)
				.orElseThrow(() -> new CustomerNotFoundException(id));
	}

	@Override
	public Customer create(CreateCustomerCommand command) {
		Long tenantId = TenantContext.requireCurrentTenantId();
		Customer customer = Customer.builder()
				.tenantId(tenantId)
				.name(command.name())
				.phone(command.phone())
				.documentId(command.documentId())
				.build();
		return customerRepositoryPort.save(customer);
	}

	@Override
	public Customer update(Long id, UpdateCustomerCommand command) {
		Customer existing = getById(id);
		Customer updated = Customer.builder()
				.id(existing.getId())
				.tenantId(existing.getTenantId())
				.name(command.name())
				.phone(command.phone())
				.documentId(command.documentId())
				.build();
		return customerRepositoryPort.save(updated);
	}

	@Override
	public void delete(Long id) {
		if (!customerRepositoryPort.findById(id).isPresent()) {
			throw new CustomerNotFoundException(id);
		}
		customerRepositoryPort.deleteById(id);
	}
}
