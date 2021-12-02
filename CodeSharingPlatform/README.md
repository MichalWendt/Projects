POST examples:

POST <http://localhost:8889/api/code/new>

Body:

{

  "code": "class Code { ...",

  "time": 0,

  "views": 0

}

Or:

{

  "code": "class Code2 { ...",

  "time": 100,

  "views": 10

}

Response:

{ "id" : "2187c46e-03ba-4b3a-828b-963466ea348c" }

GET examples:

GET <http://localhost:8889/api/code/2187c46e-03ba-4b3a-828b-963466ea348c>

Or

GET <http://localhost:8889/code/2187c46e-03ba-4b3a-828b-963466ea348c>

Response:

{

  "code": "Secret code",

  "date": "2020/05/05 12:01:45",

  "time": 95,

  "views": 9

}

GET <http://localhost:8889/api/code/latest>

Or

GET <http://localhost:8889/code/latest>

Response:

[

{

  "code": "public static void ...",

  "date": "2020/05/05 12:00:43",

  "time": 0,

  "views": 0

},

{

  "code": "class Code { ...",

  "date": "2020/05/05 11:59:12",

  "time": 0,

  "views": 0

}

]



