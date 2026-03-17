
  --criação  de tabelas

CREATE TABLE "usuario" (
   "id" SERIAL PRIMARY KEY,
   "nome" VARCHAR(50),
   "email"  VARCHAR(50) UNIQUE,
   "cep" VARCHAR(8),
   "estado" VARCHAR(2)
 );

 CREATE TABLE "candidato" (
   "cpf" VARCHAR(11) PRIMARY KEY,
   "idade" INT,
   "candidato_id" INT
 );

 CREATE TABLE "empresa" (
   "cnpj" VARCHAR(14) PRIMARY KEY,
   "pais" VARCHAR(15),
   "empresa_id" INT
 );

 CREATE TABLE "vaga" (
   "id" SERIAL PRIMARY KEY,
   "nome" VARCHAR(100),
   "descricao" TEXT,
   "contratante" VARCHAR(14)
 );

 CREATE TABLE "especialidade_vaga" (
   "id" SERIAL PRIMARY KEY,
   "vaga" INT,
   "especialidade" VARCHAR(5)
 );

 CREATE TABLE "curtida" (
   "id" SERIAL PRIMARY KEY,
   "vaga" INT,
   "candidato" VARCHAR(11)
 );

 CREATE TABLE "estado" (
   "sigla" VARCHAR(2) PRIMARY KEY,
   "nome" VARCHAR(20) UNIQUE
 );

 CREATE TABLE "especialidade" (
   "sigla" VARCHAR(5) PRIMARY KEY,
   "nome" VARCHAR(20) UNIQUE
 );

 CREATE TABLE "especialidade_usuario" (
   "id" SERIAL PRIMARY KEY,
   "especialidade" VARCHAR(5),
   "usuario" INT
 );

 CREATE TABLE "matchs" (
   "id" SERIAL PRIMARY KEY,
   "empresa" VARCHAR(14),
   "candidato" VARCHAR(11),
   "vaga" INT
 );

 ALTER TABLE "usuario" ADD FOREIGN KEY ("estado") REFERENCES "estado" ("sigla") DEFERRABLE INITIALLY IMMEDIATE;

 ALTER TABLE "candidato" ADD FOREIGN KEY ("candidato_id") REFERENCES "usuario" ("id") DEFERRABLE INITIALLY IMMEDIATE;

 ALTER TABLE "empresa" ADD FOREIGN KEY ("empresa_id") REFERENCES "usuario" ("id") DEFERRABLE INITIALLY IMMEDIATE;

 ALTER TABLE "vaga" ADD FOREIGN KEY ("contratante") REFERENCES "empresa" ("cnpj") DEFERRABLE INITIALLY IMMEDIATE;

 ALTER TABLE "especialidade_vaga" ADD FOREIGN KEY ("vaga") REFERENCES "vaga" ("id") DEFERRABLE INITIALLY IMMEDIATE;

 ALTER TABLE "especialidade_vaga" ADD FOREIGN KEY ("especialidade") REFERENCES "especialidade" ("sigla") DEFERRABLE INITIALLY IMMEDIATE;

 ALTER TABLE "curtida" ADD FOREIGN KEY ("vaga") REFERENCES "vaga" ("id") DEFERRABLE INITIALLY IMMEDIATE;

 ALTER TABLE "curtida" ADD FOREIGN KEY ("candidato") REFERENCES "candidato" ("cpf") DEFERRABLE INITIALLY IMMEDIATE;

 ALTER TABLE "especialidade_usuario" ADD FOREIGN KEY ("especialidade") REFERENCES "especialidade" ("sigla") DEFERRABLE INITIALLY IMMEDIATE;

 ALTER TABLE "especialidade_usuario" ADD FOREIGN KEY ("usuario") REFERENCES "usuario" ("id") DEFERRABLE INITIALLY IMMEDIATE;

 ALTER TABLE "matchs" ADD FOREIGN KEY ("empresa") REFERENCES "empresa" ("cnpj") DEFERRABLE INITIALLY IMMEDIATE;

 ALTER TABLE "matchs" ADD FOREIGN KEY ("candidato") REFERENCES "candidato" ("cpf") DEFERRABLE INITIALLY IMMEDIATE;

 ALTER TABLE "matchs" ADD FOREIGN KEY ("vaga") REFERENCES "vaga" ("id") DEFERRABLE INITIALLY IMMEDIATE;


--  --povoamento estados


 INSERT INTO estado("sigla","nome") VALUES ('AC','Acre');
 INSERT INTO estado("sigla","nome") VALUES ('AL','Alagoas');
 INSERT INTO estado("sigla","nome") VALUES ('AP','Amapa');
 INSERT INTO estado("sigla","nome") VALUES ('AM','Amazonas');
 INSERT INTO estado("sigla","nome") VALUES ('BH','Bahia');
 INSERT INTO estado("sigla","nome") VALUES ('CE','Ceara');
 INSERT INTO estado("sigla","nome") VALUES ('DF','Distrito Federal');
 INSERT INTO estado("sigla","nome") VALUES ('ES','Espirito Santo');
 INSERT INTO estado("sigla","nome") VALUES ('GO','Goias');
 INSERT INTO estado("sigla","nome") VALUES ('MA','maranhão');
 INSERT INTO estado("sigla","nome") VALUES ('MT','Mato Grosso');
 INSERT INTO estado("sigla","nome") VALUES ('MS','Mato Grosso do Sul');
 INSERT INTO estado("sigla","nome") VALUES ('MG','Minas Gerais');
 INSERT INTO estado("sigla","nome") VALUES ('PA','Pará');
 INSERT INTO estado("sigla","nome") VALUES ('PB','Paraíba');
 INSERT INTO estado("sigla","nome") VALUES ('PR','Paraná');
 INSERT INTO estado("sigla","nome") VALUES ('PE','Pernambuco');
 INSERT INTO estado("sigla","nome") VALUES ('PI','Piaui');
 INSERT INTO estado("sigla","nome") VALUES ('RJ','Rio de Janeiro');
 INSERT INTO estado("sigla","nome") VALUES ('RN','Rio Grande do Norte');
 INSERT INTO estado("sigla","nome") VALUES ('RS','Rio Grande do Sul');
 INSERT INTO estado("sigla","nome") VALUES ('RO','Rondônia');
 INSERT INTO estado("sigla","nome") VALUES ('RR','Roraima');
 INSERT INTO estado("sigla","nome") VALUES ('SC','Santa Catarina');
 INSERT INTO estado("sigla","nome") VALUES ('SP','São Paulo');
 INSERT INTO estado("sigla","nome") VALUES ('SE','Sergipe');
 INSERT INTO estado("sigla","nome") VALUES ('TO','Tocantins');


--  --povoando especialidades

 INSERT INTO especialidade ("sigla","nome") VALUES ('PT','Phyton');
 INSERT INTO especialidade ("sigla","nome") VALUES ('JAV','Java');
 INSERT INTO especialidade ("sigla","nome") VALUES ('ANG','Angular');
 INSERT INTO especialidade ("sigla","nome") VALUES ('SPR','Spring');
 INSERT INTO especialidade ("sigla","nome") VALUES ('HT','Html');
 INSERT INTO especialidade ("sigla","nome") VALUES ('CS','Css');


--  --povoando usuario


 INSERT INTO usuario ("nome","email","cep","estado") VALUES ('empresa1','ep1@Gmail','12345678','RJ');
 INSERT INTO usuario ("nome","email","cep","estado") VALUES ('empresa2','ep2@Gmail','12345679','SP');
 INSERT INTO usuario ("nome","email","cep","estado") VALUES ('empresa3','ep3@Gmail','12345670','DF');
 INSERT INTO usuario ("nome","email","cep","estado") VALUES ('empresa4','ep4@Gmail','12345671','MA');
 INSERT INTO usuario ("nome","email","cep","estado") VALUES ('empresa5','ep5@Gmail','12345672','RS');
 INSERT INTO usuario ("nome","email","cep","estado") VALUES ('candidato1','c1@Gmail','12345673','PI');
 INSERT INTO usuario ("nome","email","cep","estado") VALUES ('candidato2','c2@Gmail','12345674','GO');
 INSERT INTO usuario ("nome","email","cep","estado") VALUES ('candidato3','c3@Gmail','12345675','MA');
 INSERT INTO usuario ("nome","email","cep","estado") VALUES ('candidato4','c4@Gmail','12345676','MT');
 INSERT INTO usuario ("nome","email","cep","estado") VALUES ('candidato5','c5@Gmail','12345677','RN');


--  --povoando especialidades do usuario

 INSERT INTO especialidade_usuario ("especialidade","usuario") VALUES ('JAV','1');
 INSERT INTO especialidade_usuario ("especialidade","usuario") VALUES ('PT','1');
 INSERT INTO especialidade_usuario ("especialidade","usuario") VALUES ('JAV','2');
 INSERT INTO especialidade_usuario ("especialidade","usuario") VALUES ('SPR','2');
 INSERT INTO especialidade_usuario ("especialidade","usuario") VALUES ('HT','3');
 INSERT INTO especialidade_usuario ("especialidade","usuario") VALUES ('CS','4');
 INSERT INTO especialidade_usuario ("especialidade","usuario") VALUES ('ANG','5');
 INSERT INTO especialidade_usuario ("especialidade","usuario") VALUES ('HT','6');
 INSERT INTO especialidade_usuario ("especialidade","usuario") VALUES ('CS','6');
 INSERT INTO especialidade_usuario ("especialidade","usuario") VALUES ('JAV','7');
 INSERT INTO especialidade_usuario ("especialidade","usuario") VALUES ('PT','7');
 INSERT INTO especialidade_usuario ("especialidade","usuario") VALUES ('JAV','8');
 INSERT INTO especialidade_usuario ("especialidade","usuario") VALUES ('ANG','9');
 INSERT INTO especialidade_usuario ("especialidade","usuario") VALUES ('JAV','10');
 INSERT INTO especialidade_usuario ("especialidade","usuario") VALUES ('ANG','10');
 INSERT INTO especialidade_usuario ("especialidade","usuario") VALUES ('HT','10');
 INSERT INTO especialidade_usuario ("especialidade","usuario") VALUES ('CS','10');
 INSERT INTO especialidade_usuario ("especialidade","usuario") VALUES ('SPR','10');

--  --povoando empresas

 INSERT INTO empresa("cnpj","pais","empresa_id") VALUES ('12345678901234','brasil',1);
 INSERT INTO empresa("cnpj","pais","empresa_id") VALUES ('12345678901235','brasil',2);
 INSERT INTO empresa("cnpj","pais","empresa_id") VALUES ('12345678901236','brasil',3);
 INSERT INTO empresa("cnpj","pais","empresa_id") VALUES ('12345678901237','brasil',4);
 INSERT INTO empresa("cnpj","pais","empresa_id") VALUES ('12345678901238','brasil',5);

--  --povoando candidatos

 INSERT INTO candidato ("cpf","idade","candidato_id") VALUES ('12345678901',20,6);
 INSERT INTO candidato ("cpf","idade","candidato_id") VALUES ('12345678902',22,7);
 INSERT INTO candidato ("cpf","idade","candidato_id") VALUES ('12345678903',18,8);
 INSERT INTO candidato ("cpf","idade","candidato_id") VALUES ('12345678904',25,9);
 INSERT INTO candidato ("cpf","idade","candidato_id") VALUES ('12345678905',30,10);


--  --exemplo de vagas 

 INSERT INTO vaga ("nome","descricao","contratante") VALUES ('vaga 1','Exemplo vaga 1', '12345678901234');
 INSERT INTO vaga ("nome","descricao","contratante") VALUES ('vaga 2','Exemplo vaga 2', '12345678901235');
 INSERT INTO vaga ("nome","descricao","contratante") VALUES ('vaga 3','Exemplo vaga 3', '12345678901236');
 INSERT INTO vaga ("nome","descricao","contratante") VALUES ('vaga 4','Exemplo vaga 4', '12345678901234');


--  --povoando espeacialidades para as vagas

 INSERT INTO especialidade_vaga ("vaga","especialidade") VALUES (1,'HT');
 INSERT INTO especialidade_vaga ("vaga","especialidade") VALUES (1,'CS');
 INSERT INTO especialidade_vaga ("vaga","especialidade") VALUES (1,'ANG');
 INSERT INTO especialidade_vaga ("vaga","especialidade") VALUES (2,'JAV');
 INSERT INTO especialidade_vaga ("vaga","especialidade") VALUES (2,'SPR');
 INSERT INTO especialidade_vaga ("vaga","especialidade") VALUES (3,'CS');
 INSERT INTO especialidade_vaga ("vaga","especialidade") VALUES (3,'PT');
 INSERT INTO especialidade_vaga ("vaga","especialidade") VALUES (4,'PT');
	



--  --exemplo curtidas 


 INSERT INTO curtida ("vaga","candidato") VALUES ('1','12345678901');
 INSERT INTO curtida ("vaga","candidato") VALUES ('2','12345678901');
 INSERT INTO curtida ("vaga","candidato") VALUES ('1','12345678902');
 INSERT INTO curtida ("vaga","candidato") VALUES ('3','12345678902');
 INSERT INTO curtida ("vaga","candidato") VALUES ('3','12345678903');
 INSERT INTO curtida ("vaga","candidato") VALUES ('2','12345678903');


--  --povoamento de match

 INSERT iNTO matchs ("empresa","candidato","vaga") VALUES ('12345678901234','12345678901','1');
 INSERT iNTO matchs ("empresa","candidato","vaga") VALUES ('12345678901236','12345678902','3');


-- -- alguns exemplos de pesquisa


-- todos os candidatos (a mesma logica pode ser aplicada a empresa)


 SELECT u.nome,c.idade,c.cpf , ec.especialidade
 FROM candidato AS c
 JOIN usuario AS u
 ON u.id = c.candidato_id
 
 JOIN especialidade_usuario AS ec
 ON ec.usuario = u.id

 ORDER BY u.nome DESC


 -- determinadas vagas de uma empresa

 SELECT v.nome, STRING_AGG(ev.especialidade,',') ,v.contratante
 FROM vaga AS v 
 JOIN especialidade_vaga AS ev
 ON v.id=ev.vaga
 WHERE v.contratante = '12345678901234'
 GROUP BY v.nome,v.contratante;

 --determinados match de uma empresa (mesma logica pra candidato)

SELECT 
    u_cand.nome AS nome_candidato,
    u_emp.nome AS nome_empresa,
    v.nome AS nome_vaga
	
	
FROM matchs AS m
JOIN vaga AS v ON m.vaga = v.id


JOIN empresa AS emp ON m.empresa = emp.cnpj
JOIN usuario AS u_emp ON emp.empresa_id = u_emp.id


JOIN candidato AS c ON m.candidato = c.cpf
JOIN usuario AS u_cand ON c.candidato_id = u_cand.id;


 
 
 



