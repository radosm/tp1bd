--
-- Drops
--
drop function lista_vuelo(integer, integer, integer);
drop function lista_vuelos(vuelo in integer,orden_desde in integer, orden_hasta integer);
drop function una_conexion(origen aeropuerto.aer_codigo%type,destino aeropuerto.aer_codigo%type);
drop function sin_conexiones(origen aeropuerto.aer_codigo%type,destino aeropuerto.aer_codigo%type);
drop trigger check_escala on pasa_por;
drop function check_escala();

drop table conexion;
drop table pasa_por;
drop table vuelo;
drop table aeropuerto;


--
-- Tablas y constraints
--

create table aeropuerto (aer_codigo char(3), aer_nombre varchar);
alter table aeropuerto add primary key (aer_codigo);

create table vuelo (vue_numero int,hora_salida time);
alter table vuelo add primary key (vue_numero);

create table pasa_por(vue_numero int,orden int,aer_codigo char(3),tiempo_de_vuelo interval hour to minute ,tiempo_de_escala interval hour to minute );
alter table pasa_por add primary key (vue_numero,orden);
alter table pasa_por add foreign key (vue_numero) references vuelo;
alter table pasa_por add foreign key (aer_codigo) references aeropuerto;

create table conexion (vue_numero_llega int,orden_llega int,vue_numero_sale int,orden_sale int);
alter table conexion add primary key (vue_numero_llega,orden_llega,vue_numero_sale,orden_sale);
alter table conexion add foreign key (vue_numero_llega,orden_llega) references pasa_por(vue_numero,orden);
alter table conexion add foreign key (vue_numero_sale,orden_sale) references pasa_por(vue_numero,orden);


--
-- Funciones plpgsql
--

--
-- Muestra un vuelo entre dos escalas
--
create function lista_vuelos(vuelo in integer,orden_desde in integer, orden_hasta integer)
returns setof pasa_por as
$$
begin
  return query select * from pasa_por where vue_numero=$1 and orden between $2 and $3;
end;
$$ language plpgsql;


--
-- Vuelos sin conexiones
--
create function sin_conexiones(origen aeropuerto.aer_codigo%type,destino aeropuerto.aer_codigo%type)
returns setof pasa_por as
$$
declare
  v cursor (origen char(3),destino char(3)) for 
    select vue_numero from pasa_por pp 
    where aer_codigo=origen -- Sale de donde quiero
      and exists (select * from pasa_por           -- Llega hasta donde quiero
                   where vue_numero=pp.vue_numero 
                     and aer_codigo=destino
                     and orden > pp.orden);
  orden_ori int;
  orden_dst int;
begin
  for vue in v(origen,destino) loop
    select orden into orden_ori from pasa_por 
    where vue_numero=vue.vue_numero and aer_codigo=origen;
  
    select orden into orden_dst from pasa_por 
    where vue_numero=vue.vue_numero and aer_codigo=destino;
  
    return query select * from lista_vuelos(vue.vue_numero,orden_ori,orden_dst);
  end loop;
  return;
end;
$$ language plpgsql;


--
-- Vuelos con una conexion
--
create function una_conexion(origen aeropuerto.aer_codigo%type,destino aeropuerto.aer_codigo%type)
returns setof pasa_por as
$$
declare
  cx cursor (origen char(3),destino char(3)) for 
    select * from conexion cx 
     where vue_numero_llega in (select vue_numero from pasa_por pp where aer_codigo=origen)
       and exists (select * from pasa_por 
                    where vue_numero=cx.vue_numero_sale 
                      and aer_codigo=destino and orden > cx.orden_sale);
  desde_llega int;
  hasta_sale int;
begin
  for conex in cx(origen,destino) loop
    select orden into desde_llega from pasa_por 
    where vue_numero=conex.vue_numero_llega and aer_codigo=origen;

    select orden into hasta_sale from pasa_por 
    where vue_numero=conex.vue_numero_sale and aer_codigo=destino;
 
    -- No muestra conexiones ridiculas
    if desde_llega < conex.orden_llega and conex.orden_sale < hasta_sale
    then
      return query 
        select * from lista_vuelos(conex.vue_numero_llega,desde_llega,conex.orden_llega);
      return query 
        select * from lista_vuelos(conex.vue_numero_sale,conex.orden_sale,hasta_sale);
    end if;
  end loop;
  return;
end;
$$ language plpgsql;


--
-- Triggers
--
create function check_escala() returns trigger as 
$$
begin
  if new.tiempo_de_vuelo < '00:00' then
    raise exception 'Tiempo de vuelo no puede ser negativo.';
  end if;
  if new.tiempo_de_escala < '00:00' then
    raise exception 'Tiempo de escala no puede ser negativo.';
  end if;
  return new;
 end;
$$ language plpgsql;

create trigger check_escala before insert or update on pasa_por
    for each row execute procedure check_escala();


--
-- Datos
--

insert into aeropuerto values ('EZE','Aeropuerto Internacional de Ezeiza');
insert into aeropuerto values ('AEP','Aeroparque CABA');
insert into aeropuerto values ('MDQ','Aeropuerto de Mar del Plata');
insert into aeropuerto values ('MZA','Aeropuerto de Mendoza');
insert into aeropuerto values ('CBA','Aeropuerto de Cordoba');
insert into aeropuerto values ('SFE','Aeropuerto de Sta Fe');
insert into aeropuerto values ('ROS','Aeropuerto de Rosario');

insert into vuelo values (1109,'10:00');
insert into vuelo values (1110,'10:00');
insert into vuelo values (1111,'10:00');

insert into pasa_por values (1109,1,'AEP',null,'01:00');
insert into pasa_por values (1109,2,'ROS','01:00','01:00');
insert into pasa_por values (1109,3,'MDQ','01:00','01:00');
insert into pasa_por values (1109,4,'CBA','01:00',null);

insert into pasa_por values (1110,1,'AEP',null,'01:00');
insert into pasa_por values (1110,2,'MDQ','01:00','01:00');
insert into pasa_por values (1110,3,'CBA','02:00',null);

insert into pasa_por values (1111,1,'EZE',null,'01:00');
insert into pasa_por values (1111,2,'MDQ','01:00','01:00');
insert into pasa_por values (1111,3,'MZA','02:00',null);

insert into conexion values (1109,3,1111,2);
insert into conexion values (1110,2,1111,2);
