
INSERT INTO "users" ("id", "blocked", "confirmed", "email", "password", "timestamp", "user") VALUES
	(1, 'false', 'false', 'email1@mail.com', '$2a$11$i0wYvFxC9RqzPduQIAaJUe2qkyS3iB19Uz4yHO8nsdec19Ec1mNSm', '2018-11-16 12:56:44.606', 'user');

INSERT INTO "roles" ("id", "name") VALUES
  (1, "ROLE_USER"),
  (2, "ROLE_ADMIN");

INSERT INTO "user_role" (user_id, role_id) VALUES
  (1, 1);