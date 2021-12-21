# Detalhes de implementação:

## Solução

Aplicação é responsável pela criação de sessões de votação para determinadas pautas a serem votadas pelos associados do Sicredi.
Para viabilizar a abertura das sessões de votação, foram disponibilizadas uma série de APIs REST para criação/manipulação dos respectivos domínios:

- Associado (associate)
- Pauta (subject)
- Sessões de votação (session) 

## Arquitetura

O design de arquitetura escolhido para esta aplicação segue o conceito de **arquitetura hexagonal (ports and adapters)**.
A escolha por esse tipo de arquitetura se deu por conta do fraco acoplamento entre camadas, facilitando a manutenção da aplicação e a exploração de testes.

Outro ponto importante foi a escolha da utilização do [Spring Webflux](https://docs.spring.io/spring-framework/docs/current/reference/html/web-reactive.html) para requisições assíncronas não bloqueantes e melhor gerenciamento de concorrência.
  
### Responsabilidades de cada camada:

* **API:** Responsável por ser a "porta de entrada" da aplicação, além de realizar a comunicação com a camada da aplicação responsável pelas regras de negócio.        
* **Core:** Principal camada da aplicação, sendo responsável por garantir que as regras de negócio sejam cumpridas. É a camada intermediária entre API e infrastructure.
* **Infrastructure:** Onde estão localizadas classes de comunicação com banco de dados e serviços externos.

## Testes:

* **Testes unitários**: 

Esta aplicação conta com quase 100% de cobertura de testes unitários, desprezando apenas algumas classes de configuração do projeto.  

* **Testes de integração**: 

Aplicação conta com testes de integração cobrindo diferentes cenários das APIs REST que foram expostas. 
