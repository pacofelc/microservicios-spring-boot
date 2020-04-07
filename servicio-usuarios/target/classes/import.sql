-- Carga automática de registros al arrancar el servicio
insert into usuarios (user_name,password,enabled,nombre,apellidos,email) values ('andres','$2a$10$ANh2X/xgvf15bjb7Oa.ZzOPUloqgCYeiwux04ThAv1ix3geOEirmO',true,'Andrés','Guzmán','andres@gmail.com');
insert into usuarios (user_name,password,enabled,nombre,apellidos,email) values ('admin','$2a$10$.dMCE8kMDCacj0K4U2SLjOi3QXRpqP.aO/76uVNPkob9Z5c5TRAO6',true,'Admin','','admin@gmail.com');

insert into roles (id,nombre) values (1,'ROLE_USER');
insert into roles (id,nombre) values (2,'ROLE_ADMIN');

insert into usuarios_roles (usuario_id,rol_id) values (1,1);
insert into usuarios_roles (usuario_id,rol_id) values (2,2);
insert into usuarios_roles (usuario_id,rol_id) values (2,1);


insert into foo (name) values ('pepe');