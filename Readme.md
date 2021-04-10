# Instructies

## Applicatie draaien 

Default in-memory DB:
```shell
java -jar assignment-0.0.1-SNAPSHOT.jar
```


File-based DB ('./local-database'):
```shell
java -jar -Dspring.profiles.active=file-based-h2 assignment-0.0.1-SNAPSHOT.jar
```

## Productie Installatie

### Opslaan

```shell
curl --location --request POST 'localhost:8080/productie-installatie' \
--header 'Content-Type: application/json' \
--data-raw '{
  "name": "My installation",
  "contact": 123,
  "outputPower": 0.123
}
'
```

### Alles opzoeken
```shell
curl --location --request GET 'localhost:8080/productie-installatie' 
```

### Zoeken met naam
```shell
curl --location --request GET 'localhost:8080/productie-installatie?name=My%20installation'
```

### Min / Max Output query
```shell
url --location --request GET 'localhost:8080/productie-installatie?minOutputPower=10.2225&maxOutputPower=5896.0001'
```

## Contact Persoon

### Opslaan
```shell
curl --location --request POST 'localhost:8080/contact-persoon' \
--header 'Content-Type: application/json' \
--data-raw '{
  "name": "My installation",
  "zipCode": "0000AA",
  "city": "Arnhem",
  "number": "12a"
}
'
```

### Alles opzoeken
```shell
curl --location --request GET 'localhost:8080/contact-persoon'
```

### Zoeken met naam

```shell
curl --location --request GET 'localhost:8080/contact-persoon?name=My%20installation'
```


