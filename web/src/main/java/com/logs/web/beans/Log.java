package com.logs.web.beans;
import java.util.ArrayList;

public class Log {
    public String name;
    public String frequency;
    public String rstart;
    public String server;
    public String type;
    public String class1;
    public String class2;
    public String threadId;
    public String threadName;
    public String timeMillis;
    public String levelValue;
    public String start;
    public String end;
    public String rend;
    public ArrayList<String> values = new ArrayList<String>();

    public void setValues(String a, String b, String c, String d, String e, String f, String g, String h, String i, String j, String k, String l, String m, String n) {
        this.name = a;
        this.frequency = b;
        this.rstart = c;
        this.server = d;
        this.type = e;
        this.class1 = f;
        this.class2 = g;
        this.threadId = h;
        this.threadName = i;
        this.timeMillis = j;
        this.levelValue = k;
        this.start = l;
        this.end = m;
        this.rend = n;
        this.values.add(a);
        this.values.add(b);
        this.values.add(c);
        this.values.add(d);
        this.values.add(e);
        this.values.add(f);
        this.values.add(g);
        this.values.add(h);
        this.values.add(i);
        this.values.add(j);
        this.values.add(k);
        this.values.add(l);
        this.values.add(m);
        this.values.add(n);
    }

    public ArrayList<String> getValues() {
        return this.values;
    }
}
