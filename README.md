# Hintahaukka

## CI

master: [![CircleCI](https://circleci.com/gh/Hintahaukka/application.svg?style=svg)](https://circleci.com/gh/Hintahaukka/application)

## Tests code coverage:

Master: [![codecov](https://codecov.io/gh/Hintahaukka/application/branch/master/graph/badge.svg)](https://codecov.io/gh/Hintahaukka/application)

Dev: [![codecov](https://codecov.io/gh/Hintahaukka/application/branch/dev/graph/badge.svg)](https://codecov.io/gh/Hintahaukka/application)

## Tavoite

Ohjelmistotuotantoprojektin tavoitteena on luoda pilvipalvelun kanssa kommunikoiva Android- ja iOS-laitteilla toimiva
mobiilisovellus liikkuvan ihmisen käyttöön. Sovelluksen tarkoitus on kertoa helposti kaupassa asioivalle henkilölle kyseisen
tuotteen hinta muissa kaupoissa. Tuotteiden hinnan tarkastus tehdään helpoksi tunnistamalla viivakoodi mobiililaitteen
kameralla.

## Repositoriot

* Android-sovellus (tämä repositorio, päärepositorio)
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
* Keskeisimpiä ominaisuuksia varten on tehty automaattiset testit
* Sovellus toimii laitteella
* Koodi on dokumentoitu yleisellä tasolla
* Uudet ominaisuudet ja merkittävät muutokset on katselmoitu
* CI on pystyssä ja testit menevät läpi
* Heroku buildaa ja toimii

## Asennusohjeet

1. Mene osoitteeseen https://github.com/Hintahaukka/application/releases
2. Paina sivulla näkyvää "Assets" nimistä valikkoa
3. Paina "app-dubug.apk" nimistä tiedostoa.

Asennustiedosto latautuu nyt laitteellesi. Kun tiedosto on ladattu siirrä se android puhelimeesi. Vaihtoehtoisesti voit ladata tiedoston suoraan puhelimeesi menemällä puhelimen selaimella kohdassa 1) olevaan osoitteeseen.

4. Seuraavaksi mene puhelimesi asetuksiin ja salli asennus tuntemattomista lähteistä. Android puhelimissa valinnan sijainti asetuksissa vaihtelee mutta esimerkiksi Huawei puhelimissa tämä tapahtuu siten että menee asetuksiin, valitsee suojaus ja sitten valitsee "Tuntematomat lähteet".
5. Kun sovellus on asennettu voit käynnistää sen valitsemalla sen sovellusvalikosta.


## Käyttöohjeet
1. Laita puhelimen GPS päälle
2. Avaa sovellus
3. Anna sovellukselle lupa käyttää sijaintitietoja
4. Valitse valikosta kauppa
5. Paina "SKANNAA VIIVAKOODI" nappia
6. Skannaa tuotteen viivakoodi puhelimen kameralla
7. Syötä tuotteen hinta ja paina "Lähetä"
8. Tuotteen hinta muissa kaupoissa tulee näkyville.

## Credits

TODO

## Lisenssi

[MIT License](LICENSE)
