# Hering.TextToSpeech

## O que é

API totalmente funcional para text-to-speech.

Criado para avaliar candidatos ao time Hering B2B. [Maiores detalhes aqui.](/Avaliacao.md)

## Pré-requisitos

As principais dependências:
* Java JDK 19
* Spring Boot
* Kafka

## Estrutura de Projetos

Módulos da solução (padrão monorepo):
* desafio-core: módulo central para reutilização de regras
* desafio-api: api para text-to-speech
* desafio-worker: processa de forma assíncrona text-to-speech. (não está implementado ainda, faz parte da avaliação)

## Compilação e fechamento de pacote

Considerando que todos o pré-requisitos para o build (declarados acima) foram instalados.

- Empacotar fat-jar
```
# https://stackoverflow.com/questions/49590459/spring-boot-maven-plugin-doesnt-create-fat-jar
./mvnw --projects :desafio-api --also-make clean package
```

## Documentação da API Rest


#### Listar consultas

```
GET /api/v1/text-to-speech

Resposta:
[
    {
        "id": 1,
        "momment": "2021-06-10T23:28:47.2324004-03:00",
        "text": "Olá mundo",
        "language": "pt-br",
        "voice": "Dinis",
        "status": "COMPLETED"
    }
]
```

#### Consulta síncrona

```
GET /api/v1/text-to-speech/sync?language=pt-br&voice=Dinis&text=Bom dia

Resposta:
{
    "id": 1,
    "momment": "2021-06-10T23:28:47.2324004-03:00",
    "text": "Olá mundo",
    "language": "pt-br",
    "voice": "Dinis",
    "status": "COMPLETED"
}

```

#### Buscar resultado

```
GET /api/v1/text-to-speech/{idConsulta}/audio

Retorna: Content-Type: audio/wav
```

#### Consulta assíncrona

```
GET /api/v1/text-to-speech/async?language=pt-br&voice=Dinis&text=Bom dia
 
Resposta:
 {
	"id": 45543915047145 // este id será utilizado para consulta
    "momment": "2021-06-10T23:28:47.2324004-03:00",
    "text": "Olá mundo",
    "language": "pt-br",
    "voice": "Dinis",
    "status": "PROCESSING"
 }

```

#### Consultar assincrono

```
GET /api/v1/text-to-speech/{idConsulta}
```

## Dicas de desenvolvimento

* [Instalar o Kafka](https://www.loginradius.com/blog/engineering/quick-kafka-installation/).
* Tudo está programado em inglês, pois o modelo de negócio de IA é internacional, mas comentários, documentação e o idioma padrão das telas continua sendo português.
* [Documentação da API](https://www.voicerss.org/api/) e [tela para testes](https://www.voicerss.org/api/demo.aspx).
