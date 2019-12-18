# Hintahaukka

## CI

### Application

master: [![CircleCI](https://circleci.com/gh/Hintahaukka/application.svg?style=svg)](https://circleci.com/gh/Hintahaukka/application)

#### Testikattavuus:

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
* Tuntikirjanpito löytyy sprinttien yhteydestä ja [kootusti omalta välilehdeltään](https://docs.google.com/spreadsheets/d/1Mazq4EFbfbMsLPeCpOckbu11LNR1Ki2RiNf460z-rpU/edit#gid=1976084857)
* [Ohje](https://github.com/Hintahaukka/application/blob/master/documentation/osm_kauppadata.md) Open Street Map -kauppadatan päivittämiseen
* [App testaus ja kehitys ](https://github.com/Hintahaukka/application/blob/master/documentation/test_and_CI.md) Applikaation testaus ja kehitysympäristön ohjeet

## Branch -käytännöt

Uusia ominaisuuksia varten tehdään aina oma branch. Kun ominaisuus on valmis, branch yhdistetään pull reqestin kautta dev-branchiin ja varmistetaan, että kaikki toimii. Sprintin lopuksi dev-branch pushataan masteriin.

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
4. Ensimmäisellä käyttökerralla lisää itsellesi nimimerkki

### Tuotteen hintahaku ja lisääminen ostoskoriin
1. Valitse valikosta kauppa, jossa olet
2. Paina "SKANNAA VIIVAKOODI" nappia
3. Skannaa tuotteen viivakoodi puhelimen kameralla
4. Syötä tuotteelle halutessasi nimi, jos sovellus kysyy sitä
5. Syötä tuotteen hinta ja paina "Lähetä"
6. Tuotteen hinta muissa kaupoissa tulee näkyville.
7. Lisää tuote halutessasi ostoskoriin painamalla "Lisää ostoskoriin"

### Ostoskori
1. Valitse sivuvalikosta "Ostoskori"
2. Ostoskorin sisältö tulee näkyville
3. Poista tuote ostoskorista pyyhkäisemällä se vasemmalle
4. Klikkaamalla ostoskorista tuotetta näet tuotteen hinnat eri kaupoissa
5. Klikkaamalla ostoskorista "Hae parhaat hinnat" näet koko ostoskorin kokonaishinnat eri kaupoissa
6. Valitsemalla tämän jälkeen yhden listalla näkyvistä kaupoista näet korissa olevien tuotteiden tarkemmat hinnat kyseisessä kaupassa

### Ahkerimmat hintahaukat
1. Kerää itsellesi pisteitä lisäämällä hintoja ja tuotenimiä
2. Katso eniten pisteitä keränneet nimimerkit valitsemalla sivuvalikosta Ahkerimmat Hintahaukat
3. Skannatessasi tuotteita kaupassa näet mitkä nimimerkit ovat keränneet eniten pisteitä kyseisessä kaupassa

<!--
## Credits

TODO
-->

## Lisenssi

[MIT License](LICENSE)
