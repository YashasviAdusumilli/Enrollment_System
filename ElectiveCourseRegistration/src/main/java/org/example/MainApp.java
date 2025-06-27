package org.example;

import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import org.example.service.EmailService;
import org.example.service.MongoService;

public class MainApp {
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();

        // Services
        EmailService emailService = new EmailService(vertx);
        MongoService mongoService = new MongoService(vertx, emailService);

        // Router setup
        Router router = Router.router(vertx);

        // Enable CORS
        router.route().handler(CorsHandler.create("*")
                .allowedMethod(io.vertx.core.http.HttpMethod.GET)
                .allowedMethod(io.vertx.core.http.HttpMethod.POST)
                .allowedHeader("Content-Type"));

        // Handle OPTIONS requests for CORS preflight
        router.route().handler(ctx -> {
            ctx.response().putHeader("Access-Control-Allow-Origin", "*");
            ctx.response().putHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
            ctx.response().putHeader("Access-Control-Allow-Headers", "Content-Type");
            if (ctx.request().method().name().equals("OPTIONS")) {
                ctx.response().setStatusCode(204).end();
            } else {
                ctx.next();
            }
        });

        // Parse JSON bodies
        router.route().handler(BodyHandler.create());

        // API routes
        router.post("/register").handler(mongoService::registerStudent);
        router.post("/login").handler(mongoService::loginStudent);
        router.get("/courses").handler(mongoService::getCourses);
        router.post("/enroll").handler(mongoService::registerForCourse);

        // Start server
        vertx.createHttpServer()
                .requestHandler(router)
                .listen(8888, result -> {
                    if (result.succeeded()) {
                        System.out.println("✅ Server started on http://localhost:8888");
                    } else {
                        System.err.println("❌ Failed to start server: " + result.cause().getMessage());
                    }
                });
    }
}
