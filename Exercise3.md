# Exercise 3

You can download the ex3 release to run the "api": https://github.com/MagicianVivi/kaiju/releases/download/ex1/kaiju-ex3.zip

or pin your repository to the ex3 tag: https://github.com/MagicianVivi/kaiju/tree/ex1

Once it runs, you can go to http://localhost:8083/docs to access the swagger UI, and test out the endpoint.

I only implemented an endpoint for getting the same results as the script with basic filters on the continent and category names.

By reworking the data model, I would also add filters by:
+ professions name or id
+ contract type
+ locations, though I would probably need to change the way the location check is done,
since my approximation of a continent would not work.

If I wanted to expose the list of jobs directly, rather than just the numbers by continent,
I would also add a way to paginate and order the results.

After that, next step is another endpoint for adding jobs to the database.
