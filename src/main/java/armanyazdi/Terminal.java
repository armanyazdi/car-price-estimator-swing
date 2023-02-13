package armanyazdi;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Terminal {
    private String model, gearbox, build, mileage, color, status, replace;

    // This method gets the car details from the user.
    public void userInput() {

        // Example: "Peugeot 207" or "renault tondar90"
        System.out.print("Car Model: ");
        Scanner carModel = new Scanner(System.in);
        model = carModel.nextLine().toLowerCase().strip().replace(" ", "-");

        dividerLine();

        // Example : "manual" or "automatic"
        System.out.println("1) Manual");
        System.out.println("2) Automatic");
        System.out.print("Car Gearbox: ");
        Scanner carGearbox = new Scanner(System.in);
        gearbox = carGearbox.nextLine();

        switch (Integer.parseInt(gearbox)) {
            case 1 -> gearbox = "manual";
            case 2 -> gearbox = "automatic";
        }

        dividerLine();

        // Example: 1400
        System.out.print("Car Build Year: ");
        Scanner buildYear = new Scanner(System.in);
        build = buildYear.nextLine();

        dividerLine();

        // Example: 10000
        System.out.print("Car Mileage (km): ");
        Scanner carMileage = new Scanner(System.in);
        mileage = carMileage.nextLine();

        dividerLine();

        // Example : "white" or "black"
        System.out.print("Car Color: ");
        Scanner carColor = new Scanner(System.in);
        color = carColor.nextLine().toLowerCase().strip();

        dividerLine();

        System.out.println("0) No Paint");
        System.out.println("1) One Paint");
        System.out.println("2) Two Paint");
        System.out.println("3) Multi Paint");
        System.out.println("4) Around Paint");
        System.out.println("5) Full Paint");
        System.out.println("6) Refinement");
        System.out.print("Car Body Status: ");
        Scanner bodyStatus = new Scanner(System.in);
        status = bodyStatus.nextLine();

        switch (Integer.parseInt(status)) {
            case 0 -> status = "no_paint";
            case 1 -> status = "one_paint";
            case 2 -> status = "two_paint";
            case 3 -> status = "multi_paint";
            case 4 -> status = "around_paint";
            case 5 -> status = "full_paint";
            case 6 -> status = "refinement";
        }

        dividerLine();

        System.out.println("0) No Replacements");
        System.out.println("1) Fender Replaced");
        System.out.println("2) Hood Replaced");
        System.out.println("3) Door Replaced");
        System.out.print("Car Body Replacements: ");
        Scanner bodyReplace = new Scanner(System.in);
        replace = bodyReplace.nextLine();

        switch (Integer.parseInt(replace)) {
            case 0 -> replace = "";
            case 1 -> replace = ",fender_replace";
            case 2 -> replace = ",hood_replace";
            case 3 -> replace = ",door_replace";
        }

        dividerLine();
    }

    // This method estimates the price of the car.
    public void priceEstimator() {

        // Gregorian to Jalali date converter.
        LocalDate localDate = LocalDate.now();
        int gregorianYear = Integer.parseInt(DateTimeFormatter.ofPattern("yyyy").format(localDate));
        int gregorianMonth = Integer.parseInt(DateTimeFormatter.ofPattern("MM").format(localDate));
        int gregorianDay = Integer.parseInt(DateTimeFormatter.ofPattern("dd").format(localDate));
        int[] gDate = gregorianToJalali(gregorianYear, gregorianMonth, gregorianDay);
        String jalaliDate = "%s/%s/%s".formatted(gDate[0], gDate[1], gDate[2]);

        // Scraper.
        System.out.println("Estimating Price ...");
        String fmtURL = "https://bama.ir/car/%s-y%s?mileage=%s&priced=1&seller=1&transmission=%s&color=%s&status=%s%s&sort=7".formatted(model, build, mileage, gearbox, color, status, replace);
        URL url;

        try {
            url = new URL(fmtURL);
            HttpURLConnection connection;

            try {
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("accept", "application/json");

                try {
                    InputStream responseStream = connection.getInputStream();
                    Document document = Jsoup.parse(responseStream, "UTF-8", fmtURL);
                    Elements prices = document.getElementsByClass("bama-ad__price");
                    ArrayList<String> pricesList = new ArrayList<>();
                    for (Element price : prices) pricesList.add(price.text());
                    int firstPrice = Integer.parseInt(pricesList.get(0).replace(",",""));
                    int secondPrice = (int) (firstPrice + firstPrice * 0.02);
                    DecimalFormat fmt = new DecimalFormat("#,###");
                    System.out.printf("\nPrice: %s - %s Toman on %s%n", fmt.format(firstPrice), fmt.format(secondPrice), jalaliDate);

                } catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (IOException e1) {
                e1.printStackTrace();
            }

        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        }
    }

    // This method generates divider lines.
    private void dividerLine() {
        System.out.println("-------------------------");
    }

    // This method converts Gregorian date to Jalali.
    private int[] gregorianToJalali(int gy, int gm, int gd) {
        int[] out = {(gm > 2) ? (gy + 1) : gy, 0, 0};
        {
            int[] g_d_m = {0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334};
            out[2] = 355666 + (365 * gy) + ((out[0] + 3) / 4) - ((out[0] + 99) / 100) + ((out[0] + 399) / 400) + gd + g_d_m[gm - 1];
        }
        out[0] = -1595 + (33 * (out[2] / 12053));
        out[2] %= 12053;
        out[0] += 4 * (out[2] / 1461);
        out[2] %= 1461;
        if (out[2] > 365) {
            out[0] += (out[2] - 1) / 365;
            out[2] = (out[2] - 1) % 365;
        }
        if (out[2] < 186) {
            out[1] = 1 + (out[2] / 31);
            out[2] = 1 + (out[2] % 31);
        } else {
            out[1] = 7 + ((out[2] - 186) / 30);
            out[2] = 1 + ((out[2] - 186) % 30);
        }
        return out;
    }
}