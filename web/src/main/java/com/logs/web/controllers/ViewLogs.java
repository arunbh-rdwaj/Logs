package com.logs.web.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ViewLogs {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public ArrayList<ArrayList<String>> getLogs() {
        int i = 0;
        List<Map<String, Object>> rows = jdbcTemplate.queryForList("select * from logs");
        ArrayList<ArrayList<String>> values = new ArrayList<ArrayList<String>>();
        for (Map<String, Object> row : rows) {
            ArrayList<String> value = new ArrayList<String>();
        //   String name = row.get("name").toString();
            String frequency = row.get("frequency").toString();
            String rstart = row.get("representing_start_time").toString();
            String server = row.get("server").toString();
            String type = row.get("type").toString();
        //    String class1 = row.get("classInfo1").toString();
        //    String class2 = row.get("classInfo2").toString();
            String threadid = row.get("threadid").toString();
            String threadname = row.get("threadName").toString();
            String timemillis = row.get("timeMillis").toString();
            String levelvalue = row.get("levelValue").toString();
            String rend = row.get("representing_end_time").toString();
            String exception=row.get("exception").toString();
            String trace = row.get("stack_trace").toString();
            ++i;
            value.add(exception);
            value.add(trace);
            value.add(frequency);
            value.add(rstart);
            value.add(rend);
            value.add(server);
            value.add(type);
            value.add(threadid);
            value.add(threadname);
            value.add(timemillis);
            value.add(levelvalue);
            values.add(value);

        }
        System.out.println(i);
        return values;
    }

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String displayLogs(Model model) {
        model.addAttribute("logs", getLogs());
        System.out.println("\nDone\n");
        return "index";
    }
}
