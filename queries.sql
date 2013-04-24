drop view if exists vw_paso_por;

--
-- Reservas confirmadas, origen y destino, se usa esta vista para mayor legibilidad de los queries
--
-- Formato:
--
-- codigo_reserva |        salio        | aeropuerto_ori |        llego        | aeropuerto_dst 
------------------+---------------------+----------------+---------------------+----------------
-- R0000001       | 2013-01-17 10:10:00 | 001            | 2013-01-17 13:10:00 | 002
-- R0000002       | 2013-01-17 10:10:00 | 001            | 2013-01-17 13:10:00 | 002
-- R0000003       | 2013-01-17 18:10:00 | 001            | 2013-01-17 21:10:00 | 002
-- R0000003       | 2013-01-31 01:10:00 | 002            | 2013-01-31 08:10:00 | 005
-- R0000004       | 2013-02-18 10:10:00 | 005            | 2013-02-18 14:10:00 | 009
-- R0000005       | 2013-02-18 10:10:00 | 005            | 2013-02-18 14:10:00 | 009
--
create or replace view vw_paso_por as
  select r.codigo_reserva,rv.fecha_viaje+v.hora_despegue salio,v.aeropuerto_ori,rv.fecha_viaje+v.hora_despegue+v.duracion llego,v.aeropuerto_dst
  from reserva r, reserva_viaje rv, vuelo v
  where rv.codigo_reserva=r.codigo_reserva
  and v.numero_vuelo=rv.numero_vuelo
  and r.estado='confirmado';


--
-- Personas que subieron/bajaron de cada aeropuerto
--

select aeropuerto, to_char(fecha, 'YYYY/Mon') periodo, sum(subio) subieron, sum(bajo) bajaron 
from (
  select pp.aeropuerto_ori aeropuerto, pp.salio fecha, cantidad_personas subio, 0 bajo
  from vw_paso_por pp, (select codigo_reserva,count(*) cantidad_personas from persona_reserva group by codigo_reserva) pr
  where pr.codigo_reserva=pp.codigo_reserva
  union all
  select pp.aeropuerto_dst, pp.llego, 0, cantidad_personas
  from vw_paso_por pp, (select codigo_reserva,count(*) cantidad_personas from persona_reserva group by codigo_reserva) pr
  where pr.codigo_reserva=pp.codigo_reserva
) datos
where fecha between 'January 1, 2012' and 'January 31, 2014'
group by aeropuerto,to_char(fecha, 'YYYY/Mon')
order by sum(subio)+sum(bajo) desc, sum(subio) desc;


--
-- Personas que viajaron a todos los destinos en los últimos 5 años
--
select * FROM persona WHERE (tipo_doc,nro_doc) IN (
  select p.tipo_doc,p.nro_doc
  from vw_paso_por pp, persona_reserva pr, persona p, aeropuerto a
  where pp.llego between (current_date - interval '5 year') and current_date
    and pr.codigo_reserva=pp.codigo_reserva
    and p.tipo_doc=pr.tipo_doc
    and p.nro_doc=pr.nro_doc
    and a.codigo_aerop IN (pp.aeropuerto_ori,pp.aeropuerto_dst)
  group by p.tipo_doc,p.nro_doc
  having count(distinct a.codigo_pais)=(select count(distinct a.codigo_pais)
                                        from vuelo v,aeropuerto a
                                        where a.codigo_aerop in (v.aeropuerto_ori,v.aeropuerto_dst))
);
