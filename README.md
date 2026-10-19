# Food API - Spring Boot

[![License](https://img.shields.io/github/license/Cordox22/Food-API-Spring-Boot)](./LICENSE) [![Java](https://img.shields.io/badge/Java-17-blue)](https://www.oracle.com/java/) [![Build Status](https://img.shields.io/github/actions/workflow/status/Cordox22/Food-API-Spring-Boot/ci.yml?branch=main&label=ci)](https://github.com/Cordox22/Food-API-Spring-Boot/actions) 

Descrizione
---------
Progetto minimalista Spring Boot che espone una semplice API REST per la gestione di risorse "Food". Implementa le classiche componenti di un'applicazione Java/Spring: entity JPA, DTO, repository, service e controller REST. Questo repository contiene esclusivamente il codice sorgente mostrato — la documentazione qui sotto descrive solo le funzionalità effettivamente presenti.

Caratteristiche implementate
---------------------------
- CRUD (limitato) sulle risorse "Food":
  - Recupero di tutti gli alimenti (GET /api/foods)
  - Recupero di un alimento per uuid (GET /api/foods/{uuid})
  - Creazione di un alimento (POST /api/foods)
  - Aggiornamento di un alimento esistente (PUT /api/foods/{uuid})
  - Cancellazione di un alimento (DELETE /api/foods/{uuid})
- Layered architecture: Controller -> Service -> Repository -> Entity/DTO
- Persistenza con Spring Data JPA (entità annotate con jakarta.persistence)

Structure del progetto (analisi del codice)
------------------------------------------
- Package principale: dev.archety.food
  - models
    - Food.java (Entity JPA)
    - FoodDTO.java (Data Transfer Object usato nelle API)
  - repositories
    - FoodRepositories.java (estende JpaRepository<Food, Long>)
      - Metodi custom presenti:
        - Optional<Food> findByUuid(String uuid)
        - void deleteByUuid(String uuid)
        - boolean existsByUuid(String uuid)
  - service
    - FoodService.java (interfaccia con i metodi esposti dal service)
      - FoodDTO create(FoodDTO sto)
      - List<FoodDTO> findAll()
      - Optional<FoodDTO> findByUuid(String uuid)
      - Optional<FoodDTO> update(String uuid, FoodDTO dto)
      - boolean delete(String uuid)
    - FoodServiceImpl.java (implementazione)
      - mapToEntity / mapToDto: mappatura tra DTO ed Entity
      - L'implementazione è transactional e utilizza FoodRepositories
  - controller
    - FoodController.java (RestController con base path /api/foods)
      - GET /api/foods
      - GET /api/foods/{uuid}
      - POST /api/foods
      - PUT /api/foods/{uuid}
      - DELETE /api/foods/{uuid}

Entity: Food (campi principali)
------------------------------
- id: Long (PK, GeneratedValue)
- uuid: String (colonna non insertable/updatable dal codice; presunto generato dal DB)
- foodName: String
- category: String
- calories: Integer
- price: BigDecimal
- available: boolean
- date: LocalDate (campo mapped su "expirationDATE")
- createdAt: LocalDateTime

DTO: FoodDTO (usato nell'API)
----------------------------
Campi esposti dalle API (nomi esatti nel codice):
- uuid: String
- food: String          (corrisponde a Food.foodName)
- category: String
- calories: Integer
- price: BigDecimal
- available: Boolean
- date: LocalDate (YYYY-MM-DD)
- createdAt: LocalDateTime (ISO-8601)

Repository
----------
Interfaccia: FoodRepositories extends JpaRepository<Food, Long>
Metodi custom:
- Optional<Food> findByUuid(String uuid)
- void deleteByUuid(String uuid)
- boolean existsByUuid(String uuid)

Service
-------
Interfaccia e implementazione (FoodServiceImpl) con i seguenti comportamenti osservabili:
- create(dto): mappa DTO su entity e salva via repository. Ritorna DTO con uuid e createdAt se presenti dopo il salvataggio.
- findAll(): ritorna lista di FoodDTO mappati da entity
- findByUuid(uuid): ritorna Optional<FoodDTO>
- update(uuid, dto): cerca per uuid, aggiorna i campi della entity (mapToEntity) e ritorna Optional<FoodDTO>. L'entity è modificata in ambito @Transactional.
- delete(uuid): cerca per uuid e se presente elimina l'entity

Controller - Endpoints
----------------------
Base path: /api/foods
- GET /api/foods
  - Response: 200 OK, array di FoodDTO
- GET /api/foods/{uuid}
  - Response: 200 OK con FoodDTO oppure 404 NOT FOUND
- POST /api/foods
  - Request body: FoodDTO (senza uuid, createdAt gestiti dal DB)
  - Response: 201 CREATED con FoodDTO creato
- PUT /api/foods/{uuid}
  - Request body: FoodDTO
  - Response: 200 OK con FoodDTO aggiornato oppure 404 NOT FOUND
- DELETE /api/foods/{uuid}
  - Response: 204 NO CONTENT oppure 404 NOT FOUND

Esempi di chiamate API
----------------------
Esempi con curl (assumendo server in esecuzione su http://localhost:8080):

1) Recuperare tutti gli alimenti
curl -sS -X GET "http://localhost:8080/api/foods" -H "Accept: application/json"

2) Recuperare un alimento per uuid
curl -sS -X GET "http://localhost:8080/api/foods/{uuid}" -H "Accept: application/json"

3) Creare un alimento
curl -sS -X POST "http://localhost:8080/api/foods" -H "Content-Type: application/json" -d '
{
  "food": "Mela",
  "category": "Frutta",
  "calories": 52,
  "price": 0.99,
  "available": true,
  "date": "2026-12-31"
}'

4) Aggiornare un alimento esistente
curl -sS -X PUT "http://localhost:8080/api/foods/{uuid}" -H "Content-Type: application/json" -d '
{
  "food": "Mela Golden",
  "category": "Frutta",
  "calories": 50,
  "price": 1.09,
  "available": true,
  "date": "2027-01-15"
}'

5) Eliminare un alimento
curl -sS -X DELETE "http://localhost:8080/api/foods/{uuid}"

Nota: il campo uuid non va fornito in fase di creazione; il progetto attuale presuppone che il valore uuid sia prodotto dal database (l'entità ha la colonna marcata insertable=false, updatable=false).

Architettura
-----------
Questo progetto segue l'architettura classica a strati (layered):
- Controller (REST) — riceve richieste HTTP e mappa i payload su DTO
- Service — logica applicativa e transazioni (@Transactional)
- Repository (Spring Data JPA) — astrazione per l'accesso dati
- Entity (JPA) e DTO — mappatura tra modello persistente e modello esposto via API

Considerazioni tecniche e limiti osservati
-----------------------------------------
- La generazione del campo uuid sembra delegata al DB (colonna non inseribile dal codice). Assicurarsi che lo schema DB fornisca un valore (trigger o default) per uuid.
- In FoodDTO il campo "available" è dichiarato come Boolean ma il getter restituisce primitive boolean; attenzione ai possibili NullPointerException in presenza di valori null.
- Il metodo update in FoodServiceImpl modifica l'entità trovata senza effettuare esplicitamente save(): ciò va bene se l'entità è gestita (inside @Transactional) e il provider JPA effettua il flush, ma è buona pratica usare save() per chiarezza.

Tecnologie e dipendenze (derivate dal codice)
--------------------------------------------
- Java (versione minima consigliata: 11+, preferibile 17)
- Spring Boot (Web, Data JPA)
- Jakarta Persistence API (jakarta.persistence)
- Jakarta Transactions (jakarta.transaction.Transactional)
- Spring Web (RestController, mapping)
- Spring Data JPA (JpaRepository)
- Database relazionale (progettato per supportare uuid generato dal DB)

Installazione e configurazione (MySQL)
-------------------------------------
1) Installare MySQL (es.: MySQL 8)
2) Creare il database e l'utente:

CREATE DATABASE food_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'food_user'@'localhost' IDENTIFIED BY 'secure_password';
GRANT ALL PRIVILEGES ON food_db.* TO 'food_user'@'localhost';
FLUSH PRIVILEGES;

3) Esempio di application.properties (o application.yml) da inserire in src/main/resources:

spring.datasource.url=jdbc:mysql://localhost:3306/food_db?useSSL=false&serverTimezone=UTC
spring.datasource.username=food_user
spring.datasource.password=secure_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

Nota: Poiché nel codice l'entità Food si aspetta che il campo uuid sia generato dal DB, assicurati che la tabella `foods` abbia una colonna `uuid` popolata automaticamente (ad esempio con DEFAULT (UUID()) o con un trigger) oppure modifica l'entità e la logica di creazione per generare uuid lato applicazione.

Badge
-----
- License badge: mostra la licenza MIT inclusa in repository
- Build badge: badge generico GitHub Actions (workflow `ci.yml`) — sostituire con il nome effettivo del workflow se presente
- Java badge: versione linguaggio

Licenza
-------
Questo repository viene rilasciato con licenza MIT. Vedi file LICENSE.

File aggiunti in questa modifica
-------------------------------
- README.md (documentazione in italiano generata automaticamente basata sul codice presente)
- LICENSE (MIT)
- .gitignore (ottimizzato per Java, Maven e Spring Boot)

