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
//

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
</html> -->

<!DOCTYPE html>
<html>
  <head>
    <title>Logs</title>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://cdn.datatables.net/1.11.4/js/jquery.dataTables.min.js"></script>
    <link rel="stylesheet" href="https://cdn.datatables.net/1.11.4/css/jquery.dataTables.min.css">
  </head>
  <body>
    <h1>Logs</h1>
    <label for="delete-row-input">Delete row:</label>
    <input type="number" id="delete-row-input">
    <button onclick="deleteRow()">Delete</button>
    <br>
    <label for="delete-col-input">Delete column:</label>
    <input type="number" id="delete-col-input">
    <button onclick="deleteColumn()">Delete</button>
    <button onclick="resetTable()">Reset table</button>
    <!-- <a href="#" download="logs.csv" onclick="downloadTable()">Download as CSV</a> -->
    <div style="width: 90%; height: 600px; overflow :auto;" border=1>
    <table id="logs-table" border="1">
      <thead>
        <tr>
          <th>Exception</th>
          <th>Stack Trace</th>
          <th>Frequency</th>
          <th>First occurrence</th>
          <th>Last occurrence</th>
          <th>Server</th>
          <th>Type</th>
          <th>Thread ID</th>
          <th>Thread Name</th>
          <th>Time millis</th>
          <th>Level value</th>
        </tr>
      </thead>
      <tbody>
        <c:forEach items="${logs}" var="row">
          <tr>
            <c:forEach items="${row}" var="col">
              <td style="max-height: 50px;
              max-width: 30px;
              overflow: hidden;
              text-overflow:ellipsis;
              white-space: nowrap;">${col}</td>
            </c:forEach>
          </tr>
        </c:forEach>
      </tbody>
    </table>
    </div>
    <script>
      function truncateText() {
          var cells = document.getElementsByTagName("td");
          for (var i = 0; i < cells.length; i++) {
              var cell = cells[i];
              var text = cell.innerHTML;
              if(text.length>40)
              {
              var truncated = text.substring(0, 40) + "...";
              cell.innerHTML = truncated;
              }
          }
      }
      window.onload = truncateText;
  </script>
    <script>
      $(document).ready(function() {
        var table = $('#logs-table').DataTable();
        table.columns().every(function() {
          var column = this;
          var header = $(column.header());
          var select = $('<select><option value=""></option></select>')
            .appendTo(header)
            .on('change', function() {
              var val = $.fn.dataTable.util.escapeRegex($(this).val());
              column.search(val ? '^' + val + '$' : '', true, false).draw();
            });
          column.data().unique().sort().each(function(d, j) {
            select.append($('<option></option>').text(d));
          });
        });
      });

      function sortByColumn(table, column) {
        var rows = Array.from(table.rows);
        var headerRow = rows.shift();
        var columnIndex = headerRow.cells[column].cellIndex;
        rows.sort(function(a, b) {
          return a.cells[columnIndex].textContent.localeCompare(b.cells[columnIndex].textContent);
        });
        table.tBodies[0].append(...rows);
      }

      function deleteColumn() {
        var table = document.querySelector('#logs-table');
        var columnNumber = document.getElementById('#delete-col-input');
        var headerRow = table.rows[0];
        headerRow.deleteCell(columnNumber);
        for (var i = 0; i < table.rows.length; i++) {
          table.rows[i].deleteCell(columnNumber);
        }
      }
      
      function deleteRow() {
        var table = document.querySelector('#logs-table');
        var rowNumber = document.getElementById('#delete-row-input');
        table.deleteRow(rowNumber);
      }

      /* function resetTable() {
        var table = $('#logs-table').DataTable();
        table.search('').columns().search('').draw();
        table.columns().visible(true);
        table.rows().deselect();
      } */
      function resetTable(){
        location.reload();
      }

      /* function downloadTable() {
        var table = document.querySelector('#logs-table');
        var csvContent = "data:text/csv;charset=utf-8,";
        var rows = Array.from(table.rows);
        
        // Get headers
        var headers = Array.from(rows[0].cells).map(function(cell) {
          return '"' + cell.textContent.trim() + '"';
        });
        var headerString = headers.join(',');
        csvContent += headerString + "\n";
        
        // Get data
        rows.slice(1).forEach(function(row, index) {
          var cells = Array.from(row.cells).map(function(cell) {
            return '"' + cell.textContent.trim() + '"';
          });
          var rowString = cells.join(',');
          csvContent += index < rows.length - 1 ? rowString + "\n" : rowString;
        });
        
        var encodedUri = encodeURI(csvContent);
        var blob = new Blob([csvContent], { type: 'text/csv' });
        var url = URL.createObjectURL(blob);
        var link = document.createElement("a");
        link.setAttribute("href", url);
        link.setAttribute("download", "logs.csv");
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
        URL.revokeObjectURL(url);
      } */
    </script>
  </body>
</html>