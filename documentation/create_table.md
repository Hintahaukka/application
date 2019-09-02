CREATE TABLE Product (

id INTEGER PRIMARY KEY,  

ean VARCHAR(13), 

name VARCHAR(50) 

);


CREATE TABLE Store (

id INTEGER PRIMARY KEY, 

name VARCHAR(50), 

address VARCHAR(50), 

lat DOUBLE, 

lon DOUBLE

);

CREATE TABLE Price (

id INTEGER PRIMARY KEY,

product_id INTEGER NOT NULL,

store_id INTEGER NOT NULL,

cents INTEGER, 

created DATETIME, 

FOREIGN KEY(product_id) REFERENCES Product (id), 

FOREIGN KEY(store_id) REFERENCES Store (id), 

);
