Aplikacja do pobierania i zapisywania w bazie H2 danych o aktualnych kursach walut
Stack technologiczny:
1. Frontend - Angular 16, TypeScript
2. Backend - Java 17, SpringBoot 2.7.14, RESTApi, SpringJPA, Lombok, Mapstruct, H2BD

Frontend działa na porcie 4200 - uruchamiany z katalogu Currencies\angular komendą npm start
Backend działa na porcie 8080 - startuje bez żadnych dodatkowych parametrów
W przykładowej aplikacji kontroler ma adnotację @CrossOrigins która służy wyłącznie do celów testowych,
w środowisku produkcyjnym powinny byś skonfigurowane odpowiednie filtry CORS do obsługi portów.

przykład zapytania POST dla backendu
curl \
-X POST \
-H 'Content-Type: application/json' \ 
-d '{"currency": "eur", "name": "sample name"}' \ 
http://localhost:8080/currencies/get-current-currency-value-command

Baza danych jest uruchamiana w pamięci, czyszczona po każdym reboocie aplikacji. 


Do usprawnienia działania aplikacji można również użyć adnotacji @Async jako że publiczne API dość długo 
procesuje zapytanie i blokowany jest jeden wątek. 
