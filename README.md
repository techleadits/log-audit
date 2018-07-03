# log-audit
Esta biblioteca é uma interface que permite fácil acesso a manutenção de logs e auditorias orientados a base de dados descritos em [schema de log para base de dados](https://github.com/lourencomcviana/log-schema).

## Dependências
- Criar uma base de dados oracle [com o schema de log](https://github.com/lourencomcviana/log-schema)

## Instalação 
- instalar o gradle na máquina
- executar `gradle jarb`
- jar será criado na pasta /build/libs/

## Hello world
- Executar jar 
  - `java -jar ./build/libs/log-audit-x.x.jar stringdeconexaodasuabase usuario senha`
- exemplo 
  - `java -jar ./build/libs/log-audit-1.1.jar jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS_LIST=(ADDRESS=(PROTOCOL=TCP)(HOST=localhost)(PORT=1521)))(CONNECT_DATA=(SERVICE_NAME=XE))) system oracle`
