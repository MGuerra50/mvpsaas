package com.saas.b2b.budget.domain.model;

import java.math.BigDecimal;

public record MonthlyRevenue(String month, BigDecimal amount) {
}
