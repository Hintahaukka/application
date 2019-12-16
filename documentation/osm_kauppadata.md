# Open Street Map -kauppadata

Sovelluksessa tarvitaan tietoa kauppojen nimistä ja sijainneista. Tiedot on haettu sovelluksen käyttöön [Open Street Mapista](https://wiki.openstreetmap.org/wiki/Main_Page). Data on tallennettu sovelluksen muistiin [OSM XML-muodossa](https://wiki.openstreetmap.org/wiki/OSM_XML).

Sovellukseen ei ole tällä hetkellä toteutettu kauppadatan automaattista päivitystä, joten Open Street Mapiin tehdyt lisäykset ja muutokset eivät automaattisesti päivity sovellukseen. Tiedot voi kuitenkin päivittää manuaalisesti.

## OSM XML-tiedoston lataaminen halutulta alueelta

Open Street Mapin dataa voi ladata OSM XML -muodossa haluamaltaan alueelta. Tämä sovellus käyttää toistaiseksi pelkästään Suomen alueen dataa. Ohjeet koko maailman kattavan datan ja esimerkiksi valtion mukaan rajatun datan lataamiseen on annettu [Open Street Mapin wikissä](https://wiki.openstreetmap.org/wiki/Planet.osm). Varsinaisia lataustiedostoja löytyy useasta eri osoitteesta; sovelluksen tekohetkellä Suomen kattava data on ladattu [Geofabrikin kautta](https://download.geofabrik.de/europe/finland.html) pakattuna pbf-tiedostona.

## Kauppadatan irrottaminen OSM-tiedostosta

OSM-tiedoston käsittelyyn voi käyttää esimerkiksi [Osmosis](https://wiki.openstreetmap.org/wiki/Osmosis)-työkalua. 

Osmosiksella kaupat voi irrottaa erilliseksi tiedostoksi Suomen kattavasta finland-latest.osm.pbf -nimisestä tiedostosta seuraavasti:

> osmosis --read-pbf \finland-latest.osm.pbf --node-key-value keyValueList="shop.alcohol,shop.bakery" --write-xml stores.osm

missä keyValueList pitää sisällään kaikki halutut kauppatyypit (esimerkissä vain shop.alcohol ja shop.bakery). Ajankohtainen lista kaikista shop-avaimen mahdollisista arvoista kannattaa tarkistaa [wikistä](https://wiki.openstreetmap.org/wiki/Key:shop). Tällä hetkellä sovelluksessa on käytetty kaikkia osoissa "Food, beverages" sekä "General store, department store, mall" lueteltuja kauppatyyppejä.

## Irrotetun kauppadatan lisääminen sovellukseen

Halutut kaupat on nyt saatu erilliseen stores.osm -nimiseen tiedostoon. Kaupat saa sovelluksen käyttöön sijoittamalla kyseinen tiedosto sovellukseen polkuun app/src/main/assets. Tiedoston nimeä ei kannata muuttaa, jotta sovellus toimii oikein.
