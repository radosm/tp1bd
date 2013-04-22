drop table cronograma;
drop table vuelo;
drop table config_modelo;
drop table configuracion;
drop table dia;
drop table avion;
drop table modelo_avion;
drop table telefonos_aeropuerto;
drop table aeropuerto;
drop table pais;
drop table persona_reserva;
drop table persona_cuenta;
drop table reserva;
drop table clase;
drop table tarjeta;
drop table cuenta;

create table cuenta (
  userid varchar(8),
  nombre varchar,
  apellido varchar,
  clave varchar,
  email varchar,
  profesion varchar,
  telefono varchar,
  direccion text,
  direccion_entrega text
);

alter table cuenta add primary key (userid);

create table tarjeta (
  numero numeric(16),
  marca varchar, -- visa, diners, etc
  banco_emisor varchar, -- bbva
  vencimiento interval year to month,
  direccion_facturacion text,
  userid varchar(8)
);

alter table tarjeta add primary key (numero);
alter table tarjeta add foreign key (userid) references cuenta;

create table clase (
  codigo_clase char(3),
  descripcion text
);

alter table clase add primary key (codigo_clase);

create table reserva (
  codigo_reserva char(8),
  forma_de_pago varchar,
  userid varchar(8),
  codigo_clase char(3)
);

alter table reserva add primary key (codigo_reserva);
alter table reserva add foreign key (userid) references cuenta;
alter table reserva add foreign key (codigo_clase) references clase;

create table persona_cuenta (
  tipo_doc varchar(20),
  nro_doc numeric(20),
  nombre varchar,
  apellido varchar,
  fecha_nac date,
  nacionalidad varchar,
  userid varchar(8)
);

alter table persona_cuenta add primary key (tipo_doc,nro_doc);
alter table persona_cuenta add foreign key (userid) references cuenta;

create table persona_reserva (
  tipo_doc varchar(20),
  nro_doc numeric(20),
  nombre varchar,
  apellido varchar,
  fecha_nac date,
  nacionalidad varchar,
  codigo char(8)
);

alter table persona_reserva add primary key (tipo_doc,nro_doc);
alter table persona_reserva add foreign key (codigo_reserva) references reserva;

create table pais (
  codigo_pais char(2),
  nombre varchar
);

alter table pais add primary key (codigo_pais);

create table aeropuerto (
  codigo_aerop char(3),
  nombre varchar,
  tasa float,
  informacion_transporte text,
  codigo_pais char(2)
);

alter table aeropuerto add primary key (codigo_aerop);
alter table aeropuerto add foreign key (codigo_pais) references pais;

create table telefonos_aeropuerto (
  numero varchar,
  codigo_aerop char(3)
);

alter table telefonos_aeropuerto add primary key (codigo_aerop,numero);

create table modelo_avion (
  codigo_modelo varchar,
  descripcion text
);

alter table modelo_avion add primary key (codigo_modelo);

create table avion (
  codigo_avion varchar,
  anio_fabric interval year,
  millas integer,
  codigo_modelo varchar,
  codigo_pais char(2)
);

alter table avion add primary key (codigo_avion);
alter table avion add foreign key (codigo_modelo) references modelo_avion;
alter table avion add foreign key (codigo_pais) references pais;

create table dia (
  dia char(3)
);

alter table dia add primary key (dia);

create table configuracion (
  codigo_config integer,
  descripcion text
);

alter table configuracion add primary key (codigo_config);

create table config_modelo(
  codigo_modelo varchar,
  codigo_config integer
);

alter table config_modelo add primary key (codigo_modelo,codigo_config);
alter table config_modelo add foreign key (codigo_modelo) references modelo_avion;
alter table config_modelo add foreign key (codigo_config) references configuracion;

create table vuelo(
  numero_vuelo integer,
  hora_despegue interval hour to minute,
  duracion interval hour to minute,
  millas integer
);

alter table vuelo add primary key (numero_vuelo);

create table cronograma (
  numero_vuelo integer,
  dia char(3),
  codigo_config integer
);

alter table cronograma add primary key (numero_vuelo,dia,codigo_config);
alter table cronograma add foreign key (numero_vuelo) references vuelo;
alter table cronograma add foreign key (dia) references dia;
alter table cronograma add foreign key (codigo_config) references configuracion;
