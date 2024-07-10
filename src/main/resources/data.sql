INSERT INTO roles (role_id, name) VALUES (1, 'admin') ON CONFLICT (name) DO NOTHING;;
INSERT INTO roles (role_id, name) VALUES (2, 'basic') ON CONFLICT (name) DO NOTHING;;