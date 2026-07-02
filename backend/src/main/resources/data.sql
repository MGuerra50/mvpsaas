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

INSERT INTO products (id, tenant_id, name, sku, unit_price, status) VALUES
  (1, 1, 'Notebook Pro 15"', 'NB-001', 4599.90, 'ACTIVE');
INSERT INTO products (id, tenant_id, name, sku, unit_price, status) VALUES
  (2, 1, 'Mouse Ergonômico', 'MS-002', 89.90, 'ACTIVE');
INSERT INTO products (id, tenant_id, name, sku, unit_price, status) VALUES
  (3, 1, 'Monitor 27" 4K', 'MN-004', 1899.00, 'ACTIVE');
INSERT INTO products (id, tenant_id, name, sku, unit_price, status) VALUES
  (4, 1, 'Mesa Ajustável', 'DK-014', 2199.00, 'ACTIVE');
INSERT INTO products (id, tenant_id, name, sku, unit_price, status) VALUES
  (5, 1, 'Licença Office 365', 'SW-015', 399.00, 'INACTIVE');
INSERT INTO products (id, tenant_id, name, sku, unit_price, status) VALUES
  (6, 2, 'Produto Beta', 'BT-001', 99.00, 'ACTIVE');

-- Reinicia sequências de ID após inserts explícitos (H2 IDENTITY não avança sozinho)
ALTER TABLE tenants ALTER COLUMN id RESTART WITH 3;
ALTER TABLE users ALTER COLUMN id RESTART WITH 3;
ALTER TABLE customers ALTER COLUMN id RESTART WITH 5;
ALTER TABLE budgets ALTER COLUMN id RESTART WITH 4;
ALTER TABLE products ALTER COLUMN id RESTART WITH 7;
