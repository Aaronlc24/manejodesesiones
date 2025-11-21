<%--
  Created by IntelliJ IDEA.
  User: ADMIN-ITQ
  Date: 21/11/2025
  Time: 7:51
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" import="models.*"%>
<%@ page import="java.util.Optional" %>
<%@ page import="java.util.List" %>

<%
    List<Producto> productos = (List<Producto>) request.getAttribute("productos");
    Optional<String> username = (Optional<String>) request.getAttribute("username");
%>

<html>
<head>
    <title>Listado de productos</title>

    <!-- ESTILOS ELEGANTES Y MODERNOS -->
    <style>

        body {
            margin: 0;
            padding: 40px;
            background: linear-gradient(135deg, #0f0c29, #302b63, #24243e);
            font-family: "Segoe UI", sans-serif;
            color: #fff;
        }

        h1 {
            text-align: center;
            font-size: 38px;
            margin-bottom: 25px;
            color: #f8f8f8;
            text-shadow: 0 2px 8px rgba(0, 0, 0, 0.6);
        }

        .welcome {
            text-align: center;
            font-size: 20px;
            margin-bottom: 25px;
            color: #00eaff;
            font-weight: bold;
        }

        .btn-crear {
            display: inline-block;
            margin-left: 45%;
            margin-bottom: 30px;
            padding: 10px 20px;
            background-color: #00eaff;
            border-radius: 10px;
            text-decoration: none;
            color: #000;
            font-weight: bold;
            transition: 0.3s;
            box-shadow: 0 4px 12px rgba(0,0,0,0.3);
        }

        .btn-crear:hover {
            transform: scale(1.08);
            background: #7ffcff;
        }

        table {
            width: 95%;
            margin: auto;
            border-collapse: collapse;
            background: rgba(255,255,255,0.10);
            border-radius: 14px;
            overflow: hidden;
            box-shadow: 0 5px 20px rgba(0,0,0,0.4);
            table-layout: fixed; /* evita que se desborde la última columna */
        }

        th, td {
            padding: 14px;
            text-align: center;
        }

        th {
            background: rgba(255,255,255,0.18);
            font-size: 16px;
            letter-spacing: 1px;
        }

        tr:nth-child(even) {
            background: rgba(255,255,255,0.05);
        }

        tr:hover {
            background: rgba(0,255,255,0.18);
            transition: 0.3s ease-in-out;
        }

        .btn {
            display: inline-block;
            padding: 6px 14px;          /* más pequeño para que no se salga */
            background: #00eaff;
            color: #000;
            border-radius: 8px;
            text-decoration: none;
            font-weight: bold;
            font-size: 13px;
            transition: 0.3s;
            box-shadow: 0 4px 8px rgba(0,0,0,0.3);
            white-space: nowrap;
        }

        .btn:hover {
            background: #81f5ff;
            transform: scale(1.07);
        }

        /* la columna de acción no hace salto de línea raro */
        td.acciones {
            white-space: nowrap;
        }

    </style>

</head>
<body>

<h1>Listado de productos</h1>

<% if (username.isPresent()) { %>
<div class="welcome">Hola <%= username.get() %> , ¡Bienvenido!</div>
<a href="<%=request.getContextPath()%>/producto/form" class="btn-crear">Crear Producto</a>
<% } %>

<table>
    <tr>
        <th>ID Producto</th>
        <th>Nombre del producto</th>
        <th>Categoria</th>
        <th>Stock</th>
        <th>Descripción</th>
        <th>Fecha elaboración</th>
        <th>Fecha caducidad</th>
        <th>Condición</th>

        <% if (username.isPresent()) { %>
        <th>Precio</th>
        <th>Acción</th>
        <% } %>
    </tr>

    <% for (Producto p : productos) { %>
    <tr>
        <td><%= p.getId() %></td>
        <td><%= p.getNombre() %></td>
        <td><%= p.getCategoria().getNombre() %></td>
        <td><%= p.getStock() %></td>
        <td><%= p.getDescripcion() %></td>
        <td><%= p.getFechaElaboracion() %></td>
        <td><%= p.getFechaCaducidad() %></td>
        <td><%= p.getCondición() %></td>

        <% if (username.isPresent()) { %>
        <td>$<%= p.getPrecio() %></td>

        <td class="acciones">
            <a class="btn"
               href="<%=request.getContextPath()%>/agregar-carro?id=<%=p.getId()%>">
                Agregar al carro
            </a>
        </td>
        <% } %>

    </tr>
    <% } %>

</table>

</body>
</html>
