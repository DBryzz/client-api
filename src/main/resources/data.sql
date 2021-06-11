DROP table users_roles;
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



-- 1 - SELLER BUYER USERS - bryzz passwd bryzz123 gmail
/*INSERT INTO users(user_id, username, user_nid)
VALUES('228', 'bryzz', '112276208');*/
