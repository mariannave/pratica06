@echo off
rem Modifique a variável abaixo para o local onde está a pasta do seu banco de dados.
set BD_HOME=d:\java\bd
echo =========
echo Local do banco de dados: %C:\Users\Marianna\Documents\Prática-JSF-06.Recursos\praticajsf06\bd%
echo =========

java -cp .\lib\hsqldb.jar org.hsqldb.server.Server --database.0 file:%BD_HOME%\turmasweb\turmasweb --dbname.0 tweb