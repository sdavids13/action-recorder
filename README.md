```
$ git clone git@github.com:sdavids13/action-recorder.git
$ cd action-recorder
$ ./gradlew bootRun

$ curl --user user:example http://localhost:8080/?size=1 2> /dev/null | python -m json.tool
{
    "_embedded": {
        "actionList": [
            {
                "createDate": 1465358270257,
                "id": 1,
                "objectType": "BOOKMARK",
                "objectUri": "http://google.com",
                "user": "user",
                "verb": "PLAY"
            }
        ]
    },
    "_links": {
        "first": {
            "href": "http://localhost:8080?page=0&size=1"
        },
        "last": {
            "href": "http://localhost:8080?page=3&size=1"
        },
        "next": {
            "href": "http://localhost:8080?page=1&size=1"
        },
        "self": {
            "href": "http://localhost:8080"
        }
    },
    "page": {
        "number": 0,
        "size": 1,
        "totalElements": 4,
        "totalPages": 4
    }
}
```

