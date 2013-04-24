-- procedimiento --
CREATE OR REPLACE FUNCTION cancelar_econom() RETURNS text AS $$
BEGIN
UPDATE reserva r SET estado='cancelada' FROM viaje v, reserva_viaje rv
	WHERE EXISTS 
	(SELECT * FROM reserva rr 
		INNER JOIN reserva_viaje rrvv ON rr.codigo_reserva=rrvv.codigo_reserva
		INNER JOIN viaje vv ON vv.numero_vuelo=rrvv.numero_vuelo		
		WHERE 
		
		(select 
			t.precio*c.cantidad_personas total
			from
			(select sum(p.tarifa) precio
				from reserva re,reserva_viaje rev, precio p
			   where re.codigo_reserva=r.codigo_reserva and re.codigo_reserva=rev.codigo_reserva    
				 and p.numero_vuelo=rev.numero_vuelo
				 and p.codigo_clase=re.codigo_clase
			  group by re.codigo_reserva) t
			,(select codigo_reserva,count(*) cantidad_personas
				from persona_reserva WHERE codigo_reserva=r.codigo_reserva
			  group by codigo_reserva) c
		)< (select 
			t.precio*c.cantidad_personas total
			from
			(select sum(p.tarifa) precio
				from reserva re,reserva_viaje rev, precio p
			   where re.codigo_reserva=rr.codigo_reserva and re.codigo_reserva=rev.codigo_reserva    
				 and p.numero_vuelo=rev.numero_vuelo
				 and p.codigo_clase=re.codigo_clase
			  group by re.codigo_reserva) t
			,(select codigo_reserva,count(*) cantidad_personas
				from persona_reserva WHERE codigo_reserva=rr.codigo_reserva
			  group by codigo_reserva) c ) AND
		r.codigo_reserva != rr.codigo_reserva AND
		r.userid = rr.userid AND 
		v.fecha_viaje = vv.fecha_viaje) 
	AND	r.fecha-date(now())<7 
	AND rv.codigo_reserva=r.codigo_reserva AND rv.numero_vuelo=v.numero_vuelo; 
	RETURN NULL;
END;
$$ LANGUAGE plpgsql;


