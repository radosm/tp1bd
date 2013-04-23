--asumo que las reservas no concretadas se borran!!

--buscar persona...
select nombre, apellido, nro_doc, tipo_doc from persona per where not exists (
	--tal que no exista ningun pais...
	select * from pais pa where not exists (
		--tal que la persona no estuvo en un viaje con origen en ese país en los últimos 5 años
		select * from persona_reserva pr 
		join reserva res on pr.codigo_reserva = res.codigo_reserva 
		join reserva_viaje rv on rv.codigo_reserva = res.codigo_reserva
		join viaje via on rv.fecha_viaje = via.fecha_viaje and rv.numero_vuelo = via.numero_vuelo
		join vuelo vue on vue.numero_vuelo = via.numero_vuelo
		join aeropuerto ori on vue.aeropuerto_ori = ori.codigo_aerop
		where 
			pr.nro_doc = per.nro_doc and
			pr.tipo_doc = per.tipo_doc and
			ori.codigo_pais = pa.codigo_pais and
			via.fecha_viaje > (
				select now() - interval '5 years'
			)
	) and not exists (
		--ni tampoco en un viaje con destino a ese país
		select * from persona_reserva pr 
		join reserva res on pr.codigo_reserva = res.codigo_reserva 
		join reserva_viaje rv on rv.codigo_reserva = res.codigo_reserva
		join viaje via on rv.fecha_viaje = via.fecha_viaje and rv.numero_vuelo = via.numero_vuelo
		join vuelo vue on vue.numero_vuelo = via.numero_vuelo
		join aeropuerto dest on vue.aeropuerto_dst = dest.codigo_aerop
		where 
			pr.nro_doc = per.nro_doc and
			pr.tipo_doc = per.tipo_doc and
			dest.codigo_pais = pa.codigo_pais and
			via.fecha_viaje > (
				select now() - interval '5 years'
			)
	) and (
		exists (
			--y que halla algun vuelo que tenga ese país como origen
			select * from vuelo vue 
			join aeropuerto ori on vue.aeropuerto_ori = ori.codigo_aerop
			where pa.codigo_pais = ori.codigo_pais
		) or exists (
			--o como destino
			select * from vuelo vue 
			join aeropuerto dest on vue.aeropuerto_dst = dest.codigo_aerop
			where pa.codigo_pais = dest.codigo_pais
		)
	)
)
