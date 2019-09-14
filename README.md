# Hintahaukka

## CI

### Application

master: [![CircleCI](https://circleci.com/gh/Hintahaukka/application.svg?style=svg)](https://circleci.com/gh/Hintahaukka/application)

### Backend

master: [![Build Status](https://travis-ci.org/Hintahaukka/backend.svg?branch=master)](https://travis-ci.org/Hintahaukka/backend)
dev: [![Build Status](https://travis-ci.org/Hintahaukka/backend.svg?branch=dev)](https://travis-ci.org/Hintahaukka/backend)

## Tavoite

Ohjelmistotuotantoprojektin tavoitteena on luoda pilvipalvelun kanssa kommunikoiva Android- ja iOS-laitteilla toimiva
mobiilisovellus liikkuvan ihmisen käyttöön. Sovelluksen tarkoitus on kertoa helposti kaupassa asioivalle henkilölle kyseisen
tuotteen hinta muissa kaupoissa. Tuotteiden hinnan tarkastus tehdään helpoksi tunnistamalla viivakoodi mobiililaitteen
kameralla.

## Repositoriot

* Android-sovellus (tämä repositorio)
* [Backend](https://github.com/Hintahaukka/backend/tree/master)

## Dokumentit

* [Product backlog](https://docs.google.com/spreadsheets/d/1Mazq4EFbfbMsLPeCpOckbu11LNR1Ki2RiNf460z-rpU/edit#gid=0)
* Sprint backlogit löytyvät samasta tiedostosta välilehdiltä
* Tuntikirjanpito löytyy sprinttien yhteydestä ja kootusti omalta välilehdeltään

## Branch -käytännöt

Uusia ominaisuuksia varten tehdään aina oma branch. Kun ominaisuus on valmis, se pushataan dev-branchiin ja varmistetaan, että kaikki toimii. Lopuksi dev-branch pushataan masteriin.

## Definition of Done

User story katsotaan valmiiksi kun seuraavat ehdot täyttyvät:

* User storyn hyväksymiskriteerit täyttyvät
* Jos on automaattisia testejä, ne menevät läpi
<!--
* The line coverage of tests is decent  
-->
* Sovellus toimii laitteella
* Koodi on dokumentoitu yleisellä tasolla
* Jos CI on pystyssä, se toimii
* Heroku buildaa ja toimii

## Asennusohjeet

TODO

## Käyttöohjeet

TODO

## Credits

TODO

## Lisenssi

[Apache License, Version 2.0](https://www.apache.org/licenses/LICENSE-2.0)
