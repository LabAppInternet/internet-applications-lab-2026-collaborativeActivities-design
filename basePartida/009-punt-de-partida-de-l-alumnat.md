# 009 · El punt de partida de l'alumnat

**Data:** 2026-09-05 · **Estat:** acceptada

## Context
Cinc anys impartint l'assignatura, i Josep més. El que segueix no és una hipòtesi
sobre qui tenim a l'aula: és observació acumulada del professorat, i explica
decisions del catàleg que altrament semblen arbitràries.

## El que l'alumnat ja porta
**Spring Framework bàsic**, d'una assignatura anterior: arrencar una aplicació
Spring Boot, escriure controladors, API REST senzilles, injecció de dependències.

No és poc i condiciona el disseny en positiu: **l'estructura per capes i el
controlador no són territori nou**. La sessió 1 pot demanar un endpoint que
travessi tota la pila el primer dia perquè aquesta part ja la coneixen.

## El que NO porta
- **JPA i el mapatge objecte-relacional.** Cap contacte previ. `@Entity` es veu
  per primera vegada a UC-01.
- **Spring Security.** Cap contacte previ.
- **Concurrència aplicada a Java real.** Poden haver vist fils i exclusió mútua
  com a teoria; no han vist mai una condició de carrera sobre una base de dades
  ni han escrit un test que la provoqui.

## Decisió
**No s'apilen dos frameworks nous a la mateixa setmana.** En particular, **JPA i
Spring Security no comparteixen la sessió d'entrada**.

## Motiu
Els tres blocs que falten —persistència, seguretat i concurrència— són cars cadascun
pel seu compte. El cost no surt del nombre de conceptes sinó de **quants n'has de
sostenir alhora**: mapatge, sessió de persistència, cadena de filtres, signatura de
testimonis i caducitat, tot la mateixa tarda, és la recepta coneguda per perdre
equips a la segona setmana.

L'observació de cinc anys va en aquesta direcció i la teoria de la càrrega
cognitiva també. **Cap de les dues és una mesura feta aquí**, i convé dir-ho: el
que tenim és experiència coherent amb la literatura, no evidència pròpia.

## Conseqüències
- **N0 pot donar per sabut el controlador REST i la injecció de dependències.** No
  pot donar per sabut res de persistència.
- **La sessió 2 no pot portar el primer mapatge JPA i la cadena de filtres alhora.**
  És el conflicte concret que es deriva d'aquesta decisió, i és la tasca 2 de la
  reunió del 4 de setembre.
- **La sessió 3 és l'escaló més alt de la primera meitat del curs**: demana mesurar
  i corregir un N+1 una sessió després del primer mapatge d'entitats. No es pot
  rebaixar sense renunciar al bloc d'eficiència, que és nuclear.
- **El repositori llavor ha de portar un exemple de mapatge complet** que serveixi
  de model, no només la configuració. És andamiatge que redueix càrrega sense
  abaixar l'exigència.
- **Les eines que no son contingut es queden invisibles.** Flyway n'es el cas
  clar: el semilla porta l'esquema versionat i la migracio inicial escrita, a N0
  es copia un fitxer seguint l'exemple i el taller no hi dedica temps. El mecanisme
  es UC-38, que es Extensio. La regla val per a qualsevol eina que no sigui la
  llico del dia.
- **Cronometrar N0 puja de prioritat**: ara no només diu si l'abast hi cap, sinó si
  dues sessions basten per a un primer contacte amb JPA.

## Pendent
El **mecanisme concret** per treure Spring Security de la sessió 2. La proposta és
[`propostes/001`](../propostes/001-ajornament-de-la-seguretat.md): separar la
identitat de l'actor de l'autenticació, de manera que les regles de propietat i
els seus tests s'escriguin des de la sessió 2 i el dia que entri Spring Security
no calgui tocar-los. **Pendent de valorar entre els dos.**
