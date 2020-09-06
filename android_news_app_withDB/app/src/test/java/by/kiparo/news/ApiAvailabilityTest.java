package by.kiparo.news;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

public class ApiAvailabilityTest {

    @Test public void testAvailability() throws Exception {
        URLConnection connection = new URL("https://fake-api.kiparo.by/news/news").openConnection();
        InputStream response = connection.getInputStream();

        StringBuffer buffer = new StringBuffer();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(response, Charset.defaultCharset()))) {
            for (String line; (line = reader.readLine()) != null; ) {
                buffer.append(line);
            }
        }

        assert buffer.length() > 0;
    }
}
