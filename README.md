Spring Boot JPA CRUD Example
====================================
### Example Usage
```
$ git clone git@github.com:sdavids13/action-recorder.git
$ cd action-recorder
$ ./gradlew bootRun
```

```
$ curl --user user:example "http://localhost:8080/?size=1&page=2&sort=objectUri&sort=createDate,DESC" 2> /dev/null | python -m json.tool
{
    "_embedded": {
        "actionList": [
            {
                "_links": {
                    "self": {
                        "href": "http://localhost:8080/2"
                    }
                },
                "createDate": "2016-06-09T03:39:30Z",
                "id": 2,
                "objectType": "BOOKMARK",
                "objectUri": "http://google.com",
                "user": "user",
                "verb": "SAVE"
            }
        ]
    },
    "_links": {
        "first": {
            "href": "http://localhost:8080?page=0&size=1&sort=objectUri,asc&sort=createDate,desc"
        },
        "last": {
            "href": "http://localhost:8080?page=3&size=1&sort=objectUri,asc&sort=createDate,desc"
        },
        "next": {
            "href": "http://localhost:8080?page=3&size=1&sort=objectUri,asc&sort=createDate,desc"
        },
        "prev": {
            "href": "http://localhost:8080?page=1&size=1&sort=objectUri,asc&sort=createDate,desc"
        },
        "self": {
            "href": "http://localhost:8080"
        }
    },
    "page": {
        "number": 2,
        "size": 1,
        "totalElements": 4,
        "totalPages": 4
    }
}
```

```
$ curl --user user:example -X POST -d "verb=MARK_AS_LIKED" -d "objectType=BOOKMARK" -d "objectUri=https://www.bti360.com" http://localhost:8080/ 2> /dev/null | python -m json.tool
{
    "_links": {
        "self": {
            "href": "http://localhost:8080/5"
        }
    },
    "createDate": "2016-06-09T03:39:51Z",
    "id": 5,
    "objectType": "BOOKMARK",
    "objectUri": "https://www.bti360.com",
    "user": "user",
    "verb": "MARK_AS_LIKED"
}
```

```
$ curl --user user:example http://localhost:8080/5 2> /dev/null | python -m json.tool
{
    "_links": {
        "self": {
            "href": "http://localhost:8080/5"
        }
    },
    "createDate": "2016-06-09T03:39:51Z",
    "id": 5,
    "objectType": "BOOKMARK",
    "objectUri": "https://www.bti360.com",
    "user": "user",
    "verb": "MARK_AS_LIKED"
}
```

```
$ curl --user user:example -X GET "http://localhost:8080/?objectUri=https://www.bti360.com" 2> /dev/null | python -m json.tool
{
    "_embedded": {
        "actionList": [
            {
                "_links": {
                    "self": {
                        "href": "http://localhost:8080/5"
                    }
                },
                "createDate": "2016-06-09T03:54:16Z",
                "id": 5,
                "objectType": "BOOKMARK",
                "objectUri": "https://www.bti360.com",
                "user": "user",
                "verb": "MARK_AS_LIKED"
            }
        ]
    },
    "_links": {
        "self": {
            "href": "http://localhost:8080"
        }
    },
    "page": {
        "number": 0,
        "size": 20,
        "totalElements": 1,
        "totalPages": 1
    }
}
```

```
$ curl --user user:example -X DELETE -I http://localhost:8080/5 2> /dev/null
HTTP/1.1 204 No Content
.....
```

```
$ curl --user user:example -X GET -I http://localhost:8080/5 2> /dev/null
HTTP/1.1 404 Not Found
.....
```

### Admin endpoints
For list of available admin endpoints for the running application see: http://localhost:8080/actuator
