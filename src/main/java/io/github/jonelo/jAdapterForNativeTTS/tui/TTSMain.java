/*
 * MIT License
 *
 * Copyright (c) 2021 Johann N. Löfflmann
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.github.jonelo.jAdapterForNativeTTS.tui;

import io.github.jonelo.jAdapterForNativeTTS.engines.SpeechEngine;
import io.github.jonelo.jAdapterForNativeTTS.engines.SpeechEngineNative;
import io.github.jonelo.jAdapterForNativeTTS.engines.Voice;
import io.github.jonelo.jAdapterForNativeTTS.engines.VoicePreferences;

import java.io.File;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

/**
 * A simple text user interface in order to demonstrate the jAdapterForNativeTTS.
 */
public class TTSMain {

    /**
     * Prints the usage to the standard output.
     */
    public static void usage() {
        System.out.println("java -jar jAdapterForNativeTTS-*.jar [word]...");
    }


    private static void printVoices(List<Voice> voices) {
        // print all voices
        int id = 0;
        for (Voice voice : voices) {
            System.out.printf("%d: %s%n", id++, voice);
        }
    }

    private int readRateFromUser() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Rate [-100..100]: ");
        String rate = scanner.nextLine();
        try {
            return Integer.parseInt(rate);
        } catch (NumberFormatException e) {
            System.err.println(e.getMessage());
            return 0;
        }
    }

    private VoicePreferences readVoicePreferences() {
        Scanner scanner = new Scanner(System.in);
        VoicePreferences voicePreferences = new VoicePreferences();

        System.out.print("Language: ");
        String language = scanner.nextLine();
        if (language.length() > 0) {
            voicePreferences.setLanguage(language.toLowerCase(Locale.US));
        }

        System.out.print("Country: ");
        String country = scanner.nextLine();
        if (country.length() > 0) {
            voicePreferences.setCountry(country.toUpperCase(Locale.US));
        }

        System.out.print("Gender: ");
        String gender = scanner.nextLine();
        if (gender.equalsIgnoreCase("female")) {
            voicePreferences.setGender(VoicePreferences.Gender.FEMALE);
        } else if (gender.equalsIgnoreCase("male")) {
            voicePreferences.setGender(VoicePreferences.Gender.MALE);
        }


        System.out.println("You have selected the following voice preferences:");
        System.out.println(voicePreferences);

        return voicePreferences;
    }

    private static Voice selectVoice(SpeechEngine speechEngine) throws Exception {
        List<Voice> voices = speechEngine.getAvailableVoices();
//        printVoices(voices);
        Voice vn = voices.stream().filter(x-> x.getCulture().contains("vi-VN")).findAny().orElse(null);
        return vn;
    }


    private String concatenateArgs(String[] args) {
        // get the text from the command line
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            text.append(args[i]);
            if (i < args.length - 1) {
                text.append(" ");
            }
        }
        return text.toString();
    }

    public static void sayText(String text , String output) {
        try {
            SpeechEngine speechEngine = SpeechEngineNative.getInstance();
            Voice voice = selectVoice(speechEngine);
            speechEngine.setVoice(voice.getName());
            speechEngine.setRate(0);
            System.out.printf("Playing the following text: \"%s\"\n ---> %s", text , output);
            if(output != null) {
                speechEngine.say(text , output);
            } else {
                speechEngine.say(text);
            }
            // Thread.sleep(1000);
            // speechEngine.stopTalking();
        } catch (Exception ex) {
            System.err.printf("Error: %s%n", ex.getMessage());
        }
    }

    /**
     * The main method.
     *
     * @param args the values passed from the command line
     */
    public TTSMain(String[] args) {
        File f = new File("D:\\Temporary\\deleteable\\AUDIO\\NTKN\\tmp\\test.wav");
        sayText("Quy trình xử lý ô nhiễm dinh dưỡng và tảo độc bằng mô hình công nghệ sinh thái sử dụng thực vật thủy sinh" , f.getAbsolutePath());
    }

    public static void main(String[] args) throws InterruptedException {
        TTSMain main = new TTSMain(args);
//        Thread.sleep(5000L);
    }
}
