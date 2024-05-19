package org.ruananta.parser.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class WebParserService {

    public List<String[]> parseWebPage(String url, String cssSelector) throws IOException {
        Document doc = Jsoup.connect(url).get();
        Elements elements = doc.select(cssSelector);

        List<String[]> data = new ArrayList<>();
        for (Element element : elements) {
            String[] row = new String[] {
                    element.text()
            };
            data.add(row);
        }
        return data;
    }
}

