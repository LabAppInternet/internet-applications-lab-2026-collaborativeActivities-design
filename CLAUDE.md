# Context del projecte

Disseny docent de **Laboratori d'Aplicacions Internet** (TecnoCampus · UPF),
6 ECTS, tercer curs, primer trimestre, curs 2026-2027. Professorat: Josep Roure
i Alfredo Rueda.

Aquest repositori conté **disseny d'activitats amb tècniques d'aprenentatge col·laboratiu'**, no codi de producció.

## Restriccions del projecte que es dissenya

Aquestes restriccions són decisions preses, no preferències obertes. No proposar
alternatives que les contradiguin sense avisar explícitament que ho fan.

- **Monolit.** Un sol servei Spring Boot i una sola base de dades PostgreSQL.
  Microserveis i sistemes distribuïts són contingut del tercer trimestre.
- **Arquitectura per capes.** Controlador, servei, repositori, model. Ports i
  adaptadors (hexagonal) són contingut del segon trimestre.
- **Entitats de domini riques**, no model anèmic.
- **Un únic mecanisme de concurrència al nucli**: l'`UPDATE` condicional decidint
  pel nombre de files afectades. Ni bloqueig optimista, ni pessimista, ni nivells
  d'aïllament. No hi ha columna `version` enlloc. Vegeu `decisions/004`.
- **Sense herència d'entitats.** Composició. Vegeu `decisions/005`.
- **Corba de dificultat progressiva**: els primers casos d'ús són senzills a
  propòsit. Vegeu `decisions/003`.
- **Punt de partida de l'alumnat.** Porten **Spring Framework bàsic** d'una
  assignatura anterior —API REST senzilles, controladors, injecció—. **No han vist
  mai JPA, ni Spring Security, ni concurrència aplicada a Java real.** D'aquí surt
  una regla de disseny: **no s'apilen dos frameworks nous a la mateixa setmana**, i
  en particular JPA i Spring Security no comparteixen sessió d'entrada. Vegeu
  `decisions/009`.
- **L'assignatura s'imparteix en anglès.** Els documents estan ara en castellà
  per treballar-los més àgilment; la versió final anirà en anglès, identificadors
  de codi inclosos.

## Flux de treball dels documents

**El contingut s'evoluciona en Markdown. L'HTML esta congelat.**

- La font de veritat es `markdown/*.md`, amb els diagrames en Mermaid. Qualsevol
  millora, correccio o contingut nou va aqui. S'edita a ma o, mes sovint, des de
  l'IDE amb el Claude Code integrat —millor a VS Code que a IntelliJ IDEA—: prompts
  i el fitxer Markdown es modifica.
- `disseny/*.html` **no s'edita mai**. Es conserva perque fixa la guia d'estil del
  material —les fitxes de cas d'us, les etiquetes de nivell, els colors dels verbs
  HTTP, les barres de la rubrica— i aquest disseny val molt. Es referencia visual,
  no contingut viu. Els fitxers estan en nomes lectura al disc.
- `tools/html2md.py` va fer la conversio una sola vegada i **no s'ha de tornar a
  executar**: sobreescriuria la font de veritat. Porta un guardia que ho impedeix.
- **El generador ja existeix**: `tools/md2pdf.py` produeix HTML i PDF des del
  Markdown aplicant el sistema de disseny de `disseny/`. Els PDF son a `pdf/` i es
  regeneren, mai s'editen. El mateix generador servira per als enunciats de
  l'alumnat: nomes canviara el Markdown d'entrada.

Si una peticio implica tocar un fitxer de `disseny/`, atura't i digues-ho. El canvi
va al Markdown.

## Aprenentatge Col·laboratiu

## Registre i to

- Registre d'enginyeria i acadèmic. Res de to editorial ni sensacionalista.
- Terminologia precisa: *lost update*, *write skew*, *read committed*.
- Les afirmacions es respalden amb mesura, no amb èmfasi. Si una xifra no està
  mesurada, dir-ho.
