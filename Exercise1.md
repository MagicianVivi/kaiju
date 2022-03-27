# Exercise 1

You can download the ex1 release to run the script: https://github.com/MagicianVivi/kaiju/releases/download/ex1/kaiju-ex1.zip

If you want to check out the code, please pin your git repository to the ex1 tag: https://github.com/MagicianVivi/kaiju/tree/ex1


Fun fact, if you have a postgres instance running, you can create tables mapping to the csv columns, load them with `copy` and then run:


``` sql
select coalesce(c.name, 'unknown') as continent, coalesce(p.category, 'unknown') as category, count(distinct j.*)
from jobs j
left join professions p on p.id = j.profession_id
left join continents c on j.longitude > c.west and j.longitude < c.east and j.latitude > c.south and j.latitude < c.north
group by c.name, p.category;
```

to get the following table and partly check your results:

|   continent   |     category      | count |
|---------------|-------------------|-------|
| africa        | Admin             |     1 |
| africa        | Business          |     3 |
| africa        | Marketing / Comm' |     1 |
| africa        | Retail            |     1 |
| africa        | Tech              |     3 |
| asia          | Admin             |     1 |
| asia          | Business          |    30 |
| asia          | Marketing / Comm' |     3 |
| asia          | Retail            |     6 |
| asia          | Tech              |    11 |
| europe        | Admin             |   396 |
| europe        | Business          |  1372 |
| europe        | Conseil           |   175 |
| europe        | Créa              |   205 |
| europe        | Marketing / Comm' |   759 |
| europe        | Retail            |   426 |
| europe        | Tech              |  1402 |
| europe        | unknown           |    60 |
| north-america | Admin             |     9 |
| north-america | Business          |    27 |
| north-america | Créa              |     7 |
| north-america | Marketing / Comm' |    12 |
| north-america | Retail            |    93 |
| north-america | Tech              |    14 |
| north-america | unknown           |     1 |
| oceania       | Marketing / Comm' |     1 |
| oceania       | Retail            |     2 |
| south-america | Business          |     4 |
| south-america | Tech              |     1 |
| unknown       | Admin             |     4 |
| unknown       | Business          |     9 |
| unknown       | Marketing / Comm' |     6 |
| unknown       | Retail            |     8 |
| unknown       | Tech              |     8 |
| unknown       | unknown           |     8 |
