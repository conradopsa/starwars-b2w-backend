# Starwars B2W Desafio Backend
![](https://upload.wikimedia.org/wikipedia/commons/thumb/e/e3/B2W_Digital_logo.png/480px-B2W_Digital_logo.png)

Nesse projeto eu utilizei o IBM Open Liberty como servidor Java EE.
Não se preocupe, deixei um tutorial de como rodá-lo sem problemas no Getting Started.
Para demais dúvidas, meu email é conradopsa@gmail.com.

Bem vindo!

## Getting Started
1. Tenha o Mongo DB instalado

Obs.: Utilizei a versão 4.4.3

2. Configure o server.env do projeto

Obs.: Ele fica em: .\src\main\liberty\config\server.env

Basta por a URI e a Database do MongoDB.

3. Cerfique que a versão do seu JDK é a 1.8
> $ java -version

4. Certifique de estar com o Maven instalado em seu ambiente

Utilizei a versão 3.6.3 nesse projeto.
> $ mvn -version

5. Na pasta raíz do projeto (onde fica o pom.xml), instale os pacotes do maven:
> $ mvn package

6. Execute o comando maven do liberty:
> $ mvn liberty:run

Seu servidor está pronto! :)

http://localhost:9080

### Que a força esteja com você!

