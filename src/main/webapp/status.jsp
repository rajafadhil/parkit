<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="id">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Cek Status Parkir - ParkIT</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }

        body {
            background: linear-gradient(135deg, #0c4a6e, #1d4ed8);
            color: #fff;
            min-height: 100vh;
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: flex-start;
            padding: 20px;
        }

        .container {
            background-color: rgba(255, 255, 255, 0.1);
            backdrop-filter: blur(10px);
            border-radius: 20px;
            box-shadow: 0 15px 35px rgba(0, 0, 0, 0.3);
            padding: 40px;
            text-align: center;
            max-width: 600px;
            width: 90%;
            margin-top: 60px;
        }

        h1 {
            font-size: 2.2rem;
            margin-bottom: 20px;
            text-shadow: 2px 2px 6px rgba(0, 0, 0, 0.4);
        }

        .form-group {
            margin: 20px 0;
            text-align: left;
        }

        .form-group input {
            width: 100%;
            padding: 12px;
            border-radius: 8px;
            border: none;
            font-size: 16px;
        }

        .btn {
            background: #3498db;
            color: white;
            border: none;
            padding: 12px 30px;
            border-radius: 8px;
            font-weight: bold;
            cursor: pointer;
            font-size: 16px;
            margin-top: 10px;
        }

        .result-box {
            background: rgba(0, 0, 0, 0.2);
            padding: 20px;
            border-radius: 15px;
            margin-top: 25px;
            text-align: left;
        }

        .result-row {
            display: flex;
            justify-content: space-between;
            margin: 10px 0;
            padding-bottom: 8px;
            border-bottom: 1px solid rgba(255, 255, 255, 0.2);
        }

        .label {
            color: #a0d2ff;
        }

        .value {
            font-weight: 600;
        }

        .wash-badge {
            background: #f39c12;
            color: white;
            padding: 3px 8px;
            border-radius: 5px;
            font-size: 0.9rem;
            margin-left: 8px;
        }

        .footer {
            margin-top: 40px;
            color: #a0d2ff;
            font-size: 0.9rem;
        }
    </style>
</head>
<body>

    <div class="container">
        <h1>🔍 Cek Status Parkir Anda</h1>
        <p>Masukkan plat nomor dari tiket parkir Anda:</p>

        <form action="CheckStatusServlet" method="post">
            <div class="form-group">
                <input type="text" name="licensePlate" placeholder="Contoh: B1234ABC" required>
            </div>
            <button type="submit" class="btn">Cek Sekarang</button>
        </form>

        <% if (request.getAttribute("result") != null) { %>
            <div class="result-box">
                <%= request.getAttribute("result") %>
            </div>
        <% } %>
    </div>

    <div class="footer">
        &copy; 2025 ParkIT — Solusi Parkir Digital Masa Kini.
    </div>

</body>
</html>