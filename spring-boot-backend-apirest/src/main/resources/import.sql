/* Populate tabla clientes */
INSERT INTO regions (name) VALUES ('Sudamérica');
INSERT INTO regions (name) VALUES ('Centroamérica');
INSERT INTO regions (name) VALUES ('Norteamérica');
INSERT INTO regions (name) VALUES ('Europa');
INSERT INTO regions (name) VALUES ('Asía');
INSERT INTO regions (name) VALUES ('Africa');
INSERT INTO regions (name) VALUES ('Oceanía');
INSERT INTO regions (name) VALUES ('Antártida');

INSERT INTO clientes (region_id, nombre, apellido, email, create_at) VALUES(1, 'Andrés', 'Guzmán', 'profesor@bolsadeideas.com', '2018-01-01');
INSERT INTO clientes (region_id, nombre, apellido, email, create_at) VALUES(2, 'Mr. John', 'Doe', 'john.doe@gmail.com', '2018-01-02');
INSERT INTO clientes (region_id, nombre, apellido, email, create_at) VALUES(4, 'Linus', 'Torvalds', 'linus.torvalds@gmail.com', '2018-01-03');
INSERT INTO clientes (region_id, nombre, apellido, email, create_at) VALUES(4, 'Rasmus', 'Lerdorf', 'rasmus.lerdorf@gmail.com', '2018-01-04');
INSERT INTO clientes (region_id, nombre, apellido, email, create_at) VALUES(4, 'Erich', 'Gamma', 'erich.gamma@gmail.com', '2018-02-01');
INSERT INTO clientes (region_id, nombre, apellido, email, create_at) VALUES(3, 'Richard', 'Helm', 'richard.helm@gmail.com', '2018-02-10');
INSERT INTO clientes (region_id, nombre, apellido, email, create_at) VALUES(3, 'Ralph', 'Johnson', 'ralph.johnson@gmail.com', '2018-02-18');
INSERT INTO clientes (region_id, nombre, apellido, email, create_at) VALUES(3, 'John', 'Vlissides', 'john.vlissides@gmail.com', '2018-02-28');
INSERT INTO clientes (region_id, nombre, apellido, email, create_at) VALUES(3, 'Dr. James', 'Gosling', 'james.gosling@gmail.com', '2018-03-03');
INSERT INTO clientes (region_id, nombre, apellido, email, create_at) VALUES(5, 'Magma', 'Lee', 'magma.lee@gmail.com', '2018-03-04');
INSERT INTO clientes (region_id, nombre, apellido, email, create_at) VALUES(6, 'Tornado', 'Roe', 'tornado.roe@gmail.com', '2018-03-05');
INSERT INTO clientes (region_id, nombre, apellido, email, create_at) VALUES(7, 'Jade', 'Doe', 'jane.doe@gmail.com', '2018-03-06');

insert into users (username, password, enabled, name, lastname, email) values ('dscifo', '$2a$10$q/oX4BSoLn49ReTUNgEyF.jcWkRYrgDqsj5toeh9UWjnYUvEvrosK', 1, 'Diego', 'Scifo', 'dscifo@mail.com');
insert into users (username, password, enabled, name, lastname, email) values ('admin', '$2a$10$w/kBSvFF10CWEFfwgu/GI.Bf29iR1d5PmnnSUr0VJinNXGFxpDJ0a', 1, 'John', 'Rambo', 'jrambo@mail.com');

insert into roles (name) values ('ROLE_USER');
insert into roles (name) values ('ROLE_ADMIN');

insert into users_authorities (user_id, role_id) values (1, 1);
insert into users_authorities (user_id, role_id) values (2, 2);
insert into users_authorities (user_id, role_id) values (2, 1);

insert into product (create_at, name, price) values (now(), 'Vodka', 350.0);
insert into product (create_at, name, price) values (now(), 'Agua', 50.0);
insert into product (create_at, name, price) values (now(), 'Vino', 100.0);
insert into product (create_at, name, price) values (now(), 'Jugo', 80.0);
insert into product (create_at, name, price) values (now(), 'Gaseosa', 150.0);
insert into product (create_at, name, price) values (now(), 'Fernet', 300.0);
insert into product (create_at, name, price) values (now(), 'Cerveza', 200.0);

insert into sale (description, comment, create_at, cliente_id) values ('El Santo', 'Comment 1', now(), 1);
insert into sale_item (sale_id, product_id, amount) values (1, 1, 2);
insert into sale_item (sale_id, product_id, amount) values (1, 6, 12);
insert into sale_item (sale_id, product_id, amount) values (1, 7, 24);

insert into sale (description, comment, create_at, cliente_id) values ('Nonqueen', 'Comment 2', now(), 1);
insert into sale_item (sale_id, product_id, amount) values (2, 2, 6);
insert into sale_item (sale_id, product_id, amount) values (2, 3, 12);
insert into sale_item (sale_id, product_id, amount) values (2, 4, 24);