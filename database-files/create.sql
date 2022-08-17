--create schema sistema_de_reembolso

create table fotos (
                       id_fotos bigint not null,
                       nome text not null,
                       tipo text not null,
                       data bytea not null,
                       primary key(id_fotos)

);

create sequence seq_fotos
    increment 1
start 1;

create table usuario (
                         id_usuario bigint not null,
                         nome text not null,
                         senha text not null,
                         email text not null unique,
                         valor_total double precision,
                         id_fotos bigint,
                         status boolean not null,
                         primary key(id_usuario),
                         constraint fk_usuario_fotos
                             foreign key (id_fotos)
                                 references fotos(id_fotos)
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

create table anexos (
                        id_anexos bigint not null,
                        nome text not null,
                        tipo text not null,
                        data bytea not null,
                        primary key(id_anexos)

);

create sequence seq_anexos
    increment 1
start 1;

create table reembolso(
                          id_reembolso bigint not null,
                          titulo text not null,
                          valor double precision not null,
                          status integer not null,
                          data_entrada timestamp not null,
                          id_anexos bigint,
                          id_usuario bigint not null,
                          primary key (id_reembolso),
                          constraint fk_reembolso_id_usuario
                              foreign key (id_usuario)
                                  references usuario(id_usuario),
                          constraint fk_reembolso_anexos
                              foreign key (id_anexos)
                                  references anexos(id_anexos)
);

create sequence seq_reembolso
    increment 1
start 1;

insert into sistema_de_reembolso.roles(id_roles, nome) values(nextval('seq_roles'), 'ROLE_ADMIN');
insert into sistema_de_reembolso.roles(id_roles, nome) values(nextval('seq_roles'), 'ROLE_FINANCEIRO');
insert into sistema_de_reembolso.roles(id_roles, nome) values(nextval('seq_roles'), 'ROLE_GESTOR');
insert into sistema_de_reembolso.roles(id_roles, nome) values(nextval('seq_roles'), 'ROLE_COLABORADOR');