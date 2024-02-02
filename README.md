# Приложение "Расписание ТГУ" представляет собой приложение для просмотра расписания преподавателей кафедры "Прикладная математика и информатика"

## Функциональность:
- Загрузка списка преподавателей из API ITStudent.
- Отображение расписания занятий выбранного преподавателя за указанный период.

## Используемый стек

- Язык программирования: Java
- Версия Java: OpenJDK 21
- Графическая библиотека: Swing

## API

Для получения данных о преподавателях и расписании используется следующее API:

- Получение списка преподавателей по кафедре: [https://its.tltsu.ru/api/teachers/department/64](https://its.tltsu.ru/api/teachers/department/64)
- Получение расписания занятий преподавателя: [https://its.tltsu.ru/api/schedule/teacher?teacherId=%d&fromDate=%s&toDate=%s](https://its.tltsu.ru/api/schedule/teacher?teacherId=%d&fromDate=%s&toDate=%s)

## Инструкции по установке и запуску

1. Установите Java Development Kit (JDK) версии OpenJDK 21 или выше.
2. Клонируйте репозиторий проекта: `git clone https://github.com/gerasimovtltsu/ScheduleTLTSU.git`
3. Запустите приложение командой
```cmd
java -jar ScheduleTLTSU.jar
```

или

```cmd
javac ScheduleApp.java
```

## Используемые внешние зависимости
[org.json](http://stleary.github.io/JSON-java/index.html)

[launch4j](https://launch4j.sourceforge.net/)

## Скриншоты
![image](https://github.com/gerasimovtltsu/ScheduleTLTSU/assets/82588219/7e15c027-0249-4ef3-93b2-b74acdc35f4b)


## Лицензия

Проект распространяется под лицензией MIT.
