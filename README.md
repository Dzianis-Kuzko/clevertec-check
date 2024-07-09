<h1 align="center">CLEVERTEC</h1>
<h2 align="center"> Test task - Check</h2>


## Task Description

### [📝 Task](./documentation/test_task_for_course_clevertec.pdf)

###  Main Technologies

**-java 21**


## Getting Started

### - Task 1 -
* Clone the branch

```console
git clone --single-branch -b feature/entry-core https://github.com/Dzianis-Kuzko/clevertec-check.git
```

* Go  to the "src"  folder
* Compile the application
```console
javac -d .\out\src main\java\ru\clevertec\check\CheckRunner.java
```
* Add the resources  folder  with the files
* Go  to the "out"  folder
* Run the application
```console
java -cp src main.java.ru.clevertec.check.CheckRunner 1-3 13-5 1-2 9-1 4-10 4-20 1-1 discountCard=1111 balanceDebitCard=100.48
```
### - Task 2 -
* Clone the branch

```console
git clone --single-branch -b feature/entry-file https://github.com/Dzianis-Kuzko/clevertec-check.git
```

* Go  to the "src"  folder
* Compile the application
```console
javac -d .\out\src main\java\ru\clevertec\check\CheckRunner.java
```
* Add the resources  folder  with the files
* Go  to the "out"  folder
* Run the application
```console
java -cp src main.java.ru.clevertec.check.CheckRunner 1-3 13-5 1-2 9-1 4-10 4-20 1-1 discountCard=1111 balanceDebitCard=100.48 pathToFile=./src/main/resources/products.csv saveToFile=./src/main/result.csv
```
### - Task 3 -
* Clone the branch
```console
git clone --single-branch -b feature/entry-database https://github.com/Dzianis-Kuzko/clevertec-check.git
```
* Go  to the "clevertec-check"  folder
* Run the application
```console
java -jar clevertec-check.jar 1-3 13-5 1-2 9-1 4-10 4-20 1-1 discountCard=1111 balanceDebitCard=100.48 saveToFil
e=result.csv datasource.url=jdbc:postgresql://localhost:5544/check datasource.username=postgres datasource.password=postgres
```









