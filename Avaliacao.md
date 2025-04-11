# Itens da avaliação

## Efetuar melhorias

1. quando a internet cai (no servidor) ocorre erro e não exibe nem armazena detalhes, dificultando diagnóstico em produção, resolver isso
2. implementar GET /api/v1/text-to-speech?status=COMPLETED&status=PROCESSING possibilitando listar as consultas
3. criar um pequeno front (html e js puro) embedded no spring para [testes rápidos na api](https://www.voicerss.org/api/demo.aspx).

## Implementar chamada assíncrona

4. Consulta assíncrona de text-to-speech assíncrona, onde a consulta é registrada e o resultado é verificado com o id retornado
    1. Utilizar a ferramenta de mensageria Kafka
	2. Receber a chamada na Api, registrar, e jogar o processamento para um Worker fazer
	3. O worker deve poder ser replicado para ganho de escalabilidade

## Implementar testes unitários automatizados

5. Faça alguns testes unitários aproveitando a estrutura de injeção de dependência que facilita isso bastante.

## Questões descritivas

6. Descreva sua visão sobre performance em bancos relacionais (SQL) e quais os principais recursos que você usa para manter leitura e escrita rápidas.
7. Na sua opinião como podemos nos proteger de ataques a nossa API e quais são os principais problemas aos quais estamos expostos.

## Bônus

8. Seria muito legal mostrar que sabe usar um host gratuido, por exemplo Heroku.com, para hospedar a Api em docker. Se fizer isso nos mande o link de acesso

## Itens que serão avaliados

* domínio de ferramenta git
* domínio da plataforma java e spring
* item 1 avalia sobre técnicas de pegar erros em produção
* item 2 avalia a capacidade de economizar recursos de banco de dados
* item 3 avalia a conhecimento em front
* item 4 avalia:
	- reutilização de fonte
	- capacidade de documentar
	- conhecimento em escalar processamento para N nós
* item 5 avalia habilidade de automatizar testes e diferenciar os tipos de testes
* item 6 avalia conhecimento de banco de dados
* item 7 avalia conhecimento de segurança
