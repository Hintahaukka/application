# Applikaation testaus ja kehitys 

Tämä tiedosto käsittää front-end applikaation testaukseen, versionhallintaan ja jatkuvaan integraatioon liittyviä ohjeita.

Applikaatio on toteutettu Java ohjelmointikielellä ja sen testaus jakautuu yksikkötesteihin (Unit tests) ja toimintotestauksiin (Espressotest).

# Kehitysympäristö (CI)

## Versionhallinta GitHub.com

Versionhallinta sekä front-end applikaatiolle että back-end ohjelmistolle on toteutettu tässä "Hintahaukka" nimisessä Github organisaatiossa jakautuneena erillisiin repositioihin "application" ja "backend". 

Tuotantoversiolle on varattu master branch. Demottaessa applikaatiota käytetään aina master branchista muodostettu APK pakettia. Jotta koodi voidaan siirtää masteriin, tulee uudelle ominaisuudelle olla riittävästi testejä ja dev branchiin pull requestilla mergetyn koodin tulee läpäistä testit käytetyssä CircleCI testausympäristössä.

Dev branch on tarkoitettu uusien ominaisuuksien yhdistämiseen olemassa olevaan versioon. Kaikki uudet ominaisuudet kehitetään omissa _feature brancheissä ja niiden tulee läpäistä testit CircleCI ympäristössä ennen pull requestia dev branchiin.

Uutta ominaisuutta kehitettäessä kloonataan dev branchin koodi omaan _feature branchiin, jossa koodi katselmoidaan ennen pull requestia dev branchiin.

## Testausympäristö CircleCI - GOOGLE CLOUD - FIREBASE TEST LAB

Front-end applikaaton testaus suoritetaan käyttämällä CircleCI kehitysympäristöä. CircleCI:n tilin on perustanut Toivo Kärkinen, asetukseksi on laitettu, että kaikki GitHub organisaatioon kuuluvat pääsevät tekemään muutoksia CircleCI asetuksiin.

GitHubissa tehty push tai pull requestin mergeäminen aiheuttaa pipelinen ajon CircleCi ympäristössä, tämän mahdollistaa "checkout SSH key" -asetus. Ajon aikana suoritetut käskyt annetaan hakemistossa .circleci olevassa tiedostossa config.yml. CircleCI -projektilla on asetukset, joista tärkeimmät on asetetut ympäristömuuttujat:

<dl>
  <dt> CIRCLE_BRANC </dt>
  <dd> Tätä CircleCi käyttää automaattisesti. Git branch, jota suoritetaan/ viimeksi suoritettu.</dt>
  <dt> CODECOV_TOKEN</dt>
  <dd> Codecov projektin tunnus, jacocoraportin lataamiseksi palveluun, jotta saadaan testikattavuustulokset.</dt>
  <dt> GCLOUD_BUCKET</dt>
  <dd> Google cloudin bucket, johon projekti pystyy tallentamaan tiedostoja</dt>
  <dt> GCLOUD_RES</dt>
  <dd> Firebase testitulosten hakemisto Google cloud bucketissa, jotta voidaan ladata ne edelleen CircleCi palvelimelle</dt>
  <dt> GCLOUD_RESULT</dt>
  <dd> Aiempi vähemmän testituloksia sisältävä hakemisto, kts GCLOUD_RES</dt>
  <dt> GCLOUD_SERVICE_KEY</dt>
  <dd> Api-key json salatussa muodossa. Puretaan ympäristömuuttujaksi CircleCI pipelinessa ja välitetään Google Cloudille   e   kirjautumisen mahdollistamiseksi. Tili on maksullinen, joten poistan tämän arvon lähipäivinä.</dt>
  <dt> GOOGLE_COMPUTE_ZONE</dt>
  <dd> GCLOUD palvelimen sijainti</dt>
  <dt> GOOGLE_PROJECT_ID</dt>
  <dd> Google Cloud palvelun projekti id</dt>
  <dt> GOOGLE_SERVICES_KEy</dt>
  <dd> Api-key json salatussa muodossa. Puretaan ympäristömuuttujaksi CircleCI pipelinessa ja välitetään Google Cloudille    Firebase kirjautumisen mahdollistamiseksi. Tili on maksullinen, joten poistan tämän arvon lähipäivinä. </dt>
</dl>

### Pipeline

CircleCI pipeline suoritus lyhyesti. Version valinta. Codecov orb käynnistetään. Ladataan android image, joka tukee Gcloudia. Asetetaan ympäristöön työhakemisto, pinon muistimäärä ja ympäristömuuttujat (parameters). Tarkistetaan muutokset vertaamalla aiempaa cachea (jos tallessa) vs GitHub branch ja buildataan applikaatio. Tarvittavat lisenssit hyväksytään ja cache tallennetaan. Google palvelujen api_key muuttujien salaus puretaan ja ne tallenetaan tiedostoihin saataville ajon aikana (huomaa nämä poistetaan lähipäivinä ja sen jälkeen pipeline ei etene, ennen kuin asetat oman tilisi api_key jsonit vastaavasti tai muutat koko ajoa.). Tili auktentitoidaan ja gcloud projekti valmistellaan.

CircleCI palvelimella ajetaan .gradlew lint testi sekä .gradlew build androidAssembleTest. CircleCI ei kuitenkaan tue emulaattoreita, joten testejä ei ajeta laiteympäristössä. Testiraporteista saadaan myös koodikattavuus raportti Codecov palveluun.

Google Firebase Test Lab antaa mahdollisuuden suorittaa testejä laiteympäristössä. Tämän vuoksi pitää olla Google account luotuna Google Cloud Platformissa ja edelleen kun gcloudissa ajetaan koodi

```
  sudo gcloud firebase test android run \
                --type=<< parameters.test_type >> \
                --app=<< parameters.apk_path >> \
                --test=<< parameters.test_apk_path >> \
                --directories-to-pull=<<parameters.dir_pull_path>> \
                --results-bucket=<<parameters.bucket_name>>-${GOOGLE_PROJECT_ID} \
                --results-dir=results \
                --environment-variables \
                coverage="true",coverageFile="/sdcard/coverage.ec"  
```
Olennaiset asetukset tässä on result_bucket ja result_dir, näiden avulla testin tulokset saadaan takaisin Gcloudiin.

gsutil työkalulla tulokset ladataan edelleen CircleCi palvelimelle ja artifacts tallentaa ne pysyvästi saataville.

Koodikattavuus raportin muodostaminen ei tämän projektin aikana onnistunut, kun emme keksineet miten *.ec muodosta saisi muodostettua codecovin tunnistaman xml tiedoston. Onnea tuleville kehittäjille, Topi.
