package org.ruananta.parser.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.ruananta.parser.engine.ExtractedData;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class WebParserService {

    public List<ExtractedData> parseWebPage(String url, String cssSelector) throws IOException {
        List<String> cssSelectors = new ArrayList<>();
        cssSelectors.add(cssSelector);
        return parse(url, cssSelectors);
    }

    public List<ExtractedData> parse(String url, List<String> selectors) throws IOException  {
        Document doc = Jsoup.connect(url).get();
        List<ExtractedData> extractedDataList = new ArrayList<>();
        for (String selector : selectors) {
            Elements elements = doc.select(selector);
            for (Element element : elements) {
                extractedDataList.add(new ExtractedData(element.text()));
            }
        }
        return extractedDataList;
    }
}

