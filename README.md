Exoplanets

Tervezett működés a triviális validáláson kívül:

Star: 

POST: nem lehet 2 egyforma nevű csillag, id (= null esetén is) generálódik
PUT:  Csak létező id-val lehet csillagot update-elni,
      egy csillagot nem lehet úgy update-elni, hogy az új név egy
      másik id-hoz tartozó csillag neve.
      Ha az id és a név nem változik csak egyéb adat, akkor azok helyesen felülíródnak.
DELETE: ha a csillagnak van bolygója nem törölhető. (miután töröltük az összes 


Planet:
 
POST, PUT: ugyanaz mint Star esetén
POST, PUT: +1: update esetén csak létező csillag id-t lehet neki megadni.
DELETE: akkor is törölhető, ha van holdja (az exoholdak detektálása még gyerekcipőben jár)
ilyenkor törlődik az összes holdjával együtt.


Moon:

POST, PUT: ugyanaz mint a Planet.
DELETE: Minden hold törölhető.