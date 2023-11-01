# Nome do projeto

## 💻 Pré-requisitos

Antes de começar, verifique se você atendeu aos seguintes requisitos:

* Instalar `Java 17`

> Recomendo utilizar SDKMAN, para facilitar na troca de versões do java

* Ter docker instalado na máquina

## 🖱 Executando o projeto

1. Navegar para a pasta docker-compose  `cd src/main/resources/docker-compose`
2. Subir os containeres `docker compose up`
3. Executar o serviço, com profile `loc`

## Objetivo

Após a execução do serviço, verifique nos logs. Há 3 exemplos, contendo 3 filas:

1. Recebendo a mensagem como String (como realizar parse da mensagem e tratamento)
2. Recebendo a mensagem como Message (Objeto da própria AWS SDK, as vantagens)
3. Recebendo a mensagem como o DTO (vantagens de recepcionamento e desvantagens)

## 📝 Licença

Esse projeto está sob licença. Veja o arquivo [LICENÇA](LICENSE.md) para mais detalhes.

[⬆ Voltar ao topo](#nome-do-projeto)<br>
