<%--
  Created by IntelliJ IDEA.
  User: ADMIN-ITQ
  Date: 11/11/2025
  Time: 7:51
  To change this template use File | Settings | File Templates.
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Formulario de Login</title>
    <style>
        /* Estilos generales */
        body {
            font-family: 'Poppins', sans-serif;
            background: linear-gradient(135deg, #6a11cb, #2575fc);
            display: flex;
            align-items: center;
            justify-content: center;
            height: 100vh;
            margin: 0;
        }

        /* Contenedor principal */
        .login-container {
            background: #fff;
            border-radius: 15px;
            box-shadow: 0 10px 25px rgba(0,0,0,0.2);
            padding: 40px;
            width: 350px;
            text-align: center;
        }

        /* Título */
        h1 {
            margin-bottom: 25px;
            color: #333;
            font-size: 24px;
        }

        /* Campos de texto */
        label {
            display: block;
            text-align: left;
            margin-bottom: 8px;
            font-weight: 600;
            color: #333;
        }

        input[type="text"], input[type="password"] {
            width: 100%;
            padding: 10px 12px;
            border: 1px solid #ccc;
            border-radius: 8px;
            font-size: 14px;
            margin-bottom: 20px;
            outline: none;
            transition: all 0.3s ease;
        }

        input[type="text"]:focus, input[type="password"]:focus {
            border-color: #2575fc;
            box-shadow: 0 0 5px rgba(37,117,252,0.4);
        }

        /* Botón */
        input[type="submit"] {
            background-color: #2575fc;
            color: white;
            border: none;
            border-radius: 25px;
            padding: 10px 20px;
            font-size: 16px;
            cursor: pointer;
            transition: all 0.3s ease;
        }

        input[type="submit"]:hover {
            background-color: #1a5ed8;
            transform: scale(1.05);
        }

        /* Pie */
        .footer {
            margin-top: 15px;
            font-size: 13px;
            color: #777;
        }
    </style>
</head>
<body>
<div class="login-container">
    <h1>Iniciar Sesión</h1>
    <form action="/manejosessiones/login" method="post">
        <div>
            <label for="username">Usuario</label>
            <input type="text" name="username" id="username" required>
        </div>
        <div>
            <label for="password">Contraseña</label>
            <input type="password" name="password" id="password" required>
        </div>
        <div>
            <input type="submit" value="Ingresar">
        </div>
    </form>
    <div class="footer">© 2025 - Proyecto Manejo de Sesiones</div>
</div>
</body>
</html>
