/*DROP table users_roles;
DROP table app_deployer;
DROP table app_users;
DROP table users;
DROP table roles;
CREATE table roles(
    role_id integer,
    role_name varchar(255),
    role_description varchar(255)
);

INSERT INTO roles(role_id, role_name, role_description) VALUES(1, 'ROLE_USER', 'Normal user can only view and download deployed applications');
INSERT INTO roles(role_id, role_name, role_description) VALUES(2, 'ROLE_DEPLOYER', 'Deployer can containerize and deploy applications');
INSERT INTO roles(role_id, role_name, role_description) VALUES(3, 'ROLE_ADMIN', 'Admin of the system');
*/

/*
-- 1 - SELLER BUYER USERS - bryzz passwd bryzz123 gmail
insert into users(user_id, email, first_name, last_name, passwd, username)
values(1, 'domoubrice@gmail.com', 'Domou', 'Brice', 'bryzz123', 'bryzz' );
insert into users(user_id, email, first_name, last_name, passwd, username)
values(2, 'domou.brice@yahoo.com', 'Namou', 'Armel', 'dbryzz123', 'dbryzz' );
insert into users(user_id, email, first_name, last_name, passwd, username)
values(3, 'cat@meow.to', 'Cat', 'Meow', 'cat123', 'cat' );

insert into users_roles(user_id, role_id) values (1, 1);
insert into users_roles(user_id, role_id) values (1, 2);
insert into users_roles(user_id, role_id) values (1, 3);
insert into users_roles(user_id, role_id) values (2, 1);
insert into users_roles(user_id, role_id) values (2, 2);
insert into users_roles(user_id, role_id) values (3, 1);
*/