# FoodAPI — API REST in Spring Boot

FoodAPI è un'applicazione Spring Boot per la gestione di risorse legate al dominio "food". È un progetto basato su Java 21 che usa Spring Boot (starter parent v4.0.3), Spring Data JPA per la persistenza e MySQL come connettore di runtime. Il codice sorgente principale si trova nel package `dev.archety.food`.

Principale utenza: sviluppatori che vogliono eseguire, estendere o integrare questa API REST sviluppata con Spring Boot.

## Stack
- Language: Java 21
- Framework / runtime: Spring Boot 4 (spring-boot-starter-parent 4.0.3)
- Notable libraries: spring-boot-starter-data-jpa, spring-boot-starter-webmvc, mysql-connector-j
- Build: Maven (wrapper incluso: `mvnw`, `mvnw.cmd`)

## Struttura rilevante del repository
```
pom.xml                         # configurazione Maven (Java 21, dipendenze JPA/WebMVC)
mvnw, mvnw.cmd                   # Maven wrapper
src/main/java/dev/archety/food/
  FoodApiApplication.java        # classe main Spring Boot
  controller/                    # controller REST (es. FoodController.java)
  models/                        # entità / DTO
  repositories/                  # Spring Data JPA repositories
  service/                       # logica di business
README.md
LICENSE
.gitignore
```

## Requisiti
- Java 21 (JDK 21)
- MySQL (o altro datasource compatibile configurato in application.properties)
- Maven (se non si usa il wrapper)

## Avviare l'applicazione (rapido)
1. Clona il repository:
   git clone https://github.com/Cordox22/Food-API-Spring-Boot.git
2. Posizionati nella cartella del progetto e avvia usando il wrapper Maven:
   ./mvnw spring-boot:run
   (Windows: mvnw.cmd spring-boot:run)
3. L'applicazione partirà sulla porta predefinita 8080 se non diversamente configurata.

Nota: l'applicazione usa Spring Data JPA e il connettore MySQL (mysql-connector-j). Configura le proprietà di connessione al database in `src/main/resources/application.properties` o tramite variabili d'ambiente (esempi sotto).

## Variabili di configurazione / proprietà comuni
La configurazione del datasource segue le convenzioni Spring Boot. Esempi (da aggiungere in application.properties o come env vars):
- spring.datasource.url=jdbc:mysql://localhost:3306/<db_name>
- spring.datasource.username=<username>
- spring.datasource.password=<password>
- spring.jpa.hibernate.ddl-auto=update (o validate / none / create-drop a seconda delle esigenze)
- server.port=8080

## Documentazione delle API
Le implementazioni REST si trovano nel package `src/main/java/dev/archety/food/controller/` (es. `FoodController.java`). Per la documentazione completa degli endpoint:
- Apri i file dei controller per leggere i mapping (`@RequestMapping`, `@GetMapping`, `@PostMapping`, ecc.).
- In mancanza di uno swagger/openapi integrato, il modo più accurato per verificare gli endpoint è leggere il codice sorgente dei controller o avviare l'applicazione ed esplorare gli endpoint in esecuzione (es. tramite cURL o Postman).

(Esempi concreti di endpoint, parametri e payload non sono inclusi qui per evitare di inventare funzionalità: tutti i dettagli sono direttamente disponibili nei file sotto `src/main/java/dev/archety/food/controller/` e `src/main/java/dev/archety/food/models/`.)

## Esecuzione dei test
Se sono presenti test (cartella `src/test`), esegui:
./mvnw test

## Contributi
- Segui lo stile del codice esistente nel package `dev.archety.food`.
- Apri una pull request con descrizione chiara delle modifiche.
- Aggiungi test per nuova logica o per correzioni.

## Licenza
Questo repository include una licenza MIT (file LICENSE).

## File di esempio per la configurazione del datasource
È stato aggiunto `src/main/resources/application.properties.example` con un esempio di configurazione MySQL per lo sviluppo locale. NON inserire credenziali reali nel file `application.properties` del repository; usa questo file come template e popolalo con valori locali o con variabili d'ambiente.

Esempio rapido:
```
# copia il file in src/main/resources/application.properties e aggiorna i valori
spring.datasource.url=jdbc:mysql://localhost:3306/fooddb
spring.datasource.username=your_db_user
spring.datasource.password=your_db_password
```
