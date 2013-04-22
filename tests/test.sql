-- 
--  CUENTA
--

INSERT INTO cuenta VALUES ("C0001", "Juan", "Gomez", "abc123", "juan@sql.com", "programador","555-0001","calle 11", "calle 11");
INSERT INTO cuenta VALUES ("C0002", "Maria", "Ramos", "qwerty", "maria@law.com", "contadora","555-0010","calle 12", "calle 12");

--
-- CLASE
--

INSERT INTO clase VALUES ("BIZ", "Clase ejecutiva");
INSERT INTO clase VALUES ("FST", "Clase primera");
INSERT INTO clase VALUES ("TUR", "Clase turista");


--
-- RESERVA
--

INSERT INTO reserva VALUES ("R0000001", "January 5, 2013", "confirmado", "efectivo", "C0001", "FST");
INSERT INTO reserva VALUES ("R0000002", "January 5, 2013", "confirmado", "efectivo", "C0001", "BIZ");
INSERT INTO reserva VALUES ("R0000003", "January 5, 2013", "confirmado", "efectivo", "C0001", "TUR");
INSERT INTO reserva VALUES ("R0000004", "January 5, 2013", "confirmado", "efectivo", "C0001", "TUR");

-- 
-- PERSONA RESERVA
--


create table persona_reserva (
  codigo_reserva char(8) not null,
  tipo_doc integer not null,
  nro_doc numeric(20) not null,
  nombre varchar not null,
  apellido varchar not null,
  fecha_nac date not null,
  nacionalidad varchar not null
);

alter table persona_reserva add primary key (codigo_reserva,tipo_doc,nro_doc);
alter table persona_reserva add foreign key (codigo_reserva) references reserva;
