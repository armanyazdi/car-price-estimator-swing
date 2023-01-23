package com.armanyazdi;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


public class Estimator extends JFrame implements ActionListener {
    String model;
    String gearbox;
    String build;
    String mileage;
    String color;
    String status;
    String price;
    JFrame frame;
    JLabel labelModel, labelGearbox, labelBuild, labelMileage, labelColor, labelStatus;
    JTextField tfBuild, tfMileage;
    JComboBox<String> cboModel;
    JComboBox<String> cboGearbox;
    JComboBox<String> cboColor;
    JComboBox<String> cboStatus;
    Font titleFont;
    String jalaliDate;
    String pleaseChoose = "----- انتخاب کنید -----";

    public void mainFrame() throws IOException, FontFormatException {

        // GitHub Repository:
        System.out.println("https://github.com/armanyazdi/car-price-estimator-java");

        frame = new JFrame("محاسبه قیمت خودرو کارکرده");
        frame.setSize(550, 640);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        JPanel panel = new JPanel();
        frame.add(panel);
        placeComponents(panel);
        frame.setVisible(true);
    }

    public void placeComponents(JPanel panel) throws IOException, FontFormatException {
        panel.setLayout(null);

        File fontFile1 = new File("src/main/resources/fonts/KalamehFaNum-Medium.ttf");
        Font font1 = Font.createFont(Font.TRUETYPE_FONT, fontFile1);
        File fontFile2 = new File("src/main/resources/fonts/KalamehFaNum-Regular.ttf");
        Font font2 = Font.createFont(Font.TRUETYPE_FONT, fontFile2);
        titleFont = font1.deriveFont(28f);
        Font textFont = font2.deriveFont(18f);

        labelModel = new JLabel("مدل خودرو", SwingConstants.RIGHT);
        labelModel.setBounds(350,35,150,40);
        labelModel.setForeground(new Color(48, 46, 73));
        labelModel.setFont(titleFont);
        panel.add(labelModel);

        labelGearbox = new JLabel("گیربکس", SwingConstants.RIGHT);
        labelGearbox.setBounds(350,110,150,40);
        labelGearbox.setForeground(new Color(48, 46, 73));
        labelGearbox.setFont(titleFont);
        panel.add(labelGearbox);

        labelBuild = new JLabel("سال تولید", SwingConstants.RIGHT);
        labelBuild.setBounds(350,185,150,40);
        labelBuild.setForeground(new Color(48, 46, 73));
        labelBuild.setFont(titleFont);
        panel.add(labelBuild);

        labelMileage = new JLabel("کارکرد (KM)", SwingConstants.RIGHT);
        labelMileage.setBounds(350,260,150,40);
        labelMileage.setForeground(new Color(48, 46, 73));
        labelMileage.setFont(titleFont);
        panel.add(labelMileage);

        labelColor = new JLabel("رنگ خودرو", SwingConstants.RIGHT);
        labelColor.setBounds(350,335,150,40);
        labelColor.setForeground(new Color(48, 46, 73));
        labelColor.setFont(titleFont);
        panel.add(labelColor);

        labelStatus = new JLabel("وضعیت بدنه", SwingConstants.RIGHT);
        labelStatus.setBounds(350,410,150,40);
        labelStatus.setForeground(new Color(48, 46, 73));
        labelStatus.setFont(titleFont);
        panel.add(labelStatus);

        String[] modelsList = {
                pleaseChoose,

                "پراید 111 EX",
                "پراید 111 LX",
                "پراید 111 SE",
                "پراید 111 SL",
                "پراید 111 SX",
                "پراید 131 EX",
                "پراید 131 LE",
                "پراید 131 SE",
                "پراید 131 SL",
                "پراید 131 SX",
                "پراید 131 TL",
                "پراید 132 ساده",
                "پراید 132 EX",
                "پراید 132 LE",
                "پراید 132 SE",
                "پراید 132 SL",
                "پراید 132 SX",
                "پراید 141 ساده",
                "پراید 141 SX",
                "پراید 151 پلاس",
                "پراید 151 کانوپی",
                "پراید 151 SE",
                "پراید 151 SL",
                "پژو 206 تیپ 1",
                "پژو 206 تیپ 2",
                "پژو 206 تیپ 3",
                "پژو 206 تیپ 3 پانوراما",
                "پژو 206 تیپ 5",
                "پژو 206 تیپ 6",
                "پژو 206 صندوقدار V1",
                "پژو 206 صندوقدار V10",
                "پژو 206 صندوقدار V2",
                "پژو 206 صندوقدار V20",
                "پژو 206 صندوقدار V6",
                "پژو 206 صندوقدار V8",
                "پژو 206 صندوقدار V9",
                "پژو 207 اتوماتیک TU5",
                "پژو 207 اتوماتیک TU5P",
                "پژو 207 پانوراما اتوماتیک TU5",
                "پژو 207 پانوراما اتوماتیک TU5P",
                "پژو 207 پانوراما دنده ای",
                "پژو 207 دنده ای",
                "پژو 207 MC اتوماتیک",
                "پژو 207 صندوقدار اتوماتیک",
                "پژو 207 صندوقدار دنده ای",
                "پژو 405 GL",
                "پژو 405 GLX بنزینی",
                "پژو 405 GLX دوگانه سوز",
                "پژو 405 SLX",
                "پژو 2008",
                "پژو پارس اتوماتیک",
                "پژو پارس دوگانه سوز",
                "پژو پارس ELX-TU5",
                "پژو پارس ELX-XU7",
                "پژو پارس ELX-XU7P",
                "پژو پارس ELX-XUM",
                "پژو پارس LX",
                "پژو پارس XU7",
                "پژو پارس XU7P",
                "پژو روآ",
                "پژو RD",
                "پژو RDI",
                "تیبا صندوقدار پلاس",
                "تیبا صندوقدار EX",
                "تیبا صندوقدار SL",
                "تیبا صندوقدار SX بنزینی",
                "تیبا صندوقدار SX دوگانه سوز",
                "تیبا هاچ بک پلاس",
                "تیبا هاچ بک EX",
                "تیبا هاچ بک SX",
                "جک S3",
                "جک S5",
                "جک جی 3 سدان",
                "جک جی 3 هاچ بک",
                "جک جی 4",
                "جک جی 5",
                "دانگ فنگ H30 کراس",
                "دنا معمولی",
                "دنا پلاس 5 دنده توربو",
                "دنا پلاس 6 دنده توربو",
                "دنا پلاس اتوماتیک توربو",
                "دنا پلاس دنده ای ساده",
                "رانا پلاس",
                "رانا پلاس پانوراما",
                "رانا EL",
                "رانا LX",
                "تارا",
                "رنو پارس تندر",
                "رنو تندر 90 اتوماتیک",
                "رنو تندر 90 پلاس اتوماتیک",
                "رنو تندر 90 پلاس دنده ای",
                "رنو تندر 90 E0",
                "رنو تندر 90 E1",
                "رنو تندر 90 E2",
                "رنو ساندرو استپ وی",
                "ساینا اتوماتیک",
                "ساینا پلاس دنده ای",
                "ساینا EX دنده ای",
                "ساینا S دنده ای",
                "سمند سورن پلاس بنزینی",
                "سمند سورن پلاس دوگانه سوز",
                "سمند سورن ساده",
                "سمند سورن ELX",
                "سمند سورن ELX توربو",
                "سمند EL",
                "سمند LX EF7",
                "سمند LX EF7 دوگانه سوز",
                "سمند LX TU5",
                "سمند LX XU7",
                "سمند SE",
                "سمند X7",
                "سیتروئن زانتیا 1.8 سوپرلوکس",
                "سیتروئن زانتیا 2.0 SX",
                "سیتروئن زانتیا 2.0 X",
                "شاهین G",
                "کوییک اتوماتیک",
                "کوییک اتوماتیک پلاس",
                "کوییک دنده ای",
                "کوییک دنده ای R",
                "کوییک دنده ای S",
                "کوییک R پلاس اتوماتیک"
        };

        cboModel = new JComboBox<>(modelsList);
        cboModel.setBounds(50,35,275,40);
        ((JLabel)cboModel.getRenderer()).setHorizontalAlignment(JLabel.RIGHT);
        cboModel.setFont(textFont);
        panel.add(cboModel);

        String[] gearboxList = {pleaseChoose, "دستی", "اتوماتیک"};

        cboGearbox = new JComboBox<>(gearboxList);
        cboGearbox.setBounds(50,110,275,40);
        ((JLabel)cboGearbox.getRenderer()).setHorizontalAlignment(JLabel.RIGHT);
        cboGearbox.setFont(textFont);
        panel.add(cboGearbox);

        tfBuild = new JTextField(4);
        tfBuild.setBounds(50,185,275,40);
        tfBuild.setFont(textFont);
        panel.add(tfBuild);

        tfMileage = new JTextField();
        tfMileage.setBounds(50,260,275,40);
        tfMileage.setFont(textFont);
        panel.add(tfMileage);

        String[] colorsList = {
                pleaseChoose,

                "سفید",
                "مشکی",
                "خاکستری",
                "نقره ای",
                "سفید صدفی",
                "نوک مدادی",
                "آبی",
                "قهوه ای",
                "قرمز",
                "سرمه ای",
                "بژ",
                "تیتانیوم",
                "سربی",
                "سبز",
                "کربن بلک",
                "آلبالویی",
                "نقرآبی",
                "دلفینی",
                "زرد",
                "مسی",
                "یشمی",
                "بادمجانی",
                "نارنجی",
                "ذغالی",
                "طوسی",
                "زیتونی",
                "کرم",
                "گیلاسی",
                "طلایی",
                "زرشکی",
                "اطلسی",
                "برنز",
                "عنابی",
                "خاکی",
                "موکا",
                "بنفش",
                "پوست پیازی",
                "یاسی",
                "اخرائی",
                "صورتی",
                "شتری",
                "مارون"
        };

        cboColor = new JComboBox<>(colorsList);
        cboColor.setBounds(50,335,275,40);
        ((JLabel)cboColor.getRenderer()).setHorizontalAlignment(JLabel.RIGHT);
        cboColor.setFont(textFont);
        panel.add(cboColor);

        String[] statusList = {
                pleaseChoose,

                "بدون رنگ",
                "یک لکه رنگ",
                "دو لکه رنگ",
                "چند لکه رنگ",
                "صافکاری بدون رنگ",
                "دور رنگ",
                "گلگیر رنگ",
                "کاپوت رنگ",
                "یک درب رنگ",
                "دو درب رنگ",
                "کامل رنگ",
                "کاپوت تعویض",
                "گلگیر تعویض",
                "درب تعویض",
                "اتاق تعویض",
                "تصادفی",
                "سوخته",
                "اوراقی"
        };

        cboStatus = new JComboBox<>(statusList);
        cboStatus.setBounds(50,410,275,40);
        ((JLabel)cboStatus.getRenderer()).setHorizontalAlignment(JLabel.RIGHT);
        cboStatus.setFont(textFont);
        panel.add(cboStatus);

        JButton estimateButton = new JButton("محاسبه قیمت");
        estimateButton.setBounds(145, 495, 250, 80);
        estimateButton.setForeground(new Color(255, 86, 119));
        estimateButton.setFont(titleFont);
        estimateButton.addActionListener(this);
        panel.add(estimateButton);

        // Gregorian to Jalali date converter.
        LocalDate localDate = LocalDate.now();
        int gregorianYear = Integer.parseInt(DateTimeFormatter.ofPattern("yyyy").format(localDate));
        int gregorianMonth = Integer.parseInt(DateTimeFormatter.ofPattern("MM").format(localDate));
        int gregorianDay = Integer.parseInt(DateTimeFormatter.ofPattern("dd").format(localDate));
        int[] gDate = gregorianToJalali(gregorianYear, gregorianMonth, gregorianDay);
        jalaliDate = "%s/%s/%s".formatted(gDate[0], gDate[1], gDate[2]);
    }

    // This method converts Persian/Arabic numbers to English.
    public static String persianToEnglish(String number) {
        char[] chars = new char[number.length()];
        for(int i = 0; i < number.length(); i++) {
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
    public int[] gregorianToJalali(int gy, int gm, int gd) {
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

    @Override
    public void actionPerformed(ActionEvent e) {

        if (cboModel.getSelectedItem() == "پراید 111 EX") model = "pride-111-ex";
        if (cboModel.getSelectedItem() == "پراید 111 LX") model = "pride-111-lx";
        if (cboModel.getSelectedItem() == "پراید 111 SE") model = "pride-111-se";
        if (cboModel.getSelectedItem() == "پراید 111 SL") model = "pride-111-ex";
        if (cboModel.getSelectedItem() == "پراید 111 SX") model = "pride-111-sx";
        if (cboModel.getSelectedItem() == "پراید 131 EX") model = "pride-131-ex";
        if (cboModel.getSelectedItem() == "پراید 131 LE") model = "pride-131-le";
        if (cboModel.getSelectedItem() == "پراید 131 SE") model = "pride-131-se";
        if (cboModel.getSelectedItem() == "پراید 131 SL") model = "pride-131-sl";
        if (cboModel.getSelectedItem() == "پراید 131 SX") model = "pride-131-sx";
        if (cboModel.getSelectedItem() == "پراید 131 TL") model = "pride-131-tl";
        if (cboModel.getSelectedItem() == "پراید 132 ساده") model = "pride-132-basic";
        if (cboModel.getSelectedItem() == "پراید 132 EX") model = "pride-132-ex";
        if (cboModel.getSelectedItem() == "پراید 132 LE") model = "pride-132-le";
        if (cboModel.getSelectedItem() == "پراید 132 SE") model = "pride-132-se";
        if (cboModel.getSelectedItem() == "پراید 132 SL") model = "pride-132-sl";
        if (cboModel.getSelectedItem() == "پراید 132 SX") model = "pride-132-sx";
        if (cboModel.getSelectedItem() == "پراید 141 ساده") model = "pride-141-basic";
        if (cboModel.getSelectedItem() == "پراید 141 SX") model = "pride-141-sx";
        if (cboModel.getSelectedItem() == "پراید 151 پلاس") model = "pride-151-plus";
        if (cboModel.getSelectedItem() == "پراید 151 کانوپی") model = "pride-151-canopi";
        if (cboModel.getSelectedItem() == "پراید 151 SE") model = "pride-151-se";
        if (cboModel.getSelectedItem() == "پراید 151 SL") model = "pride-151-sl";
        if (cboModel.getSelectedItem() == "پژو 206 تیپ 1") model = "peugeot-206ir-type1";
        if (cboModel.getSelectedItem() == "پژو 206 تیپ 2") model = "peugeot-206ir-type2";
        if (cboModel.getSelectedItem() == "پژو 206 تیپ 3") model = "peugeot-206ir-type3";
        if (cboModel.getSelectedItem() == "پژو 206 تیپ 3 پانوراما") model = "peugeot-206ir-type3panorama";
        if (cboModel.getSelectedItem() == "پژو 206 تیپ 5") model = "peugeot-206ir-type5";
        if (cboModel.getSelectedItem() == "پژو 206 تیپ 6") model = "peugeot-206ir-type6";
        if (cboModel.getSelectedItem() == "پژو 206 صندوقدار V1") model = "peugeot-206sd-v1";
        if (cboModel.getSelectedItem() == "پژو 206 صندوقدار V10") model = "peugeot-206sd-v10";
        if (cboModel.getSelectedItem() == "پژو 206 صندوقدار V2") model = "peugeot-206sd-v2";
        if (cboModel.getSelectedItem() == "پژو 206 صندوقدار V20") model = "peugeot-206sd-v20";
        if (cboModel.getSelectedItem() == "پژو 206 صندوقدار V6") model = "peugeot-206sd-v6";
        if (cboModel.getSelectedItem() == "پژو 206 صندوقدار V8") model = "peugeot-206sd-v8";
        if (cboModel.getSelectedItem() == "پژو 206 صندوقدار V9") model = "peugeot-206sd-v9";
        if (cboModel.getSelectedItem() == "پژو 207 پانوراما اتوماتیک TU5") model = "peugeot-207-at";
        if (cboModel.getSelectedItem() == "پژو 207 پانوراما اتوماتیک TU5P") model = "peugeot-207-attu5p";
        if (cboModel.getSelectedItem() == "پژو 207 پانوراما دنده ای") model = "peugeot-207-manualpanorama";
        if (cboModel.getSelectedItem() == "پژو 207 دنده ای") model = "peugeot-207-mt";
        if (cboModel.getSelectedItem() == "پژو 207 MC اتوماتیک") model = "peugeot-207-automaticmc";
        if (cboModel.getSelectedItem() == "پژو 207 صندوقدار اتوماتیک") model = "peugeot-207sd-at";
        if (cboModel.getSelectedItem() == "پژو 207 صندوقدار دنده ای") model = "peugeot-207sd-mt";
        if (cboModel.getSelectedItem() == "پژو 405 GL") model = "peugeot-405-gl";
        if (cboModel.getSelectedItem() == "پژو 405 GLX بنزینی") model = "peugeot-405-glx";
        if (cboModel.getSelectedItem() == "پژو 405 GLX دوگانه سوز") model = "peugeot-405-glxcng";
        if (cboModel.getSelectedItem() == "پژو 405 SLX") model = "peugeot-405-slx";
        if (cboModel.getSelectedItem() == "پژو 2008") model = "peugeot-2008";
        if (cboModel.getSelectedItem() == "پژو پارس اتوماتیک") model = "peugeot-pars-at";
        if (cboModel.getSelectedItem() == "پژو پارس دوگانه سوز") model = "peugeot-pars-cng";
        if (cboModel.getSelectedItem() == "پژو پارس ELX-TU5") model = "peugeot-pars-elxtu5";
        if (cboModel.getSelectedItem() == "پژو پارس ELX-XU7") model = "peugeot-pars-elx";
        if (cboModel.getSelectedItem() == "پژو پارس ELX-XU7P") model = "peugeot-pars-elxxu7p";
        if (cboModel.getSelectedItem() == "پژو پارس ELX-XUM") model = "peugeot-pars-elxxum";
        if (cboModel.getSelectedItem() == "پژو پارس LX") model = "peugeot-pars-lx";
        if (cboModel.getSelectedItem() == "پژو پارس XU7") model = "peugeot-pars-mt";
        if (cboModel.getSelectedItem() == "پژو پارس XU7P") model = "peugeot-pars-xu7p";
        if (cboModel.getSelectedItem() == "پژو روآ") model = "peugeot-roa";
        if (cboModel.getSelectedItem() == "پژو RD") model = "peugeot-rd";
        if (cboModel.getSelectedItem() == "پژو RDI") model = "peugeot-rdi";
        if (cboModel.getSelectedItem() == "تیبا صندوقدار پلاس") model = "tiba-sedan-plus";
        if (cboModel.getSelectedItem() == "تیبا صندوقدار EX") model = "tiba-sedan-ex";
        if (cboModel.getSelectedItem() == "تیبا صندوقدار SL") model = "tiba-sedan-sl";
        if (cboModel.getSelectedItem() == "تیبا صندوقدار SX بنزینی") model = "tiba-sedan-sx";
        if (cboModel.getSelectedItem() == "تیبا صندوقدار SX دوگانه سوز") model = "tiba-sedan-sxcng";
        if (cboModel.getSelectedItem() == "تیبا هاچ بک پلاس") model = "tiba-hatchback-plus";
        if (cboModel.getSelectedItem() == "تیبا هاچ بک EX") model = "tiba-hatchback-ex";
        if (cboModel.getSelectedItem() == "تیبا هاچ بک SX") model = "tiba-hatchback-sx";
        if (cboModel.getSelectedItem() == "جک S3") model = "jac-s3";
        if (cboModel.getSelectedItem() == "جک S5") model = "jac-s5";
        if (cboModel.getSelectedItem() == "جک جی 3 سدان") model = "jac-j3sedan";
        if (cboModel.getSelectedItem() == "جک جی 3 هاچ بک") model = "jac-j3hatchback";
        if (cboModel.getSelectedItem() == "جک جی 4") model = "jac-j4";
        if (cboModel.getSelectedItem() == "جک جی 5") model = "jac-j5";
        if (cboModel.getSelectedItem() == "دانگ فنگ H30 کراس") model = "dongfeng-h30cross";
        if (cboModel.getSelectedItem() == "دنا معمولی") model = "dena-1.7";
        if (cboModel.getSelectedItem() == "دنا پلاس 5 دنده توربو") model = "dena-plus-turbo";
        if (cboModel.getSelectedItem() == "دنا پلاس 6 دنده توربو") model = "dena-plus-turbo6mt";
        if (cboModel.getSelectedItem() == "دنا پلاس اتوماتیک توربو") model = "dena-plus-turboautomatic";
        if (cboModel.getSelectedItem() == "دنا پلاس دنده ای ساده") model = "dena-plus-basicmanual";
        if (cboModel.getSelectedItem() == "رانا پلاس") model = "runna-plus";
        if (cboModel.getSelectedItem() == "رانا پلاس پانوراما") model = "runna-pluspanorama";
        if (cboModel.getSelectedItem() == "رانا EL") model = "runna-el";
        if (cboModel.getSelectedItem() == "رانا LX") model = "runna-lx";
        if (cboModel.getSelectedItem() == "تارا") model = "tara";
        if (cboModel.getSelectedItem() == "رنو پارس تندر") model = "renault-parstondar";
        if (cboModel.getSelectedItem() == "رنو تندر 90 اتوماتیک") model = "renault-tondar90-at";
        if (cboModel.getSelectedItem() == "رنو تندر 90 پلاس اتوماتیک") model = "renault-tondar90-plusat";
        if (cboModel.getSelectedItem() == "رنو تندر 90 پلاس دنده ای") model = "renault-tondar90-plusmt";
        if (cboModel.getSelectedItem() == "رنو تندر 90 E0") model = "renault-tondar90-e0";
        if (cboModel.getSelectedItem() == "رنو تندر 90 E1") model = "renault-tondar90-e1";
        if (cboModel.getSelectedItem() == "رنو تندر 90 E2") model = "renault-tondar90-e2";
        if (cboModel.getSelectedItem() == "رنو ساندرو") model = "renault-sandero";
        if (cboModel.getSelectedItem() == "رنو ساندرو استپ وی") model = "renault-sanderostepway";
        if (cboModel.getSelectedItem() == "ساینا اتوماتیک") model = "saina-at";
        if (cboModel.getSelectedItem() == "ساینا پلاس دنده ای") model = "saina-manualplus";
        if (cboModel.getSelectedItem() == "ساینا EX دنده ای") model = "saina-exmt";
        if (cboModel.getSelectedItem() == "ساینا S دنده ای") model = "saina-manuals";
        if (cboModel.getSelectedItem() == "سمند سورن پلاس بنزینی") model = "samand-soren-plus";
        if (cboModel.getSelectedItem() == "سمند سورن پلاس دوگانه سوز") model = "samand-soren-pluscng";
        if (cboModel.getSelectedItem() == "سمند سورن ساده") model = "samand-soren-basic";
        if (cboModel.getSelectedItem() == "سمند سورن ELX") model = "samand-soren-elx";
        if (cboModel.getSelectedItem() == "سمند سورن ELX توربو") model = "samand-soren-elxturbo";
        if (cboModel.getSelectedItem() == "سمند EL") model = "samand-el";
        if (cboModel.getSelectedItem() == "سمند LX EF7") model = "samand-lx-ef7";
        if (cboModel.getSelectedItem() == "سمند LX EF7 دوگانه سوز") model = "samand-lx-ef7cng";
        if (cboModel.getSelectedItem() == "سمند LX TU5") model = "samand-lx-tu5";
        if (cboModel.getSelectedItem() == "سمند LX XU7") model = "samand-lx-basic";
        if (cboModel.getSelectedItem() == "سمند SE") model = "samand-se";
        if (cboModel.getSelectedItem() == "سمند X7") model = "samand-x7";
        if (cboModel.getSelectedItem() == "سیتروئن زانتیا 1.8 سوپرلوکس") model = "citroen-xantia-1.8superlux";
        if (cboModel.getSelectedItem() == "سیتروئن زانتیا 2.0 SX") model = "citroen-xantia-2.0sx";
        if (cboModel.getSelectedItem() == "سیتروئن زانتیا 2.0 X") model = "citroen-xantia-2.0x";
        if (cboModel.getSelectedItem() == "شاهین G") model = "shahin-g";
        if (cboModel.getSelectedItem() == "کوییک اتوماتیک") model = "quick-atfull";
        if (cboModel.getSelectedItem() == "کوییک اتوماتیک پلاس") model = "quick-atfullplus";
        if (cboModel.getSelectedItem() == "کوییک دنده ای") model = "quick-manual";
        if (cboModel.getSelectedItem() == "کوییک دنده ای R") model = "quick-manualr";
        if (cboModel.getSelectedItem() == "کوییک دنده ای S") model = "quick-manuals";
        if (cboModel.getSelectedItem() == "کوییک R پلاس اتوماتیک") model = "quick-manualrplus-at";

        switch (cboGearbox.getSelectedIndex()) {
            case 0 -> gearbox = "";
            case 1 -> gearbox = "manual";
            case 2 -> gearbox = "automatic";
        }

        if (cboColor.getSelectedItem() == "سفید") color = "white";
        if (cboColor.getSelectedItem() == "مشکی") color = "black";
        if (cboColor.getSelectedItem() == "خاکستری") color = "gray";
        if (cboColor.getSelectedItem() == "نقره ای") color = "silver";
        if (cboColor.getSelectedItem() == "سفید صدفی") color = "pearlwhite";
        if (cboColor.getSelectedItem() == "نوک مدادی") color = "blacklead";
        if (cboColor.getSelectedItem() == "آبی") color = "blue";
        if (cboColor.getSelectedItem() == "قهوه ای") color = "brown";
        if (cboColor.getSelectedItem() == "قرمز") color = "red";
        if (cboColor.getSelectedItem() == "سرمه ای") color = "darkblue";
        if (cboColor.getSelectedItem() == "بژ") color = "beige";
        if (cboColor.getSelectedItem() == "تیتانیوم") color = "titanium";
        if (cboColor.getSelectedItem() == "سربی") color = "slategray";
        if (cboColor.getSelectedItem() == "سبز") color = "green";
        if (cboColor.getSelectedItem() == "کربن بلک") color = "carbonblack";
        if (cboColor.getSelectedItem() == "آلبالویی") color = "maroon";
        if (cboColor.getSelectedItem() == "نقرآبی") color = "steelblue";
        if (cboColor.getSelectedItem() == "دلفینی") color = "dolohin";
        if (cboColor.getSelectedItem() == "زرد") color = "yellow";
        if (cboColor.getSelectedItem() == "مسی") color = "copper";
        if (cboColor.getSelectedItem() == "یشمی") color = "jadegreen";
        if (cboColor.getSelectedItem() == "بادمجانی") color = "eggplant";
        if (cboColor.getSelectedItem() == "نارنجی") color = "orange";
        if (cboColor.getSelectedItem() == "ذغالی") color = "charcoal";
        if (cboColor.getSelectedItem() == "طوسی") color = "darkgray";
        if (cboColor.getSelectedItem() == "زیتونی") color = "olivegreen";
        if (cboColor.getSelectedItem() == "کرم") color = "bisque";
        if (cboColor.getSelectedItem() == "گیلاسی") color = "cherry";
        if (cboColor.getSelectedItem() == "طلایی") color = "golden";
        if (cboColor.getSelectedItem() == "زرشکی") color = "crimson";
        if (cboColor.getSelectedItem() == "اطلسی") color = "satin";
        if (cboColor.getSelectedItem() == "برنز") color = "bronze";
        if (cboColor.getSelectedItem() == "عنابی") color = "darkred";
        if (cboColor.getSelectedItem() == "خاکی") color = "khaki";
        if (cboColor.getSelectedItem() == "موکا") color = "mocha";
        if (cboColor.getSelectedItem() == "بنفش") color = "purple";
        if (cboColor.getSelectedItem() == "پوست پیازی") color = "onion";
        if (cboColor.getSelectedItem() == "یاسی") color = "lilac";
        if (cboColor.getSelectedItem() == "اخرائی") color = "ochre";
        if (cboColor.getSelectedItem() == "صورتی") color = "pink";
        if (cboColor.getSelectedItem() == "شتری") color = "camellike";
        if (cboColor.getSelectedItem() == "مارون") color = "maroon";

        switch (cboStatus.getSelectedIndex()) {
            case 0, 1 -> status = "no_paint";
            case 2 -> status = "one_paint";
            case 3 -> status = "two_paint";
            case 4 -> status = "multi_paint";
            case 5 -> status = "refinement";
            case 6 -> status = "around_paint";
            case 7 -> status = "fender_paint";
            case 8 -> status = "hood_paint";
            case 9 -> status = "one_door";
            case 10 -> status = "two_door";
            case 11 -> status = "full_paint";
            case 12 -> status = "hood_replace";
            case 13 -> status = "fender_replace";
            case 14 -> status = "door_replace";
            case 15 -> status = "room_replace";
            case 16 -> status = "crashed";
            case 17 -> status = "burned";
            case 18 -> status = "scrap";
        }

        build = persianToEnglish(tfBuild.getText());
        mileage = persianToEnglish(tfMileage.getText());

        // URL
        String fmtURL = "https://bama.ir/car/%s-y%s?mileage=%s&priced=1&seller=1&transmission=%s&color=%s&status=%s&sort=7".formatted(model, build, mileage, gearbox, color, status);
        URL url;

        // Loading
        System.out.println("Estimating Price ...");

        // Scraper
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
                    price = "%s تا %s تومان در تاریخ %s".formatted(fmt.format(firstPrice), fmt.format(secondPrice), jalaliDate);

                } catch (IOException e0) {
                    e0.printStackTrace();
                }

            } catch (IOException e1) {
                e1.printStackTrace();
            }

        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        }

        // Done
        System.out.println("Done!");

        if (e.getActionCommand().equals("محاسبه قیمت")) {
            JDialog dialog = new JDialog(frame);
            dialog.setSize(650, 200);
            dialog.setLocationRelativeTo(null);
            dialog.setResizable(false);
            dialog.setVisible(true);
            JLabel labelPrice = new JLabel(price, SwingConstants.CENTER);
            labelPrice.setForeground(new Color(48, 46, 73));
            labelPrice.setFont(titleFont);
            dialog.add(labelPrice);
        }
    }
}