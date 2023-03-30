package com.logs.web;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@SpringBootApplication
public class WebApplication implements CommandLineRunner{
	public static void main(String[] args)  {
		SpringApplication.run(WebApplication.class, args);
	}
/* */
@Autowired
private JdbcTemplate jdbcTemplate;

public static String convertTimeFormat(String time) {
    DateTimeFormatter originalFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
    LocalDateTime localDateTime = LocalDateTime.parse(time, originalFormatter);
    ZonedDateTime zonedDateTime = ZonedDateTime.of(localDateTime, ZonedDateTime.now().getZone());
    DateTimeFormatter desiredFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss Z");
    String desiredString = zonedDateTime.format(desiredFormatter);
    desiredString = desiredString.substring(0, desiredString.length() - 2) + ":" + desiredString.substring(desiredString.length() - 2, desiredString.length());
    return desiredString;
}

public static ArrayList<Object> extractDetailsFromALog(String log) {
    String key;
    ArrayList<Object> value = new ArrayList<Object>();
    ArrayList<Object> keyValue = new ArrayList<Object>();

    Pattern pattern = Pattern.compile("\\[(.*?)\\]");
    Matcher matcher = pattern.matcher(log);

    String time = "", server = "", type = "", classInfo1 = "", classInfo2 = "", threadId = "", threadName = "", timeMillis = "", levelValue = "", reason = "";

    int i = 0;
    while (matcher.find()) {
        String group = matcher.group(1);
        switch (i) {
            case 0:
                time = group.trim();
                break;
            case 1:
                server = group.trim();
                break;
            case 2:
                type = group.trim();
                break;
            case 3:
                classInfo1 = group.trim();
                break;
            case 4:
                classInfo2 = group.trim();
                break;
            case 5:
                threadId = group.split("=")[1];
                threadId = threadId.replaceAll("_ThreadName", "").trim();
                threadName = group.split("=")[2].trim();
                break;
            case 6:
                timeMillis = group.split(": ")[1].trim();
                break;
            case 7:
                levelValue = group.split(": ")[1].trim();
                break;
            case 8:
                reason = group;
                reason = reason.replaceAll("\\]$", "").replaceAll("^\\[", "").trim();
                break;
        }
        i++;
    }
    if (reason.isBlank() || reason.indexOf("Exception:") == -1) {
        keyValue.add(false);
        keyValue.add(false);
        keyValue.add(false);
        return keyValue;
    }
    key = reason;
    value.add(convertTimeFormat(time));
    value.add(server);
    value.add(type);
    value.add(classInfo1);
    value.add(classInfo2);
    value.add(threadId);
    value.add(threadName);
    value.add(timeMillis);
    value.add(levelValue);
    value.add(time);
    String key1 = key;
    String exceptionWord = "Exception:";
    String exception="";
    String stack_trace="";
    int index = key1.indexOf(exceptionWord);
    index += 9;
    if (index != -1) {
        exception = key1.substring(0, index).trim();
        stack_trace = key1.substring(index + 1).trim();
        // System.out.println("Variable 1: " + exception);
        // System.out.println("Variable 2: " + stack_trace);
    }
    value.add(time);
    value.add(time);
    value.add(exception);
    value.add(stack_trace);
    keyValue.add(key);
    keyValue.add(value);
    keyValue.add(true);

    return keyValue;
}

public static boolean isSameLog(String text1, String text2) {
    if (text2.length() > text1.length()) {
        return isSameLog(text2, text1);
    }
    char[] t1 = text1.toCharArray();
    char[] t2 = text2.toCharArray();
    int[] prev = new int[t2.length + 1];
    int[] curr = new int[t2.length + 1];

    for (int pos1 = t1.length - 1; pos1 >= 0; pos1--) {
        for (int pos2 = t2.length - 1; pos2 >= 0; pos2--) {
            if (t1[pos1] == t2[pos2]) {
                curr[pos2] = 1 + prev[pos2 + 1];
            } else {
                curr[pos2] = Math.max(prev[pos2], curr[pos2 + 1]);
            }
        }
        int[] temp = prev;
        prev = curr;
        curr = temp;
    }
    int minLength = Math.min(text1.length(), text2.length());
    if (prev[0] >= (int)((float)minLength * 0.9)) {
        return false;
    }
    return false;
}

public static boolean compareTime(String time1, String time2) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
    LocalDateTime localDateTime1 = LocalDateTime.parse(time1, formatter);
    LocalDateTime localDateTime2 = LocalDateTime.parse(time2, formatter);
    int result = localDateTime1.compareTo(localDateTime2);
    if (result < 0) {
        return true;    // time1 occurs first
    }
    return false;
}
@Override
	public void run(String... args) throws Exception {
		String sql = "insert into logs values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
		HashMap<String, ArrayList<Object>> finalLogs = new HashMap<String, ArrayList<Object>>();
        File directory = new File("F:/Users/iamar/Downloads/logs");
        File[] files = directory.listFiles();
        File readFolder = new File("F:/Users/iamar/Downloads/read");
        if (!readFolder.exists()) {
            readFolder.mkdir();
        }

        for (File file : files) {
            try {
                FileReader fileReader = new FileReader(file.toString());
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                String line = bufferedReader.readLine();
                String log = line;
                while (line != null) {
                    line = bufferedReader.readLine();
                    if (line != null && line.isBlank() == false) {
                        log += line;
                    } else {
                        ArrayList<Object> keyValue = extractDetailsFromALog(log);
                        if ((boolean)keyValue.get(2) != false) {
                            String key = (String) keyValue.get(0);
                            @SuppressWarnings("unchecked")
                            ArrayList<Object> value = (ArrayList<Object>)keyValue.get(1);
                            if (finalLogs.containsKey(key)) {
                                ArrayList<Object> arrayList = finalLogs.get(key);
                                int frequency = (int)arrayList.get(0);
                                ++frequency;
                                arrayList.set(0, frequency);
                                if (compareTime(value.get(9).toString(), arrayList.get(10).toString())) {
                                    arrayList.set(10, value.get(9));
                                    arrayList.set(1, value.get(0));
                                }
                                if (compareTime(arrayList.get(11).toString(), value.get(9).toString())) {
                                    arrayList.set(11, value.get(9));
                                    arrayList.set(12, value.get(0));
                                }
                                finalLogs.put(key, arrayList);
                            } else {
                                boolean flag = false;
                                for (String logKey : finalLogs.keySet()) {
                                    if (isSameLog(key, logKey)) {
                                        flag = true;
                                        ArrayList<Object> arrayList = finalLogs.get(logKey);
                                        int frequency = (int)arrayList.get(0);
                                        ++frequency;
                                        arrayList.set(0, frequency);
                                        if (compareTime(value.get(9).toString(), arrayList.get(10).toString())) {
                                            arrayList.set(10, value.get(9));
                                            arrayList.set(1, value.get(0));
                                        }
                                        if (compareTime(arrayList.get(11).toString(), value.get(9).toString())) {
                                            arrayList.set(11, value.get(9));
                                            arrayList.set(12, value.get(0));
                                        }
                                        finalLogs.put(logKey, arrayList);
                                        break;
                                    }
                                }
                                if (!flag) {
                                    value.add(0, 1);
                                    value.add(value.get(10));
                                    value.add(value.get(1));
                                    finalLogs.put(key, value);
                                }
                            }
                        }
                        log = "";
                    }
                }
                bufferedReader.close();
                byte[] fileBytes = Files.readAllBytes(Paths.get(file.getPath()));

                    // write the contents of the file to the "read" folder
                    Path readFilePath = Paths.get(readFolder.getPath() + "/" + file.getName());
                    Files.write(readFilePath, fileBytes);

                    // delete the original file from the directory
                    file.delete();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        int count = 0;
        for (String key : finalLogs.keySet()) {
            ArrayList<Object> value = finalLogs.get(key);
			jdbcTemplate.update(sql,
							    key,
								value.get(0).toString(),
								value.get(1).toString(),
								value.get(2).toString(),
								value.get(3).toString(),
								value.get(4).toString(),
								value.get(5).toString(),
								value.get(6).toString(),
								value.get(7).toString(),
								value.get(8).toString(),
								value.get(9).toString(),
								value.get(10).toString(),
								value.get(11).toString(),
								value.get(12).toString(),
                                value.get(13).toString(),
                                value.get(14).toString());
            ++count;
        }
		System.out.println(count);
    }
}