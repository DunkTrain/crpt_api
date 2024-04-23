# Описание задачи

Реализация класса на языке Java для работы с API Честного знака с поддержкой ограничения количества запросов в определенный интервал времени. Класс должен быть thread-safe.

## Требования

- Язык программирования: Java 17.
- Ограничение запросов указывается в конструкторе как количество запросов в определенный временной интервал (`TimeUnit`).
- При превышении лимита запрос должен блокироваться, чтобы не превысить максимальное количество запросов к API без выбрасывания исключений.
- Превышение лимита запросов запрещено для метода.

## Методы

- Метод `createDocument(Object document, String signature)` для создания документа для ввода товара, произведенного в РФ.
- Вызов метода осуществляется по HTTPS методом POST на URL: [https://ismp.crpt.ru/api/v3/lk/documents/create](https://ismp.crpt.ru/api/v3/lk/documents/create).
- Формат тела запроса: JSON-документ.

```json
{
  "description": {
    "participantInn": "string"
  },
  "doc_id": "string",
  "doc_status": "string",
  "doc_type": "LP_INTRODUCE_GOODS",
  "importRequest": true,
  "owner_inn": "string",
  "participant_inn": "string",
  "producer_inn": "string",
  "production_date": "2020-01-23",
  "production_type": "string",
  "products": [
    {
      "certificate_document": "string",
      "certificate_document_date": "2020-01-23",
      "certificate_document_number": "string",
      "owner_inn": "string",
      "producer_inn": "string",
      "production_date": "2020-01-23",
      "tnved_code": "string",
      "uit_code": "string",
      "uitu_code": "string"
    }
  ],
  "reg_date": "2020-01-23",
  "reg_number": "string"
}
```
## Используемые технологии

- Использование библиотек HTTP клиента и JSON сериализации для работы с запросами и JSON данными.
- Реализация должна быть удобной для дальнейшего расширения функционала.

## Организация кода

- Весь код оформлен в одном файле `CrptApi.java`.
- Дополнительные классы, используемые внутри, должны быть внутренними и скрытыми.

## Примечания

- Реализация метода `createDocument` представляет только вызов API метода без реального взаимодействия с внешним API.

---

Пример кода находится в файле `CrptApi.java`.
