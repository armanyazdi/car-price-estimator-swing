package armanyazdi;

import java.awt.*;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, FontFormatException {

        // Use with GUI:
        CarPriceEstimator carPriceEstimator = new CarPriceEstimator();
        carPriceEstimator.mainFrame();

        /* Use with terminal:
        Terminal carPriceEstimator = new Terminal();
        carPriceEstimator.userInput();
        carPriceEstimator.priceEstimator(); */
    }
}