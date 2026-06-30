-- Seed de dados de exemplo (dois tenants para validar isolamento multi-tenant)
-- Senha dos usuários: password (BCrypt)

INSERT INTO tenants (id, name, status) VALUES (1, 'Loja Alpha', 'ACTIVE');
INSERT INTO tenants (id, name, status) VALUES (2, 'Loja Beta', 'ACTIVE');

INSERT INTO users (id, tenant_id, name, email, password_hash) VALUES
  (1, 1, 'Admin Alpha', 'admin@alpha.com', '${app.seed.bcrypt-password}');
INSERT INTO users (id, tenant_id, name, email, password_hash) VALUES
  (2, 2, 'Admin Beta', 'admin@beta.com', '${app.seed.bcrypt-password}');

INSERT INTO customers (id, tenant_id, name, phone, document_id) VALUES
  (1, 1, 'Cliente Alpha 1', '11999990001', '11111111101');
INSERT INTO customers (id, tenant_id, name, phone, document_id) VALUES
  (2, 1, 'Cliente Alpha 2', '11999990002', '11111111102');
INSERT INTO customers (id, tenant_id, name, phone, document_id) VALUES
  (3, 2, 'Cliente Beta 1', '21999990001', '22222222201');
INSERT INTO customers (id, tenant_id, name, phone, document_id) VALUES
  (4, 2, 'Cliente Beta 2', '21999990002', '22222222202');

INSERT INTO budgets (id, tenant_id, customer_id, total_amount, status, created_at) VALUES
  (1, 1, 1, 1500.00, 'APPROVED', TIMESTAMP '2026-06-01 10:00:00');
INSERT INTO budgets (id, tenant_id, customer_id, total_amount, status, created_at) VALUES
  (2, 1, 2, 800.00, 'DRAFT', TIMESTAMP '2026-06-15 14:30:00');
INSERT INTO budgets (id, tenant_id, customer_id, total_amount, status, created_at) VALUES
  (3, 2, 3, 2300.50, 'DRAFT', TIMESTAMP '2026-06-10 09:00:00');
