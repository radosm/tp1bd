drop view if exists vw_paso_por;

--
-- reservas confirmadas, origen y destino
--
create or replace view vw_paso_por as
  select r.codigo_reserva,rv.fecha_viaje+v.hora_despegue salio,v.aeropuerto_ori,rv.fecha_viaje+v.hora_despegue+v.duracion llego,v.aeropuerto_dst
  from reserva r, reserva_viaje rv, vuelo v
  where rv.codigo_reserva=r.codigo_reserva
  and v.numero_vuelo=rv.numero_vuelo
  and r.estado='confirmado';

--
-- Personas que viajaron a todos los destinos en los últimos 5 años
--
select * from persona where (tipo_doc,nro_doc) in (
  select p.tipo_doc,p.nro_doc
  from vw_paso_por pp, persona_reserva pr, persona p, aeropuerto a
  where pp.llego between (current_date - interval '5 year') and current_date
    and pr.codigo_reserva=pp.codigo_reserva
    and p.tipo_doc=pr.tipo_doc
    and p.nro_doc=pr.nro_doc
    and a.codigo_aerop in (pp.aeropuerto_ori,pp.aeropuerto_dst)
  group by p.tipo_doc,p.nro_doc
  having count(distinct a.codigo_pais)=(select count(distinct a.codigo_pais)
                                        from vuelo v,aeropuerto a
                                        where a.codigo_aerop in (v.aeropuerto_ori,v.aeropuerto_dst))
);
