drop view if exists vw_paso_por;
drop trigger if exists check_reserva on reserva_viaje;
drop function if exists check_reserva();
drop function if exists cancelar_reservas_economicas(date);
drop view if exists vw_datos_reserva;
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
drop table if exists persona_reserva;
drop table if exists persona;
drop table if exists pais;
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
  cantidad_personas integer not null,
  estado varchar not null,
  forma_de_pago varchar not null,
  userid varchar(8) not null,
  direccion_entrega text not null,
  codigo_clase char(3) not null
);

alter table reserva add primary key (codigo_reserva);
alter table reserva add foreign key (userid) references cuenta;
alter table reserva add foreign key (codigo_clase) references clase;
alter table reserva add constraint check_estado check (estado in ('confirmado','pendiente','cancelado'));
alter table reserva add constraint check_fpago check (forma_de_pago in ('efectivo','tarjeta','debito','transferencia','cheque'));
alter table reserva add constraint check_cpers check (cantidad_personas > 0);

create table pais (
  codigo_pais char(2) not null,
  nombre varchar not null
);

alter table pais add primary key (codigo_pais);

create table persona (
  tipo_doc integer not null,
  nro_doc numeric(20) not null,
  nombre varchar not null,
  apellido varchar not null,
  fecha_nac date not null,
  codigo_pais char(2) not null
);

alter table persona add primary key (tipo_doc,nro_doc);
alter table persona add foreign key (codigo_pais) references pais;

create table persona_reserva (
  codigo_reserva char(8) not null,
  tipo_doc integer not null,
  nro_doc numeric(20) not null
);

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


--
-- Para que el código quede más comprensible se usa esta vista
-- cuya salida es del tipo:
--
-- userid | codigo_reserva |   estado   | fecha_sale | fecha_llega | aeropuerto_ori | aeropuerto_dst | precio | cantidad | total 
----------+----------------+------------+------------+-------------+----------------+----------------+--------+----------+-------
-- C0001  | R0000001       | confirmado | 2013-01-17 | 2013-01-17  | 001            | 002            |    100 |        3 |   300
-- C0001  | R0000002       | confirmado | 2013-01-17 | 2013-01-17  | 001            | 002            |    200 |        3 |   600
-- C0002  | R0000003       | confirmado | 2013-01-17 | 2013-01-31  | 001            | 005            |   1800 |        4 |  7200
-- C0002  | R0000004       | confirmado | 2013-02-18 | 2013-02-18  | 005            | 009            |   1500 |        4 |  6000
-- C0002  | R0000005       | confirmado | 2013-02-18 | 2013-02-18  | 005            | 009            |   1500 |        1 |  1500
--

create or replace view vw_datos_reserva as
               select 
                r.userid , r.codigo_reserva, r.estado, r1.fecha_viaje fecha_sale 
               ,r2.fecha_viaje fecha_llega, v1.aeropuerto_ori, v2.aeropuerto_dst
               ,t.precio, r.cantidad_personas cantidad, t.precio*r.cantidad_personas total
               from reserva r ,reserva_viaje r1 ,reserva_viaje r2 ,vuelo v1 ,vuelo v2 
               ,(select r.codigo_reserva,sum(p.tarifa) precio
                   from reserva r,reserva_viaje rv, precio p
                  where r.codigo_reserva=rv.codigo_reserva
                    and p.numero_vuelo=rv.numero_vuelo
                    and p.codigo_clase=r.codigo_clase
                 group by r.codigo_reserva) t
               where r.codigo_reserva=r1.codigo_reserva 
                 and r1.codigo_reserva=r2.codigo_reserva
                 and v1.numero_vuelo=r1.numero_vuelo
                 and v2.numero_vuelo=r2.numero_vuelo
                 and (r1.codigo_reserva,r1.orden,r2.orden) in (select codigo_reserva,min(orden),max(orden)
                                                               from reserva_viaje group by codigo_reserva)
                 and t.codigo_reserva=r.codigo_reserva;


create function check_reserva() returns trigger as
$$
declare
  v_count int;
  v_nr vw_datos_reserva%rowtype;  -- Reserva que se está dando de alta
begin

  select * from vw_datos_reserva into v_nr where codigo_reserva=new.codigo_reserva;

  --
  -- Verifica mas de dos en la misma fecha/aerop de salida y fecha/aerop de llegada
  --
  select count(*) into v_count from vw_datos_reserva
  where userid=v_nr.userid and estado='pendiente'
    and (fecha_sale,fecha_llega,aeropuerto_ori,aeropuerto_dst) 
                                =
        (v_nr.fecha_sale ,v_nr.fecha_llega ,v_nr.aeropuerto_ori ,v_nr.aeropuerto_dst);

  if v_count >2 then
    raise exception 'No se permiten más de dos reservas para la misma fecha/aerop de salida y fecha/aerop de llegada!';
  end if;
  
  -- Solo una si estamos a menos de 7 días de la fecha de salida
  if v_count = 2 and (v_nr.fecha_sale >= current_date) and (v_nr.fecha_sale - current_date) < 7 days then
    raise exception 'No se permite más de una reserva para la misma fecha/aerop de salida y fecha/aerop de llegada cuando faltan menos de 7 dias para la fecha de salida!';
  end if;

  -- 
  -- Verifica solapamiento
  --
  select count(*) into v_count from vw_datos_reserva
  where userid=v_nr.userid and estado='pendiente'
    and (fecha_sale,fecha_llega,aeropuerto_ori,aeropuerto_dst) 
                                !=
        (v_nr.fecha_sale ,v_nr.fecha_llega ,v_nr.aeropuerto_ori ,v_nr.aeropuerto_dst)
    and (
           (v_nr.fecha_sale between fecha_sale and fecha_llega)
        or 
           (fecha_llega between v_nr.fecha_sale and v_nr.fecha_llega)
        ); 

  if v_count >0 then
    raise exception 'Hay solapamiento de reservas!';
  end if;

  return new;
end;
$$ language plpgsql;

create constraint trigger check_reserva after insert or update on reserva_viaje
    deferrable initially deferred
    for each row execute procedure check_reserva();


--
-- Cancela reservas más económicas cuando coinciden fecha de salida y llegada y aeropuertos origen y destino
-- y la fecha de salida está dentro de los 7 días desde el parámetro "p_dia"
--
create or replace function cancelar_reservas_economicas(p_dia in date) returns void AS 
$$
begin
  update reserva set estado='cancelado' where codigo_reserva in (
    select dr.codigo_reserva from vw_datos_reserva dr
    where dr.estado='pendiente'
      and dr.fecha_sale >= p_dia
      and (dr.fecha_sale - p_dia) < 7
      and exists (
            select * from vw_datos_reserva 
            where estado='pendiente'
              and fecha_sale >= p_dia
              and (fecha_sale - p_dia) < 7
              and codigo_reserva!=dr.codigo_reserva
              and (userid,fecha_sale,aeropuerto_ori,fecha_llega,aeropuerto_dst)
                                          =
                  (dr.userid,dr.fecha_sale,dr.aeropuerto_ori,dr.fecha_llega,dr.aeropuerto_dst)
              and total>=dr.total)
  );

  return;
end;
$$ language plpgsql;


