# План автоматизации тестирования возможности покупки тура

---

### Описание автоматизируемых сценариев:
Цель автоматизации — проверить корректность работы формы оплаты тура при различных сценариях использования дебетовых карт. Все тесты будут проводиться на странице http://localhost:8080/. Каждый тест начинается с открытия указанного URL и нажатия кнопки «Купить».

---

## Перечень автоматизируемых сценариев:

### Сценарии перехода с главной страницы сайта (http://localhost:8080/) к форме ввода данных карты:

**Сценарий №1. Через кнопку "Купить":**
1. Нажать кнопку "Купить";
#### Ожидаемый результат: открылась форма ввода данных карты.
**Сценарий №2. Через кнопку "Купить в кредит":**
1. Нажать кнопку "Купить в кредит";
#### Ожидаемый результат: открылась форма ввода данных карты.
### Сценарии оформления тура с оплатой по дебетовой карте:
**Сценарий 1: Успешная покупка тура с валидными данными по карте APPROVED**
#### Предусловия:
* Браузер открыт на странице http://localhost:8080/
* Нажата кнопка «Купить»
#### Шаги:
1. На форме «Оплата по карте» заполнить:
* Номер карты: 4444 4444 4444 4441
* Месяц: от 01 до 12
* Год: больше текущего, но меньше чем на 5 лет вперед
* Владелец: Фамилия Имя на латинице
* CVC/CVV: три цифры
2. Нажать кнопку «Продолжить».
#### Ожидаемый результат:
* Появляется сообщение «Успешно Операция одобрена банком».
* В базе данных сохраняется информация о том, что платеж по дебетовой карте совершен успешно.

**Сценарий 2: Отказ банка при валидных данных по карте DECLINED**
#### Предусловия:
* Браузер открыт на странице http://localhost:8080/
* Нажата кнопка «Купить»
#### Шаги:
1. На форме «Оплата по карте» заполнить:
* Номер карты: 4444 4444 4444 4442
* Месяц: от 01 до 12
* Год: больше текущего, но меньше чем на 6 лет вперед
* Владелец: Фамилия Имя на латинице
* CVC/CVV: три цифры
2. Нажать кнопку «Продолжить».
#### Ожидаемый результат:
* Появляется сообщение «Ошибка Банк отказал в проведении операции».

**Сценарий 3: Отказ банка при незарегистрированной карте**
#### Предусловия:
* Браузер открыт на странице http://localhost:8080/
* Нажата кнопка «Купить»
#### Шаги:
1. На форме «Оплата по карте» заполнить:
* Номер карты: случайный 16-значный номер
* Месяц: от 01 до 12
* Год: больше текущего, но меньше чем на 6 лет вперед
* Владелец: Фамилия Имя на латинице
* CVC/CVV: три цифры
2. Нажать кнопку «Продолжить».
#### Ожидаемый результат:
* Появляется сообщение «Ошибка Банк отказал в проведении операции».

---

### Сценарии с ошибками при отправке заявки на покупку тура с невалидными данными:
**Сценарий 1: Ошибка при вводе букв в номере карты**
#### Шаги:
1. На форме «Оплата по карте» заполнить:
* Номер карты: текст из 16 символов
* Остальные поля заполнить валидными данными
2. Нажать кнопку «Продолжить».
#### Ожидаемый результат:
* Появляется сообщение об ошибке «Неверный формат» под полем номера карты.

**Сценарий 2: Ошибка при истекшем сроке действия карты**
#### Шаги:
1. На форме «Оплата по карте» заполнить:
* Номер карты: 4444 4444 4444 4441
* Месяц: текущий
* Год: меньше текущего на один год
* Владелец: Фамилия Имя на латинице
* CVC/CVV: три цифры
2. Нажать кнопку «Продолжить».
#### Ожидаемый результат:
* Появляется сообщение об ошибке «Истёк срок действия карты» под полем года.

**Сценарий 3: Ошибка при невалидном имени владельца**
#### Шаги:
1. На форме «Оплата по карте» заполнить:
* Номер карты: 4444 4444 4444 4441
* Остальные поля заполнить валидными данными
* Владелец: имя на кириллице
2. Нажать кнопку «Продолжить».
#### Ожидаемый результат:
* Появляется сообщение об ошибке «Поле обязательно для заполнения» под полем владельца.

**Сценарий 4: Ошибка при невалидном CVC/CVV**
#### Шаги:
1. На форме «Оплата по карте» заполнить:
* Номер карты: 4444 4444 4444 4441
* Остальные поля заполнить валидными данными
* CVC/CVV: одна цифра
2. Нажать кнопку «Продолжить».
#### Ожидаемый результат:
* Появляется сообщение об ошибке «Неверный формат» под полем CVC/CVV.

**Сценарий 5: Ошибка при отправке пустой формы**
#### Шаги:
1. На форме «Оплата по карте» оставить все поля пустыми.
2. Нажать кнопку «Продолжить».
#### Ожидаемый результат:
* Появляются сообщения об ошибках «Неверный формат» и «Поле обязательно для заполнения» под соответствующими полями.

---

### Сценарии оформления тура в кредит по данным карты:
**Сценарий 1: Успешная покупка тура в кредит**
#### Предусловия:
* Браузер открыт на странице http://localhost:8080/
* Нажата кнопка «Купить в кредит»
#### Шаги:
1. На форме «Купить в кредит» заполнить:
* Номер карты: 4444 4444 4444 4441
* Месяц: от 01 до 12
* Год: больше текущего, но меньше чем на 5 лет вперед
* Владелец: Фамилия Имя на латинице
* CVC/CVV: три цифры
1. Нажать кнопку «Продолжить».
#### Ожидаемый результат:
* Появляется сообщение «Успешно Операция одобрена банком».
* В базе данных сохраняется информация о том, что платеж по дебетовой карте совершен успешно.

**Сценарий 2: Отказ банка при валидных данных по карте DECLINED**
#### Предусловия:
* Браузер открыт на странице http://localhost:8080/
* Нажата кнопка «Купить в кредит»
#### Шаги:
1. На форме «Купить в кредит» заполнить:
* Номер карты: 4444 4444 4444 4442
* Месяц: от 01 до 12
* Год: больше текущего, но меньше чем на 6 лет вперед
* Владелец: Фамилия Имя на латинице
* CVC/CVV: три цифры
2. Нажать кнопку «Продолжить».
#### Ожидаемый результат:
* Появляется сообщение «Ошибка Банк отказал в проведении операции».

**Сценарий 3: Отказ банка при незарегистрированной карте**
#### Предусловия:
* Браузер открыт на странице http://localhost:8080/
* Нажата кнопка «Купить в кредит»
#### Шаги:
1. На форме «Купить в кредит» заполнить:
* Номер карты: случайный 16-значный номер
* Месяц: от 01 до 12
* Год: больше текущего, но меньше чем на 6 лет вперед
* Владелец: Фамилия Имя на латинице
* CVC/CVV: три цифры
2. Нажать кнопку «Продолжить».
#### Ожидаемый результат:
* Появляется сообщение «Ошибка Банк отказал в проведении операции».

---

## План автоматизации тестирования сценариев заполнения форм оплаты тура и покупки тура
### Используемые инструменты и обоснование выбора:
1. IntelliJ IDEA - интегрированная среда разработки (IDE), используемая для написания и отладки кода автотестов.
2. Java - основной язык программирования для разработки автотестов благодаря своей популярности и широким возможностям.
3. Git - распределенная система управления версиями, обеспечивающая хранение и контроль версий кода автотестов.
4. Gradle - инструмент автоматизации сборки, управляющий зависимостями и обеспечивающий сборку проекта.
5. JUnit 5 - фреймворк для модульного тестирования, используемый для написания и выполнения автотестов.
6. Selenide - фреймворк для автоматизированного тестирования веб-приложений на основе Selenium WebDriver, упрощающий работу с веб-элементами.
7. Lombok - библиотека для генерации стандартного кода в классах, уменьшающая объем рукописного кода.
8. Faker - библиотека для генерации тестовых данных, обеспечивающая создание случайных данных для тестирования.
9. Docker - инструмент для контейнеризации приложений, позволяющий создавать изолированные окружения для тестирования.
10. Allure - фреймворк для генерации отчетов о выполнении тестов, предоставляющий наглядное представление о результатах тестирования.
### Необходимые разрешения, данные и доступы:
1. Разрешение на проведение автоматизированного тестирования сайта.
2. Доступ к документации, включая требования к функциональности.
3. Доступ к базе данных для проверки сохранения платежей.
4. Доступ к существующим тестовым данным.
### Возможные риски при автоматизации:
1. Широкий охват тестирования может привести к переработке и увеличению затрат времени и ресурсов.
2. Неполное покрытие функциональности может привести к пропуску важных дефектов.
3. Недостаток навыков и знаний у команды может привести к созданию некорректных или неэффективных тестовых сценариев.
4. Автоматизация требует постоянной поддержки: тесты могут устаревать при изменении функциональности или интерфейса сайта.
5. Отсутствие документации по инструментам, настройке и использованию автоматизированных тестов может затруднить их использование и поддержку.
6. Ложные срабатывания автотестов могут увеличить время, затрачиваемое на анализ ошибок.
### Необходимые специалисты:
* Специалист по автоматизированному тестированию.
### Интервальная оценка времени с учетом рисков:
* Оценка времени: 30 часов
* Оценка времени с учетом рисков: 50 часов
### План сдачи работ:
* Написание и выполнение автотестов: до 25.07.2024
* Оценка результатов тестирования и подготовка отчета по автоматизации: до 26.07.2024