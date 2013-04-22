--SELECT
--	codigo_aerop AS "Código de Aeropuerto",
--	to_char(viaje.fecha_viaje, 'YYYY/Mon') AS "Año/Mes"
--	--COUNT(vuelo.origen) AS NumSubieron ,
--	--COUNT(vuelo.destino) AS NumBajaron
--FROM aeropuerto a, vuelo vu, viaje vi, reserva_viaje r_v, reserva r
--WHERE 
--	( vu.origen = a.codigo_aerop OR vu.destino = codigo_AND vi.numero_vuelo = vu.numero_vuelo AND r_v.numero_vuelo = viaje.id_viaje AND persona_reserva.es_para = reserva.codigo_reserva)
--GROUP BY codigo_aerop
--ORDER BY


SELECT a.codigo_aerop, vu.origen, vu.destino
FROM aeropuerto a, vuelo vu, viaje vi, reserva_viaje r_v, reserva r, persona_reserva
WHERE 
	( (vu.origen = a.codigo_aerop OR vu.destino = a.codigo_aerop)
		AND vi.numero_vuelo = vu.numero_vuelo
		AND reserva_viaje.fecha_viaje = viaje.fecha_viaje
		AND reserva_viaje.numero_vuelo = viaje.numero_vuelo
		AND reserva.codigo_reserva = reserva_viaje.codigo_reserva
		AND persona_reserva.codigo_reserva = reserva.codigo_reserva
	)
GROUP BY codigo_aerop
ORDER BY
