# Documentazione API REST — FoodAPI

Questa documentazione è allineata al contenuto del repository e non aggiunge né presume endpoint non presenti nel codice sorgente.

## Dove trovare gli endpoint
Tutti i controller REST si trovano in:
`src/main/java/dev/archety/food/controller/`

File significativi:
- `src/main/java/dev/archety/food/controller/FoodController.java` — controller principale trovato nel repository.

Per ottenere la lista completa e i dettagli (URL, metodi HTTP, payload, response):
1. Apri i file nella cartella `controller`.
2. Cerca annotazioni Spring MVC: `@RequestMapping`, `@GetMapping`, `@PostMapping`, `@PutMapping`, `@DeleteMapping`.
3. Consulta le entità correlate in `src/main/java/dev/archety/food/models/` per conoscere i campi dei corpi richiesta/risposta.

## Come esplorare gli endpoint in esecuzione
1. Avvia l'applicazione:
   ./mvnw spring-boot:run
2. Osserva la console per i log di avvio e i mapping pubblicati (Spring mostra i RequestMappings all'avvio).
3. Usa curl o uno strumento come Postman per testare gli endpoint esposti:
   - GET /... (endpoint effettivi vanno ricavati dal controller)
   - POST /... (payload JSON in base alle entità del package models)

Esempio generico (sostituireURL e payload con i valori reali trovati nel controller):
curl -X GET "http://localhost:8080/<endpoint-reale>" -H "Accept: application/json"

## Persistenza
Il progetto usa Spring Data JPA (dipendenza `spring-boot-starter-data-jpa`) e il driver MySQL (`mysql-connector-j`). Le repository si trovano in:
`src/main/java/dev/archety/food/repositories/`

Verifica le annotazioni `@Entity` nelle classi sotto `models` per conoscere la mappatura delle tabelle.

## Note
- Non ho incluso elenchi di endpoint, esempi di request/response o payload concreti per non inventare funzionalità: tutti i dettagli concreti sono da leggere direttamente nei file dei controller e dei modelli.
- Se desideri, posso generare una versione estesa della documentazione con esempi cURL o uno schema OpenAPI dopo aver estratto i mapping direttamente dal codice dei controller.
