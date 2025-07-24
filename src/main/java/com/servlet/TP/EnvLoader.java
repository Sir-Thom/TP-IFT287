package com.servlet.TP;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class EnvLoader {
    public static Map<String, String> loadEnv(String path) {
        Map<String, String> env = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty() || line.startsWith("#")) continue;
                String[] parts = line.split("=", 2);
                if (parts.length == 2) {
                    env.put(parts[0].trim(), parts[1].trim());
                }
            }
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement du fichier .env : " + e.getMessage());
        }
        return env;
    }
}
