# EZI - laboratorium
## 1. Ranking dokumentów przy użyciu TF-IDF
Program na podstawie podanego zapytania wyszukuje dokumenty, które pasują do termów zawartych w zapytaniu.
Dokumenty są sortowane od najbardziej odpowiedniego do najmniej. Dokumenty, które nie pasują do zapytania (miara równa 0) nie są wyświetlane.
Program wczytuje słowa kluczowe oraz dokumenty z plików. Następnie wszystkie słowa zostają poddane stemmingowi oraz obliczane są wszystkie wartość TF i IDF.
W momencie podania przez użytkownika zapytania, obliczana jest miara cosinusa kata (sim).

### Budowanie
Program został napisany przy użyciu języka Java oraz wykorzystano narzędzie Gradle do budowania i zarządzania projektem.

####Budowanie projektu przy użyciu narzędzia Gradle:
```
gradle build
```

####Tworzenie pliku jar:
```
gradle jar
```

###Uruchamianie programu
Pliki binarne znajdują się w folderze **build**. Do uruchomienia programu **nie** jest wymagane narzędzie Gradle.
####Plik jar
Program można uruchomić na wiele sposobów. Najłatwiej uruchomić go przy użyciu pliku jar, który znajduje się w folderze **/build/libs**.

```
java -jar lab-1.0-SNAPSHOT.jar -d documents.txt -k keywords.txt -v
```

####Pliki binarne
Pliki **.class* zbudowanego programu znajdują się w folderze **/build/classes**. Można uruchomić program przy ich użyciu ale należy również dołączyć zewnętrzne biblioteki znajdujące się w folderze **/build/libs/external**. Pliki zawierające treść potrzebną do uruchomienia programu (documents.txt i keywords.txt) znajdują się w folderze **build/resources/main**.
```
java -cp "classes/java/main/:libs/external/jcommander-1.72.jar" com.lab1.Main -d resources/main/documents.txt -k resources/main/keywords.txt -v
```

####Gradle run
Program można uruchomić również przy użyciu narzędzia gradle. Komendę wykonywana musi być w głównym katalogu projektu.
```
gradle run --args="-d src/main/resources/documents.txt -k src/main/resources/keywords.txt -v"
```

####IDE
W momencie uruchamianie programu przy użyciu IDE należy zwrócić uwagę jakie jest ustawiony katalog roboczy. Zwykle jest to katalog główny projektu.

###Zakończenie programu
Aktualnie program działa w nieskończonej pętli, oczekując na input użytkownika. Aby zakończyć program należy wysłać sygnał np. SIGINT (ctrl+c).
