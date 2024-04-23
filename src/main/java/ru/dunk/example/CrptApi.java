package ru.dunk.example;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class CrptApi {

    private final HttpClient httpClient;
    private final Semaphore requestSemaphore;

    public CrptApi(TimeUnit timeUnit, int requestLimit) {

        this.httpClient = HttpClient.newHttpClient();
        this.requestSemaphore = new Semaphore(requestLimit);

        // Создание планировщика задач
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

        // Запуск задачи по расписанию для восстановления разрешений семафор
        executorService.scheduleAtFixedRate(() -> requestSemaphore
                .release(requestLimit - requestSemaphore
                        .availablePermits()), 0, timeUnit.toMillis(1), TimeUnit.MILLISECONDS);
    }

    public void createDocument(Object document, String signature) {
        try {
            requestSemaphore.acquire(); // Запрашиваем разрешение у семафора

            // Подготовка JSON-представления данных для отправки
            String jsonPayload = "{\"description\": {\"participantInn\": \"string\"}, " +
                    "\"doc_id\": \"string\", \"doc_status\": \"string\", \"doc_type\": \"LP_INTRODUCE_GOODS\"," +
                    "\"importRequest\": true, \"owner_inn\": \"string\", \"participant_inn\": \"string\"," +
                    "\"producer_inn\": \"string\", \"production_date\": \"2020-01-23\", \"production_type\": \"string\"," +
                    "\"products\": [{\"certificate_document\": \"string\", \"certificate_document_date\": \"2020-01-23\"," +
                    "\"certificate_document_number\": \"string\", \"owner_inn\": \"string\", \"producer_inn\": \"string\"," +
                    "\"production_date\": \"2020-01-23\", \"tnved_code\": \"string\", \"uit_code\": \"string\"," +
                    "\"uitu_code\": \"string\" }], \"reg_date\": \"2020-01-23\", \"reg_number\": \"string\"}";


            // Формирование HTTP-запроса
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://ismp.crpt.ru/api/v3/lk/documents/create"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                    .build();

            // Отправка запроса и получение ответа
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            // Вывод кода ответа и тела ответа на консоль
            System.out.println("Response code: " + response.statusCode());
            System.out.println("Response body: " + response.body());

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            requestSemaphore.release();
        }
    }

    public static void main(String[] args) {

        CrptApi crptApi = new CrptApi(TimeUnit.MINUTES, 10);

        crptApi.createDocument(new Object(), "signature string");
    }


}