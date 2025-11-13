<%--
  Created by IntelliJ IDEA.
  User: Aaron
  Date: 13/11/2025
  Time: 8:33
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" import="models.*,com.itextpdf.text.*,com.itextpdf.text.pdf.*,java.io.*" %>
<%@ page import="models.DetalleCarro" %>
<%@ page import="models.ItemCarro" %>

<%
    DetalleCarro detalleCarro = (DetalleCarro) session.getAttribute("carro");

    String action = request.getParameter("action");
    if ("pdf".equals(action) && detalleCarro != null) {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=factura_carro.pdf");

        com.itextpdf.text.Document document = new com.itextpdf.text.Document();
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        // 🧾 Título principal
        Font tituloFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, new BaseColor(40, 60, 90));
        Paragraph titulo = new Paragraph("Factura - Carro de Compras\n\n", tituloFont);
        titulo.setAlignment(Element.ALIGN_CENTER);
        document.add(titulo);

        // 🧱 Tabla
        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{2, 4, 3, 2, 3});

        // Encabezados
        Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.WHITE);
        BaseColor headerColor = new BaseColor(50, 80, 120);
        String[] headers = {"ID Producto", "Nombre", "Precio", "Cantidad", "Subtotal (IVA 15%)"};
        for (String h : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(h, headerFont));
            cell.setBackgroundColor(headerColor);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(8);
            table.addCell(cell);
        }

        // Filas
        Font cellFont = new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL, BaseColor.BLACK);
        double totalSinIVA = 0;
        for (ItemCarro item : detalleCarro.getItem()) {
            double subtotalSinIVA = item.getProducto().getPrecio() * item.getCantidad();
            totalSinIVA += subtotalSinIVA;

            table.addCell(new Phrase(String.valueOf(item.getProducto().getId()), cellFont));
            table.addCell(new Phrase(item.getProducto().getNombre(), cellFont));
            table.addCell(new Phrase("$" + item.getProducto().getPrecio(), cellFont));
            table.addCell(new Phrase(String.valueOf(item.getCantidad()), cellFont));
            table.addCell(new Phrase("$" + String.format("%.2f", item.getSubtotalConIVA()), cellFont));
        }

        double totalConIVA = totalSinIVA * 1.15;

        // Total sin IVA
        PdfPCell total1 = new PdfPCell(new Phrase(
                "Total sin IVA: $" + String.format("%.2f", totalSinIVA),
                new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)
        ));
        total1.setColspan(5);
        total1.setHorizontalAlignment(Element.ALIGN_RIGHT);
        total1.setPadding(8);
        total1.setBackgroundColor(new BaseColor(245, 245, 245));
        table.addCell(total1);

        // Total con IVA
        PdfPCell total2 = new PdfPCell(new Phrase(
                "Total con IVA (15%): $" + String.format("%.2f", totalConIVA),
                new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)
        ));
        total2.setColspan(5);
        total2.setHorizontalAlignment(Element.ALIGN_RIGHT);
        total2.setPadding(10);
        total2.setBackgroundColor(new BaseColor(230, 240, 255));
        table.addCell(total2);

        document.add(table);
        document.add(new Paragraph("\nGracias por su compra 🛍️", new Font(Font.FontFamily.HELVETICA, 11, Font.ITALIC)));
        document.close();
        return;
    }
%>

<html>
<head>
    <title>Carro de Compras</title>
    <style>
        body {
            font-family: 'Segoe UI', Roboto, Helvetica, sans-serif;
            background: linear-gradient(135deg, #e0eafc, #cfdef3);
            margin: 0;
            padding: 0;
            display: flex;
            flex-direction: column;
            align-items: center;
            min-height: 100vh;
            color: #2b2b2b;
        }
        .header-container {
            display: flex;
            align-items: center;
            justify-content: center;
            gap: 20px;
            margin-top: 60px;
        }
        h1 {
            font-size: 2.2em;
            color: #1f3b57;
            text-transform: uppercase;
            letter-spacing: 1px;
            border-bottom: 3px solid #4b79a1;
            padding-bottom: 10px;
            text-shadow: 1px 1px 3px rgba(0,0,0,0.1);
            margin: 0;
        }
        .btn-descargar {
            background: linear-gradient(135deg, #4b79a1, #283e51);
            color: white;
            font-weight: 600;
            border: none;
            border-radius: 30px;
            padding: 12px 25px;
            cursor: pointer;
            box-shadow: 0 4px 15px rgba(43, 72, 101, 0.3);
            transition: all 0.3s ease;
            font-size: 0.95em;
        }
        .btn-descargar:hover {
            transform: scale(1.05);
            background: linear-gradient(135deg, #283e51, #4b79a1);
        }
        table {
            width: 80%;
            max-width: 900px;
            border-collapse: collapse;
            margin-top: 30px;
            background: rgba(255,255,255,0.9);
            border-radius: 12px;
            box-shadow: 0 8px 25px rgba(0,0,0,0.15);
            overflow: hidden;
            animation: fadeIn 1s ease-out;
        }
        th, td { padding: 15px; text-align: center; }
        th {
            background: #305680;
            color: #fff;
            font-weight: 600;
            text-transform: uppercase;
        }
        tr:nth-child(even) { background: #f4f7fa; }
        tr:hover { background: #dbe7f5; transition: background 0.3s ease; }
        td { color: #2c3e50; font-size: 1em; }
        tr.totales td {
            font-weight: bold;
            font-size: 1.1em;
            border-top: 2px solid #305680;
        }
        a {
            display: inline-block;
            margin: 15px 10px;
            padding: 12px 25px;
            text-decoration: none;
            background: linear-gradient(135deg, #4b79a1, #283e51);
            color: white;
            font-weight: 600;
            border-radius: 30px;
            box-shadow: 0 4px 15px rgba(43, 72, 101, 0.3);
            transition: all 0.3s ease;
        }
        a:hover {
            transform: scale(1.05);
            background: linear-gradient(135deg, #283e51, #4b79a1);
        }
        @keyframes fadeIn {
            from { opacity: 0; transform: translateY(30px); }
            to { opacity: 1; transform: translateY(0); }
        }
    </style>
</head>

<body>

<div class="header-container">
    <h1>Carro de Compras</h1>
    <form method="get">
        <input type="hidden" name="action" value="pdf">
        <button type="submit" class="btn-descargar">📄 Descargar PDF</button>
    </form>
</div>

<%
    if (detalleCarro == null || detalleCarro.getItem().isEmpty()) {
%>
<p>Lo sentimos, no hay productos en el carro de compras.</p>
<%
} else {
    double totalSinIVA = 0;
    for (ItemCarro item : detalleCarro.getItem()) {
        totalSinIVA += item.getProducto().getPrecio() * item.getCantidad();
    }
    double totalConIVA = totalSinIVA * 1.15;
%>

<table>
    <tr>
        <th>ID Producto</th>
        <th>Nombre</th>
        <th>Precio</th>
        <th>Cantidad</th>
        <th>Subtotal</th>
    </tr>

    <%
        for (ItemCarro item : detalleCarro.getItem()) {
    %>
    <tr>
        <td><%= item.getProducto().getId() %></td>
        <td><%= item.getProducto().getNombre() %></td>
        <td>$<%= item.getProducto().getPrecio() %></td>
        <td><%= item.getCantidad() %></td>
        <td>$<%= String.format("%.2f", item.getSubtotal()) %></td>
    </tr>
    <% } %>
    <tr class="totales">
        <td colspan="4" style="text-align:right; color:#1f3b57;">Total sin IVA:</td>
        <td style="font-weight:bold; color:#1f3b57;">$<%= String.format("%.2f", totalSinIVA) %></td>
    </tr>
    <tr class="totales">
        <td colspan="4" style="text-align:right; color:#1f3b57;">Total con IVA (15%):</td>
        <td style="font-weight:bold; color:#1f3b57;">$<%= String.format("%.2f", totalConIVA) %></td>
    </tr>
</table>

<% } %>

<p><a href="<%=request.getContextPath()%>/productos">Seguir comprando</a></p>
<p><a href="<%=request.getContextPath()%>/index.html">🔙</a></p>

</body>
</html>

