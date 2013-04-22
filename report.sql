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
