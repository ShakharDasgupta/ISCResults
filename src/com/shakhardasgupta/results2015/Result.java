package com.shakhardasgupta.results2015;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class Result {

    private final String exam;
    private final int uniqueID;

    public Result(String exam, int uniqueID) {
        this.exam = exam;
        this.uniqueID = uniqueID;
    }

    public String getDetails() throws IOException {
        URL url = new URL("http://cisceresults00.azurewebsites.net/Results/Result/ShowResult?courseCode=" + exam + "&uniqueId=" + uniqueID + "&captcha=BGFDA&code=H94VzaXibHhcq7GPmUqn9Q==");
        URLConnection uc = url.openConnection();
        Document doc = Jsoup.parse(uc.getInputStream(), "UTF-8", url.toExternalForm());
        if (doc.body().text().equals("Result not found!!!. Please check your entry or contact school")) {
            return "Invalid ID";
        }
        Iterator<Element> e = doc.select("span#Name, span#uid, span#SNo, span#school, div.datacontainer div.cellssub[style=\"width:48%\"], span#mark1, div#passresult").iterator();
        String text = "";
        while (e.hasNext()) {
            text += e.next().text() + ";";
        }
        return text;
    }

    public static void main(String[] args) {
        int i = Integer.parseInt(args[1]);
        Result result;
        String text;
        BufferedWriter output;
        try {
            while (i < Integer.parseInt(args[2])) {
                result = new Result(args[0], i);
                text = result.getDetails();
                if (!text.equals("Invalid ID")) {
                    output = new BufferedWriter(new FileWriter("result", true));
                    output.write(text + "\n");
                    output.close();
                    System.out.println(text);
                }
                i++;
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
            main(new String[]{args[0], Integer.toString(i), args[2]});
        }
    }
}
