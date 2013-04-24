SELECT
 aeropuerto.codigo_aerop AS "Codigo Aeropuerto",
 COALESCE(periodo_col1, periodo_col2) AS "Periodo",
 COALESCE(col1.count, 0) AS "Subieron",
 COALESCE(col2.count, 0) AS "Bajaron" 
FROM aeropuerto
LEFT JOIN
 ( SELECT a.codigo_aerop AS codaero, periodo_col1 , count(*) AS "count"
  FROM  aeropuerto a,
   (SELECT
    vu.aeropuerto_ori AS origen,
    vu.aeropuerto_dst AS destino,
    to_char(vi.fecha_viaje, 'YYYY/Mon') AS periodo_col1
    FROM vuelo vu, viaje vi, persona_reserva pr, reserva r, reserva_viaje rv
    WHERE (
    vi.numero_vuelo = vu.numero_vuelo
    AND rv.fecha_viaje = vi.fecha_viaje
    AND rv.numero_vuelo = vi.numero_vuelo
    AND r.codigo_reserva = rv.codigo_reserva
    AND pr.codigo_reserva = r.codigo_reserva
    -- FILTRO?
    --AND rv.fecha_viaje > 'January 1, 2012' AND rv.fecha_viaje < 'February 28, 2014'
    )
    ) AS reporte_intermedio
   WHERE origen = a.codigo_aerop
   GROUP BY a.codigo_aerop, periodo_col1 ) col1
ON (aeropuerto.codigo_aerop = col1.codaero)
LEFT JOIN
 ( SELECT a.codigo_aerop AS codaero, periodo_col2 , count(*) AS "count"
  FROM  aeropuerto a,
   (SELECT
    vu.aeropuerto_ori AS origen,
    vu.aeropuerto_dst AS destino,
    to_char(vi.fecha_viaje + vu.hora_despegue + vu.duracion, 'YYYY/Mon') AS periodo_col2
    FROM vuelo vu, viaje vi, persona_reserva pr, reserva r, reserva_viaje rv
    WHERE (
    vi.numero_vuelo = vu.numero_vuelo
    AND rv.fecha_viaje = vi.fecha_viaje
    AND rv.numero_vuelo = vi.numero_vuelo
    AND r.codigo_reserva = rv.codigo_reserva
    AND pr.codigo_reserva = r.codigo_reserva
    --AND rv.fecha_viaje > 'January 1, 2012' AND rv.fecha_viaje < 'February 28, 2014'
    )
    ) AS reporte_intermedio
   WHERE destino = a.codigo_aerop
   GROUP BY a.codigo_aerop, periodo_col2 ) col2
ON (aeropuerto.codigo_aerop = col2.codaero)
ORDER BY "Subieron" DESC;
