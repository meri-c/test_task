# Test task
## This is a test_task repo with "one api calls two others api from different threads" program.
-----------

## How to run

1. Load the test_task project and make sure that port 8080 is available (application runs on localhost:8080).
2. Load the test_task_server project.
3. Make sure your 2300 and 3307 ports are free.
4. Build the server using docker-compose and run it.
5. Run the application and make a get request with the following link http://localhost:8080/start

## Structure

### 1. Config
File with beans: connection to elasticsearch, beans with supplier get method for CompletableFuture thread.

### 2. Controller
RestController class with start api method, setups for calling externals api, calling external api itself and returning
the result with found data.

### 3. Entity 
Entity objects for mysql and elasticsearch.

### 4. Repository
Mysql and elastic search repository interfaces for CRUD operations.

### 5. Service
Folder with all the services and objects, which take part in external api calling.


### 6. Tests
Since this is a test_task, it has two tests: for jpa repository and for external api caller, just for example.


 
    
