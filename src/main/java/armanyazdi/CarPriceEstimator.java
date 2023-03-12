package armanyazdi;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

public class CarPriceEstimator extends JFrame implements ActionListener {
    private String[] model, color;
    private String gearbox, production, mileage, status;
    private JLabel labelProductionToLetters, labelMileageToLetters;
    private JTextField textFieldProduction, textFieldMileage;
    private JComboBox<String> comboBoxModel, comboBoxGearbox, comboBoxColor, comboBoxStatus;
    private Font textFont, infoFont, priceFont;
    private long roundedFirstPrice, roundedSecondPrice;
    private final NumberFormat format = NumberFormat.getNumberInstance();

    private void fileReader(String file, ArrayList<String> list) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/data/".concat(file)));
            String line;
            while ((line = reader.readLine()) != null) list.add(line);
            reader.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void mainFrame() throws IOException, FontFormatException {
        JFrame frame = new JFrame("محاسبه قیمت خودرو کارکرده");
        frame.setSize(550, 640);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        JPanel panel = new JPanel();
        frame.add(panel);
        placeComponents(panel);
        frame.setVisible(true);
    }

    private void placeComponents(JPanel panel) throws IOException, FontFormatException {
        panel.setLayout(null);

        // Fonts
        File fileKalamehRegular = new File("src/main/resources/fonts/KalamehFaNum-Regular.ttf");
        File fileKalamehMedium = new File("src/main/resources/fonts/KalamehFaNum-Medium.ttf");
        Font fontKalamehRegular = Font.createFont(Font.TRUETYPE_FONT, fileKalamehRegular);
        Font fontKalamehMedium = Font.createFont(Font.TRUETYPE_FONT, fileKalamehMedium);

        Font titleFont = fontKalamehMedium.deriveFont(28f);
        Font lettersFont = fontKalamehRegular.deriveFont(16f);
        textFont = fontKalamehRegular.deriveFont(18f);
        infoFont = fontKalamehRegular.deriveFont(24f);
        priceFont = fontKalamehMedium.deriveFont(32f);

        // Lists
        String pleaseChoose = "----- انتخاب کنید -----";
        ArrayList<String> modelsList = new ArrayList<>();
        ArrayList<String> colorsList = new ArrayList<>();
        ArrayList<String> statusList = new ArrayList<>();

        // Add Models from the file to the list
        modelsList.add(pleaseChoose);
        fileReader("models.txt", modelsList);

        // Add Colors from the file to the list
        colorsList.add(pleaseChoose);
        fileReader("colors.txt", colorsList);

        // Add Statuses from the file to the list
        statusList.add(pleaseChoose);
        fileReader("status.txt", statusList);

        // Gearboxes List
        String[] gearboxList = {pleaseChoose, "اتوماتیک", "دنده ای"};

        // Labels
        JLabel labelModel = new JLabel("مدل خودرو", SwingConstants.RIGHT);
        labelModel.setBounds(350, 35, 150, 40);
        labelModel.setForeground(new Color(48, 46, 73));
        labelModel.setFont(titleFont);
        panel.add(labelModel);

        JLabel labelGearbox = new JLabel("گیربکس", SwingConstants.RIGHT);
        labelGearbox.setBounds(350, 110, 150, 40);
        labelGearbox.setForeground(new Color(48, 46, 73));
        labelGearbox.setFont(titleFont);
        panel.add(labelGearbox);

        JLabel labelProduction = new JLabel("سال تولید", SwingConstants.RIGHT);
        labelProduction.setBounds(350, 185, 150, 40);
        labelProduction.setForeground(new Color(48, 46, 73));
        labelProduction.setFont(titleFont);
        panel.add(labelProduction);

        labelProductionToLetters = new JLabel();
        labelProductionToLetters.setHorizontalAlignment(SwingConstants.RIGHT);
        labelProductionToLetters.setBounds(45, 222, 275, 40);
        labelProductionToLetters.setForeground(new Color(48, 46, 73));
        labelProductionToLetters.setFont(lettersFont);
        panel.add(labelProductionToLetters);

        JLabel labelMileage = new JLabel("کارکرد (km)", SwingConstants.RIGHT);
        labelMileage.setBounds(350, 260, 150, 40);
        labelMileage.setForeground(new Color(48, 46, 73));
        labelMileage.setFont(titleFont);
        panel.add(labelMileage);

        labelMileageToLetters = new JLabel();
        labelMileageToLetters.setHorizontalAlignment(SwingConstants.RIGHT);
        labelMileageToLetters.setBounds(45, 297, 275, 40);
        labelMileageToLetters.setForeground(new Color(48, 46, 73));
        labelMileageToLetters.setFont(lettersFont);
        panel.add(labelMileageToLetters);

        JLabel labelColor = new JLabel("رنگ خودرو", SwingConstants.RIGHT);
        labelColor.setBounds(350, 335, 150, 40);
        labelColor.setForeground(new Color(48, 46, 73));
        labelColor.setFont(titleFont);
        panel.add(labelColor);

        JLabel labelStatus = new JLabel("وضعیت بدنه", SwingConstants.RIGHT);
        labelStatus.setBounds(350, 410, 150, 40);
        labelStatus.setForeground(new Color(48, 46, 73));
        labelStatus.setFont(titleFont);
        panel.add(labelStatus);

        // Combo Boxes
        comboBoxModel = new JComboBox<>();
        comboBoxModel.setModel(new DefaultComboBoxModel<>(modelsList.toArray(new String[0])));
        comboBoxModel.setBounds(50, 35, 275, 40);
        ((JLabel) comboBoxModel.getRenderer()).setHorizontalAlignment(JLabel.RIGHT);
        comboBoxModel.setFont(textFont);
        panel.add(comboBoxModel);

        comboBoxGearbox = new JComboBox<>(gearboxList);
        comboBoxGearbox.setBounds(50, 110, 275, 40);
        ((JLabel) comboBoxGearbox.getRenderer()).setHorizontalAlignment(JLabel.RIGHT);
        comboBoxGearbox.setFont(textFont);
        panel.add(comboBoxGearbox);

        comboBoxColor = new JComboBox<>();
        comboBoxColor.setModel(new DefaultComboBoxModel<>(colorsList.toArray(new String[0])));
        comboBoxColor.setBounds(50, 335, 275, 40);
        ((JLabel) comboBoxColor.getRenderer()).setHorizontalAlignment(JLabel.RIGHT);
        comboBoxColor.setFont(textFont);
        panel.add(comboBoxColor);

        comboBoxStatus = new JComboBox<>();
        comboBoxStatus.setModel(new DefaultComboBoxModel<>(statusList.toArray(new String[0])));
        comboBoxStatus.setBounds(50, 410, 275, 40);
        ((JLabel) comboBoxStatus.getRenderer()).setHorizontalAlignment(JLabel.RIGHT);
        comboBoxStatus.setFont(textFont);
        panel.add(comboBoxStatus);

        // Text Fields
        textFieldProduction = new JTextField(4);
        textFieldProduction.setBounds(50, 185, 275, 40);
        textFieldProduction.setFont(textFont);
        textFieldProduction.addKeyListener(new textChangedListener());
        panel.add(textFieldProduction);

        textFieldMileage = new JTextField();
        textFieldMileage.setBounds(50, 260, 275, 40);
        textFieldMileage.setFont(textFont);
        textFieldMileage.addKeyListener(new textChangedListener());
        panel.add(textFieldMileage);

        // Buttons
        JButton estimateButton = new JButton("محاسبه قیمت");
        estimateButton.setBounds(145, 485, 250, 90);
        estimateButton.setForeground(new Color(255, 86, 119));
        estimateButton.setFont(titleFont);
        estimateButton.addActionListener(this);
        panel.add(estimateButton);
    }

    private void estimatePrice() {
        ArrayList<String> bamaPricesList = new ArrayList<>();
        ArrayList<String> divarPricesList = new ArrayList<>();
        long sumBama = 0;
        long sumDivar = 0;

        // Loading
        System.out.println("Estimating Price...");

        // URL
        String linkBama = "https://bama.ir/car/%s-y%s?mileage=%s&priced=1&seller=1&transmission=%s&color=%s&status=%s&sort=7"
                .formatted(model[0], production, mileage, gearbox, color[0], status);
        String linkDivar = "https://divar.ir/s/tehran/car/%s?color=%s&production-year=%s-%s&usage=%s-%s&business-type=personal&exchange=exclude-exchanges"
                .formatted(model[1], color[1], production, production, mileage, (int) (Integer.parseInt(mileage) * 1.5));
        URL urlBama, urlDivar;

        // Data Scraper
        try {
            urlBama = new URL(linkBama);
            urlDivar = new URL(linkDivar);
            HttpURLConnection connection;

            // Bama
            try {
                connection = (HttpURLConnection) urlBama.openConnection();
                connection.setRequestProperty("accept", "application/json");

                try {
                    InputStream responseStream = connection.getInputStream();
                    Document bama = Jsoup.parse(responseStream, "UTF-8", linkBama);
                    Elements bamaPrices = bama.getElementsByClass("bama-ad__price");

                    for (Element price : bamaPrices) bamaPricesList.add(price.text().replace(",", ""));
                    for (String price : bamaPricesList) sumBama += Long.parseLong(price);
                }
                catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            catch (IOException e2) {
                e2.printStackTrace();
            }

            // Divar
            try {
                connection = (HttpURLConnection) urlDivar.openConnection();
                connection.setRequestProperty("accept", "application/json");

                try {
                    InputStream responseStream = connection.getInputStream();
                    Document divar = Jsoup.parse(responseStream, "UTF-8", linkDivar);
                    Elements divarPrices = divar.getElementsByClass("kt-post-card__description");

                    for (Element price : divarPrices)
                        if (price.toString().contains("تومان"))
                            divarPricesList.add(persianToEnglish(price.text().replaceAll("[, تومان]", "")));

                    for (String price : divarPricesList) sumDivar += Long.parseLong(price);
                }
                catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            catch (IOException e2) {
                e2.printStackTrace();
            }
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }

        // Price Estimator
        // averagePrice = ((sumBama / bamaPricesList.size()) + (sumDivar / divarPricesList.size())) / 2;
        long averagePrice = sumBama / bamaPricesList.size();
        long firstPrice = (long) (averagePrice - averagePrice * 0.01);
        long secondPrice = (long) (averagePrice + averagePrice * 0.01);
        roundedFirstPrice = (firstPrice + 500000) / 1000000 * 1000000;
        roundedSecondPrice = (secondPrice + 500000) / 1000000 * 1000000;

        // Done
        System.out.println("Done!");
    }

    private void showPrice(JPanel panelPrice) {
        // Gregorian to Jalali date converter.
        LocalDate localDate = LocalDate.now();
        short gregorianYear = Short.parseShort(DateTimeFormatter.ofPattern("yyyy").format(localDate));
        byte gregorianMonth = Byte.parseByte(DateTimeFormatter.ofPattern("MM").format(localDate));
        byte gregorianDay = Byte.parseByte(DateTimeFormatter.ofPattern("dd").format(localDate));
        int[] gDate = gregorianToJalali(gregorianYear, gregorianMonth, gregorianDay);
        String jalaliDate = "%s/%s/%s".formatted(gDate[0], gDate[1], gDate[2]);

        // Day Name of Week
        Calendar calendar = Calendar.getInstance();
        String[] weekDays = new String[] {"یکشنبه", "دوشنبه", "سه شنبه", "چهارشنبه", "پنجشنبه", "جمعه", "شنبه"};
        String dayName = weekDays[calendar.get(Calendar.DAY_OF_WEEK) - 1];

        // Labels
        JLabel labelCar = new JLabel("%s، %s، مدل %s".formatted(
                comboBoxModel.getSelectedItem(),
                comboBoxColor.getSelectedItem(),
                production
        ), SwingConstants.CENTER);
        labelCar.setBounds(0, 35, 550, 40);
        labelCar.setForeground(new Color(48, 46, 73));
        labelCar.setFont(infoFont);
        panelPrice.add(labelCar);

        JLabel labelDetail = new JLabel("%s کیلومتر، %s".formatted(
                format.format(Integer.parseInt(mileage)),
                comboBoxStatus.getSelectedItem()
        ), SwingConstants.CENTER);
        labelDetail.setBounds(0, 95, 550, 40);
        labelDetail.setForeground(new Color(48, 46, 73));
        labelDetail.setFont(infoFont);
        panelPrice.add(labelDetail);

        JLabel labelPrice = new JLabel("%s تا %s تومان".formatted(format.format(roundedFirstPrice), format.format(roundedSecondPrice)), SwingConstants.CENTER);
        labelPrice.setBounds(0, 390, 550, 40);
        labelPrice.setForeground(new Color(84, 116, 248));
        labelPrice.setFont(priceFont);
        panelPrice.add(labelPrice);

        JLabel labelDate = new JLabel("تاریخ: %s %s".formatted(dayName, jalaliDate), SwingConstants.CENTER);
        labelDate.setBounds(0, 535, 550, 40);
        labelDate.setForeground(new Color(48, 46, 73));
        labelDate.setFont(infoFont);
        panelPrice.add(labelDate);

        JLabel labelDown = new JLabel("پایین", JLabel.CENTER);
        labelDown.setBounds(50, 305, 100, 40);
        labelDown.setOpaque(true);
        labelDown.setForeground(Color.WHITE);
        labelDown.setBackground(new Color(60, 160, 86));
        labelDown.setFont(textFont);
        panelPrice.add(labelDown);

        JLabel labelFair = new JLabel("منصفانه", JLabel.CENTER);
        labelFair.setBounds(150, 305, 250, 40);
        labelFair.setOpaque(true);
        labelFair.setForeground(Color.WHITE);
        labelFair.setBackground(new Color(84, 219, 135));
        labelFair.setFont(textFont);
        panelPrice.add(labelFair);

        JLabel labelUp = new JLabel("بالا", JLabel.CENTER);
        labelUp.setBounds(400, 305, 100, 40);
        labelUp.setOpaque(true);
        labelUp.setForeground(Color.WHITE);
        labelUp.setBackground(new Color(255, 213, 88));
        labelUp.setFont(textFont);
        panelPrice.add(labelUp);

        JLabel labelFirstPrice = new JLabel("%s تومان".formatted(format.format(roundedFirstPrice)), SwingConstants.CENTER);
        labelFirstPrice.setBounds(0, 235, 300, 40);
        labelFirstPrice.setForeground(new Color(48, 46, 73));
        labelFirstPrice.setFont(textFont);
        panelPrice.add(labelFirstPrice);

        JLabel labelSecondPrice = new JLabel("%s تومان".formatted(format.format(roundedSecondPrice)), SwingConstants.CENTER);
        labelSecondPrice.setBounds(275, 235, 250, 40);
        labelSecondPrice.setForeground(new Color(48, 46, 73));
        labelSecondPrice.setFont(textFont);
        panelPrice.add(labelSecondPrice);

        // Lines
        JLabel minimumLine = new JLabel("", JLabel.CENTER);
        minimumLine.setBounds(150, 280, 3, 25);
        minimumLine.setOpaque(true);
        minimumLine.setBackground(new Color(67, 69, 75));
        panelPrice.add(minimumLine);

        JLabel maximumLine = new JLabel("", JLabel.CENTER);
        maximumLine.setBounds(397, 280, 3, 25);
        maximumLine.setOpaque(true);
        maximumLine.setBackground(new Color(67, 69, 75));
        panelPrice.add(maximumLine);

        // Separators
        JSeparator firstSeparator = new JSeparator();
        firstSeparator.setOrientation(SwingConstants.HORIZONTAL);
        firstSeparator.setBounds(0, 165, 550, 10);
        panelPrice.add(firstSeparator);

        JSeparator secondSeparator = new JSeparator();
        secondSeparator.setOrientation(SwingConstants.HORIZONTAL);
        secondSeparator.setBounds(0, 495, 550, 10);
        panelPrice.add(secondSeparator);
    }

    // This method converts Persian/Arabic numbers to English.
    private static String persianToEnglish(String number) {
        char[] chars = new char[number.length()];
        for (int i = 0; i < number.length(); i++) {
            char ch = number.charAt(i);
            if (ch >= 0x0660 && ch <= 0x0669)
                ch -= 0x0660 - '0';
            else if (ch >= 0x06f0 && ch <= 0x06F9)
                ch -= 0x06f0 - '0';
            chars[i] = ch;
        }
        return new String(chars);
    }

    // This method converts Gregorian date to Jalali.
    private static int[] gregorianToJalali(short gy, byte gm, byte gd) {
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
        }
        else {
            out[1] = 7 + ((out[2] - 186) / 30);
            out[2] = 1 + ((out[2] - 186) % 30);
        }
        return out;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Cars
        switch (Objects.requireNonNull(comboBoxModel.getSelectedItem()).toString()) {
            case "پراید 111 EX" -> model = new String[]{"pride-111-ex", "pride/111/ex"};
            case "پراید 111 LX" -> model = new String[]{"pride-111-lx", "pride/111"};
            case "پراید 111 SE" -> model = new String[]{"pride-111-se", "pride/111/se"};
            case "پراید 111 SL" -> model = new String[]{"pride-111-sl", "pride/111/sl"};
            case "پراید 111 SX" -> model = new String[]{"pride-111-sx", "pride/111/sx"};
            case "پراید 131 EX" -> model = new String[]{"pride-131-ex", "pride/131/ex"};
            case "پراید 131 LE" -> model = new String[]{"pride-131-le", "pride/131/le"};
            case "پراید 131 SE" -> model = new String[]{"pride-131-se", "pride/131/se"};
            case "پراید 131 SL" -> model = new String[]{"pride-131-sl", "pride/131/sl"};
            case "پراید 131 SX" -> model = new String[]{"pride-131-sx", "pride/131/sx"};
            case "پراید 131 TL" -> model = new String[]{"pride-131-tl", "pride/131/tl"};
            case "پراید 132 ساده" -> model = new String[]{"pride-132-basic", "pride/132/basic"};
            case "پراید 132 EX" -> model = new String[]{"pride-132-ex", "pride/132/ex"};
            case "پراید 132 LE" -> model = new String[]{"pride-132-le", "pride/132"};
            case "پراید 132 SE" -> model = new String[]{"pride-132-se", "pride/132/se"};
            case "پراید 132 SL" -> model = new String[]{"pride-132-sl", "pride/132/sl"};
            case "پراید 132 SX" -> model = new String[]{"pride-132-sx", "pride/132/sx"};
            case "پراید 141 ساده" -> model = new String[]{"pride-141-basic", "pride/141/basic"};
            case "پراید 141 SE" -> model = new String[]{"pride-141-se", "pride/141/se"};
            case "پراید 141 SL" -> model = new String[]{"pride-141-sl", "pride/141/sl"};
            case "پراید 141 SX" -> model = new String[]{"pride-141-sx", "pride/141/sx"};
            case "پراید 151 پلاس" -> model = new String[]{"pride-151-plus", "pride/pickup/plus"};
            case "پراید 151 SE" -> model = new String[]{"pride-151-se", "pride/pickup/151-se"};
            case "پراید 151 SL" -> model = new String[]{"pride-151-sl", "pride/pickup/151-sl"};
            case "پراید صندوق دار" -> model = new String[]{"pride-sedan", "pride/sedan/petrol"};
            case "پراید هاچ بک" -> model = new String[]{"pride-hatchback", "pride/hatchback"};
            case "پژو 206 تیپ 1" -> model = new String[]{"peugeot-206ir-type1", "peugeot/206/1"};
            case "پژو 206 تیپ 2" -> model = new String[]{"peugeot-206ir-type2", "peugeot/206/2"};
            case "پژو 206 تیپ 3" -> model = new String[]{"peugeot-206ir-type3", "peugeot/206/3"};
            case "پژو 206 تیپ 3 پانوراما" -> model = new String[]{"peugeot-206ir-type3panorama", "peugeot/206/3p"};
            case "پژو 206 تیپ 4" -> model = new String[]{"peugeot-206ir-type4", "peugeot/206/4"};
            case "پژو 206 تیپ 5" -> model = new String[]{"peugeot-206ir-type5", "peugeot/206/5"};
            case "پژو 206 تیپ 6" -> model = new String[]{"peugeot-206ir-type6", "peugeot/206/6"};
            case "پژو 206 صندوقدار V1" -> model = new String[]{"peugeot-206sd-v1", "peugeot/206-sd/v1"};
            case "پژو 206 صندوقدار V10" -> model = new String[]{"peugeot-206sd-v10", "peugeot/206-sd/v10"};
            case "پژو 206 صندوقدار V2" -> model = new String[]{"peugeot-206sd-v2", "peugeot/206-sd/v2"};
            case "پژو 206 صندوقدار V20" -> model = new String[]{"peugeot-206sd-v20", "peugeot/206-sd/v20"};
            case "پژو 206 صندوقدار V6" -> model = new String[]{"peugeot-206sd-v6", "peugeot/206-sd/v6"};
            case "پژو 206 صندوقدار V8" -> model = new String[]{"peugeot-206sd-v8", "peugeot/206-sd/v8"};
            case "پژو 206 صندوقدار V9" -> model = new String[]{"peugeot-206sd-v9", "peugeot/206-sd/v9"};
            case "پژو 207 اتوماتیک TU5" -> model = new String[]{"peugeot-207-at", "peugeot/207i/automatic"};
            case "پژو 207 اتوماتیک TU5P" -> model = new String[]{"peugeot-207-attu5p", "peugeot/207i/automatic"};
            case "پژو 207 پانوراما اتوماتیک TU5" -> model = new String[]{"peugeot-207-automaticpanorama", "peugeot/207i/automatic-p"};
            case "پژو 207 پانوراما اتوماتیک TU5P" -> model = new String[]{"peugeot-207-automaticpanoramatu5p", "peugeot/207i/automatic-p"};
            case "پژو 207 پانوراما دنده ای" -> model = new String[]{"peugeot-207-manualpanorama", "peugeot/207i/manual-p"};
            case "پژو 207 دنده ای" -> model = new String[]{"peugeot-207-mt", "peugeot/207i/manual"};
            case "پژو 207 MC اتوماتیک" -> model = new String[]{"peugeot-207-automaticmc", "peugeot/207i/automatic-mc"};
            case "پژو 207 صندوقدار اتوماتیک" -> model = new String[]{"peugeot-207sd-at", "peugeot/207i-sd/automatic"};
            case "پژو 207 صندوقدار دنده ای" -> model = new String[]{"peugeot-207sd-mt", "peugeot/207i-sd/manual"};
            case "پژو 405 GL" -> model = new String[]{"peugeot-405-gl", "peugeot/405/gl-petrol"};
            case "پژو 405 GLI" -> model = new String[]{"peugeot-405-gli", "peugeot/405/gli-petrol"};
            case "پژو 405 GLX بنزینی" -> model = new String[]{"peugeot-405-glx", "peugeot/405/glx-petrol"};
            case "پژو 405 GLX دوگانه سوز" -> model = new String[]{"peugeot-405-glxcng", "peugeot/405/glx-bi-fuel(cng)"};
            case "پژو 405 SLX" -> model = new String[]{"peugeot-405-slx", "peugeot/405/slx"};
            case "پژو 2008" -> model = new String[]{"peugeot-2008", "peugeot/2008"};
            case "پژو پارس اتوماتیک" -> model = new String[]{"peugeot-pars-at", "peugeot/pars/automatic-tu5"};
            case "پژو پارس دوگانه سوز" -> model = new String[]{"peugeot-pars-cng", "peugeot/pars/bi-fuel"};
            case "پژو پارس ELX-TU5" -> model = new String[]{"peugeot-pars-elxtu5", "peugeot/pars/elx-tu5"};
            case "پژو پارس ELX-XU7" -> model = new String[]{"peugeot-pars-elx", "peugeot/pars/elx"};
            case "پژو پارس ELX-XU7P" -> model = new String[]{"peugeot-pars-elxxu7p", "peugeot/pars/elx"};
            case "پژو پارس ELX-XUM" -> model = new String[]{"peugeot-pars-elxxum", "peugeot/pars/elx-xum"};
            case "پژو پارس LX" -> model = new String[]{"peugeot-pars-lx", "peugeot/pars/lx-tu5"};
            case "پژو پارس XU7" -> model = new String[]{"peugeot-pars-mt", "peugeot/pars/latest"};
            case "پژو پارس XU7P" -> model = new String[]{"peugeot-pars-xu7p", "peugeot/pars/xu7p"};
            case "پژو روآ" -> model = new String[]{"peugeot-roa", "peugeot/roa/petrol"};
            case "پژو RD" -> model = new String[]{"peugeot-rd", "peugeot/rd/petrol"};
            case "پژو RDI" -> model = new String[]{"peugeot-rdi", "peugeot/rdi/petrol"};
            case "تیبا صندوقدار پلاس" -> model = new String[]{"tiba-sedan-plus", "tiba/sedan/plus"};
            case "تیبا صندوقدار EX" -> model = new String[]{"tiba-sedan-ex", "tiba/sedan/ex"};
            case "تیبا صندوقدار SL" -> model = new String[]{"tiba-sedan-sl", "tiba/sedan"};
            case "تیبا صندوقدار SX بنزینی" -> model = new String[]{"tiba-sedan-sx", "tiba/sedan/sx"};
            case "تیبا صندوقدار SX دوگانه سوز" -> model = new String[]{"tiba-sedan-sxcng", "tiba/sedan/sx-bi-fuel"};
            case "تیبا هاچ بک پلاس" -> model = new String[]{"tiba-hatchback-plus", "tiba/hatchback/plus"};
            case "تیبا هاچ بک EX" -> model = new String[]{"tiba-hatchback-ex", "tiba/hatchback/ex"};
            case "تیبا هاچ بک SX" -> model = new String[]{"tiba-hatchback-sx", "tiba/hatchback/sx"};
            case "جک S3 اتوماتیک" -> model = new String[]{"jac-s3-at", "jac/s3/automatic"};
            case "جک S5 اتوماتیک" -> model = new String[]{"jac-s5-at2000", "jac/s5/automatic"};
            case "جک S5 دنده ای" -> model = new String[]{"jac-s5-mt2000", "jac/s5/manual"};
            case "جک S5 نیوفیس" -> model = new String[]{"jac-s5-at1500", "jac/s5-new-face"};
            case "جک جی 3 سدان" -> model = new String[]{"jac-j3sedan", "jac/j3-sedan"};
            case "جک جی 3 هاچ بک" -> model = new String[]{"jac-j3hatchback", "jac/j3-hatchback"};
            case "جک جی 4" -> model = new String[]{"jac-j4", "jac/j4"};
            case "جک جی 5 اتوماتیک" -> model = new String[]{"jac-j5-at", "jac/j5/automatic-1500cc"};
            case "جک جی 5 دنده ای" -> model = new String[]{"jac-j5-mt", "jac/j5/manual-1500cc"};
            case "دانگ فنگ H30 کراس" -> model = new String[]{"dongfeng-h30cross", "dongfeng/h30-cross"};
            case "دنا معمولی" -> model = new String[]{"dena-1.7", "dena/basic"};
            case "دنا پلاس 5 دنده توربو" -> model = new String[]{"dena-plus-turbo", "dena/plus/turbo-2"};
            case "دنا پلاس 6 دنده توربو" -> model = new String[]{"dena-plus-turbo6mt", "dena/plus/manual-6-turbo"};
            case "دنا پلاس اتوماتیک توربو" -> model = new String[]{"dena-plus-turboautomatic", "dena/plus/automatic"};
            case "دنا پلاس دنده ای ساده" -> model = new String[]{"dena-plus-basicmanual", "dena/plus/manual-2"};
            case "رانا پلاس" -> model = new String[]{"runna-plus", "runna/plus"};
            case "رانا پلاس پانوراما" -> model = new String[]{"runna-pluspanorama", "runna/plus-p"};
            case "رانا EL" -> model = new String[]{"runna-el", "runna/el"};
            case "رانا LX" -> model = new String[]{"runna-lx", "runna/lx"};
            case "تارا اتوماتیک" -> model = new String[]{"tara-automatic", "tara/automatic"};
            case "تارا دنده ای" -> model = new String[]{"tara-manual", "tara/manual"};
            case "رنو پارس تندر" -> model = new String[]{"renault-parstondar", "renault/pars-tondar"};
            case "رنو تندر 90 اتوماتیک" -> model = new String[]{"renault-tondar90-at", "renault/tondar-90/automatic"};
            case "رنو تندر 90 پلاس اتوماتیک" -> model = new String[]{"renault-tondar90-plusat", "renault/tondar-90-plus/automatic"};
            case "رنو تندر 90 پلاس دنده ای" -> model = new String[]{"renault-tondar90-plusmt", "renault/tondar-90-plus/manual"};
            case "رنو تندر 90 E0" -> model = new String[]{"renault-tondar90-e0", "renault/tondar-90/e0-petrol"};
            case "رنو تندر 90 E1" -> model = new String[]{"renault-tondar90-e1", "renault/tondar-90/e1-petrol"};
            case "رنو تندر 90 E2" -> model = new String[]{"renault-tondar90-e2", "renault/tondar-90/e2-petrol"};
            case "رنو ساندرو اتوماتیک" -> model = new String[]{"renault-sandero-at", "renault/sandero/automatic"};
            case "رنو ساندرو دنده ای" -> model = new String[]{"renault-sandero-mt", "renault/sandero/manual"};
            case "رنو ساندرو استپ وی اتوماتیک" -> model = new String[]{"renault-sanderostepway-at", "renault/sandero-stepway/automatic"};
            case "رنو ساندرو استپ وی دنده ای" -> model = new String[]{"renault-sanderostepway-mt", "renault/sandero-stepway/manual"};
            case "ساینا اتوماتیک" -> model = new String[]{"saina-at", "saina/automatic"};
            case "ساینا پلاس دنده ای" -> model = new String[]{"saina-manualplus", "saina/manual/plus"};
            case "ساینا EX دنده ای" -> model = new String[]{"saina-exmt", "saina/manual/ex"};
            case "ساینا S دنده ای" -> model = new String[]{"saina-manuals", "saina/manual/s"};
            case "سمند سورن پلاس بنزینی" -> model = new String[]{"samand-soren-plus", "samand/soren-plus"};
            case "سمند سورن ساده" -> model = new String[]{"samand-soren-basic", "samand/soren/basic"};
            case "سمند سورن ELX" -> model = new String[]{"samand-soren-elx", "samand/soren/elx"};
            case "سمند سورن ELX توربو" -> model = new String[]{"samand-soren-elxturbo", "samand/soren/elx-turbo"};
            case "سمند EL" -> model = new String[]{"samand-el", "samand/el/petrol"};
            case "سمند LX EF7" -> model = new String[]{"samand-lx-ef7", "samand/lx/ef7-petrol"};
            case "سمند LX EF7 دوگانه سوز" -> model = new String[]{"samand-lx-ef7cng", "samand/lx/ef7"};
            case "سمند LX XU7" -> model = new String[]{"samand-lx-basic", "samand/lx/basic"};
            case "سمند SE" -> model = new String[]{"samand-se", "samand/se"};
            case "سمند X7" -> model = new String[]{"samand-x7", "samand/x7/petrol"};
            case "سیتروئن زانتیا 1.8" -> model = new String[]{"citroen-xantia-1.8superlux", "citroen/xantia/1800cc"};
            case "سیتروئن زانتیا 2.0 SX" -> model = new String[]{"citroen-xantia-2.0sx", "citroen/xantia/2000cc"};
            case "شاهین G" -> model = new String[]{"shahin-g", "shahin/g"};
            case "کوییک اتوماتیک" -> model = new String[]{"quick-atfull", "quick/automatic/full"};
            case "کوییک اتوماتیک پلاس" -> model = new String[]{"quick-atfullplus", "quick/automatic/full-plus"};
            case "کوییک دنده ای" -> model = new String[]{"quick-manual", "quick/manual/basic"};
            case "کوییک دنده ای R" -> model = new String[]{"quick-manualr", "quick/manual/r"};
            case "کوییک دنده ای S" -> model = new String[]{"quick-manuals", "quick/manual/s"};
            case "کوییک R پلاس اتوماتیک" -> model = new String[]{"quick-manualrplus-at", "quick/automatic/p-plus"};
            case "هایما S5 6 سرعته اتوماتیک" -> model = new String[]{"haima-s5-6at", "haima/s5/6-at"};
            case "هایما S5 گیربکس CVT" -> model = new String[]{"haima-s5-cvt", "haima/s5/at-cvt"};
            case "هایما S7 2.0" -> model = new String[]{"haima-s7-2.0l", "haima/s7/automatic-2000cc"};
            case "هایما S7 1.8 توربو" -> model = new String[]{"haima-s7-1.8lturbo", "haima/s7/automatic-turbo-1800cc"};
            case "هایما S7 1.8 توربو پلاس" -> model = new String[]{"haima-s7-1.8lturboplus", "haima/s7-plus"};
        }

        // Colors
        switch (Objects.requireNonNull(comboBoxColor.getSelectedItem()).toString()) {
            case "سفید" -> color = new String[]{"white", URLEncoder.encode("سفید", StandardCharsets.UTF_8)};
            case "مشکی" -> color = new String[]{"black", URLEncoder.encode("مشکی", StandardCharsets.UTF_8)};
            case "خاکستری" -> color = new String[]{"gray", URLEncoder.encode("خاکستری", StandardCharsets.UTF_8)};
            case "نقره ای" -> color = new String[]{"silver", URLEncoder.encode("نقره‌ای", StandardCharsets.UTF_8)};
            case "سفید صدفی" -> color = new String[]{"pearlwhite", URLEncoder.encode("سفید صدفی", StandardCharsets.UTF_8)};
            case "نوک مدادی" -> color = new String[]{"blacklead", URLEncoder.encode("نوک‌مدادی", StandardCharsets.UTF_8)};
            case "آبی" -> color = new String[]{"blue", URLEncoder.encode("آبی", StandardCharsets.UTF_8)};
            case "قهوه ای" -> color = new String[]{"brown", URLEncoder.encode("قهوه‌ای", StandardCharsets.UTF_8)};
            case "قرمز" -> color = new String[]{"red", URLEncoder.encode("قرمز", StandardCharsets.UTF_8)};
            case "سرمه ای" -> color = new String[]{"darkblue", URLEncoder.encode("سرمه‌ای", StandardCharsets.UTF_8)};
            case "بژ" -> color = new String[]{"beige", URLEncoder.encode("بژ", StandardCharsets.UTF_8)};
            case "تیتانیوم" -> color = new String[]{"titanium", URLEncoder.encode("تیتانیوم", StandardCharsets.UTF_8)};
            case "سربی" -> color = new String[]{"slategray", URLEncoder.encode("سربی", StandardCharsets.UTF_8)};
            case "سبز" -> color = new String[]{"green", URLEncoder.encode("سبز", StandardCharsets.UTF_8)};
            case "کربن بلک" -> color = new String[]{"carbonblack", URLEncoder.encode("کربن‌بلک", StandardCharsets.UTF_8)};
            case "آلبالویی" -> color = new String[]{"maroon" , URLEncoder.encode("آلبالویی", StandardCharsets.UTF_8)};
            case "نقرآبی" -> color = new String[]{"steelblue", URLEncoder.encode("نقرآبی", StandardCharsets.UTF_8)};
            case "دلفینی" -> color = new String[]{"dolohin", URLEncoder.encode("دلفینی", StandardCharsets.UTF_8)};
            case "زرد" -> color = new String[]{"yellow", URLEncoder.encode("زرد", StandardCharsets.UTF_8)};
            case "مسی" -> color = new String[]{"copper", URLEncoder.encode("مسی", StandardCharsets.UTF_8)};
            case "یشمی" -> color = new String[]{"jadegreen", URLEncoder.encode("یشمی", StandardCharsets.UTF_8)};
            case "بادمجانی" -> color = new String[]{"eggplant", URLEncoder.encode("بادمجانی", StandardCharsets.UTF_8)};
            case "نارنجی" -> color = new String[]{"orange", URLEncoder.encode("نارنجی", StandardCharsets.UTF_8)};
            case "ذغالی" -> color = new String[]{"charcoal", URLEncoder.encode("ذغالی", StandardCharsets.UTF_8)};
            case "طوسی" -> color = new String[]{"darkgray", URLEncoder.encode("طوسی", StandardCharsets.UTF_8)};
            case "زیتونی" -> color = new String[]{"olivegreen", URLEncoder.encode("زیتونی", StandardCharsets.UTF_8)};
            case "کرم" -> color = new String[]{"bisque", URLEncoder.encode("کرم", StandardCharsets.UTF_8)};
            case "گیلاسی" -> color = new String[]{"cherry", URLEncoder.encode("گیلاسی", StandardCharsets.UTF_8)};
            case "طلایی" -> color = new String[]{"golden", URLEncoder.encode("طلایی", StandardCharsets.UTF_8)};
            case "زرشکی" -> color = new String[]{"crimson", URLEncoder.encode("زرشکی", StandardCharsets.UTF_8)};
            case "اطلسی" -> color = new String[]{"satin", URLEncoder.encode("اطلسی", StandardCharsets.UTF_8)};
            case "برنز" -> color = new String[]{"bronze", URLEncoder.encode("برنز", StandardCharsets.UTF_8)};
            case "عنابی" -> color = new String[]{"darkred", URLEncoder.encode("عنابی", StandardCharsets.UTF_8)};
            case "خاکی" -> color = new String[]{"khaki", URLEncoder.encode("خاکی", StandardCharsets.UTF_8)};
            case "موکا" -> color = new String[]{"mocha", URLEncoder.encode("موکا", StandardCharsets.UTF_8)};
            case "بنفش" -> color = new String[]{"purple", URLEncoder.encode("بنفش", StandardCharsets.UTF_8)};
            case "پوست پیازی" -> color = new String[]{"onion", URLEncoder.encode("پوست‌پیازی", StandardCharsets.UTF_8)};
            case "یاسی" -> color = new String[]{"lilac", URLEncoder.encode("بنفش", StandardCharsets.UTF_8)};
            case "اخرائی" -> color = new String[]{"ochre", URLEncoder.encode("نارنجی", StandardCharsets.UTF_8)};
            case "صورتی" -> color = new String[]{"pink", URLEncoder.encode("بنفش", StandardCharsets.UTF_8)};
            case "شتری" -> color = new String[]{"camellike", URLEncoder.encode("خاکی", StandardCharsets.UTF_8)};
            case "مارون" -> color = new String[]{"maroon", URLEncoder.encode("آلبالویی", StandardCharsets.UTF_8)};
        }

        // Statuses
        switch (Objects.requireNonNull(comboBoxStatus.getSelectedItem()).toString()) {
            case "بدون رنگ" -> status = "no_paint";
            case "یک لکه رنگ" -> status = "one_paint";
            case "دو لکه رنگ" -> status = "two_paint";
            case "چند لکه رنگ" -> status = "multi_paint";
            case "صافکاری بدون رنگ" -> status = "refinement";
            case "دور رنگ" -> status = "around_paint";
            case "گلگیر رنگ" -> status = "fender_paint";
            case "کاپوت رنگ" -> status = "hood_paint";
            case "یک درب رنگ" -> status = "one_door";
            case "دو درب رنگ" -> status = "two_door";
            case "کامل رنگ" -> status = "full_paint";
            case "کاپوت تعویض" -> status = "hood_replace";
            case "گلگیر تعویض" -> status = "fender_replace";
            case "درب تعویض" -> status = "door_replace";
            case "اتاق تعویض" -> status = "room_replace";
            case "تصادفی" -> status = "crashed";
            case "سوخته" -> status = "burned";
            case "اوراقی" -> status = "scrap";
        }

        // Gearboxes
        switch (comboBoxGearbox.getSelectedIndex()) {
            case 0 -> gearbox = "";
            case 1 -> gearbox = "automatic";
            case 2 -> gearbox = "manual";
        }

        production = persianToEnglish(textFieldProduction.getText().trim());
        mileage = persianToEnglish(textFieldMileage.getText().trim());

        estimatePrice();

        if (e.getActionCommand().equals("محاسبه قیمت")) {
            JFrame framePrice = new JFrame("محاسبه قیمت خودرو کارکرده");
            framePrice.setSize(550, 640);
            framePrice.setLocationRelativeTo(null);
            framePrice.setResizable(false);
            JPanel panelPrice = new JPanel();
            framePrice.add(panelPrice);
            showPrice(panelPrice);
            panelPrice.setLayout(null);
            framePrice.setVisible(true);
        }
    }

    private class textChangedListener implements KeyListener {
        public void keyPressed(KeyEvent e) {}
        public void keyReleased(KeyEvent e) {

            if (!Objects.equals(textFieldProduction.getText(), "")) {
                String textProduction = NumberUtils.numberToWords(Short.parseShort(textFieldProduction.getText()));
                if (textProduction.contains("یک هزار و")) textProduction = textProduction.replace("یک هزار و", "هزار و");
                labelProductionToLetters.setText(textProduction);
            }

            if (!Objects.equals(textFieldMileage.getText(), "")) {
                String textMileage = NumberUtils.numberToWords(Integer.parseInt(textFieldMileage.getText()));
                labelMileageToLetters.setText("%s کیلومتر".formatted(textMileage));
            }
        }
        public void keyTyped(KeyEvent e) {}
    }
}