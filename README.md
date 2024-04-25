# fmi-retele
Proiect la disciplina "Retele de calculatoare" – an II ID @ FMI

## Continut
1. [Cerinte proiect](#cerinte)
2. [Descriere proiect](#descriere-proiect)
3. [Video demo](#video-demo)

----

### Cerinte 
O arhiva continand:
- Programe `.java`
- Un `.docx` cu descrierea programului.


Programul trebuie sa contina cat mai multe dintre subiectele prezentate:
- Fluxuri, serializare
- Fire de executare
- Clienti - Server
- Interfete grafice, evenimente
- Resurse comune pe server accesate "simulatan" de client
- Metode de rezolvare a problemelor de concurenta: monitoare, semafoare

### Descriere proiect
#### A fost implementat un joc simplu de **Loto 6/49** 🍀

**Serverul** genereaza numerele castigatoare la instantiere si apoi deschide conexiuni pentru **clienti**.

![screenshot server](docs/server-cli.png)

**Clientii**, prin interfata grafica, introduc numele si aleg cele 6 numere.
Aceste numere sunt trimise la server, unde se verifica daca sunt castigatoare.

![screenshot client](docs/client-ui.png)

#### 🥚 Easter egg
Foloseste numele `java` pentru a castiga la loto.


### Video demo
https://github.com/mihaituhari/fmi-retele/assets/12177811/bac202c2-8aa1-4276-a4cd-e6fee8edb79b
