<%--
  Created by IntelliJ IDEA.
  User: Aaron
  Date: 21/11/2025
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java"
         import="java.time.format.DateTimeFormatter, models.*" %>
<%@ page import="java.util.List, java.util.Map" %>

<%
    List<Categoria> categorias = (List<Categoria>) request.getAttribute("categorias");
    Map<String, String> errores = (Map<String, String>) request.getAttribute("errores");
    Producto producto = (Producto) request.getAttribute("producto");

    if (producto == null) {
        producto = new Producto();
        producto.setCategoria(new Categoria());
    }

    String fechaElaboracion = producto.getFechaElaboracion() != null ?
            producto.getFechaElaboracion().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) : "";
    String fechaCaducidad = producto.getFechaCaducidad() != null ?
            producto.getFechaCaducidad().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) : "";
%>

<html lang="es">
<head>
    <title>Formulario Productos</title>

    <style>
        body {
            margin: 0;
            padding: 0;
            font-family: "Segoe UI", sans-serif;
            background: linear-gradient(135deg, #0f0c29, #302b63, #24243e);
            color: #f5f5f5;
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
        }

        /* TARJETA MÁS PEQUEÑA */
        .card {
            background: rgba(15, 12, 41, 0.92);
            border-radius: 15px;
            padding: 25px 30px;
            width: 350px;   /* REDUCIDO */
            box-shadow: 0 12px 28px rgba(0,0,0,0.5);
            border: 1px solid rgba(255,255,255,0.05);
        }

        h1 {
            text-align: center;
            margin-top: 0;
            margin-bottom: 18px;
            font-size: 22px; /* MÁS PEQUEÑO */
            color: #ffffff;
            text-shadow: 0 2px 6px rgba(0,0,0,0.6);
        }

        .form-group {
            margin-bottom: 12px;
        }

        label {
            display: block;
            margin-bottom: 4px;
            font-size: 13px;
            color: #cfd4ff;
        }

        input[type="text"],
        input[type="number"],
        input[type="date"],
        select,
        textarea {
            width: 100%;
            padding: 7px 9px;
            border-radius: 8px;
            border: 1px solid rgba(255,255,255,0.15);
            background: rgba(20, 20, 60, 0.9);
            color: #f5f5f5;
            font-size: 13px;
            outline: none;
            box-sizing: border-box;
            transition: all 0.2s ease-in-out;
        }

        textarea {
            resize: vertical;
            min-height: 70px;
            max-height: 150px;
        }

        input:focus, textarea:focus, select:focus {
            border-color: #00eaff;
            box-shadow: 0 0 0 1px rgba(0,234,255,0.4);
        }

        .error {
            color: #ff6b6b;
            font-size: 12px;
            margin-top: 2px;
        }

        .btn-submit {
            width: 100%;
            padding: 9px 0;
            margin-top: 6px;
            border: none;
            border-radius: 10px;
            background: #00eaff;
            color: #000;
            font-weight: bold;
            font-size: 14px;
            cursor: pointer;
            box-shadow: 0 6px 15px rgba(0,0,0,0.4);
            transition: all 0.25s ease-in-out;
        }

        .btn-submit:hover {
            background: #7ffcff;
            transform: translateY(-1px);
        }

        .btn-back {
            display: inline-block;
            margin-top: 10px;
            width: 100%;
            padding: 7px 0;
            border-radius: 10px;
            text-align: center;
            background: transparent;
            border: 1px solid rgba(255,255,255,0.25);
            color: #e0e7ff;
            font-size: 13px;
            text-decoration: none;
            transition: 0.25s ease-in-out;
        }

        .btn-back:hover {
            background: rgba(255,255,255,0.1);
        }

    </style>
</head>
<body>

<div class="card">
    <h1><%= (producto.getId() != null && producto.getId() > 0) ? "Editar producto" : "Crear producto" %></h1>

    <form action="<%=request.getContextPath()%>/producto/form" method="post">

        <div class="form-group">
            <label for="nombre">Nombre</label>
            <input type="text" name="nombre" id="nombre"
                   value="<%= producto.getNombre() != null ? producto.getNombre() : "" %>">
            <% if (errores != null && errores.containsKey("nombre")) { %>
            <div class="error"><%= errores.get("nombre") %></div>
            <% } %>
        </div>

        <div class="form-group">
            <label for="categoria">Categoría</label>
            <select name="categoria" id="categoria">
                <option value="">------ Seleccionar --------</option>
                <% if (categorias != null) {
                    for (Categoria c : categorias) { %>
                <option value="<%= c.getId() %>"
                        <%= (producto.getCategoria() != null &&
                                c.getId().equals(producto.getCategoria().getId())) ? "selected" : "" %>>
                    <%= c.getNombre() %>
                </option>
                <%   }
                } %>
            </select>
            <% if (errores != null && errores.containsKey("categoria")) { %>
            <div class="error"><%= errores.get("categoria") %></div>
            <% } %>
        </div>

        <div class="form-group">
            <label for="stock">Stock</label>
            <input type="number" name="stock" id="stock"
                   value="<%= producto != null ? producto.getStock() : 0 %>">
            <% if (errores != null && errores.containsKey("stock")) { %>
            <div class="error"><%= errores.get("stock") %></div>
            <% } %>
        </div>

        <div class="form-group">
            <label for="precio">Precio</label>
            <input type="number" step="0.01" name="precio" id="precio"
                   value="<%= producto != null ? producto.getPrecio() : 0 %>">
            <% if (errores != null && errores.containsKey("precio")) { %>
            <div class="error"><%= errores.get("precio") %></div>
            <% } %>
        </div>

        <div class="form-group">
            <label for="descripcion">Descripción</label>
            <textarea name="descripcion" id="descripcion"><%= producto.getDescripcion() != null ? producto.getDescripcion() : "" %></textarea>
        </div>

        <div class="form-group">
            <label for="fecha_elaboracion">Fecha de Elaboración</label>
            <input type="date" name="fecha_elaboracion" id="fecha_elaboracion"
                   value="<%= fechaElaboracion %>">
        </div>

        <div class="form-group">
            <label for="fecha_caducidad">Fecha de Caducidad</label>
            <input type="date" name="fecha_caducidad" id="fecha_caducidad"
                   value="<%= fechaCaducidad %>">
        </div>

        <input type="hidden" name="id"
               value="<%= producto.getId() != null ? producto.getId() : 0 %>">

        <input type="submit" class="btn-submit"
               value="<%= (producto.getId() != null && producto.getId() > 0) ? "Editar" : "Crear" %>">
    </form>

    <a href="<%=request.getContextPath()%>/productos" class="btn-back">Volver al listado</a>
</div>

</body>
</html>
