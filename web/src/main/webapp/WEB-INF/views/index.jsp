<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!--
<!DOCTYPE html>
<html>
  <head>
    <title>Logs</title>
  </head>
  <h1>Logs</h1>
  <body>
    <h1>Hello world</h1>
    <table border="1">
      <tr>
        <th>Name</th>
        <th>Frequency</th>
        <th>First occurrence</th>
        <th>Last occurrence</th>
        <th>Server</th>
        <th>Type</th>
        <th>Class1</th>
        <th>Class2</th>
        <th>Thread ID</th>
        <th>Thread Name</th>
        <th>Time millis</th>
        <th>Level value</th>
        <th>Exception</th>
        <th>Stack Trace</th>
      </tr>
      <c:forEach items="${logs}" var="row">
        <tr>
          <c:forEach items="${row}" var="col">
            <td style="max-height: 100px; overflow: hidden;">${col}</td>
          </c:forEach>
        </tr>
      </c:forEach>
    </table>
  </body>
</html>
//-->
%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
  <head>
    <title>Logs</title>
    <script>
      function sortTable(colNum) {
        var table, rows, switching, i, x, y, shouldSwitch;
        table = document.getElementsByTagName("table")[0];
        switching = true;
        while (switching) {
          switching = false;
          rows = table.rows;
          for (i = 1; i < rows.length - 1; i++) {
            shouldSwitch = false;
            x = rows[i].getElementsByTagName("td")[colNum];
            y = rows[i + 1].getElementsByTagName("td")[colNum];
            if (x.innerHTML.toLowerCase() > y.innerHTML.toLowerCase()) {
              shouldSwitch = true;
              break;
            }
          }
          if (shouldSwitch) {
            rows[i].parentNode.insertBefore(rows[i + 1], rows[i]);
            switching = true;
          }
        }
      }
    </script>
  </head>
  <body>
    <h1>Hello world</h1>
    <label for="column">Select a column to sort:</label>
    <input type="number" id="column" name="column" min="0" max="11">
    <button onclick="sortTable(document.getElementById('column').value)">Sort</button>
    <table border="1">
      <tr>
        <th>Name</th>
        <th>Frequency</th>
        <th>First occurrence</th>
        <th>Last occurrence</th>
        <th>Server</th>
        <th>Type</th>
        <th>Class1</th>
        <th>Class2</th>
        <th>Thread ID</th>
        <th>Thread Name</th>
        <th>Time millis</th>
        <th>Level value</th>
        <th>Exception</th>
        <th>Stack Trace</th>
      </tr>
      <c:forEach items="${logs}" var="row">
        <tr>
          <c:forEach items="${row}" var="col" >
            <td>${col}</td>
          </c:forEach>
        </tr>
      </c:forEach>
    </table>
  </body>
</html>