package com.expensetracker;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class ExpenseTracker {

    public static void main(String[] args) throws IOException {

        HttpServer server = HttpServer.create(
                new InetSocketAddress(8081), 0
        );

        server.createContext("/", ExpenseTracker::handleHome);

        server.setExecutor(null);
        server.start();

        System.out.println(
                "Expense Tracker running on http://localhost:8081"
        );
    }

    private static void handleHome(HttpExchange exchange)
            throws IOException {

        String html = """
                <!DOCTYPE html>
                <html>
                <head>

                    <meta charset="UTF-8">

                    <title>Expense Tracker</title>

                    <style>

                        body {
                            font-family: Arial, sans-serif;
                            background: #f4f4f4;
                            margin: 0;
                            padding: 40px;
                        }

                        .container {
                            width: 550px;
                            margin: auto;
                            background: white;
                            padding: 30px;
                            border-radius: 12px;
                            box-shadow: 0 4px 15px rgba(0,0,0,0.1);
                        }

                        h1 {
                            text-align: center;
                            margin-bottom: 30px;
                        }

                        input, select, button {
                            width: 100%;
                            padding: 12px;
                            margin-top: 10px;
                            box-sizing: border-box;
                            border-radius: 6px;
                            border: 1px solid #ccc;
                            font-size: 15px;
                        }

                        button {
                            background: #222;
                            color: white;
                            border: none;
                            cursor: pointer;
                        }

                        button:hover {
                            background: #444;
                        }

                        .total {
                            margin-top: 25px;
                            font-size: 20px;
                            font-weight: bold;
                        }

                        .expense {
                            display: flex;
                            justify-content: space-between;
                            align-items: center;
                            background: #f5f5f5;
                            padding: 12px;
                            margin-top: 10px;
                            border-radius: 6px;
                        }

                        .delete {
                            width: auto;
                            margin: 0;
                            padding: 6px 10px;
                            background: #d9534f;
                        }

                        .delete:hover {
                            background: #c9302c;
                        }

                    </style>

                </head>

                <body>

                    <div class="container">

                        <h1>Expense Tracker</h1>

                        <input
                            type="text"
                            id="description"
                            placeholder="Expense Description"
                        >

                        <input
                            type="number"
                            id="amount"
                            placeholder="Amount"
                        >

                        <select id="category">

                            <option>Food</option>
                            <option>Travel</option>
                            <option>Shopping</option>
                            <option>Entertainment</option>
                            <option>Other</option>

                        </select>

                        <button id="addExpense">
                            Add Expense
                        </button>

                        <div id="expenseList"></div>

                        <div class="total">
                            Total Expense: ₹<span id="total">0</span>
                        </div>

                    </div>


                    <script>

                        let total = 0;

                        document
                            .getElementById("addExpense")
                            .addEventListener("click", function() {

                                const description =
                                    document.getElementById("description").value;

                                const amount =
                                    parseFloat(
                                        document.getElementById("amount").value
                                    );

                                const category =
                                    document.getElementById("category").value;


                                if (description === "" || isNaN(amount) || amount <= 0) {

                                    alert("Please enter a valid description and amount.");

                                    return;
                                }


                                total += amount;

                                document.getElementById("total").textContent =
                                    total.toFixed(2);


                                const expense =
                                    document.createElement("div");

                                expense.className = "expense";


                                expense.innerHTML = `
                                    <div>
                                        <strong>${description}</strong>
                                        <br>
                                        ${category} - ₹${amount.toFixed(2)}
                                    </div>

                                    <button class="delete">
                                        Delete
                                    </button>
                                `;


                                expense
                                    .querySelector(".delete")
                                    .addEventListener("click", function() {

                                        total -= amount;

                                        document.getElementById("total")
                                            .textContent =
                                            total.toFixed(2);

                                        expense.remove();

                                    });


                                document
                                    .getElementById("expenseList")
                                    .appendChild(expense);


                                document.getElementById("description").value = "";

                                document.getElementById("amount").value = "";

                            });

                    </script>

                </body>
                </html>
                """;


        byte[] response =
                html.getBytes(StandardCharsets.UTF_8);


        exchange.getResponseHeaders()
                .set("Content-Type", "text/html; charset=UTF-8");


        exchange.sendResponseHeaders(
                200,
                response.length
        );


        exchange.getResponseBody().write(response);

        exchange.getResponseBody().close();
    }
}