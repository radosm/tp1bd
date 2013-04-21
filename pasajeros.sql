--buscar persona...
select nombre, apellido, nro_doc, tipo_doc from persona_reserva per where not exists (
	--tal que no exista ningun pais...
	select select * from pais pa where not exists (
		--tal que la persona no estuvo en un viaje con origen en ese país en los últimos 5 años
		select * from es_para ep 
		join reserva res on ep.codigo_reserva = res.codigo_reserva 
		join reserva_viaje rv on rv.codigo_reserva = res.codigo_reserva
		join viaje via on reserva_viaje.id_viaje = via.id_viaje
		join vuelo vue on vue.numero = via.numero
		join aeropuerto ori on vue.origen = ori.codigo_aerop
		where 
			ep.nro_doc = per.nro_doc and
			ep.tipo_doc = per.tipo_doc and
			ori.codigo_pais = pa.codigo_pais and
			via.fecha > (
				select now() - interval '5 years'
			)
	) and not exists (
		--ni tampoco en un viaje con destino a ese país
		select * from es_para ep 
		join reserva res on ep.codigo_reserva = res.codigo_reserva 
		join reserva_viaje rv on rv.codigo_reserva = res.codigo_reserva
		join viaje via on reserva_viaje.id_viaje = via.id_viaje
		join vuelo vue on vue.numero = via.numero
		join aeropuerto dest on vue.destino = dest.codigo_aerop
		where 
			ep.nro_doc = per.nro_doc and
			ep.tipo_doc = per.tipo_doc and
			dest.codigo_pais = pa.codigo_pais and
			via.fecha > (
				select now() - interval '5 years'
			)
	) and (exists (
		--y que halla algun vuelo que tenga ese país como origen
		select * from vuelo vue 
		join aeropuerto ori on vue.origen = aeropuerto.codigo_aerop
		where pa = ori.codigo_pais
	) or exists (
		--o como destino
		select * from vuelo vue 
		join aeropuerto dest on vue.destino = aeropuerto.codigo_aerop
		where pa = dest.codigo_pais
	))
)