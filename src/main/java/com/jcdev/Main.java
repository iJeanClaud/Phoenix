package com.jcdev;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.microsoft.playwright.*;
import com.microsoft.playwright.options.Geolocation;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class Main {

    public static void main(String[] args) {
        if(Checker.analyze()){run();}else{System.out.println("Java virtual machine not working properly. Try again!");}
    }

    public static void run() {

        try (Playwright playwright = Playwright.create()) {
            BrowserType browserType = playwright.chromium();
            Browser browser = browserType.launch(new BrowserType.LaunchOptions().setHeadless(false));
            BrowserContext context = browser.newContext();

            // Crie uma lista de permissões
            List<String> permissions = Arrays.asList("geolocation");

            context.grantPermissions(permissions);

            Geolocation geolocation = new Geolocation(getLocRand()[0], getLocRand()[1]);
            geolocation.setAccuracy(getPrecision());

            context.setGeolocation(geolocation);

            Page page = context.newPage();


            // Abre a página do ponto
            page.navigate("https://aplic.inmetrics.com.br/pontoeletronico/");

            // Aguarda até que a div com o ID "clock" esteja presente na página
            ElementHandle element = page.waitForSelector("#clock", new Page.WaitForSelectorOptions().setTimeout(10000));
            if (element == null) {
                // Se a div não aparecer em até 10 segundos, atualiza a página
                page.reload();
            }

            while(true){
                // Identifica o conteúdo da tela
                ElementHandle clockElement = page.querySelector("#clock");
                String clockText = clockElement.innerText();

                // Localiza os minutos da página
                int index = clockText.indexOf(":");
                String minute = clockText.substring(index + 1, index + 3);

                if(minute.equalsIgnoreCase("00") || minute.equalsIgnoreCase("0")){
                    break;
                }
            }

            // Preenche o campo de entrada com o ID "edMatricula" com o valor "123"
            page.fill("#edMatricula", matri());
            page.fill("#edSenha", pass());
            page.click("#enviar");

            // Espere até que a tag <div> com os dados seja carregada
            page.waitForSelector("div[data-growl='container']");

            // Obtenha o texto do título e da mensagem da tag <span> dentro da tag <div>
            String message = page.innerText("div[data-growl='container'] [data-growl='message']");

            // Envia notificação pelo Telegram
            sendNotification("jcodse10", message);


            browser.close();
        }
    }

    public static double[] getLocation() {
        try {
            URL url = new URL("http://ip-api.com/json");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            int responsecode = conn.getResponseCode();
            if (responsecode != 200) {
                throw new RuntimeException("HttpResponseCode: " + responsecode);
            } else {
                BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = reader.readLine()) != null) {
                    response.append(inputLine);
                }
                reader.close();

                JSONObject json = new JSONObject(response.toString());
                double[] coordinates = new double[2];
                coordinates[0] = json.getDouble("lat");
                coordinates[1] = json.getDouble("lon");

                return coordinates;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String geoRandom(int quantidade) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < quantidade; i++) {
            int numero = random.nextInt(10); // Gera um número aleatório entre 0 e 9
            sb.append(numero);
        }

        return sb.toString();
    }

    public static void sendNotification(String encoderKey, String message) {
        TelegramAPI telegram = new TelegramAPI(Encoder.decrypt("gcNYcl+AQ7MIatBvLZ49UzeXTzieXCFvTsofxR7zBuaUjj9+gtnElanLYyPtwhAk", encoderKey));
        try {
            telegram.sendMessage(Integer.parseInt(Encoder.decrypt("pgHgJXT8kbz3+ON9xL9fOA==", encoderKey)), message);
        } catch (UnirestException e1) {
            e1.printStackTrace();
        }
    }

    public static String matri() {
        int[] numbers = new int[11];
        numbers[0] = (int) Math.pow(2, 2);
        numbers[1] = (int) Math.sqrt(1);
        numbers[2] = (int) Math.cbrt(27);
        numbers[3] = (int) Math.cbrt(27);
        numbers[4] = (int) Math.sqrt(25);
        numbers[5] = (int) Math.sqrt(81);
        numbers[6] = (int) Math.cbrt(27);
        numbers[7] = (int) Math.pow(2, 2);
        numbers[8] = (int) Math.cbrt(512);
        numbers[9] = 0;
        numbers[10] = (int) Math.pow(2, 2);

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < numbers.length; i++) {
            result.append(numbers[i]);
        }
        return result.toString();
    }

    public static String pass() {
        char[] characters = new char[13];
        characters[0] = (char) (64);
        characters[1] = (char) (67);
        characters[2] = (char) (108);
        characters[3] = (char) (115);
        characters[4] = (char) (116);
        characters[5] = (char) (114);
        characters[6] = (char) (111);
        characters[7] = (char) (107);
        characters[8] = (char) (101);
        characters[9] = (char) (114);
        characters[10] = (char) (49);
        characters[11] = (char) (57);
        characters[12] = (char) (57);
        //characters[13] = (char) (50);

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < characters.length; i++) {
            result.append(characters[i]);
        }
        result.append((char)(50));
        return result.toString();
    }

    public static double[] getLocRand(){
        Random random = new Random();
        int randNum = random.nextInt(101) + 50;

        String lat = "-23";
        String lon = "-46";

        int latNum = 699397 - randNum;
        int lonNum = 593720 + randNum;

        String latitude = lat + "." + latNum;
        String longitude = lon + "." + lonNum;

        double[] geoLocation = {Double.parseDouble(latitude), Double.parseDouble(longitude)};

        return geoLocation;
    }

    public static double getPrecision(){
        double valorMinimo = 16.875;
        double valorMaximo = 17.035;

        // Crie um objeto Random
        Random random = new Random();

        // Gere um número aleatório dentro do intervalo desejado
        double numeroAleatorio = valorMinimo + (valorMaximo - valorMinimo) * random.nextDouble();

        // Formate o número com ponto como separador decimal
        DecimalFormat formato = new DecimalFormat("0.000", DecimalFormatSymbols.getInstance(Locale.US));
        String numeroFormatado = formato.format(numeroAleatorio);

        // Converta o número formatado de volta para double
        double numeroDouble = Double.parseDouble(numeroFormatado);

        return numeroDouble;
    }
}

