package xrate;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jdk.nashorn.internal.parser.JSONParser;

import java.io.*;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Properties;

import java.io.IOException;
import java.io.InputStream;

/**
 * Provide access to basic currency exchange rate services.
 * 
 * @author earthnet
 */
public class ExchangeRateReader {

    /**
     * Construct an exchange rate reader using the given base URL. All requests
     * will then be relative to that URL. If, for example, your source is Xavier
     * Finance, the base URL is http://api.finance.xaviermedia.com/api/ Rates
     * for specific days will be constructed from that URL by appending the
     * year, month, and day; the URL for 25 June 2010, for example, would be
     * http://api.finance.xaviermedia.com/api/2010/06/25.xml
     * 
     * @param baseURL
     *            the base URL for requests
     */

    private String accessKey;
    private String baseURL;

    public ExchangeRateReader(String baseURL) throws IOException {

        this.baseURL = baseURL;
        readAccessKeys();
    }

    private void readAccessKeys() throws IOException {
        Properties properties = new Properties();
        FileInputStream in = null;
        try {
            // Don't change this filename unless you know what you're doing.
            // It's crucial that we don't commit the file that contains the
            // (private) access keys. This file is listed in `.gitignore` so
            // it's safe to put keys there as we won't accidentally commit them.
            in = new FileInputStream("etc/access_keys.properties");
        } catch (FileNotFoundException e) {
            /*
             * If this error gets generated, make sure that you have the desired
             * properties file in your project's `etc` directory. You may need
             * to rename the file ending in `.sample` by removing that suffix.
             */
            System.err.println("Couldn't open etc/access_keys.properties; have you renamed the sample file?");
            throw(e);
        }
        properties.load(in);
        // This assumes we're using Fixer.io and that the desired access key is
        // in the properties file in the key labelled `fixer_io`.
        accessKey = properties.getProperty("fixer_io");
    }

    /**
     * Get the exchange rate for the specified currency against the base
     * currency (the Euro) on the specified date.
     * 
     * @param currencyCode
     *            the currency code for the desired currency
     * @param year
     *            the year as a four digit integer
     * @param month
     *            the month as an integer (1=Jan, 12=Dec)
     * @param day
     *            the day of the month as an integer
     * @return the desired exchange rate
     * @throws IOException
     */
    public float getExchangeRate(String currencyCode, int year, int month, int day) throws IOException {
        // TODO Your code here

        String monthStr = Integer.toString(month);
        String dayStr = Integer.toString(day);

        if (day < 10) {
            dayStr = "0" + dayStr;
        }
        if (month < 10) {
            monthStr = "0" + monthStr;
        }

        String url = baseURL + year + "-" + monthStr + "-" + dayStr + "?access_key=";
        URL xrReader = new URL(url);
        InputStream inputStream = xrReader.openStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        JsonObject reader2 = new JsonParser().parse(reader).getAsJsonObject();
        JsonObject rate = reader2.getAsJsonObject().get("rates").getAsJsonObject();
        float curr = rate.getAsJsonObject().get(currencyCode).getAsFloat();
        System.out.println("the currency value is " + curr);
        return curr;


    }

    /**
     * Get the exchange rate of the first specified currency against the second
     * on the specified date.
     * 
     * @param fromCurrency
     *            the currency code we're exchanging *from*
     * @param toCurrency
     *            the currency code we're exchanging *to*
     * @param year
     *            the year as a four digit integer
     * @param month
     *            the month as an integer (1=Jan, 12=Dec)
     * @param day
     *            the day of the month as an integer
     * @return the desired exchange rate
     * @throws IOException
     */
    public float getExchangeRate(
            String fromCurrency, String toCurrency,
            int year, int month, int day) throws IOException {
        // TODO Your code here
        throw new UnsupportedOperationException();
    }
}

