ALTER TABLE users ADD CONSTRAINT UK_cpf UNIQUE (cpf);
ALTER TABLE users ADD CONSTRAINT UK_email UNIQUE (email);