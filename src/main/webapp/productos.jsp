<%--
  Created by IntelliJ IDEA.
  User: Aaron
  Date: 20/11/2025
  Time: 8:59
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" import="models.*"%>
<%@ page import="java.util.Optional" %>
<%@ page import="java.util.List" %>
<%------%>
<%
    List<Producto> productos = (List<Producto>) request.getAttribute("productos");
    Optional<String> username = (Optional<String>) request.getAttribute("username");
%>
<html>
<head>
    <title>listado producto</title>
</head>
<body>
<h1>Listado de productos</h1>
<% if (username.isPresent()) { %>
<div>Hola <%=username.get()%>,Bienvenido!</div>
<p><a href="<%=request.getContextPath()%>">Crear un producto</a></p>
<%}%>
<table>
    <tr>
        <th>ID Producto</th>
        <th>Nombre del producto</th>
        <th>Categoria</th>
        <th>stock</th>
        <th>Descripcion</th>
        <th>fecha elaboracion</th>
        <th>fecha caducidad</th>
        <th>condicion</th>
        <% if (username.isPresent()) { %>
        <th>Precio</th>
        <th>Accion</th>
        <% } %>
    </tr>
    <% for (Producto p : productos) { %>
    <tr>
        <td><%= p.getId()%>
        </td>
        <td><%= p.getNombre()%>
        </td>
        <td><%= p.getCategoria().getNombre()%>
        </td>
        <td><%= p.getStock()%>
        </td>
        <td><%= p.getDescripcion()%>
        </td>
        <td><%= p.getFechaElaboracion()%>
        </td>
        <td><%= p.getFechaCaducidad()%>
        </td>
        <td><%= p.getCondición()%>
        </td>
        <% if (username.isPresent()) { %>
        <td><%= p.getPrecio()%>
        </td>
        <td>
            <a href="<%=request.getContextPath()%>/agregar-carro?id=<%=p.getId()%>">Agregar al carro
            </a></td>
        <% } %>
    </tr>
    <% } %>
</table>

</body>
</html>
