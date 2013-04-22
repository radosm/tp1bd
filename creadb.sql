drop table if exists precio;
drop table if exists asiento;
drop table if exists tripulacion;
drop table if exists tripulante;
drop table if exists reserva_viaje;
drop table if exists viaje;
drop table if exists cronograma;
drop table if exists vuelo;
drop table if exists config_modelo;
drop table if exists configuracion;
drop table if exists dia;
drop table if exists avion;
drop table if exists modelo_avion;
drop table if exists telefonos_aeropuerto;
drop table if exists aeropuerto;
drop table if exists pais;
drop table if exists persona_reserva;
drop table if exists persona;
drop table if exists reserva;
drop table if exists clase;
drop table if exists tarjeta;
drop table if exists cuenta;

create table cuenta (
  userid varchar(8) not null,
  nombre varchar not null,
  apellido varchar not null,
  clave varchar not null,
  email varchar not null,
  profesion varchar not null,
  telefono varchar not null,
  direccion text not null
);

alter table cuenta add primary key (userid);

create table tarjeta (
  numero numeric(16) not null,
  marca varchar not null, -- visa, diners, etc
  banco_emisor varchar not null, -- bbva
  validez interval year to month not null,
  direccion_facturacion text not null,
  userid varchar(8) not null
);

alter table tarjeta add primary key (numero);
alter table tarjeta add foreign key (userid) references cuenta;

create table clase (
  codigo_clase char(3) not null,
  descripcion text not null
);

alter table clase add primary key (codigo_clase);

create table reserva (
  codigo_reserva char(8) not null,
  fecha date not null,
  estado varchar not null,
  forma_de_pago varchar not null,
  userid varchar(8) not null,
  direccion_entrega text not null,
  codigo_clase char(3) not null
);

alter table reserva add primary key (codigo_reserva);
alter table reserva add foreign key (userid) references cuenta;
alter table reserva add foreign key (codigo_clase) references clase;

create table persona (
  tipo_doc integer not null,
  nro_doc numeric(20) not null,
  nombre varchar not null,
  apellido varchar not null,
  fecha_nac date not null,
  nacionalidad varchar not null
);

alter table persona add primary key (tipo_doc,nro_doc);

create table persona_reserva (
  codigo_reserva char(8) not null,
  tipo_doc integer not null,
  nro_doc numeric(20) not null
);

alter table persona_reserva add primary key (codigo_reserva,tipo_doc,nro_doc);
alter table persona_reserva add foreign key (codigo_reserva) references reserva;
alter table persona_reserva add foreign key (tipo_doc,nro_doc) references persona;

create table pais (
  codigo_pais char(2) not null,
  nombre varchar not null
);

alter table pais add primary key (codigo_pais);

create table aeropuerto (
  codigo_aerop char(3) not null,
  nombre varchar not null,
  tasa float not null,
  informacion_transporte text not null,
  codigo_pais char(2) not null
);

alter table aeropuerto add primary key (codigo_aerop);
alter table aeropuerto add foreign key (codigo_pais) references pais;

create table telefonos_aeropuerto (
  codigo_aerop char(3) not null,
  numero varchar not null
);

alter table telefonos_aeropuerto add primary key (codigo_aerop,numero);
alter table telefonos_aeropuerto add foreign key (codigo_aerop) references aeropuerto;

create table modelo_avion (
  codigo_modelo varchar not null,
  descripcion text not null
);

alter table modelo_avion add primary key (codigo_modelo);

create table avion (
  codigo_avion varchar not null,
  anio_fabric interval year not null,
  codigo_modelo varchar not null,
  codigo_pais char(2) not null
);

alter table avion add primary key (codigo_avion);
alter table avion add foreign key (codigo_modelo) references modelo_avion;
alter table avion add foreign key (codigo_pais) references pais;

create table dia (
  dia char(3) not null
);

alter table dia add primary key (dia);

create table configuracion (
  codigo_config integer not null,
  descripcion text not null
);

alter table configuracion add primary key (codigo_config);

create table config_modelo(
  codigo_modelo varchar not null,
  codigo_config integer not null
);

alter table config_modelo add primary key (codigo_modelo,codigo_config);
alter table config_modelo add foreign key (codigo_modelo) references modelo_avion;
alter table config_modelo add foreign key (codigo_config) references configuracion;

create table vuelo(
  numero_vuelo integer not null,
  aeropuerto_ori char(3) not null,
  aeropuerto_dst char(3) not null,
  hora_despegue time not null,
  duracion interval hour to minute not null,
  millas integer not null
);

alter table vuelo add primary key (numero_vuelo);
alter table vuelo add foreign key (aeropuerto_ori) references aeropuerto (codigo_aerop);
alter table vuelo add foreign key (aeropuerto_dst) references aeropuerto (codigo_aerop);

create table cronograma (
  numero_vuelo integer not null,
  dia char(3) not null,
  codigo_config integer not null
);

alter table cronograma add primary key (numero_vuelo,dia);
alter table cronograma add foreign key (numero_vuelo) references vuelo;
alter table cronograma add foreign key (dia) references dia;
alter table cronograma add foreign key (codigo_config) references configuracion;

create table viaje (
  numero_vuelo integer not null,
  fecha_viaje date not null
);

alter table viaje add primary key (numero_vuelo,fecha_viaje);
alter table viaje add foreign key (numero_vuelo) references vuelo;

create table reserva_viaje (
  codigo_reserva char(8) not null,
  numero_vuelo integer not null,
  fecha_viaje date not null,
  orden integer not null
);

alter table reserva_viaje add primary key (codigo_reserva,numero_vuelo,fecha_viaje,orden);
alter table reserva_viaje add foreign key (numero_vuelo) references vuelo;
alter table reserva_viaje add foreign key (codigo_reserva) references reserva;
alter table reserva_viaje add foreign key (numero_vuelo,fecha_viaje) references viaje;

create table tripulante (
  codigo_tripulante integer not null,
  nombre varchar not null,
  apellido varchar not null,
  cargo varchar not null
);

alter table tripulante add primary key (codigo_tripulante);

create table tripulacion (
  numero_vuelo integer not null,
  fecha_viaje date not null,
  codigo_avion varchar not null,
  codigo_tripulante integer not null
);

alter table tripulacion add primary key (numero_vuelo,fecha_viaje,codigo_tripulante);
alter table tripulacion add foreign key (numero_vuelo,fecha_viaje) references viaje;
alter table tripulacion add foreign key (codigo_avion) references avion;
alter table tripulacion add foreign key (codigo_tripulante) references tripulante;

create table asiento (
  codigo_config integer not null,
  codigo_clase char(3) not null,
  cantidad integer not null
);

alter table asiento add primary key (codigo_config,codigo_clase);
alter table asiento add foreign key (codigo_config) references configuracion;
alter table asiento add foreign key (codigo_clase) references clase;

create table precio (
  numero_vuelo integer not null,
  codigo_clase char(3) not null,
  tarifa float not null
);

alter table precio add primary key (numero_vuelo,codigo_clase);
alter table precio add foreign key (numero_vuelo) references vuelo;
alter table precio add foreign key (codigo_clase) references clase;
