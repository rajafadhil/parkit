<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="id">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ParkIT - Sistem Manajemen Parkir Terminal</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }

        body {
            background: linear-gradient(135deg, #1e3c72 0%, #2a5298 100%);
            color: #fff;
            min-height: 100vh;
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            padding: 0;
            position: relative;
        }

        /* Navbar */
        .navbar {
            width: 100%;
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 15px 40px;
            background: rgba(255, 255, 255, 0.1);
            backdrop-filter: blur(10px);
            position: fixed;
            top: 0;
            left: 0;
            z-index: 100;
        }

        .navbar-logo {
            font-size: 1.5rem;
            font-weight: bold;
            color: #4dabf7;
            text-shadow: 0 0 10px rgba(77, 171, 247, 0.5);
        }

        .navbar-right {
            display: flex;
            gap: 15px;
        }

        .nav-btn {
            padding: 8px 20px;
            border-radius: 5px;
            font-weight: 600;
            text-decoration: none;
            transition: all 0.3s;
        }

        .btn-register {
            background: #2ecc71;
            color: white;
        }

        .btn-register:hover {
            background: #27ae60;
        }

        .btn-login {
            background: #3498db;
            color: white;
        }

        .btn-login:hover {
            background: #217dbb;
        }

        /* Hero Section */
        .hero {
            text-align: center;
            margin-top: 80px;
            max-width: 800px;
            z-index: 10;
        }

        .hero-title {
            font-size: 3.5rem;
            font-weight: 900;
            line-height: 1.2;
            margin-bottom: 20px;
            text-shadow: 2px 2px 8px rgba(0, 0, 0, 0.5);
        }

        .hero-subtitle {
            font-size: 1.2rem;
            color: #a0d2ff;
            margin-bottom: 30px;
            font-weight: 300;
        }

        .hero-cta {
            display: flex;
            justify-content: center;
            gap: 20px;
            margin-top: 30px;
            flex-wrap: wrap;
        }

        .btn {
            padding: 12px 30px;
            border-radius: 30px;
            font-weight: 600;
            text-decoration: none;
            display: inline-block;
            transition: all 0.3s;
            font-size: 1rem;
        }

        .btn-register-hero {
            background: #2ecc71;
            color: white;
        }

        .btn-register-hero:hover {
            background: #27ae60;
        }

        .btn-login-hero {
            background: #3498db;
            color: white;
        }

        .btn-login-hero:hover {
            background: #217dbb;
        }

        .btn-status-hero {
            background: #9b59b6;
            color: white;
        }

        .btn-status-hero:hover {
            background: #8e44ad;
        }

        /* Features Section */
        .features {
            display: flex;
            justify-content: center;
            flex-wrap: wrap;
            gap: 30px;
            margin: 50px auto;
            max-width: 900px;
        }

        .feature-card {
            background: rgba(255, 255, 255, 0.1);
            backdrop-filter: blur(10px);
            border-radius: 15px;
            padding: 30px;
            width: 200px;
            text-align: center;
            transition: transform 0.3s;
            cursor: pointer;
        }

        .feature-card:hover {
            transform: translateY(-10px);
        }

        .feature-icon {
            font-size: 3rem;
            margin-bottom: 15px;
            color: #4dabf7;
        }

        .feature-card h3 {
            margin-bottom: 10px;
            font-size: 1.2rem;
        }

        .feature-card p {
            font-size: 0.95rem;
            color: #a0d2ff;
        }

        /* Footer */
        .footer {
            margin-top: 60px;
            padding-bottom: 30px;
            color: #a0d2ff;
            font-size: 0.9rem;
            text-align: center;
        }
    </style>
</head>
<body>

    <!-- Navbar -->
    <div class="navbar">
        <div class="navbar-logo">ParkIT</div>
        <div class="navbar-right">
            <a href="login.jsp" class="nav-btn btn-login">Login</a>
            <a href="register.jsp" class="nav-btn btn-register">Register</a>
        </div>
    </div>

    <!-- Hero Section -->
    <div class="hero">
        <h1 class="hero-title">PROTECT<br>YOUR VEHICLE</h1>
        <p class="hero-subtitle">Sistem Manajemen Parkir Digital Modern — Aman, Cepat, dan Terpercaya</p>
        <div class="hero-cta">
            <a href="register.jsp" class="btn btn-register-hero">Register</a>
            <a href="login.jsp" class="btn btn-login-hero">Login</a>
            <a href="status.jsp" class="btn btn-status-hero">Cek Status</a>
        </div>
    </div>

    <!-- Features -->
    <div class="features">
        <div class="feature-card">
            <div class="feature-icon">⏱️</div>
            <h3>Waktu Otomatis</h3>
            <p>Catat waktu masuk/keluar otomatis</p>
        </div>
        <div class="feature-card">
            <div class="feature-icon">💳</div>
            <h3>Tarif Digital</h3>
            <p>Tarif otomatis berdasarkan jenis kendaraan</p>
        </div>
        <div class="feature-card">
            <div class="feature-icon">📊</div>
            <h3>Laporan Lengkap</h3>
            <p>Riwayat parkir dan pendapatan harian</p>
        </div>
        <div class="feature-card">
            <div class="feature-icon">🔒</div>
            <h3>Keamanan Data</h3>
            <p>Data kendaraan Anda terlindungi</p>
        </div>
    </div>

    <!-- Footer -->
    <div class="footer">
        &copy; 2025 ParkIT — Solusi Parkir Digital Masa Kini.
    </div>

</body>
</html>