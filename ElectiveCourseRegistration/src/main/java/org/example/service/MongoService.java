package org.example.service;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.RoutingContext;

import java.util.UUID;

public class MongoService {

    private final MongoClient client;
    private final EmailService emailService;

    public MongoService(Vertx vertx, EmailService emailService) {
        this.client = MongoClient.createShared(vertx, new JsonObject()
                .put("connection_string", "mongodb://localhost:27017")
                .put("db_name", "elective_db"));
        this.emailService = emailService;
    }

    // Register Student
    public void registerStudent(RoutingContext ctx) {
        JsonObject body = ctx.getBodyAsJson();
        String name = body.getString("name");
        String email = body.getString("email");
        String password = UUID.randomUUID().toString().substring(0, 8);

        JsonObject query = new JsonObject().put("email", email);
        client.findOne("students", query, null, res -> {
            if (res.succeeded() && res.result() != null) {
                ctx.response().setStatusCode(400).end("Email already registered.");
            } else {
                JsonObject newStudent = new JsonObject()
                        .put("name", name)
                        .put("email", email)
                        .put("password", password);

                client.insert("students", newStudent, insertRes -> {
                    if (insertRes.succeeded()) {
                        emailService.sendEmail(email, "Elective Registration Password",
                                "Hi " + name + ",\n\nYour password is: " + password);
                        ctx.response().end("Registered. Password sent to email.");
                    } else {
                        ctx.response().setStatusCode(500).end("Failed to register.");
                    }
                });
            }
        });
    }

    // Login Student
    public void loginStudent(RoutingContext ctx) {
        JsonObject body = ctx.getBodyAsJson();
        String email = body.getString("email");
        String password = body.getString("password");

        JsonObject query = new JsonObject().put("email", email).put("password", password);
        client.findOne("students", query, null, res -> {
            if (res.succeeded() && res.result() != null) {
                ctx.response().end("Login successful");
            } else {
                ctx.response().setStatusCode(401).end("Invalid email or password");
            }
        });
    }

    // Get Courses
    public void getCourses(RoutingContext ctx) {
        client.find("courses", new JsonObject(), res -> {
            if (res.succeeded()) {
                ctx.response()
                        .putHeader("Content-Type", "application/json")
                        .end(res.result().toString());
            } else {
                ctx.response().setStatusCode(500).end("Failed to fetch courses");
            }
        });
    }

    // Register for Course
    public void registerForCourse(RoutingContext ctx) {
        JsonObject body = ctx.getBodyAsJson();
        String email = body.getString("email");
        String courseName = body.getString("courseName");

        // Check available seats
        JsonObject courseQuery = new JsonObject().put("name", courseName);
        client.findOne("courses", courseQuery, null, courseRes -> {
            if (courseRes.succeeded() && courseRes.result() != null) {
                int seats = courseRes.result().getInteger("seatsAvailable");

                if (seats > 0) {
                    // Decrease seat count
                    JsonObject update = new JsonObject().put("$set", new JsonObject().put("seatsAvailable", seats - 1));
                    client.updateCollection("courses", courseQuery, update, updateRes -> {
                        if (updateRes.succeeded()) {
                            // Save enrollment
                            JsonObject enrollment = new JsonObject()
                                    .put("email", email)
                                    .put("course", courseName);

                            client.insert("enrollments", enrollment, enrollRes -> {
                                if (enrollRes.succeeded()) {
                                    ctx.response().end("Successfully enrolled in " + courseName);
                                } else {
                                    ctx.response().setStatusCode(500).end("Failed to enroll");
                                }
                            });
                        } else {
                            ctx.response().setStatusCode(500).end("Failed to update course");
                        }
                    });
                } else {
                    ctx.response().setStatusCode(400).end("No seats available");
                }
            } else {
                ctx.response().setStatusCode(404).end("Course not found");
            }
        });
    }
}
