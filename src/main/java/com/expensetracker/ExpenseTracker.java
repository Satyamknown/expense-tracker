package com.expensetracker;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class ExpenseTracker {

    public static void main(String[] args) throws Exception {

        // Jenkins will use port 8083.
        // Normal local/Docker execution can still use 8081.
        int port = Integer.parseInt(
                System.getProperty("server.port", "8081")
        );

        HttpServer server = HttpServer.create(
                new InetSocketAddress(port), 0
        );

        server.createContext("/", ExpenseTracker::handleRequest);

        server.setExecutor(null);
        server.start();

        System.out.println(
                "Expense Tracker running on http://localhost:" + port
        );
    }

    private static void handleRequest(HttpExchange exchange)
            throws IOException {

        String html = """
                <!DOCTYPE html>
                <html lang="en">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport"
                          content="width=device-width, initial-scale=1.0">

                    <title>Expense Tracker</title>

                    <style>
                        * {
                            box-sizing: border-box;
                            margin: 0;
                            padding: 0;
                            font-family: Arial, sans-serif;
                        }

                        body {
                            background: linear-gradient(
                                135deg,
                                #667eea,
                                #764ba2
                            );
                            min-height: 100vh;
                            padding: 40px 20px;
                        }

                        .container {
                            max-width: 650px;
                            margin: auto;
                            background: white;
                            padding: 30px;
                            border-radius: 18px;
                            box-shadow: 0 15px 40px rgba(0,0,0,0.2);
                        }

                        h1 {
                            text-align: center;
                            margin-bottom: 25px;
                            color: #333;
                        }

                        .form-group {
                            margin-bottom: 15px;
                        }

                        label {
                            display: block;
                            margin-bottom: 6px;
                            font-weight: bold;
                            color: #444;
                        }

                        input,
                        select {
                            width: 100%;
                            padding: 12px;
                            border: 1px solid #ccc;
                            border-radius: 8px;
                            font-size: 15px;
                        }

                        button {
                            width: 100%;
                            padding: 13px;
                            border: none;
                            border-radius: 8px;
                            background: #667eea;
                            color: white;
                            font-size: 16px;
                            cursor: pointer;
                            margin-top: 5px;
                        }

                        button:hover {
                            background: #5568d9;
                        }

                        .expense-list {
                            margin-top: 25px;
                        }

                        .expense-item {
                            display: flex;
                            justify-content: space-between;
                            align-items: center;
                            padding: 15px;
                            margin-bottom: 10px;
                            background: #f5f6fa;
                            border-radius: 10px;
                        }

                        .expense-info {
                            flex: 1;
                        }

                        .expense-name {
                            font-weight: bold;
                            color: #333;
                        }

                        .expense-category {
                            font-size: 13px;
                            color: #777;
                            margin-top: 4px;
                        }

                        .expense-amount {
                            font-weight: bold;
                            color: #667eea;
                            margin-right: 10px;
                        }

                        .delete-btn {
                            width: auto;
                            margin: 0;
                            padding: 7px 12px;
                            background: #e74c3c;
                            font-size: 13px;
                        }

                        .delete-btn:hover {
                            background: #c0392b;
                        }

                        .total {
                            margin-top: 20px;
                            padding: 18px;
                            border-radius: 10px;
                            background: #667eea;
                            color: white;
                            text-align: center;
                            font-size: 20px;
                            font-weight: bold;
                        }

                        .empty {
                            text-align: center;
                            color: #888;
                            padding: 20px;
                        }
                    </style>
                </head>

                <body>

                <div class="container">

                    <h1>💰 Expense Tracker</h1>

                    <div class="form-group">
                        <label for="description">
                            Description
                        </label>

                        <input
                            type="text"
                            id="description"
                            placeholder="Enter expense description"
                        >
                    </div>

                    <div class="form-group">
                        <label for="amount">
                            Amount
                        </label>

                        <input
                            type="number"
                            id="amount"
                            placeholder="Enter amount"
                            min="0"
                            step="0.01"
                        >
                    </div>

                    <div class="form-group">
                        <label for="category">
                            Category
                        </label>

                        <select id="category">
                            <option>Food</option>
                            <option>Travel</option>
                            <option>Shopping</option>
                            <option>Entertainment</option>
                            <option>Other</option>
                        </select>
                    </div>

                    <button id="addExpense">
                        Add Expense
                    </button>

                    <div class="expense-list" id="expenseList">
                        <div class="empty">
                            No expenses added yet.
                        </div>
                    </div>

                    <div class="total">
                        Total Expense:
                        ₹<span id="total">0.00</span>
                    </div>

                </div>

                <script>

                    let expenses = [];

                    const description =
                        document.getElementById("description");

                    const amount =
                        document.getElementById("amount");

                    const category =
                        document.getElementById("category");

                    const addButton =
                        document.getElementById("addExpense");

                    const expenseList =
                        document.getElementById("expenseList");

                    const total =
                        document.getElementById("total");


                    addButton.addEventListener("click", function () {

                        const name = description.value.trim();
                        const value = parseFloat(amount.value);
                        const selectedCategory = category.value;

                        if (name === "" || isNaN(value) || value <= 0) {
                            alert("Please enter a valid description and amount.");
                            return;
                        }

                        expenses.push({
                            name: name,
                            amount: value,
                            category: selectedCategory
                        });

                        description.value = "";
                        amount.value = "";

                        renderExpenses();
                    });


                    function renderExpenses() {

                        expenseList.innerHTML = "";

                        if (expenses.length === 0) {

                            expenseList.innerHTML =
                                '<div class="empty">' +
                                'No expenses added yet.' +
                                '</div>';

                            total.textContent = "0.00";
                            return;
                        }

                        let totalAmount = 0;

                        expenses.forEach(function (expense, index) {

                            totalAmount += expense.amount;

                            const item =
                                document.createElement("div");

                            item.className = "expense-item";

                            item.innerHTML = `
                                <div class="expense-info">
                                    <div class="expense-name">
                                        ${escapeHtml(expense.name)}
                                    </div>

                                    <div class="expense-category">
                                        ${escapeHtml(expense.category)}
                                    </div>
                                </div>

                                <div class="expense-amount">
                                    ₹${expense.amount.toFixed(2)}
                                </div>

                                <button
                                    class="delete-btn"
                                    onclick="deleteExpense(${index})">
                                    Delete
                                </button>
                            `;

                            expenseList.appendChild(item);
                        });

                        total.textContent =
                            totalAmount.toFixed(2);
                    }


                    function deleteExpense(index) {

                        expenses.splice(index, 1);

                        renderExpenses();
                    }


                    function escapeHtml(text) {

                        const div =
                            document.createElement("div");

                        div.textContent = text;

                        return div.innerHTML;
                    }

                </script>

                </body>
                </html>
                """;

        byte[] response =
                html.getBytes(StandardCharsets.UTF_8);

        exchange.getResponseHeaders().set(
                "Content-Type",
                "text/html; charset=UTF-8"
        );

        exchange.sendResponseHeaders(
                200,
                response.length
        );

        try (OutputStream output =
                     exchange.getResponseBody()) {

            output.write(response);
        }
    }
}