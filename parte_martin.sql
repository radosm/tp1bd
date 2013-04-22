select 
 r.userid 
,r1.codigo_reserva
,r1.fecha_viaje
,v1.aeropuerto_ori
,v2.aeropuerto_dst
,t.total
from
 reserva r
,reserva_viaje r1
,reserva_viaje r2
,vuelo v1
,vuelo v2
,(select r.codigo_reserva,sum(p.tarifa) total
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
  and t.codigo_reserva=r1.codigo_reserva
;

