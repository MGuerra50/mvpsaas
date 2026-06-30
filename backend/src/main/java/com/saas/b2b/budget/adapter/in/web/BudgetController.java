package com.saas.b2b.budget.adapter.in.web;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.saas.b2b.budget.application.port.in.CreateBudgetUseCase;
import com.saas.b2b.budget.application.port.in.ListBudgetsUseCase;
import com.saas.b2b.budget.application.port.in.UpdateBudgetStatusUseCase;
import com.saas.b2b.budget.domain.model.BudgetStatus;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/budgets")
public class BudgetController {

	private final ListBudgetsUseCase listBudgetsUseCase;
	private final CreateBudgetUseCase createBudgetUseCase;
	private final UpdateBudgetStatusUseCase updateBudgetStatusUseCase;

	public BudgetController(
			ListBudgetsUseCase listBudgetsUseCase,
			CreateBudgetUseCase createBudgetUseCase,
			UpdateBudgetStatusUseCase updateBudgetStatusUseCase) {
		this.listBudgetsUseCase = listBudgetsUseCase;
		this.createBudgetUseCase = createBudgetUseCase;
		this.updateBudgetStatusUseCase = updateBudgetStatusUseCase;
	}

	@GetMapping
	List<BudgetResponse> list(@RequestParam(required = false) BudgetStatus status) {
		return listBudgetsUseCase.list(status).stream().map(BudgetResponse::from).toList();
	}

	@PostMapping
	ResponseEntity<BudgetResponse> create(@Valid @RequestBody CreateBudgetRequest request) {
		var budget = createBudgetUseCase.create(new CreateBudgetUseCase.CreateBudgetCommand(
				request.customerId(), request.totalAmount()));
		return ResponseEntity.status(HttpStatus.CREATED).body(BudgetResponse.from(budget));
	}

	@PatchMapping("/{id}/status")
	BudgetResponse updateStatus(@PathVariable Long id, @Valid @RequestBody UpdateBudgetStatusRequest request) {
		return BudgetResponse.from(updateBudgetStatusUseCase.updateStatus(id, request.status()));
	}
}
