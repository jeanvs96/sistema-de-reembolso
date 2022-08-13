--create schema sistema_de_reembolso

create table usuario (
	id_usuario bigint not null,
	nome text not null,
	senha text not null,
	email text not null unique,
	foto bytea,
	status boolean not null,
	primary key(id_usuario)
);

create sequence seq_usuario
    increment 1
start 1;

create table roles(
	id_roles bigint not null,
	nome text not null,
	primary key(id_roles)
);

create sequence seq_roles
    increment 1
start 1;

create sequence seq_usuario_roles
    increment 1
start 1;

create table usuario_roles(
	id_usuario_roles bigint not null default nextval('sistema_de_reembolso.seq_usuario_roles'),
	id_usuario bigint not null,
	id_roles bigint not null,
	primary key(id_usuario_roles),
	constraint fk_usuario_roles_id_usuario
		foreign key (id_usuario)
		references usuario(id_usuario),
	constraint fk_usuario_roles_id_roles
		foreign key (id_roles)
		references roles(id_roles)
);


create table reembolso(
	id_reembolso bigint not null,
	titulo text not null,
	valor double precision not null,
	status text not null, 
	data_entrada timestamp not null,
	anexo bytea not null,
	id_usuario bigint not null,
	primary key (id_reembolso),
	constraint fk_usuario_roles_id_usuario
		foreign key (id_usuario)
		references usuario(id_usuario)
);

create sequence seq_reembolso
    increment 1
start 1;
