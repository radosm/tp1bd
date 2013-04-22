--
-- La siguiente query lista todos los viajes que realizó cada persona 
--

SELECT
	vu.aeropuerto_ori AS "Origen",
	vu.aeropuerto_dst AS "Destino",
	to_char(vi.fecha_viaje, 'YYYY/Mon') AS "Período",
	pr.nombre, pr.apellido
FROM vuelo vu, viaje vi, persona_reserva pr, reserva r, reserva_viaje rv
WHERE (
	vi.numero_vuelo = vu.numero_vuelo
	AND rv.fecha_viaje = vi.fecha_viaje
	AND rv.numero_vuelo = vi.numero_vuelo
	AND r.codigo_reserva = rv.codigo_reserva
	AND pr.codigo_reserva = r.codigo_reserva
	);



-- 
-- Primer intento:
-- 		Lista aeropuerto de origen (cod)  y cantidad de personas que subieron ó que bajaron (solo una columna)
--		Distingue período de tiempo
--		Ordena por cantidad que subieron (ó bajaron)
--
 

SELECT a.codigo_aerop, "Período" , count("Origen") AS "Subieron"
FROM  aeropuerto a,
	(SELECT
	vu.aeropuerto_ori AS "Origen",
	vu.aeropuerto_dst AS "Destino",
	to_char(vi.fecha_viaje, 'YYYY/Mon') AS "Período"
	FROM vuelo vu, viaje vi, persona_reserva pr, reserva r, reserva_viaje rv
	WHERE (
	vi.numero_vuelo = vu.numero_vuelo
	AND rv.fecha_viaje = vi.fecha_viaje
	AND rv.numero_vuelo = vi.numero_vuelo
	AND r.codigo_reserva = rv.codigo_reserva
	AND pr.codigo_reserva = r.codigo_reserva
	)
	) AS reporte_intermedio
WHERE "Origen" = a.codigo_aerop
GROUP BY a.codigo_aerop, "Período"
ORDER BY "Subieron" DESC;


-- 
-- Segundo intento: no es muy elegante, pero funciona
-- 
SELECT aeropuerto.codigo_aerop, periodo_col1, col1.count, col2.count
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
    to_char(vi.fecha_viaje, 'YYYY/Mon') AS periodo_col2
    FROM vuelo vu, viaje vi, persona_reserva pr, reserva r, reserva_viaje rv
    WHERE (
    vi.numero_vuelo = vu.numero_vuelo
    AND rv.fecha_viaje = vi.fecha_viaje
    AND rv.numero_vuelo = vi.numero_vuelo
    AND r.codigo_reserva = rv.codigo_reserva
    AND pr.codigo_reserva = r.codigo_reserva
    )
    ) AS reporte_intermedio
   WHERE destino = a.codigo_aerop
   GROUP BY a.codigo_aerop, periodo_col2 ) col2
ON (aeropuerto.codigo_aerop = col2.codaero);
