# Ranking dokumentów przy użyciu TF-IDF
Program na podstawie podanego zapytania wyszukuje dokumenty, które pasują do termów zawartych w zapytaniu.
Dokumenty są sortowane od najbardziej odpowiedniego do najmniej. Dokumenty, które nie pasują do zapytania (miara równa 0) nie są wyświetlane.
Program wczytuje słowa kluczowe oraz dokumenty z plików. Następnie wszystkie słowa zostają poddane stemmingowi oraz obliczane są wszystkie wartość TF i IDF.
W momencie podania przez użytkownika zapytania, obliczana jest miara cosinusa kąta (sim). Na podstawie miary kąta tworzony jest ranking najbardziej pasujących dokumentów do zapytania.
Możliwe jest wykorzystanie metody Rocchio do uaktualnienia wektora zapytania za pomocą liniowej kombinacji poprzednich zapytań oraz feedbacku użytkownika.

### Budowanie
Program został napisany przy użyciu języka Java oraz wykorzystano narzędzie Gradle do budowania i zarządzania projektem.

#### Budowanie projektu przy użyciu narzędzia Gradle:
```
gradle build
```

#### Tworzenie pliku jar:
```
gradle jar
```

### Argumenty programu
Aby wyświetlić pomoc dotyczącą programu, wystarczy uruchomić go z parametrem *-h* lub *--help*.
```
Usage: EZI [options]
  Options:
  * --document, -d
      name of a file containing all documents title and body
    --help, -h
      Shows program usage
  * --keywords, -k
      name of a file containing all the keywords that will be considered while 
      analyzing documents and querry
    --verbose, -v
      Run program in verbose mode. It will print more details than normally
      Default: false
```

### Uruchamianie programu
Pliki binarne znajdują się w folderze **build**. Do uruchomienia programu **nie** jest wymagane narzędzie Gradle.
#### Plik jar
Program można uruchomić na wiele sposobów. Najłatwiej uruchomić go przy użyciu pliku jar, który znajduje się w folderze **/build/libs**.

```
java -jar lab-1.0-SNAPSHOT.jar -d documents.txt -k keywords.txt -v
```

#### Pliki binarne
Pliki **.class* zbudowanego programu znajdują się w folderze **/build/classes**. Można uruchomić program przy ich użyciu ale należy również dołączyć zewnętrzne biblioteki znajdujące się w folderze **/build/libs/external**. Pliki zawierające treść potrzebną do uruchomienia programu (documents.txt i keywords.txt) znajdują się w folderze **build/resources/main**.
```
java -cp "classes/java/main/:libs/external/jcommander-1.72.jar" com.tfidf.Main -d resources/main/documents.txt -k resources/main/keywords.txt -v
```

#### Gradle run
Program można uruchomić również przy użyciu narzędzia gradle. Komenda musi być wykonywana w głównym katalogu projektu.
```
gradle run --args="-d src/main/resources/documents.txt -k src/main/resources/keywords.txt -v"
```

#### IDE
W momencie uruchamianie programu przy użyciu IDE należy zwrócić uwagę jakie jest ustawiony katalog roboczy. Zwykle jest to katalog główny projektu.

### Zakończenie programu
Aktualnie program działa w nieskończonej pętli, oczekując na input użytkownika. Aby zakończyć program należy wysłać sygnał np. SIGINT (ctrl+c).
