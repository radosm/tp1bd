DROP VIEW IF EXISTS vw_paso_por;

--
-- reservas confirmadas, origen y destino
--
CREATE or replace VIEW vw_paso_por AS
  SELECT r.codigo_reserva,rv.fecha_viaje+v.hora_despegue salio,v.aeropuerto_ori,rv.fecha_viaje+v.hora_despegue+v.duracion llego,v.aeropuerto_dst
  FROM reserva r, reserva_viaje rv, vuelo v
  WHERE rv.codigo_reserva=r.codigo_reserva
  AND v.numero_vuelo=rv.numero_vuelo
  AND r.estado='confirmado';

--
-- Personas que viajaron a todos los destinos en los últimos 5 años
--
SELECT * FROM persona WHERE (tipo_doc,nro_doc) IN (
  SELECT p.tipo_doc,p.nro_doc
  FROM vw_paso_por pp, persona_reserva pr, persona p, aeropuerto a
  WHERE pp.llego between (CURRENT_DATE - INTERVAL '5 year') AND CURRENT_DATE
    AND pr.codigo_reserva=pp.codigo_reserva
    AND p.tipo_doc=pr.tipo_doc
    AND p.nro_doc=pr.nro_doc
    AND a.codigo_aerop IN (pp.aeropuerto_ori,pp.aeropuerto_dst)
  GROUP by p.tipo_doc,p.nro_doc
  HAVING count(DISTINCT a.codigo_pais)=(SELECT count(DISTINCT a.codigo_pais)
                                        FROM vuelo v,aeropuerto a
                                        WHERE a.codigo_aerop IN (v.aeropuerto_ori,v.aeropuerto_dst))
);
