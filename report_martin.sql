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
